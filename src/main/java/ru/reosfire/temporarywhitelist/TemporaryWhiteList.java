package ru.reosfire.temporarywhitelist;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import ru.reosfire.temporarywhitelist.Commands.TwlCommand;
import ru.reosfire.temporarywhitelist.Configuration.Config;
import ru.reosfire.temporarywhitelist.Configuration.Messages;
import ru.reosfire.temporarywhitelist.Data.IDataProvider;
import ru.reosfire.temporarywhitelist.Data.MysqlDataBase;
import ru.reosfire.temporarywhitelist.Data.YamlDataBase;
import ru.reosfire.temporarywhitelist.Lib.Text.Text;
import ru.reosfire.temporarywhitelist.Lib.Yaml.YamlConfig;

import java.io.IOException;

public final class TemporaryWhiteList extends JavaPlugin
{
    private static TemporaryWhiteList singleton;
    private static Config configuration;
    private static Messages messages;
    private static IDataProvider dataProvider;
    private static final TwlCommand commands = new TwlCommand();

    public static TemporaryWhiteList getSingleton()
    {
        return singleton;
    }

    public static Config getConfiguration()
    {
        return configuration;
    }

    public static IDataProvider getDataProvider()
    {
        return dataProvider;
    }

    public static Messages getMessages()
    {
        return messages;
    }

    private BukkitTask KickerTask;

    @Override
    public void onEnable()
    {
        singleton = this;

        getLogger().info("Loading configurations...");
        ReloadConfigurations();

        getLogger().info("Loading commands...");
        commands.Register(getCommand("twl"));

        getLogger().info("Loading data...");
        ReloadData();

        getLogger().info("Loaded");
    }

    public static void ReloadConfigurations()
    {
        try
        {
            configuration = new Config(YamlConfig.LoadOrCreate("config.yml", singleton));
            messages = new Messages(YamlConfig.LoadOrCreate("messages.yml", singleton));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException("Error while loading config!");
        }
    }

    public static void ReloadData()
    {
        if (configuration.DataProvider.equals("yaml"))
        {
            ReloadYaml();
        }
        else if (configuration.DataProvider.equals("mysql"))
        {
            try
            {
                dataProvider = new MysqlDataBase(configuration.SqlConfiguration);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                Bukkit.getLogger().warning("Can't connect to mysql data base! This plugin will use yaml data storing");
                ReloadYaml();
            }
        }
    }

    private static void ReloadYaml()
    {
        try
        {
            dataProvider = new YamlDataBase(YamlConfig.LoadOrCreate("data.yml", singleton));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void Enable()
    {
        KickerTask = new BukkitRunnable()
        {
            @Override
            public void run()
            {
                for (Player player : getServer().getOnlinePlayers())
                {
                    if (!dataProvider.CanJoin(player.getName()) && !player.isOp())
                    {
                        player.kickPlayer(Text.Colorize(player, getMessages().KickConnected));
                    }
                }
            }
        }.runTaskTimer(this, 0, getConfiguration().SubscriptionEndCheckTicks);

        getConfiguration().SetEnabled(true);
    }

    public void Disable()
    {
        if (getConfiguration().Enabled) KickerTask.cancel();
        getConfiguration().SetEnabled(false);
    }
}