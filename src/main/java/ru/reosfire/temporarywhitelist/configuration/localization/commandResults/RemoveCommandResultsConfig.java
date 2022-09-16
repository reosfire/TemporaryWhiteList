package ru.reosfire.temporarywhitelist.configuration.localization.commandResults;

import org.bukkit.configuration.ConfigurationSection;
import ru.reosfire.temporarywhitelist.lib.yaml.common.text.MultilineMessage;
import ru.reosfire.temporarywhitelist.lib.yaml.YamlConfig;

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