package ru.reosfire.twl.common.configuration.localization.commandResults;

import ru.reosfire.twl.common.lib.yaml.ConfigSection;
import ru.reosfire.twl.common.lib.yaml.YamlConfig;
import ru.reosfire.twl.common.lib.yaml.common.text.MultilineMessage;

public class CheckCommandResultsConfig extends YamlConfig {
    public final MultilineMessage Usage;
    public final MultilineMessage Format;
    public final MultilineMessage ForPlayerOnly;
    public final MultilineMessage InfoNotFound;
    public final String PermanentTrue;
    public final String PermanentFalse;

    public CheckCommandResultsConfig(ConfigSection configurationSection) {
        super(configurationSection);

        Usage = getMultilineMessage("Usage");
        Format = getMultilineMessage("Format");
        ForPlayerOnly = getMultilineMessage("ForPlayerOnly");
        InfoNotFound = getMultilineMessage("InfoNotFound");
        PermanentTrue = getString("Permanent.True");
        PermanentFalse = getString("Permanent.False");
    }
}