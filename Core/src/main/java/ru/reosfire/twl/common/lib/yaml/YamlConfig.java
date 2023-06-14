package ru.reosfire.twl.common.lib.yaml;

import org.apache.commons.lang.NullArgumentException;
import ru.reosfire.twl.common.lib.yaml.common.text.MultilineMessage;
import ru.reosfire.twl.common.lib.yaml.common.text.TextComponentConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class YamlConfig
{
    protected final ConfigSection configSection;

    public ConfigSection getSection()
    {
        return configSection;
    }

    public YamlConfig(ConfigSection configSection)
    {
        if (configSection == null) throw new NullArgumentException("data");
        this.configSection = configSection;
    }
    public YamlConfig(Map<String, Object> data)
    {
        this(new ConfigSection(data));
    }

    public <T extends YamlConfig> List<T> getList(IConfigCreator<T> creator, String path)
    {
        List<?> list = (List<?>) getSection().data.get(path);
        if (list == null) return null;

        ArrayList<T> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i++)
        {
            try
            {
                result.add(creator.Create((Map<String, Object>) list.get(i)));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return result;
    }

    public String getString(String path) {
        return getSection().getString(path);
    }
    public String getString(String path, String def) {
        return getSection().getString(path, def);
    }

    public int getInt(String path) {
        return getSection().getInt(path);
    }
    public int getInt(String path, int def) {
        return getSection().getInt(path, def);
    }

    public long getLong(String path) {
        return getSection().getLong(path);
    }
    public long getLong(String path, long def) {
        return getSection().getLong(path, def);
    }

    public boolean getBoolean(String path, boolean def)
    {
        return getSection().getOrDefault(path, Boolean.class, def);
    }

    public ConfigSection getSection(String path) {
        return getSection().getSubsection(path);
    }
    public ConfigSection getSection(String path, ConfigSection def) {
        return getSection().getSubsection(path, def);
    }

    public List<String> getStringList(String path)
    {
        List<String> stringList = (List<String>) getSection().data.get(path);
        if (stringList.isEmpty())
        {
            String string = getString(path);
            if (string != null)
            {
                ArrayList<String> strings = new ArrayList<>();
                strings.add(string);
                return strings;
            }
        }
        return stringList;
    }


    public boolean isList(String path) {
        return getSection().data.get(path) instanceof List;
    }

    public MultilineMessage getMultilineMessage(String path) {
        return new MultilineMessage(getList(TextComponentConfig::new, path));
    }
}