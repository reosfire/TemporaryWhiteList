package ru.reosfire.temporarywhitelist.Lib.Yaml.Default.Wrappers.Text;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import ru.reosfire.temporarywhitelist.Lib.Text.IColorizer;
import ru.reosfire.temporarywhitelist.Lib.Text.Replacement;
import ru.reosfire.temporarywhitelist.Lib.Text.Text;
import ru.reosfire.temporarywhitelist.Lib.Yaml.Default.Wrappers.WrapperConfig;
import ru.reosfire.temporarywhitelist.Lib.Yaml.YamlConfig;

public class ClickConfig extends YamlConfig implements WrapperConfig<ClickEvent>
{
    public final ClickEvent.Action Action;
    public final String Value;
    public ClickConfig(ConfigurationSection configurationSection)
    {
        super(configurationSection);
        Action = ClickEvent.Action.valueOf(getString("Action"));
        Value = getColoredString("Value");
    }

    public ClickEvent Unwrap(OfflinePlayer player, Replacement... replacements)
    {
        return Unwrap(s -> Text.Colorize(player, s, replacements));
    }
    public ClickEvent Unwrap(Replacement... replacements)
    {
        return Unwrap(s -> Replacement.Set(s, replacements));
    }

    @Override
    public ClickEvent Unwrap()
    {
        return new ClickEvent(Action, Value);
    }

    public ClickEvent Unwrap(IColorizer colorizer)
    {
        return new ClickEvent(Action, colorizer.Colorize(Value));
    }
}