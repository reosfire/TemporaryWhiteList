package ru.reosfire.twl.configuration.localization.commandResults;

import org.bukkit.configuration.ConfigurationSection;
import ru.reosfire.twl.lib.yaml.YamlConfig;
import ru.reosfire.twl.lib.yaml.common.text.MultilineMessage;

public class TwlCommandResultsConfig extends YamlConfig
{
    public final MultilineMessage Usage;

    public TwlCommandResultsConfig(ConfigurationSection configurationSection)
    {
        super(configurationSection);
        Usage = getMultilineMessage("Usage");
    }
}