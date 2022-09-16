package ru.reosfire.temporarywhitelist.configuration.localization.commandResults;

import org.bukkit.configuration.ConfigurationSection;
import ru.reosfire.temporarywhitelist.lib.yaml.common.text.MultilineMessage;
import ru.reosfire.temporarywhitelist.lib.yaml.YamlConfig;

public class CheckCommandResultsConfig extends YamlConfig
{
    public final MultilineMessage Usage;
    public final MultilineMessage Format;
    public final MultilineMessage ForPlayerOnly;
    public final MultilineMessage InfoNotFound;
    public final String PermanentTrue;
    public final String PermanentFalse;

    public CheckCommandResultsConfig(ConfigurationSection configurationSection)
    {
        super(configurationSection);

        Usage = getMultilineMessage("Usage");
        Format = getMultilineMessage("Format");
        ForPlayerOnly = getMultilineMessage("ForPlayerOnly");
        InfoNotFound = getMultilineMessage("InfoNotFound");
        PermanentTrue = getColoredString("Permanent.True");
        PermanentFalse = getColoredString("Permanent.False");
    }
}