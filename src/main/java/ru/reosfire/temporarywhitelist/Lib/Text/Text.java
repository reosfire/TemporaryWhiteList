package ru.reosfire.temporarywhitelist.Lib.Text;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.List;

public class Text
{
    public static String SetColors(String input)
    {
        if (input == null) return null;
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static List<String> SetColors(List<String> input)
    {
        if (input == null) return null;
        ArrayList<String> result = new ArrayList<>();
        for (String s : input)
        {
            result.add(SetColors(s));
        }
        return result;
    }

    public static String SetPlaceholders(OfflinePlayer player, String input)
    {
        if (input == null) return null;
        return PlaceholderAPI.setPlaceholders(player, input);
    }
    public static String SetPlaceholders(OfflinePlayer player, OfflinePlayer player2, String input)
    {
        if (input == null) return null;
        String relSet = PlaceholderAPI.setRelationalPlaceholders(player.getPlayer(), player2.getPlayer(), input);
        return SetPlaceholders(player, relSet);
    }

    public static String Colorize(OfflinePlayer player, String input, Replacement... replacements)
    {
        String result = SetPlaceholders(player, input);
        result = Replacement.Set(result, replacements);
        return SetColors(result);
    }
    public static List<String> Colorize(OfflinePlayer player, List<String> input, Replacement... replacements)
    {
        List<String> result = new ArrayList<>(input.size());
        for (String s : input)
        {
            result.add(Colorize(player, s, replacements));
        }
        return result;
    }

    public static String Colorize(OfflinePlayer player, OfflinePlayer player1, String input, Replacement... replacements)
    {
        String result = SetPlaceholders(player, player1, input);
        result = Replacement.Set(result, replacements);
        return SetColors(result);
    }
    public static List<String> Colorize(OfflinePlayer player, OfflinePlayer player1, List<String> input, Replacement... replacements)
    {
        List<String> result = new ArrayList<>(input.size());
        for (String s : input)
        {
            result.add(Colorize(player, player1, s, replacements));
        }
        return result;
    }
}