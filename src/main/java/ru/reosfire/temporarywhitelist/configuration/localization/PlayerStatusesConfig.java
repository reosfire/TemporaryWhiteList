package ru.reosfire.temporarywhitelist.configuration.localization;

import org.bukkit.configuration.ConfigurationSection;
import ru.reosfire.temporarywhitelist.lib.yaml.YamlConfig;

public class PlayerStatusesConfig extends YamlConfig
{
    public final String Undefined;
    public final String NeverEnd;
    public final String Ended;

    public PlayerStatusesConfig(ConfigurationSection configurationSection)
    {
        super(configurationSection);

        Undefined = getColoredString("Undefined");
        NeverEnd = getColoredString("NeverEnd");
        Ended = getColoredString("Ended");
    }
}