package ru.reosfire.twl.common.lib.yaml.common.text;

import ru.reosfire.twl.common.lib.yaml.ConfigSection;
import ru.reosfire.twl.common.lib.yaml.YamlConfig;

public class ClickConfig extends YamlConfig
{
    //public final ClickEvent.Action Action;
    public final String Value;
    public ClickConfig(ConfigSection configurationSection)
    {
        super(configurationSection);
        //Action = ClickEvent.Action.valueOf(getString("Action"));
        Value = getString("Value");
    }
}