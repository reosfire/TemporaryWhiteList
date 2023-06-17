package ru.reosfire.twl.spigot;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import ru.reosfire.twl.common.lib.text.Replacement;

import java.util.ArrayList;
import java.util.List;

public class Text
{
    public static boolean placeholderApiEnabled;

    public static String setColors(String input)
    {
        if (input == null) return null;
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static List<String> setColors(List<String> input)
    {
        if (input == null) return null;
        ArrayList<String> result = new ArrayList<>();
        for (String s : input)
        {
            result.add(setColors(s));
        }
        return result;
    }

    public static String setPlaceholders(OfflinePlayer player, String input)
    {
        if (!placeholderApiEnabled) return input;
        if (input == null) return null;
        return PlaceholderAPI.setPlaceholders(player, input);
    }

    public static String colorize(OfflinePlayer player, String input, Replacement... replacements)
    {
        String result = setPlaceholders(player, input);
        result = Replacement.set(result, replacements);
        return setColors(result);
    }
    public static List<String> colorize(OfflinePlayer player, List<String> input, Replacement... replacements)
    {
        List<String> result = new ArrayList<>(input.size());
        for (String s : input)
        {
            result.add(colorize(player, s, replacements));
        }
        return result;
    }
}