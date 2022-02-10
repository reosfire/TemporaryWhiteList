package ru.reosfire.temporarywhitelist.Lib.Text;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Replacement
{
    private String From;
    private String To;

    public Replacement(String from, String to)
    {
        From = from;
        To = to;
    }

    public String Set(String Input)
    {
        if (Input == null) return null;
        if (From == null) return Input;
        if (To == null) return Input;
        return Input.replace(From, To);
    }

    public TextComponent Set(TextComponent Input)
    {
        if (Input == null) return null;
        if (From == null) return Input;
        if (To == null) return Input;

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
            result.setClickEvent(this.Set(result.getClickEvent()));
            result.setHoverEvent(this.Set(result.getHoverEvent()));
            return result;
        }

        result.setText(text.substring(0, startIndex));
        HoverEvent resultHoverEvent = this.Set(result.getHoverEvent());
        ClickEvent resultClickEvent = result.getClickEvent();
        result.setHoverEvent(this.Set(result.getHoverEvent()));
        result.setClickEvent(this.Set(result.getClickEvent()));

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

    public ClickEvent Set(ClickEvent event)
    {
        if (event != null)
        {
            String resultClickValue = this.Set(event.getValue());
            return new ClickEvent(event.getAction(), resultClickValue);
        }
        else { return null; }
    }

    public static String Set(String message, Replacement replacement)
    {
        return replacement.Set(message);
    }

    public static String Set(String message, Replacement... replacements)
    {
        if (replacements == null) return message;
        for (Replacement replacement : replacements)
        {
            message = replacement.Set(message);
        }
        return message;
    }

    public static Iterable<String> Set(Iterable<String> messages, Replacement replacement)
    {
        LinkedList<String> result = new LinkedList<>();
        for (String message : messages)
        {
            result.add(replacement.Set(message));
        }
        return result;
    }

    public static Iterable<String> Set(Iterable<String> messages, Replacement... replacements)
    {
        if (replacements == null) return messages;
        Iterable<String> temp = messages;
        for (Replacement replacement : replacements)
        {
            temp = Set(messages, replacement);
        }
        return temp;
    }

    public static TextComponent SetToJson(TextComponent message, Replacement replacement)
    {
        return replacement.Set(message);
    }

    public static TextComponent SetToJson(TextComponent message, Replacement... replacements)
    {
        if (replacements == null) return message;
        for (Replacement replacement : replacements)
        {
            message = replacement.Set(message);
        }
        return message;
    }

    public static Iterable<TextComponent> SetToJson(Iterable<TextComponent> messages, Replacement replacement)
    {
        LinkedList<TextComponent> result = new LinkedList<>();
        for (TextComponent message : messages)
        {
            result.add(replacement.Set(message));
        }
        return result;
    }

    public static Iterable<TextComponent> SetToJson(Iterable<TextComponent> messages, Replacement... replacements)
    {
        if (replacements == null) return messages;
        Iterable<TextComponent> temp = messages;
        for (Replacement replacement : replacements)
        {
            temp = SetToJson(messages, replacement);
        }
        return temp;
    }
}