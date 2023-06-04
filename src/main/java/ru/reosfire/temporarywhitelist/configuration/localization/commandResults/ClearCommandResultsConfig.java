package ru.reosfire.temporarywhitelist.configuration.localization.commandResults;

import org.bukkit.configuration.ConfigurationSection;
import ru.reosfire.temporarywhitelist.lib.yaml.YamlConfig;
import ru.reosfire.temporarywhitelist.lib.yaml.common.text.MultilineMessage;

public class ClearCommandResultsConfig extends YamlConfig {
    MultilineMessage Confirmation;

    public ClearCommandResultsConfig(ConfigurationSection configurationSection) {
        super(configurationSection);

        Confirmation = getMultilineMessage("Confirmation");
    }
}