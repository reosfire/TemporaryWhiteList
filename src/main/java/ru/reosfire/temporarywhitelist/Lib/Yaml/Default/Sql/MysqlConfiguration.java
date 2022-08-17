package ru.reosfire.temporarywhitelist.Lib.Yaml.Default.Sql;

import org.bukkit.configuration.ConfigurationSection;
import ru.reosfire.temporarywhitelist.Lib.Sql.ISqlConfiguration;
import ru.reosfire.temporarywhitelist.Lib.Sql.SqlRequirementsNotSatisfiedException;
import ru.reosfire.temporarywhitelist.Lib.Yaml.YamlConfig;

public class MysqlConfiguration extends YamlConfig implements ISqlConfiguration
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
    public MysqlConfiguration(ConfigurationSection configurationSection)
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
        MaxReconnects = getInt("MaxReconnects", 8);
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
    public String getConnectionString()
    {
        return "jdbc:mysql://" + Ip + ":" + Port + "/" + Database
                + "?useSSL=" + (UseSsl ? "true" : "false")
                + "&useUnicode=" + (UseUnicode ? "true" : "false")
                + "&autoReconnect=" + (AutoReconnect ? "true" : "false")
                + "&failOverReadOnly=" + (FailOverReadOnly ? "true" : "false")
                + "&maxReconnects=" + MaxReconnects;
    }

    @Override
    public void CheckRequirements() throws SqlRequirementsNotSatisfiedException
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            throw new SqlRequirementsNotSatisfiedException(e);
        }
    }
}