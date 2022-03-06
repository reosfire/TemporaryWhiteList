package ru.reosfire.temporarywhitelist.Lib.Yaml.Default;

import org.bukkit.configuration.ConfigurationSection;
import ru.reosfire.temporarywhitelist.Lib.Sql.ISqlConfiguration;
import ru.reosfire.temporarywhitelist.Lib.Yaml.YamlConfig;

public class SqlConfiguration extends YamlConfig implements ISqlConfiguration
{
    public final String Ip;
    public final String User;
    public final String Password;
    public final String Database;
    public final boolean UseSsl, UseUnicode, AutoReconnect, FailOverReadOnly;
    public final int Port, MaxReconnects;

    /**
     * @param configurationSection Ip, Port(3306), User, Password, Database, UseSsl(false), UseUnicode(true)
     *                             AutoReconnect(true), FailOverReadOnly(false), MaxReconnects(8)
     */
    public SqlConfiguration(ConfigurationSection configurationSection)
    {
        super(configurationSection);
        Ip = getString("Ip");
        Port = getInt("Port", 3306);
        User = getString("User");
        Password = getString("Password");
        Database = getString("Database");
        UseSsl = getBoolean("UseSsl", false);
        UseUnicode = getBoolean("UseUnicode", true);
        AutoReconnect = getBoolean("AutoReconnect", true);
        FailOverReadOnly = getBoolean("FailOverReadOnly", false);
        MaxReconnects = getInt("MaxReconnects", 2);
    }

    @Override
    public String getIp()
    {
        return Ip;
    }

    @Override
    public String getUser()
    {
        return User;
    }

    @Override
    public String getPassword()
    {
        return Password;
    }

    @Override
    public String getDatabase()
    {
        return Database;
    }

    @Override
    public boolean getUseSsl()
    {
        return UseSsl;
    }

    @Override
    public boolean getUseUnicode()
    {
        return UseUnicode;
    }

    @Override
    public boolean getAutoReconnect()
    {
        return AutoReconnect;
    }

    @Override
    public boolean getFailOverReadOnly()
    {
        return FailOverReadOnly;
    }

    @Override
    public int getMaxReconnects()
    {
        return MaxReconnects;
    }

    @Override
    public int getPort()
    {
        return Port;
    }
}