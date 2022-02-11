package ru.reosfire.temporarywhitelist;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import ru.reosfire.temporarywhitelist.Commands.TwlCommand;
import ru.reosfire.temporarywhitelist.Configuration.Config;
import ru.reosfire.temporarywhitelist.Data.IDataProvider;
import ru.reosfire.temporarywhitelist.Data.MysqlDataBase;
import ru.reosfire.temporarywhitelist.Data.YamlDataBase;
import ru.reosfire.temporarywhitelist.Lib.Text.Text;
import ru.reosfire.temporarywhitelist.Lib.Yaml.YamlConfig;

public final class TemporaryWhiteList extends JavaPlugin
{
    private Config configuration;
    private IDataProvider dataProvider;

    private BukkitTask KickerTask;

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

        getLogger().info("Loaded");
    }

    public void ReloadConfigurations()
    {
        try
        {
            configuration = new Config(YamlConfig.LoadOrCreate("config.yml", this), this);
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

    public void Enable()
    {
        if (configuration.Enabled) return;

        KickerTask = new BukkitRunnable()
        {
            @Override
            public void run()
            {
                for (Player player : getServer().getOnlinePlayers())
                {
                    if (!dataProvider.CanJoin(player.getName()) && !player.isOp())
                    {
                        player.kickPlayer(Text.Colorize(player, configuration.Messages.KickConnected));
                    }
                }
            }
        }.runTaskTimer(this, 0, configuration.SubscriptionEndCheckTicks);

        configuration.SetEnabled(true);
    }

    public void Disable()
    {
        if (!configuration.Enabled) return;

        KickerTask.cancel();
        configuration.SetEnabled(false);
    }
}