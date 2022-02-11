package ru.reosfire.temporarywhitelist.Configuration;

import org.bukkit.configuration.ConfigurationSection;
import ru.reosfire.temporarywhitelist.Lib.Yaml.YamlConfig;

public class DatabaseMessagesConfig extends YamlConfig
{
    public final String PlayerUndefined;
    public final String SubscribeNeverEnd;
    public final String SubscribeEnd;

    public DatabaseMessagesConfig(ConfigurationSection configurationSection)
    {
        super(configurationSection);

        PlayerUndefined = getColoredString("PlayerUndefined");
        SubscribeNeverEnd = getColoredString("SubscribeNeverEnd");
        SubscribeEnd = getColoredString("SubscribeEnd");
    }
}