package ru.reosfire.temporarywhitelist.Lib.Yaml.Default.Wrappers.Text;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import ru.reosfire.temporarywhitelist.Lib.Text.IColorizer;
import ru.reosfire.temporarywhitelist.Lib.Text.Replacement;
import ru.reosfire.temporarywhitelist.Lib.Text.Text;
import ru.reosfire.temporarywhitelist.Lib.Yaml.Default.Wrappers.WrapperConfig;
import ru.reosfire.temporarywhitelist.Lib.Yaml.YamlConfig;

import java.util.List;
import java.util.Locale;

public class TextComponentConfig extends YamlConfig implements WrapperConfig<TextComponent>
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
        if (receiver instanceof Player) ((Player)receiver).spigot().sendMessage(Unwrap(replacements));
        else receiver.sendMessage(toString(replacements));
    }

    public TextComponent Unwrap(Replacement... replacements)
    {
        return Unwrap(s -> Replacement.Set(s, replacements));
    }

    public TextComponent Unwrap(OfflinePlayer player, Replacement... replacements)
    {
        return Unwrap(s -> Text.Colorize(player, s, replacements));
    }

    @Override
    public TextComponent Unwrap()
    {
        return Unwrap(s -> s);
    }

    public TextComponent Unwrap(IColorizer colorizer)
    {
        TextComponent result;
        if (Content == null) result = new TextComponent(colorizer.Colorize(TextContent));
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
        if (TextContent != null) return Replacement.Set(TextContent, replacements);

        StringBuilder resultBuilder = new StringBuilder();

        for (TextComponentConfig subComponent : Content)
        {
            resultBuilder.append(subComponent.toString(replacements));
        }

        return resultBuilder.toString();
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