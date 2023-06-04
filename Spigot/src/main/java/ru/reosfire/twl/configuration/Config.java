package ru.reosfire.twl.configuration;

import org.bukkit.configuration.ConfigurationSection;
import ru.reosfire.twl.data.providers.SqlDataProvider;
import ru.reosfire.twl.lib.yaml.YamlConfig;
import ru.reosfire.twl.lib.yaml.common.sql.MysqlConfiguration;

public class Config extends YamlConfig
{
    public final String DataFile;
    public final int SubscriptionEndCheckTicks;
    public final long RefreshAfter;
    public final boolean IgnoreCase;
    public final String DataProvider;
    public final MysqlConfiguration SqlConfiguration;
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
        RefreshAfter = getLong("RefreshAfter");
        IgnoreCase = getBoolean("IgnoreCase", false);
        DataProvider = getString("DataProvider");
        SqlConfiguration = new MysqlConfiguration(getSection("Mysql"));
        SqlTable = getString("Mysql.Table");
        Translation = getString("Translation");
        DurationFormat = getString("DurationFormat");
        DateTimeFormat = getString("DateTimeFormat");
        ListPageSize = getInt("ListPageSize");
    }

    public SqlDataProvider.Configuration getSqlProviderConfiguration() {
        return new SqlDataProvider.Configuration(SqlTable, SqlConfiguration);
    }
}