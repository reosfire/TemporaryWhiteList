package ru.reosfire.twl.spigot.configuration.localization;

import org.bukkit.configuration.ConfigurationSection;
import ru.reosfire.twl.spigot.lib.yaml.YamlConfig;

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