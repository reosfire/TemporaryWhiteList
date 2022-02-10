package ru.reosfire.temporarywhitelist.Lib.Yaml.Default;

import org.bukkit.configuration.ConfigurationSection;
import ru.reosfire.temporarywhitelist.Lib.Yaml.YamlConfig;

import java.util.List;

public class ScoreBoardConfig extends YamlConfig
{
    public final String Name;
    public final List<String> Lines;
    public final int UpdateInterval;

    public ScoreBoardConfig(ConfigurationSection configurationSection)
    {
        super(configurationSection);
        Name = getString("Name");
        Lines = getStringList("Lines");
        UpdateInterval = getInt("UpdateInterval");
    }
}