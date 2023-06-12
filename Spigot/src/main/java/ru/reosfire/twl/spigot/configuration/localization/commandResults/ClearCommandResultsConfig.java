package ru.reosfire.twl.spigot.configuration.localization.commandResults;

import org.bukkit.configuration.ConfigurationSection;
import ru.reosfire.twl.spigot.lib.yaml.YamlConfig;
import ru.reosfire.twl.spigot.lib.yaml.common.text.MultilineMessage;

public class ClearCommandResultsConfig extends YamlConfig {
    MultilineMessage Confirmation;

    public ClearCommandResultsConfig(ConfigurationSection configurationSection) {
        super(configurationSection);

        Confirmation = getMultilineMessage("Confirmation");
    }
}