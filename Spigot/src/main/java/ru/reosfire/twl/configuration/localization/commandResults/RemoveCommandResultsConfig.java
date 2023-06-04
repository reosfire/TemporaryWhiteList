package ru.reosfire.twl.configuration.localization.commandResults;

import org.bukkit.configuration.ConfigurationSection;
import ru.reosfire.twl.lib.yaml.YamlConfig;
import ru.reosfire.twl.lib.yaml.common.text.MultilineMessage;

public class RemoveCommandResultsConfig extends YamlConfig
{
    public final MultilineMessage Usage;
    public final MultilineMessage NothingChanged;
    public final MultilineMessage Success;
    public final MultilineMessage Error;

    public RemoveCommandResultsConfig(ConfigurationSection configurationSection)
    {
        super(configurationSection);

        Usage = getMultilineMessage("Usage");
        NothingChanged = getMultilineMessage("NothingChanged");
        Success = getMultilineMessage("Success");
        Error = getMultilineMessage("Error");
    }
}