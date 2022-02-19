package ru.reosfire.temporarywhitelist.Lib.Text;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class JsonReplacement
{
    private final String From;
    private final TextComponent To;

    public JsonReplacement(String from, TextComponent to)
    {
        From = from;
        To = to;
    }

    public TextComponent Set(TextComponent Input)
    {
        if (Input == null) return null;
        if (From == null) return Input;

        TextComponent result = new TextComponent();
        result.setText(Input.getText());
        result.copyFormatting(Input);

        List<BaseComponent> inputExtra = Input.getExtra();
        if (inputExtra != null)
        {
            for (BaseComponent baseComponent : inputExtra)
            {
                if (baseComponent instanceof TextComponent)
                {
                    TextComponent innerText = (TextComponent) baseComponent;
                    result.addExtra(Set(innerText));
                }
            }
        }

        String text = Input.getText();
        int startIndex = text.indexOf(From);
        int length = From.length();
        if (startIndex == -1 || length < 1)
        {
            result.setHoverEvent(this.Set(result.getHoverEvent()));
            return result;
        }

        result.setText(text.substring(0, startIndex));
        HoverEvent resultHoverEvent = this.Set(result.getHoverEvent());
        ClickEvent resultClickEvent = result.getClickEvent();
        result.setHoverEvent(this.Set(result.getHoverEvent()));

        TextComponent rightPart = new TextComponent();
        rightPart.copyFormatting(Input);
        rightPart.setText(text.substring(startIndex + length));
        rightPart.setClickEvent(resultClickEvent);
        rightPart.setHoverEvent(resultHoverEvent);

        result.addExtra(To);
        if (startIndex + length < text.length()) result.addExtra(rightPart);

        return result;
    }

    public HoverEvent Set(HoverEvent event)
    {
        if (event != null)
        {
            ArrayList<BaseComponent> resultHoverValue = new ArrayList<>();
            for (BaseComponent valueComponent : event.getValue())
            {
                if (valueComponent instanceof TextComponent)
                {
                    TextComponent textValue = (TextComponent) valueComponent;
                    resultHoverValue.add(this.Set(textValue));
                }
            }
            return new HoverEvent(event.getAction(), resultHoverValue.toArray(new BaseComponent[0]));
        }
        else { return null; }
    }

    public static TextComponent Set(TextComponent message, JsonReplacement replacement)
    {
        return replacement.Set(message);
    }

    public static TextComponent Set(TextComponent message, JsonReplacement... replacements)
    {
        if (replacements == null) return message;
        for (JsonReplacement replacement : replacements)
        {
            message = replacement.Set(message);
        }
        return message;
    }

    public static Iterable<TextComponent> Set(Iterable<TextComponent> messages, JsonReplacement replacement)
    {
        LinkedList<TextComponent> result = new LinkedList<>();
        for (TextComponent message : messages)
        {
            result.add(replacement.Set(message));
        }
        return result;
    }

    public static Iterable<TextComponent> Set(Iterable<TextComponent> messages, JsonReplacement... replacements)
    {
        if (replacements == null) return messages;
        Iterable<TextComponent> temp = messages;
        for (JsonReplacement replacement : replacements)
        {
            temp = Set(messages, replacement);
        }
        return temp;
    }
}