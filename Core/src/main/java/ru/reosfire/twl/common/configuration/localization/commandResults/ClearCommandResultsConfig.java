package ru.reosfire.twl.common.configuration.localization.commandResults;

import ru.reosfire.twl.common.lib.yaml.ConfigSection;
import ru.reosfire.twl.common.lib.yaml.YamlConfig;
import ru.reosfire.twl.common.lib.yaml.common.text.MultilineMessage;

public class ClearCommandResultsConfig extends YamlConfig {
    MultilineMessage Confirmation;

    public ClearCommandResultsConfig(ConfigSection configurationSection) {
        super(configurationSection);

        Confirmation = getMultilineMessage("Confirmation");
    }
}