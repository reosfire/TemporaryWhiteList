package ru.reosfire.twl.common.configuration.localization.commandResults;

import ru.reosfire.twl.common.lib.yaml.ConfigSection;
import ru.reosfire.twl.common.lib.yaml.YamlConfig;
import ru.reosfire.twl.common.lib.yaml.common.text.MultilineMessage;

public class ReloadCommandResultsConfig extends YamlConfig
{
    public final MultilineMessage Success;
    public final MultilineMessage Error;

    public ReloadCommandResultsConfig(ConfigSection configurationSection)
    {
        super(configurationSection);

        Success = getMultilineMessage("Success");
        Error = getMultilineMessage("Error");
    }
}