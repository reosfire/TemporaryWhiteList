package ru.reosfire.twl.spigot.lib.yaml.common.sql;

import org.bukkit.configuration.ConfigurationSection;
import ru.reosfire.twl.common.lib.sql.ISqlConfiguration;
import ru.reosfire.twl.common.lib.sql.SqlRequirementsNotSatisfiedException;
import ru.reosfire.twl.spigot.lib.yaml.YamlConfig;

public class FileSqlConfiguration extends YamlConfig implements ISqlConfiguration
{
    public final String FilePath;
    public final long MaxConnectionLifetime;

    public FileSqlConfiguration(ConfigurationSection configurationSection)
    {
        super(configurationSection);
        FilePath = getString("FilePath");
        MaxConnectionLifetime = getLong("MaxConnectionLifetime", 550000);
    }

    @Override
    public String getUser()
    {
        return null;
    }

    @Override
    public String getPassword()
    {
        return null;
    }

    @Override
    public String getConnectionString()
    {
        return "jdbc:sqlite:" + FilePath;
    }

    @Override
    public long getMaxConnectionLifetime()
    {
        return MaxConnectionLifetime;
    }

    @Override
    public void CheckRequirements() throws SqlRequirementsNotSatisfiedException
    {
        try
        {
            Class.forName("org.sqlite.JDBC");
        }
        catch (ClassNotFoundException e)
        {
            throw new SqlRequirementsNotSatisfiedException(e);
        }
    }
}