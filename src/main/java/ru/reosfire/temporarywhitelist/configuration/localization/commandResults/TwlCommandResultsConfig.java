package ru.reosfire.temporarywhitelist.configuration.localization.commandResults;

import org.bukkit.configuration.ConfigurationSection;
import ru.reosfire.temporarywhitelist.lib.yaml.common.text.MultilineMessage;
import ru.reosfire.temporarywhitelist.lib.yaml.YamlConfig;

public class TwlCommandResultsConfig extends YamlConfig
{
    public final MultilineMessage Usage;

    public TwlCommandResultsConfig(ConfigurationSection configurationSection)
    {
        super(configurationSection);
        Usage = getMultilineMessage("Usage");
    }
}