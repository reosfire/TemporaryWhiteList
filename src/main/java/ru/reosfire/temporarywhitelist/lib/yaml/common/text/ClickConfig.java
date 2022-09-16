package ru.reosfire.temporarywhitelist.lib.yaml.common.text;

import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.configuration.ConfigurationSection;
import ru.reosfire.temporarywhitelist.lib.text.IColorizer;
import ru.reosfire.temporarywhitelist.lib.yaml.YamlConfig;

public class ClickConfig extends YamlConfig
{
    public final ClickEvent.Action Action;
    public final String Value;
    public ClickConfig(ConfigurationSection configurationSection)
    {
        super(configurationSection);
        Action = ClickEvent.Action.valueOf(getString("Action"));
        Value = getColoredString("Value");
    }

    public ClickEvent Unwrap(IColorizer colorizer)
    {
        return new ClickEvent(Action, colorizer.colorize(Value));
    }
}