package ru.reosfire.temporarywhitelist;

import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bstats.charts.SingleLineChart;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import ru.reosfire.temporarywhitelist.commands.TwlCommand;
import ru.reosfire.temporarywhitelist.commands.TwlSyncCommand;
import ru.reosfire.temporarywhitelist.configuration.Config;
import ru.reosfire.temporarywhitelist.configuration.localization.MessagesConfig;
import ru.reosfire.temporarywhitelist.data.IDataProvider;
import ru.reosfire.temporarywhitelist.data.PlayerDatabase;
import ru.reosfire.temporarywhitelist.data.providers.SqlDataProvider;
import ru.reosfire.temporarywhitelist.data.providers.YamlDataProvider;
import ru.reosfire.temporarywhitelist.lib.text.Text;
import ru.reosfire.temporarywhitelist.lib.yaml.YamlConfig;
import ru.reosfire.temporarywhitelist.loaders.LocalizationsLoader;

import java.io.*;
import java.util.Objects;

public final class TemporaryWhiteList extends JavaPlugin
{
    private boolean loaded;
    private boolean enabled;
    private Config configuration;
    private PlayerDatabase database;
    private MessagesConfig messages;
    private PlaceholdersExpansion placeholdersExpansion;
    private TimeConverter timeConverter;
    private OnlinePlayersKicker onlinePlayersKicker;

    public Config getConfiguration()
    {
        return configuration;
    }
    public MessagesConfig getMessages()
    {
        return messages;
    }
    public PlayerDatabase getDatabase()
    {
        return database;
    }
    public TimeConverter getTimeConverter()
    {
        return timeConverter;
    }

    public boolean isWhiteListEnabled()
    {
        return enabled;
    }

    @SuppressWarnings("unused")
    @Override
    public void onEnable()
    {
        load();
        Metrics metrics = new Metrics(this, 14858);
        metrics.addCustomChart(new SingleLineChart("whitelisted_players", () -> database.allList().size()));
        metrics.addCustomChart(new SimplePie("whitelisted_players_per_server", () -> Integer.toString(database.allList().size())));
        metrics.addCustomChart(new SimplePie("data_provider", () -> configuration.DataProvider));

        UpdateChecker updateChecker = new UpdateChecker(this, 99914);
        updateChecker.getVersion(version ->
        {
            if (version.equalsIgnoreCase(getDescription().getVersion()))
                getLogger().info("Plugin is up to date. Please rate it: https://www.spigotmc.org/resources/temporarywhitelist.99914");
            else
                getLogger().info("There is a new version (" + version + ") available: https://www.spigotmc.org/resources/temporarywhitelist.99914");
        });
    }

    public void load()
    {
        if (loaded) unload();

        getLogger().info("Loading configurations...");
        configuration = loadConfiguration();

        getLogger().info("Loading messages...");
        LocalizationsLoader localizationsLoader = new LocalizationsLoader(this);
        localizationsLoader.copyDefaultTranslations();
        messages = localizationsLoader.loadMessages();

        timeConverter = new TimeConverter(configuration);

        getLogger().info("Loading data...");
        database = loadDatabase(configuration);

        getLogger().info("Loading commands...");
        TwlCommand commands = new TwlCommand(this);
        commands.register(Objects.requireNonNull(getCommand("twl")));
        TwlSyncCommand syncCommands = new TwlSyncCommand(this);
        syncCommands.register(Objects.requireNonNull(getCommand("twl-sync")));

        getLogger().info("Loading placeholders...");
        Plugin placeholderAPI = getServer().getPluginManager().getPlugin("PlaceholderAPI");
        if (placeholderAPI == null)
        {
            getLogger().warning("Placeholder api plugin not found");
        }
        else
        {
            placeholdersExpansion = new PlaceholdersExpansion(messages, database, timeConverter, this);
            placeholdersExpansion.register();
            Text.placeholderApiEnabled = true;
        }

        getLogger().info("Loading events handler...");
        EventsListener eventsListener = new EventsListener(messages, database, this);
        getServer().getPluginManager().registerEvents(eventsListener, this);

        onlinePlayersKicker = new OnlinePlayersKicker(this);

        if (getEnabledInFile())
        {
            getLogger().info("Enabling...");
            enable();
        }

        loaded = true;
        getLogger().info("Loaded");
    }

    private void unload()
    {
        HandlerList.unregisterAll(this);

        if (placeholdersExpansion != null) placeholdersExpansion.unregister();
    }

    private Config loadConfiguration()
    {
        try
        {
            return new Config(YamlConfig.loadOrCreate("config.yml", this));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException("Error while loading config!");
        }
    }

    private PlayerDatabase loadDatabase(Config config)
    {
        IDataProvider dataProvider;

        if (config.DataProvider.equals("yaml"))
        {
            dataProvider = loadYamlData(config);
        }
        else if (config.DataProvider.equals("mysql"))
        {
            try
            {
                dataProvider = new SqlDataProvider(config);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                Bukkit.getLogger().warning("Can't connect to mysql data base! This plugin will use yaml data storing");
                dataProvider = loadYamlData(config);
            }
        }
        else throw new RuntimeException("cannot load data provider of type: " + config.DataProvider);

        return new PlayerDatabase(dataProvider, config.RefreshAfter, config.IgnoreCase);
    }

    public YamlDataProvider loadYamlData(Config config)
    {
        try
        {
            return new YamlDataProvider(YamlConfig.loadOrCreateFile(config.DataFile, this));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException("Error while loading yaml database!");
        }
    }

    public SqlDataProvider loadSqlData(Config config)
    {
        try
        {
            return new SqlDataProvider(config);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException("Error while loading sql database!");
        }
    }

    public boolean enable()
    {
        if (enabled) return false;
        onlinePlayersKicker.start();
        setEnabledInFile(true);
        enabled = true;
        return true;
    }

    public boolean disable()
    {
        if (!enabled) return false;
        onlinePlayersKicker.stop();
        setEnabledInFile(false);
        enabled = false;
        return true;
    }

    private void setEnabledInFile(boolean enabled)
    {
        File configFile = new File(this.getDataFolder(), "enabled.txt");

        try
        {
            if (!configFile.exists()) configFile.createNewFile();

            try(FileWriter fileWriter = new FileWriter(configFile);
                BufferedWriter writer = new BufferedWriter(fileWriter))
            {
                writer.write(enabled ? "true" : "false");
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error while setting enabled in file", e);
        }

        this.enabled = enabled;
    }

    private boolean getEnabledInFile()
    {
        try
        {
            File configFile = new File(this.getDataFolder(), "enabled.txt");
            if (!configFile.exists())
            {
                setEnabledInFile(true);
                return true;
            }
            try(FileReader fileReader = new FileReader(configFile);
                BufferedReader reader = new BufferedReader(fileReader))
            {
                return reader.readLine().equals("true");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return true;
        }
    }
}