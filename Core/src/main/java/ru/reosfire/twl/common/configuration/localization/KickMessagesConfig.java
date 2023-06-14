package ru.reosfire.twl.common.configuration.localization;

import ru.reosfire.twl.common.lib.yaml.ConfigSection;
import ru.reosfire.twl.common.lib.yaml.YamlConfig;

import java.util.List;

public class KickMessagesConfig extends YamlConfig {
    public final List<String> Connecting;
    public final List<String> WhilePlaying;

    public KickMessagesConfig(ConfigSection configurationSection) {
        super(configurationSection);

        Connecting = getStringList("Connecting");
        WhilePlaying = getStringList("WhilePlaying");
    }
}