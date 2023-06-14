package ru.reosfire.twl.common.configuration.localization.commandResults;

import ru.reosfire.twl.common.lib.yaml.ConfigSection;
import ru.reosfire.twl.common.lib.yaml.YamlConfig;
import ru.reosfire.twl.common.lib.yaml.common.text.MultilineMessage;

public class EnableCommandResultsConfig extends YamlConfig {
    public final MultilineMessage Success;
    public final MultilineMessage NothingChanged;
    public final MultilineMessage Error;

    public EnableCommandResultsConfig(ConfigSection configurationSection) {
        super(configurationSection);

        Success = getMultilineMessage("Success");
        NothingChanged = getMultilineMessage("NothingChanged");
        Error = getMultilineMessage("Error");
    }
}