package ru.reosfire.temporarywhitelist.configuration.localization.commandResults;

import org.bukkit.configuration.ConfigurationSection;
import ru.reosfire.temporarywhitelist.lib.yaml.common.text.MultilineMessage;
import ru.reosfire.temporarywhitelist.lib.yaml.YamlConfig;

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