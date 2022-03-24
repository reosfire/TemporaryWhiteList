package ru.reosfire.temporarywhitelist.Lib.Text;

import java.util.ArrayList;
import java.util.List;

public class Replacement
{
    private final String _from;
    private final String _to;

    public Replacement(String from, String to)
    {
        _from = from;
        _to = to;
    }

    public String Set(String Input)
    {
        if (Input == null) return null;
        if (_from == null) return Input;
        if (_to == null) return Input;
        return Input.replace(_from, _to);
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
        List<String> result = new ArrayList<>();
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
}