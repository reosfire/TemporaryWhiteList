package ru.reosfire.temporarywhitelist.configuration.localization;

import org.bukkit.configuration.ConfigurationSection;
import ru.reosfire.temporarywhitelist.lib.yaml.YamlConfig;

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