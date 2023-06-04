package ru.reosfire.twl.spigot.configuration.localization.commandResults;

import org.bukkit.configuration.ConfigurationSection;
import ru.reosfire.twl.spigot.lib.yaml.YamlConfig;
import ru.reosfire.twl.spigot.lib.yaml.common.text.MultilineMessage;

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