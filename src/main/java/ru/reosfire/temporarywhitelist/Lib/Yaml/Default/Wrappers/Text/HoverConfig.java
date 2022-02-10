package ru.reosfire.temporarywhitelist.Lib.Yaml.Default.Wrappers.Text;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import ru.reosfire.temporarywhitelist.Lib.Text.Replacement;
import ru.reosfire.temporarywhitelist.Lib.Text.Text;
import ru.reosfire.temporarywhitelist.Lib.Yaml.Default.Wrappers.WrapperConfig;
import ru.reosfire.temporarywhitelist.Lib.Yaml.YamlConfig;

public class HoverConfig extends YamlConfig implements WrapperConfig<HoverEvent>
{
    public final HoverEvent.Action Action;
    public final String Value; //TODO implement all possibilities
    public HoverConfig(ConfigurationSection configurationSection)
    {
        super(configurationSection);
        Action = HoverEvent.Action.valueOf(getString("Action"));
        Value = getColoredString("Value");
    }

    public HoverEvent Unwrap(OfflinePlayer player, Replacement... replacements)
    {
        return new HoverEvent(Action, new BaseComponent[] {new TextComponent(Text.Colorize(player, Value, replacements))});
    }

    @Override
    public HoverEvent Unwrap()
    {
        return new HoverEvent(Action, new BaseComponent[] {new TextComponent(Value)});
    }
}