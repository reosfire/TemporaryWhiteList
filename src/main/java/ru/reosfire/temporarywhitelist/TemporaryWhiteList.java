package ru.reosfire.temporarywhitelist;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import ru.reosfire.temporarywhitelist.Commands.TwlCommand;
import ru.reosfire.temporarywhitelist.Configuration.Config;
import ru.reosfire.temporarywhitelist.Configuration.Localization.MessagesConfig;
import ru.reosfire.temporarywhitelist.Data.IDataProvider;
import ru.reosfire.temporarywhitelist.Data.MysqlDataBase;
import ru.reosfire.temporarywhitelist.Data.YamlDataBase;
import ru.reosfire.temporarywhitelist.Lib.Text.Text;
import ru.reosfire.temporarywhitelist.Lib.Yaml.YamlConfig;
import ru.reosfire.temporarywhitelist.Loaders.LocalizationsLoader;

import java.io.File;
import java.io.IOException;

public final class TemporaryWhiteList extends JavaPlugin
{
    private boolean _loaded;
    private boolean _enabled;
    private Config _configuration;
    private IDataProvider _dataProvider;
    private MessagesConfig _messages;
    private PlaceholdersExpansion _placeholdersExpansion;

    private BukkitTask _kickerTask;

    public boolean isWhiteListEnabled()
    {
        return _enabled;
    }

    @Override
    public void onEnable()
    {
        Load();
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

        TimeConverter timeConverter = new TimeConverter(_configuration.DurationFormat);

        getLogger().info("Loading data...");
        _dataProvider = ReloadDataProvider(_configuration, timeConverter);

        getLogger().info("Loading commands...");
        TwlCommand commands = new TwlCommand(_messages, _dataProvider, this, timeConverter);
        commands.Register(getCommand("twl"));

        getLogger().info("Loading placeholders...");
        Plugin placeholderAPI = getServer().getPluginManager().getPlugin("PlaceholderAPI");
        if (placeholderAPI == null)
        {
            getLogger().warning("Placeholder api plugin not found");
        }
        else
        {
            _placeholdersExpansion = new PlaceholdersExpansion(_messages, _dataProvider, this);
            _placeholdersExpansion.register();
            Text.placeholderApiEnabled = true;
        }

        getLogger().info("Loading events handler...");
        EventsListener eventsListener = new EventsListener(_messages, _dataProvider, this);
        getServer().getPluginManager().registerEvents(eventsListener, this);

        _enabled = _configuration.getBoolean("Enabled");
        if (_enabled)
        {
            getLogger().info("Enabling...");
            RunKickerTask();
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

    private IDataProvider ReloadDataProvider(Config config, TimeConverter converter)
    {
        if (config.DataProvider.equals("yaml"))
        {
            return LoadYamlData(converter);
        }
        else if (config.DataProvider.equals("mysql"))
        {
            try
            {
                return new MysqlDataBase(config, _messages, converter);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                Bukkit.getLogger().warning("Can't connect to mysql data base! This plugin will use yaml data storing");
                return LoadYamlData(converter);
            }
        }
        else throw new RuntimeException("cannot load data provider of type: " + config.DataProvider);
    }

    private YamlDataBase LoadYamlData(TimeConverter converter)
    {
        try
        {
            return new YamlDataBase(_messages, YamlConfig.LoadOrCreateFile("data.yml", this), converter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException("Error while loading yaml database!");
        }
    }

    public void Enable() throws IOException, InvalidConfigurationException
    {
        if (_enabled) return;

        SetEnabledInConfiguration(true);
    }

    private void RunKickerTask()
    {
        _kickerTask = Bukkit.getScheduler().runTaskTimer(this, () ->
        {
            for (Player player : getServer().getOnlinePlayers())
            {
                if (!_dataProvider.CanJoin(player.getName()) && !player.isOp())
                {
                    player.kickPlayer(String.join("\n", Text.Colorize(player, _messages.Kick.WhilePlaying)));
                }
            }
        }, 0, _configuration.SubscriptionEndCheckTicks);
    }

    public void Disable() throws IOException, InvalidConfigurationException
    {
        if (!_enabled) return;

        SetEnabledInConfiguration(false);

        _kickerTask.cancel();
    }

    private void SetEnabledInConfiguration(boolean enabled) throws IOException, InvalidConfigurationException
    {
        File configFile = new File(this.getDataFolder(), "config.yml");
        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        yamlConfiguration.load(configFile);

        if (enabled == yamlConfiguration.getBoolean("Enabled")) return;

        yamlConfiguration.save(configFile);
        _enabled = enabled;
    }
}