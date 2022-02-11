package ru.reosfire.temporarywhitelist.Configuration;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import ru.reosfire.temporarywhitelist.Lib.Yaml.Default.SqlConfiguration;
import ru.reosfire.temporarywhitelist.Lib.Yaml.YamlConfig;
import ru.reosfire.temporarywhitelist.TemporaryWhiteList;

import java.io.File;
import java.io.IOException;

public class Config extends YamlConfig
{
    public final String DataFile;
    public final int SubscriptionEndCheckTicks;
    public final String DataProvider;
    public final SqlConfiguration SqlConfiguration;
    public final String SqlTable;
    public final MessagesConfig Messages;

    public Config(ConfigurationSection configuration)
    {
        super(configuration);

        DataFile = getString("DataFile");
        SubscriptionEndCheckTicks = getInt("SubscriptionEndCheckTicks");
        DataProvider = getString("DataProvider");
        SqlConfiguration = new SqlConfiguration(getSection("Mysql"));
        SqlTable = getString("Mysql.Table");
        Messages = new MessagesConfig(getSection("Messages"));
    }
}