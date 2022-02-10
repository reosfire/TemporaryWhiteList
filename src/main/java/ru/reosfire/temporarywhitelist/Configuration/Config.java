package ru.reosfire.temporarywhitelist.Configuration;

import org.bukkit.configuration.file.YamlConfiguration;
import ru.reosfire.temporarywhitelist.Lib.Yaml.Default.SqlConfiguration;
import ru.reosfire.temporarywhitelist.Lib.Yaml.YamlConfig;
import ru.reosfire.temporarywhitelist.TemporaryWhiteList;

import java.io.File;

public class Config extends YamlConfig
{
    public final String DataFile;
    public final int SubscriptionEndCheckTicks;
    public boolean Enabled;
    public final String DataProvider;
    public final SqlConfiguration SqlConfiguration;
    public final String SqlTable;

    private YamlConfiguration configuration;
    public Config(YamlConfiguration configuration)
    {
        super(configuration);

        DataFile = getString("DataFile");
        SubscriptionEndCheckTicks = getInt("SubscriptionEndCheckTicks");
        Enabled = getBoolean("Enabled");
        DataProvider = getString("DataProvider");
        SqlConfiguration = new SqlConfiguration(getSection("Mysql"));
        SqlTable = getString("Mysql.Table");
    }

    public void SetEnabled(boolean enabled)
    {
        configuration.set("Enabled", enabled);
        File dataFile = new File(TemporaryWhiteList.getSingleton().getDataFolder(), "config.yml");
        try
        {
            configuration.save(dataFile);
            Enabled = enabled;
        }
        catch (Exception e)
        {
            TemporaryWhiteList.getSingleton().getLogger().info(e.getMessage());
        }
    }
}