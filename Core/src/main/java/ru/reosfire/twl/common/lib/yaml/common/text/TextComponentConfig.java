package ru.reosfire.twl.common.lib.yaml.common.text;

import ru.reosfire.twl.common.lib.text.ColorizersCollection;
import ru.reosfire.twl.common.lib.text.Replacement;
import ru.reosfire.twl.common.lib.yaml.ConfigSection;
import ru.reosfire.twl.common.lib.yaml.YamlConfig;

import java.util.List;

public class TextComponentConfig extends YamlConfig
{
    public final String TextContent;
    public final List<TextComponentConfig> Content;
    public final ClickConfig ClickConfig;
    public final HoverConfig HoverConfig;
    public final String Color;
    public final boolean Bold;
    public final boolean Italic;
    public final boolean Strikethrough;
    public final boolean Underlined;

    public TextComponentConfig(ConfigSection configurationSection)
    {
        super(configurationSection);
        if (isList("Content"))
        {
            Content = getList(TextComponentConfig::new, "Content");
            TextContent = null;
        }
        else
        {
            TextContent = getString("Content", null);
            Content = null;
        }

        ConfigSection clickSection = getSection("Click", null);
        ClickConfig = clickSection == null ? null : new ClickConfig(clickSection);
        ConfigSection hoverSection = getSection("Hover", null);
        HoverConfig = hoverSection == null ? null : new HoverConfig(hoverSection);

        Color = getString("Color", null);

        Bold = getBoolean("Bold", false);
        Italic = getBoolean("Italic", false);
        Strikethrough = getBoolean("Strikethrough", false);
        Underlined = getBoolean("Underlined", false);
    }

    public String toString(Replacement... replacements)
    {
        if (TextContent != null) return ColorizersCollection.shared.apply(Replacement.set(TextContent, replacements));

        StringBuilder resultBuilder = new StringBuilder();

        for (TextComponentConfig subComponent : Content)
        {
            resultBuilder.append(subComponent.toString(replacements));
        }

        return ColorizersCollection.shared.apply(resultBuilder.toString());
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