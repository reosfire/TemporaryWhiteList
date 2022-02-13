package ru.reosfire.temporarywhitelist.Configuration.Localization;

import org.bukkit.configuration.ConfigurationSection;
import ru.reosfire.temporarywhitelist.Lib.Yaml.YamlConfig;

import java.util.List;

public class KickMessagesConfig extends YamlConfig
{
    public final List<String> Connecting;
    public final List<String> WhilePlaying;

    public KickMessagesConfig(ConfigurationSection configurationSection)
    {
        super(configurationSection);

        Connecting = getColoredStringList("Connecting");
        WhilePlaying = getColoredStringList("WhilePlaying");
    }
}