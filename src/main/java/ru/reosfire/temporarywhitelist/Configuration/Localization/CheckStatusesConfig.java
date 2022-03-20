package ru.reosfire.temporarywhitelist.Configuration.Localization;

import org.bukkit.configuration.ConfigurationSection;
import ru.reosfire.temporarywhitelist.Lib.Yaml.YamlConfig;

public class CheckStatusesConfig extends YamlConfig
{
    public final String PlayerUndefined;
    public final String SubscribeNeverEnd;
    public final String SubscribeEnd;
    public final String PermanentTrue;
    public final String PermanentFalse;

    public CheckStatusesConfig(ConfigurationSection configurationSection)
    {
        super(configurationSection);

        PlayerUndefined = getColoredString("PlayerUndefined");
        SubscribeNeverEnd = getColoredString("SubscribeNeverEnd");
        SubscribeEnd = getColoredString("SubscribeEnd");
        PermanentTrue = getColoredString("Permanent.True");
        PermanentFalse = getColoredString("Permanent.False");
    }
}