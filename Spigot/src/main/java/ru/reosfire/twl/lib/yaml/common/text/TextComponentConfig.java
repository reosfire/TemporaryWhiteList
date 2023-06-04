package ru.reosfire.twl.lib.yaml.common.text;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import ru.reosfire.twl.lib.text.IColorizer;
import ru.reosfire.twl.lib.text.Replacement;
import ru.reosfire.twl.lib.text.Text;
import ru.reosfire.twl.lib.yaml.YamlConfig;

import java.util.List;
import java.util.Locale;

public class TextComponentConfig extends YamlConfig
{
    public final String TextContent;
    public final List<TextComponentConfig> Content;
    public final ClickConfig ClickConfig;
    public final HoverConfig HoverConfig;
    public final ChatColor Color;
    public final boolean Bold;
    public final boolean Italic;
    public final boolean Strikethrough;
    public final boolean Underlined;

    public TextComponentConfig(ConfigurationSection configurationSection)
    {
        super(configurationSection);
        if (isList("Content"))
        {
            Content = getList(TextComponentConfig::new, "Content");
            TextContent = null;
        }
        else
        {
            TextContent = getColoredString("Content", null);
            Content = null;
        }

        ConfigurationSection clickSection = getSection("Click", null);
        ClickConfig = clickSection == null ? null : new ClickConfig(clickSection);
        ConfigurationSection hoverSection = getSection("Hover", null);
        HoverConfig = hoverSection == null ? null : new HoverConfig(hoverSection);

        String color = getString("Color");
        Color = color == null ? null : ChatColor.valueOf(color.toUpperCase(Locale.ROOT));

        Bold = getBoolean("Bold", false);
        Italic = getBoolean("Italic", false);
        Strikethrough = getBoolean("Strikethrough", false);
        Underlined = getBoolean("Underlined", false);
    }

    public void Send(CommandSender receiver, Replacement... replacements)
    {
        if (receiver instanceof Player)
        {
            Player player = (Player) receiver;
            player.spigot().sendMessage(Unwrap(player, replacements));
        }
        else receiver.sendMessage(toString(replacements));
    }

    public TextComponent Unwrap(OfflinePlayer player, Replacement... replacements)
    {
        return Unwrap(s -> Text.colorize(player, s, replacements));
    }

    public TextComponent Unwrap(IColorizer colorizer)
    {
        TextComponent result;
        if (Content == null) result = new TextComponent(TextComponent.fromLegacyText(colorizer.colorize(TextContent)));
        else
        {
            TextComponent[] subComponents = new TextComponent[Content.size()];
            for (int i = 0; i < subComponents.length; i++)
                subComponents[i] = Content.get(i).Unwrap(colorizer);
            result = new TextComponent(subComponents);
        }

        if (ClickConfig != null) result.setClickEvent(ClickConfig.Unwrap(colorizer));
        if (HoverConfig != null) result.setHoverEvent(HoverConfig.Unwrap(colorizer));

        if (Color != null) result.setColor(Color);

        result.setBold(Bold);
        result.setItalic(Italic);
        result.setUnderlined(Underlined);
        result.setStrikethrough(Strikethrough);

        return result;
    }

    public String toString(Replacement... replacements)
    {
        if (TextContent != null) return Text.setColors(Replacement.set(TextContent, replacements));

        StringBuilder resultBuilder = new StringBuilder();

        for (TextComponentConfig subComponent : Content)
        {
            resultBuilder.append(subComponent.toString(replacements));
        }

        return Text.setColors(resultBuilder.toString());
    }

    @Override
    public String toString()
    {
        if (TextContent != null) return TextContent;

        StringBuilder resultBuilder = new StringBuilder();

        for (TextComponentConfig subComponent : Content)
        {
            resultBuilder.append(subComponent.toString());
        }

        return resultBuilder.toString();
    }
}