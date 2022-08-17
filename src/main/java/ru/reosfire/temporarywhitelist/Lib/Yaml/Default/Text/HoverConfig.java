package ru.reosfire.temporarywhitelist.Lib.Yaml.Default.Text;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.configuration.ConfigurationSection;
import ru.reosfire.temporarywhitelist.Lib.Text.IColorizer;
import ru.reosfire.temporarywhitelist.Lib.Yaml.YamlConfig;

public class HoverConfig extends YamlConfig
{
    public final HoverEvent.Action Action;
    public final String Value;
    public HoverConfig(ConfigurationSection configurationSection)
    {
        super(configurationSection);
        Action = HoverEvent.Action.valueOf(getString("Action"));
        Value = getColoredString("Value");
    }

    public HoverEvent Unwrap(IColorizer colorizer)
    {
        //noinspection deprecation because 1.12.2
        return new HoverEvent(Action, new BaseComponent[] {new TextComponent(colorizer.colorize(Value))});
    }
}