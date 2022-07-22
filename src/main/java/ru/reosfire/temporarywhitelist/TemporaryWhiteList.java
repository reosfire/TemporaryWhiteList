package ru.reosfire.temporarywhitelist;

import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bstats.charts.SingleLineChart;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import ru.reosfire.temporarywhitelist.Commands.TwlCommand;
import ru.reosfire.temporarywhitelist.Configuration.Config;
import ru.reosfire.temporarywhitelist.Configuration.Localization.MessagesConfig;
import ru.reosfire.temporarywhitelist.Data.*;
import ru.reosfire.temporarywhitelist.Lib.Text.Text;
import ru.reosfire.temporarywhitelist.Lib.Yaml.YamlConfig;
import ru.reosfire.temporarywhitelist.Loaders.LocalizationsLoader;

import java.io.*;
import java.util.Objects;

public final class TemporaryWhiteList extends JavaPlugin
{
    private boolean _loaded;
    private boolean _enabled;
    private Config _configuration;
    private PlayerDatabase _database;
    private MessagesConfig _messages;
    private PlaceholdersExpansion _placeholdersExpansion;

    public Config getConfiguration()
    {
        return _configuration;
    }
    public MessagesConfig getMessages()
    {
        return _messages;
    }

    private BukkitTask _kickerTask;

    public boolean isWhiteListEnabled()
    {
        return _enabled;
    }

    @Override
    public void onEnable()
    {
        Load();
        Metrics metrics = new Metrics(this, 14858);
        metrics.addCustomChart(new SingleLineChart("whitelisted_players", () -> _database.AllList().size()));
        metrics.addCustomChart(new SimplePie("whitelisted_players_per_server", () -> Integer.toString(_database.AllList().size())));
        metrics.addCustomChart(new SimplePie("data_provider", () -> _configuration.DataProvider));

        UpdateChecker updateChecker = new UpdateChecker(this, 99914);
        updateChecker.getVersion(version ->
        {
            if (version.equalsIgnoreCase(getDescription().getVersion()))
                getLogger().info("Plugin is up to date. Please rate it: https://www.spigotmc.org/resources/temporarywhitelist.99914");
            else
                getLogger().info("There is a new version (" + version + ") available: https://www.spigotmc.org/resources/temporarywhitelist.99914");
        });
    }

    public void Load()
    {
        if (_loaded) Unload();

        getLogger().info("Loading configurations...");
        _configuration = LoadConfiguration();

        getLogger().info("Loading messages...");
        LocalizationsLoader localizationsLoader = new LocalizationsLoader(this, _configuration);
        localizationsLoader.CopyDefaultTranslations();
        _messages = localizationsLoader.LoadMessages();

        TimeConverter _timeConverter = new TimeConverter(_configuration);

        getLogger().info("Loading data...");
        _database = LoadDatabase(_configuration);

        getLogger().info("Loading commands...");
        TwlCommand commands = new TwlCommand(_messages, _database, this, _timeConverter);
        commands.Register(Objects.requireNonNull(getCommand("twl")));
        commands.Register(Objects.requireNonNull(getCommand("/twl")));

        getLogger().info("Loading placeholders...");
        Plugin placeholderAPI = getServer().getPluginManager().getPlugin("PlaceholderAPI");
        if (placeholderAPI == null)
        {
            getLogger().warning("Placeholder api plugin not found");
        }
        else
        {
            _placeholdersExpansion = new PlaceholdersExpansion(_messages, _database, _timeConverter, this);
            _placeholdersExpansion.register();
            Text.placeholderApiEnabled = true;
        }

        getLogger().info("Loading events handler...");
        EventsListener eventsListener = new EventsListener(_messages, _database, this);
        getServer().getPluginManager().registerEvents(eventsListener, this);

        if (GetEnabledInFile())
        {
            getLogger().info("Enabling...");
            Enable();
        }

        _loaded = true;
        getLogger().info("Loaded");
    }

    private void Unload()
    {
        HandlerList.unregisterAll(this);

        if (_placeholdersExpansion != null) _placeholdersExpansion.unregister();
    }

    private Config LoadConfiguration()
    {
        try
        {
            return new Config(YamlConfig.LoadOrCreate("config.yml", this));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException("Error while loading config!");
        }
    }

    private PlayerDatabase LoadDatabase(Config config)
    {
        IDataProvider dataProvider;

        if (config.DataProvider.equals("yaml"))
        {
            dataProvider = LoadYamlData(config);
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
                dataProvider = LoadYamlData(config);
            }
        }
        else throw new RuntimeException("cannot load data provider of type: " + config.DataProvider);

        return new PlayerDatabase(dataProvider, config.RefreshAfter, config.IgnoreCase);
    }

    public YamlDataProvider LoadYamlData(Config config)
    {
        try
        {
            return new YamlDataProvider(YamlConfig.LoadOrCreateFile(config.DataFile, this));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException("Error while loading yaml database!");
        }
    }

    public SqlDataProvider LoadSqlData(Config config)
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

    public boolean Enable()
    {
        if (_enabled) return false;

        SetEnabledInFile(true);
        RunKickerTask();
        _enabled = true;
        return true;
    }

    private void RunKickerTask()
    {
        _kickerTask = Bukkit.getScheduler().runTaskTimer(this, () ->
        {
            for (Player player : getServer().getOnlinePlayers())
            {
                if (_database.CanJoin(player.getName())) continue;
                if (player.isOp()) continue;
                if (player.hasPermission("TemporaryWhitelist.Bypass")) continue;

                player.kickPlayer(String.join("\n", Text.Colorize(player, _messages.Kick.WhilePlaying)));
            }
        }, 0, _configuration.SubscriptionEndCheckTicks);
    }

    public boolean Disable()
    {
        if (!_enabled) return false;

        SetEnabledInFile(false);
        _kickerTask.cancel();
        _enabled = false;
        return true;
    }

    private void SetEnabledInFile(boolean enabled)
    {
        File configFile = new File(this.getDataFolder(), "enabled.txt");

        try
        {
            if (!configFile.exists()) configFile.createNewFile();

            try(BufferedWriter writer = new BufferedWriter(new FileWriter(configFile)))
            {
                writer.write(enabled ? "true" : "false");
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error while setting enabled in file", e);
        }

        _enabled = enabled;
    }

    private boolean GetEnabledInFile()
    {
        try
        {
            File configFile = new File(this.getDataFolder(), "enabled.txt");
            if (!configFile.exists())
            {
                SetEnabledInFile(true);
                return true;
            }
            try(BufferedReader reader = new BufferedReader(new FileReader(configFile)))
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