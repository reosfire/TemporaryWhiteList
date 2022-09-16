package ru.reosfire.temporarywhitelist.configuration.localization.commandResults;

import org.bukkit.configuration.ConfigurationSection;
import ru.reosfire.temporarywhitelist.lib.yaml.common.text.MultilineMessage;
import ru.reosfire.temporarywhitelist.lib.yaml.YamlConfig;

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