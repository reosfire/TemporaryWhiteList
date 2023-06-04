package ru.reosfire.twl.configuration.localization.commandResults;

import org.bukkit.configuration.ConfigurationSection;
import ru.reosfire.twl.lib.yaml.YamlConfig;
import ru.reosfire.twl.lib.yaml.common.text.MultilineMessage;

public class AddCommandResultsConfig extends YamlConfig
{
    public final MultilineMessage Usage;
    public final MultilineMessage AlreadyPermanent;
    public final MultilineMessage IncorrectTime;

    public final MultilineMessage Success;
    public final MultilineMessage Error;

    public AddCommandResultsConfig(ConfigurationSection configurationSection)
    {
        super(configurationSection);

        Usage = getMultilineMessage("Usage");
        AlreadyPermanent = getMultilineMessage("AlreadyPermanent");
        IncorrectTime = getMultilineMessage("IncorrectTime");
        Success = getMultilineMessage("Success");
        Error = getMultilineMessage("Error");
    }
}