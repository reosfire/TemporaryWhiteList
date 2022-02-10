package ru.reosfire.temporarywhitelist.Lib.Yaml.Default.Wrappers.Text;

import java.util.ArrayList;
import java.util.List;

public class Colorizer
{
    private IColorizer colorizer;
    public Colorizer(IColorizer colorizer)
    {
        this.colorizer = colorizer;
    }

    public String Colorize(String input)
    {
        return colorizer.Colorize(input);
    }
    public List<String> Colorize(List<String> inputs)
    {
        List<String> result = new ArrayList<>(inputs.size());
        for (String input : inputs)
        {
            result.add(Colorize(input));
        }
        return result;
    }
    public List<String> Colorize(String... inputs)
    {
        List<String> result = new ArrayList<>(inputs.length);
        for (String input : inputs)
        {
            result.add(Colorize(input));
        }
        return result;
    }
}