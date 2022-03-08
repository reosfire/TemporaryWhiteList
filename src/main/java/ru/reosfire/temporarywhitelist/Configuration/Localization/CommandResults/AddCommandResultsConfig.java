package ru.reosfire.temporarywhitelist.Configuration.Localization.CommandResults;

import org.bukkit.configuration.ConfigurationSection;
import ru.reosfire.temporarywhitelist.Lib.Yaml.Default.Wrappers.Text.MultilineMessage;
import ru.reosfire.temporarywhitelist.Lib.Yaml.YamlConfig;

public class AddCommandResultsConfig extends YamlConfig
{
    public final MultilineMessage Usage;
    public final MultilineMessage AlreadyPermanent;
    public final MultilineMessage IncorrectTime;

    public final MultilineMessage SuccessfullyAddedPermanent;
    public final MultilineMessage ErrorWhileAddingPermanent;
    public final MultilineMessage SuccessfullyAdded;
    public final MultilineMessage ErrorWhileAdding;

    public AddCommandResultsConfig(ConfigurationSection configurationSection)
    {
        super(configurationSection);

        Usage = getMultilineMessage("Usage");
        AlreadyPermanent = getMultilineMessage("AlreadyPermanent");
        IncorrectTime = getMultilineMessage("IncorrectTime");
        SuccessfullyAddedPermanent = getMultilineMessage("SuccessfullyAddedPermanent");
        ErrorWhileAddingPermanent = getMultilineMessage("ErrorWhileAddingPermanent");
        SuccessfullyAdded = getMultilineMessage("SuccessfullyAdded");
        ErrorWhileAdding = getMultilineMessage("ErrorWhileAdding");
    }
}