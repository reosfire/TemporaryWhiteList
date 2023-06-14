package ru.reosfire.twl.common.configuration.localization.commandResults;

import ru.reosfire.twl.common.lib.yaml.ConfigSection;
import ru.reosfire.twl.common.lib.yaml.YamlConfig;
import ru.reosfire.twl.common.lib.yaml.common.text.MultilineMessage;

public class AddCommandResultsConfig extends YamlConfig
{
    public final MultilineMessage Usage;
    public final MultilineMessage AlreadyPermanent;
    public final MultilineMessage IncorrectTime;

    public final MultilineMessage Success;
    public final MultilineMessage Error;

    public AddCommandResultsConfig(ConfigSection configurationSection)
    {
        super(configurationSection);

        Usage = getMultilineMessage("Usage");
        AlreadyPermanent = getMultilineMessage("AlreadyPermanent");
        IncorrectTime = getMultilineMessage("IncorrectTime");
        Success = getMultilineMessage("Success");
        Error = getMultilineMessage("Error");
    }
}