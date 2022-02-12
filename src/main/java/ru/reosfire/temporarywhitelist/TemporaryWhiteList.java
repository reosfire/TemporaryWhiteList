package ru.reosfire.temporarywhitelist;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import ru.reosfire.temporarywhitelist.Commands.TwlCommand;
import ru.reosfire.temporarywhitelist.Configuration.Config;
import ru.reosfire.temporarywhitelist.Data.IDataProvider;
import ru.reosfire.temporarywhitelist.Data.MysqlDataBase;
import ru.reosfire.temporarywhitelist.Data.YamlDataBase;
import ru.reosfire.temporarywhitelist.Lib.Text.Text;
import ru.reosfire.temporarywhitelist.Lib.Yaml.YamlConfig;

import java.io.File;
import java.io.IOException;

public final class TemporaryWhiteList extends JavaPlugin
{
    private boolean Enabled;
    private Config configuration;
    private IDataProvider dataProvider;

    private BukkitTask KickerTask;

    public boolean isWhiteListEnabled()
    {
        return Enabled;
    }

    @Override
    public void onEnable()
    {
        getLogger().info("Loading configurations...");
        ReloadConfigurations();

        getLogger().info("Loading data...");
        ReloadData();

        getLogger().info("Loading commands...");
        TwlCommand commands = new TwlCommand(configuration, dataProvider, this);
        commands.Register(getCommand("twl"));

        getLogger().info("Loading placeholders...");
        PlaceholdersExpansion placeholdersExpansion = new PlaceholdersExpansion(configuration, dataProvider, this);
        placeholdersExpansion.register();

        getLogger().info("Loading events handler...");
        EventsListener eventsListener = new EventsListener(configuration, dataProvider, this);
        getServer().getPluginManager().registerEvents(eventsListener, this);

        Enabled = configuration.getBoolean("Enabled");
        if (Enabled)
        {
            getLogger().info("Enabling...");
            RunKickerTask();
        }

        getLogger().info("Loaded");
    }

    public void ReloadConfigurations()
    {
        try
        {
            configuration = new Config(YamlConfig.LoadOrCreate("config.yml", this));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException("Error while loading config!");
        }
    }

    public void ReloadData()
    {
        if (configuration.DataProvider.equals("yaml"))
        {
            ReloadYamlData();
        }
        else if (configuration.DataProvider.equals("mysql"))
        {
            try
            {
                dataProvider = new MysqlDataBase(configuration);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                Bukkit.getLogger().warning("Can't connect to mysql data base! This plugin will use yaml data storing");
                ReloadYamlData();
            }
        }
    }

    private void ReloadYamlData()
    {
        try
        {
            dataProvider = new YamlDataBase(configuration, YamlConfig.LoadOrCreateFile("data.yml", this));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void Enable() throws IOException, InvalidConfigurationException
    {
        if (Enabled) return;

        SetEnabledInConfiguration(true);
    }

    private void RunKickerTask()
    {
        KickerTask = Bukkit.getScheduler().runTaskTimer(this, () ->
        {
            for (Player player : getServer().getOnlinePlayers())
            {
                if (!dataProvider.CanJoin(player.getName()) && !player.isOp())
                {
                    player.kickPlayer(Text.Colorize(player, configuration.Messages.KickConnected));
                }
            }
        }, 0, configuration.SubscriptionEndCheckTicks);
    }

    public void Disable() throws IOException, InvalidConfigurationException
    {
        if (!Enabled) return;

        SetEnabledInConfiguration(false);

        KickerTask.cancel();
    }

    private void SetEnabledInConfiguration(boolean enabled) throws IOException, InvalidConfigurationException
    {
        File configFile = new File(this.getDataFolder(), "config.yml");
        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        yamlConfiguration.load(configFile);

        if (enabled == yamlConfiguration.getBoolean("Enabled")) return;

        yamlConfiguration.save(configFile);
        Enabled = enabled;
    }
}