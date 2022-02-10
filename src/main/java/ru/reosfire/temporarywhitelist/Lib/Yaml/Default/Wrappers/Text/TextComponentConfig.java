package ru.reosfire.temporarywhitelist.Lib.Yaml.Default.Wrappers.Text;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import ru.reosfire.temporarywhitelist.Lib.Text.Replacement;
import ru.reosfire.temporarywhitelist.Lib.Text.Text;
import ru.reosfire.temporarywhitelist.Lib.Yaml.Default.Wrappers.WrapperConfig;
import ru.reosfire.temporarywhitelist.Lib.Yaml.YamlConfig;

import java.util.List;

public class TextComponentConfig extends YamlConfig implements WrapperConfig<TextComponent>
{
    public final String TextContent;
    public final List<TextComponentConfig> Content;
    public final ClickConfig ClickConfig;
    public final HoverConfig HoverConfig;

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
    }

    public TextComponent Unwrap(OfflinePlayer player, Replacement... replacements)
    {
        TextComponent result;
        if (Content == null) result = new TextComponent(Text.Colorize(player, TextContent, replacements));
        else
        {
            TextComponent[] subComponents = new TextComponent[Content.size()];
            for (int i = 0; i < subComponents.length; i++)
                subComponents[i] = Content.get(i).Unwrap(player, replacements);
            result = new TextComponent(subComponents);
        }

        if (ClickConfig != null) result.setClickEvent(ClickConfig.Unwrap(player, replacements));
        if (HoverConfig != null) result.setHoverEvent(HoverConfig.Unwrap(player, replacements));

        return result;
    }

    @Override
    public TextComponent Unwrap()
    {
        TextComponent result;
        if (Content == null) result = new TextComponent(TextContent);
        else
        {
            TextComponent[] subComponents = new TextComponent[Content.size()];
            for (int i = 0; i < subComponents.length; i++)
                subComponents[i] = Content.get(i).Unwrap();
            result = new TextComponent(subComponents);
        }

        if (ClickConfig != null) result.setClickEvent(ClickConfig.Unwrap());
        if (HoverConfig != null) result.setHoverEvent(HoverConfig.Unwrap());

        return result;
    }
}