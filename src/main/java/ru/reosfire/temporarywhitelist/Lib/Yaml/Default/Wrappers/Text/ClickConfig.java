package ru.reosfire.temporarywhitelist.Lib.Yaml.Default.Wrappers.Text;

import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
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
        return new ClickEvent(Action, Text.Colorize(player, Value, replacements));
    }

    @Override
    public ClickEvent Unwrap()
    {
        return new ClickEvent(Action, Value);
    }
}