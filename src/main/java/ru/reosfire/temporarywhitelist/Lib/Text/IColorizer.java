package ru.reosfire.temporarywhitelist.Lib.Text;

import java.util.ArrayList;
import java.util.List;

public interface IColorizer
{
    String Colorize(String string);

    static String Colorize(IColorizer colorizer, String input)
    {
        return colorizer.Colorize(input);
    }
    static List<String> Colorize(IColorizer colorizer, List<String> inputs)
    {
        List<String> result = new ArrayList<>(inputs.size());
        for (String input : inputs)
        {
            result.add(Colorize(colorizer, input));
        }
        return result;
    }
    static List<String> Colorize(IColorizer colorizer, String... inputs)
    {
        List<String> result = new ArrayList<>(inputs.length);
        for (String input : inputs)
        {
            result.add(Colorize(colorizer, input));
        }
        return result;
    }
}