package ru.reosfire.temporarywhitelist.Lib.Yaml.Default.Sql;

import org.bukkit.configuration.ConfigurationSection;
import ru.reosfire.temporarywhitelist.Lib.Sql.ISqlConfiguration;
import ru.reosfire.temporarywhitelist.Lib.Sql.SqlRequirementsNotSatisfiedException;
import ru.reosfire.temporarywhitelist.Lib.Yaml.YamlConfig;

public class FileSqlConfiguration extends YamlConfig implements ISqlConfiguration
{
    public final String FilePath;

    public FileSqlConfiguration(ConfigurationSection configurationSection)
    {
        super(configurationSection);
        FilePath = getString("FilePath");
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