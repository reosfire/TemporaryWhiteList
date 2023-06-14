package ru.reosfire.twl.common.configuration.localization.commandResults;

import ru.reosfire.twl.common.lib.yaml.ConfigSection;
import ru.reosfire.twl.common.lib.yaml.YamlConfig;
import ru.reosfire.twl.common.lib.yaml.common.text.MultilineMessage;

public class TwlCommandResultsConfig extends YamlConfig {
    public final MultilineMessage Usage;

    public TwlCommandResultsConfig(ConfigSection configurationSection) {
        super(configurationSection);

        Usage = getMultilineMessage("Usage");
    }
}