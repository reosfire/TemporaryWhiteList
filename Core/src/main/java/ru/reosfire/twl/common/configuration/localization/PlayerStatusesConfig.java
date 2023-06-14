package ru.reosfire.twl.common.configuration.localization;

import ru.reosfire.twl.common.lib.yaml.ConfigSection;
import ru.reosfire.twl.common.lib.yaml.YamlConfig;

public class PlayerStatusesConfig extends YamlConfig {
    public final String Undefined;
    public final String NeverEnd;
    public final String Ended;

    public PlayerStatusesConfig(ConfigSection configurationSection) {
        super(configurationSection);

        Undefined = getString("Undefined");
        NeverEnd = getString("NeverEnd");
        Ended = getString("Ended");
    }
}