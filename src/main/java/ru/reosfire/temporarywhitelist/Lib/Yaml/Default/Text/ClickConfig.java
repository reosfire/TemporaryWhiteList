package ru.reosfire.temporarywhitelist.Lib.Yaml.Default.Text;

import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.configuration.ConfigurationSection;
import ru.reosfire.temporarywhitelist.Lib.Text.IColorizer;
import ru.reosfire.temporarywhitelist.Lib.Yaml.YamlConfig;

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
        return new ClickEvent(Action, colorizer.Colorize(Value));
    }
}