package ru.reosfire.temporarywhitelist.Lib.Yaml;

import org.apache.commons.lang.NullArgumentException;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import ru.reosfire.temporarywhitelist.Lib.Text.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class YamlConfig
{
    public static <T extends YamlConfig> T Create(ConfigurationSection configurationSection, IConfigCreator<T> creator)
    {
        return creator.Create(configurationSection);
    }

    public static YamlConfiguration LoadOrCreate(String resultFileName, String defaultConfigurationResource,
                                                 JavaPlugin plugin) throws IOException, InvalidConfigurationException
    {
        YamlConfiguration config = new YamlConfiguration();
        config.load(LoadOrCreateFile(resultFileName, defaultConfigurationResource, plugin));
        return config;
    }

    public static YamlConfiguration LoadOrCreate(File file) throws IOException, InvalidConfigurationException
    {
        YamlConfiguration config = new YamlConfiguration();
        config.load(file);
        return config;
    }

    public static YamlConfiguration LoadOrCreate(String fileName, JavaPlugin plugin) throws IOException,
            InvalidConfigurationException
    {
        return LoadOrCreate(fileName, fileName, plugin);
    }

    public static File LoadOrCreateFile(String resultFileName, String defaultConfigurationResource,
                                        JavaPlugin plugin) throws IOException
    {
        File configFile = new File(plugin.getDataFolder(), resultFileName);
        if (!configFile.exists())
        {
            configFile.getParentFile().mkdirs();
            InputStream resource = plugin.getResource(defaultConfigurationResource);
            byte[] buffer = new byte[resource.available()];
            resource.read(buffer);

            FileOutputStream fileOutputStream = new FileOutputStream(configFile);
            fileOutputStream.write(buffer);
            fileOutputStream.close();
        }
        return configFile;
    }

    public static File LoadOrCreateFile(String fileName, JavaPlugin plugin) throws IOException
    {
        return LoadOrCreateFile(fileName, fileName, plugin);
    }

    protected final ConfigurationSection _configurationSection;

    public ConfigurationSection getSection()
    {
        return _configurationSection;
    }

    public YamlConfig(ConfigurationSection configurationSection)
    {
        if (configurationSection == null) throw new NullArgumentException("configurationSection");
        this._configurationSection = configurationSection;
    }

    public <T extends YamlConfig> List<T> getNestedConfigs(IConfigCreator<T> creator, String path)
    {
        ArrayList<T> result = new ArrayList<>();
        ConfigurationSection configsParent = getSection(path, null);
        if (configsParent == null) return result;
        return getNestedConfigs(creator, configsParent);
    }
    public <T extends YamlConfig> List<T> getNestedConfigs(IConfigCreator<T> creator)
    {
        return getNestedConfigs(creator, getSection());
    }
    private  <T extends YamlConfig> List<T> getNestedConfigs(IConfigCreator<T> creator, ConfigurationSection section)
    {
        ArrayList<T> result = new ArrayList<>();
        for (String key : section.getKeys(false))
        {
            try
            {
                result.add(creator.Create(section.getConfigurationSection(key)));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return result;
    }
    public <T extends YamlConfig> List<T> getList(IConfigCreator<T> creator, String path)
    {
        List<?> list = getSection().getList(path);
        if (list == null) return null;

        MemoryConfiguration tempConfig = new MemoryConfiguration();
        for (int i = 0; i < list.size(); i++)
        {
            tempConfig.createSection(Integer.toString(i), (Map<?, ?>) list.get(i));
        }

        return getNestedConfigs(creator, tempConfig);
    }

    public String getString(String path)
    {
        return _configurationSection.getString(path);
    }

    public String getString(String path, String def)
    {
        return _configurationSection.getString(path, def);
    }

    public String getColoredString(String path)
    {
        return ChatColor.translateAlternateColorCodes('&', getString(path));
    }

    public String getColoredString(String path, String def)
    {
        return ChatColor.translateAlternateColorCodes('&', getString(path, def));
    }

    public int getInt(String path)
    {
        return _configurationSection.getInt(path);
    }

    public int getInt(String path, int def)
    {
        return _configurationSection.getInt(path, def);
    }

    public long getLong(String path)
    {
        return _configurationSection.getLong(path);
    }
    public long getLong(String path, long def)
    {
        return _configurationSection.getLong(path, def);
    }

    public boolean getBoolean(String path)
    {
        return _configurationSection.getBoolean(path);
    }

    public boolean getBoolean(String path, boolean def)
    {
        return _configurationSection.getBoolean(path, def);
    }

    public double getDouble(String path)
    {
        return _configurationSection.getDouble(path);
    }

    public double getDouble(String path, double def)
    {
        return _configurationSection.getDouble(path, def);
    }

    public float getFloat(String path, float def)
    {
        String string = getString(path);
        if (string == null) return def;
        return Float.parseFloat(string);
    }

    public ConfigurationSection getSection(String path)
    {
        ConfigurationSection result = this._configurationSection.getConfigurationSection(path);
        if (result == null) throw new IllegalArgumentException("Unknown path: " + getSection().getCurrentPath() + "." + path);
        return result;
    }
    public ConfigurationSection getSection(String path, ConfigurationSection def)
    {
        ConfigurationSection result = this._configurationSection.getConfigurationSection(path);
        if (result == null) return def;
        return result;
    }

    public List<String> getStringList(String path)
    {
        List<String> stringList = _configurationSection.getStringList(path);
        if (stringList == null || stringList.isEmpty())
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

    public List<String> getStringList(String path, List<String> def)
    {
        List<String> stringList = getStringList(path);
        if (stringList == null || stringList.isEmpty()) return def;
        return stringList;
    }

    public List<String> getColoredStringList(String path)
    {
        return Text.SetColors(getStringList(path));
    }

    public List<String> getColoredStringList(String path, List<String> def)
    {
        return Text.SetColors(getStringList(path, def));
    }

    public List<Integer> getIntegerList(String path)
    {
        List<String> stringList = getStringList(path);
        if (stringList == null) return null;
        List<Integer> result = new ArrayList<>();
        for (String s : stringList)
        {
            result.add(Integer.parseInt(s));
        }
        return result;
    }

    public List<Integer> getIntegerList(String path, List<Integer> def)
    {
        List<String> stringList = getStringList(path);
        if (stringList == null) return def;
        List<Integer> result = new ArrayList<>();
        for (String s : stringList)
        {
            result.add(Integer.parseInt(s));
        }
        return result.isEmpty() ? def : result;
    }

    public byte getByte(String path)
    {
        return (byte) getInt(path);
    }

    public byte getByte(String path, byte def)
    {
        try
        {
            return (byte) getInt(path, def);
        }
        catch (Exception e)
        {
            return def;
        }
    }

    public boolean isSection(String path)
    {
        return getSection().isConfigurationSection(path);
    }
    public boolean isList(String path)
    {
        return getSection().isList(path);
    }
}