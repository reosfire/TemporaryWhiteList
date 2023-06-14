package ru.reosfire.twl.common.lib.yaml.common.text;

import ru.reosfire.twl.common.lib.yaml.ConfigSection;
import ru.reosfire.twl.common.lib.yaml.YamlConfig;

public class HoverConfig extends YamlConfig {
    public final String Action;
    public final String Value;
    public HoverConfig(ConfigSection configurationSection) {
        super(configurationSection);

        Action = getString("Action");
        Value = getString("Value");
    }
}