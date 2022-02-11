package ru.reosfire.temporarywhitelist.Configuration;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import ru.reosfire.temporarywhitelist.Lib.Yaml.Default.SqlConfiguration;
import ru.reosfire.temporarywhitelist.Lib.Yaml.YamlConfig;
import ru.reosfire.temporarywhitelist.TemporaryWhiteList;

import java.io.File;

public class Config extends YamlConfig
{
    private final TemporaryWhiteList PluginInstance;

    public final String DataFile;
    public final int SubscriptionEndCheckTicks;
    public boolean Enabled;
    public final String DataProvider;
    public final SqlConfiguration SqlConfiguration;
    public final String SqlTable;
    public final MessagesConfig Messages;

    private YamlConfiguration configuration;
    public Config(ConfigurationSection configuration, TemporaryWhiteList pluginInstance)
    {
        super(configuration);

        PluginInstance = pluginInstance;

        DataFile = getString("DataFile");
        SubscriptionEndCheckTicks = getInt("SubscriptionEndCheckTicks");
        Enabled = getBoolean("Enabled");
        DataProvider = getString("DataProvider");
        SqlConfiguration = new SqlConfiguration(getSection("Mysql"));
        SqlTable = getString("Mysql.Table");
        Messages = new MessagesConfig(getSection("Messages"));
    }

    public void SetEnabled(boolean enabled)
    {
        configuration.set("Enabled", enabled);
        File dataFile = new File(PluginInstance.getDataFolder(), "config.yml");
        try
        {
            configuration.save(dataFile);
            Enabled = enabled;
        }
        catch (Exception e)
        {
            PluginInstance.getLogger().info(e.getMessage());
        }
    }
}