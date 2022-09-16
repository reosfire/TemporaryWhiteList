package ru.reosfire.temporarywhitelist.configuration.localization.commandResults;

import org.bukkit.configuration.ConfigurationSection;
import ru.reosfire.temporarywhitelist.lib.yaml.common.text.MultilineMessage;
import ru.reosfire.temporarywhitelist.lib.yaml.YamlConfig;

public class ReloadCommandResultsConfig extends YamlConfig
{
    public final MultilineMessage Success;
    public final MultilineMessage Error;

    public ReloadCommandResultsConfig(ConfigurationSection configurationSection)
    {
        super(configurationSection);

        Success = getMultilineMessage("Success");
        Error = getMultilineMessage("Error");
    }
}