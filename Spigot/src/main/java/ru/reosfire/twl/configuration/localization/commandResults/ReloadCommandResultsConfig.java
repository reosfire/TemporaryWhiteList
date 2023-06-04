package ru.reosfire.twl.configuration.localization.commandResults;

import org.bukkit.configuration.ConfigurationSection;
import ru.reosfire.twl.lib.yaml.YamlConfig;
import ru.reosfire.twl.lib.yaml.common.text.MultilineMessage;

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