package ru.reosfire.twl.common.configuration.localization.commandResults;

import ru.reosfire.twl.common.lib.yaml.ConfigSection;
import ru.reosfire.twl.common.lib.yaml.YamlConfig;
import ru.reosfire.twl.common.lib.yaml.common.text.MultilineMessage;

public class SetCommandResultsConfig extends YamlConfig {
    public final MultilineMessage Usage;
    public final MultilineMessage NothingChanged;
    public final MultilineMessage Success;
    public final MultilineMessage Error;
    public final MultilineMessage IncorrectTime;

    public SetCommandResultsConfig(ConfigSection configurationSection) {
        super(configurationSection);

        Usage = getMultilineMessage("Usage");
        NothingChanged = getMultilineMessage("NothingChanged");
        Success = getMultilineMessage("Success");
        Error = getMultilineMessage("Error");
        IncorrectTime = getMultilineMessage("IncorrectTime");
    }
}