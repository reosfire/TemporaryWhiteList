package ru.reosfire.temporarywhitelist.Configuration;

import org.bukkit.configuration.ConfigurationSection;
import ru.reosfire.temporarywhitelist.Lib.Yaml.Default.SqlConfiguration;
import ru.reosfire.temporarywhitelist.Lib.Yaml.YamlConfig;

public class Config extends YamlConfig
{
    public final String DataFile;
    public final int SubscriptionEndCheckTicks;
    public final String DataProvider;
    public final SqlConfiguration SqlConfiguration;
    public final String SqlTable;
    public final String Translation;
    public final String DurationFormat;
    public final String DateTimeFormat;
    public final int ListPageSize;

    public Config(ConfigurationSection configuration)
    {
        super(configuration);

        DataFile = getString("DataFile");
        SubscriptionEndCheckTicks = getInt("SubscriptionEndCheckTicks");
        DataProvider = getString("DataProvider");
        SqlConfiguration = new SqlConfiguration(getSection("Mysql"));
        SqlTable = getString("Mysql.Table");
        Translation = getString("Translation");
        DurationFormat = getString("DurationFormat");
        DateTimeFormat = getString("DateTimeFormat");
        ListPageSize = getInt("ListPageSize");
    }
}