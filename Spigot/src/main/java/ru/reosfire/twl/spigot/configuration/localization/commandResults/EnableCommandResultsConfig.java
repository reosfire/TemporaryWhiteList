package ru.reosfire.twl.spigot.configuration.localization.commandResults;

import org.bukkit.configuration.ConfigurationSection;
import ru.reosfire.twl.spigot.lib.yaml.YamlConfig;
import ru.reosfire.twl.spigot.lib.yaml.common.text.MultilineMessage;

public class EnableCommandResultsConfig extends YamlConfig
{
    public final MultilineMessage Success;
    public final MultilineMessage NothingChanged;
    public final MultilineMessage Error;

    public EnableCommandResultsConfig(ConfigurationSection configurationSection)
    {
        super(configurationSection);

        Success = getMultilineMessage("Success");
        NothingChanged = getMultilineMessage("NothingChanged");
        Error = getMultilineMessage("Error");
    }
}