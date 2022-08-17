package ru.reosfire.temporarywhitelist.Lib.Yaml;

import org.apache.commons.lang.NullArgumentException;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import ru.reosfire.temporarywhitelist.Lib.Text.Text;
import ru.reosfire.temporarywhitelist.Lib.Yaml.Default.Text.MultilineMessage;
import ru.reosfire.temporarywhitelist.Lib.Yaml.Default.Text.TextComponentConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class YamlConfig
{
    public static YamlConfiguration loadOrCreate(String resultFileName, String defaultConfigurationResource,
                                                 JavaPlugin plugin) throws IOException, InvalidConfigurationException
    {
        YamlConfiguration config = new YamlConfiguration();
        config.load(loadOrCreateFile(resultFileName, defaultConfigurationResource, plugin));
        return config;
    }

    public static YamlConfiguration loadOrCreate(File file) throws IOException, InvalidConfigurationException
    {
        YamlConfiguration config = new YamlConfiguration();
        config.load(file);
        return config;
    }

    public static YamlConfiguration loadOrCreate(String fileName, JavaPlugin plugin) throws IOException,
            InvalidConfigurationException
    {
        return loadOrCreate(fileName, fileName, plugin);
    }

    public static File loadOrCreateFile(String resultFileName, String defaultConfigurationResource,
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

    public static File loadOrCreateFile(String fileName, JavaPlugin plugin) throws IOException
    {
        return loadOrCreateFile(fileName, fileName, plugin);
    }

    protected final ConfigurationSection configurationSection;

    public ConfigurationSection getSection()
    {
        return configurationSection;
    }

    public YamlConfig(ConfigurationSection configurationSection)
    {
        if (configurationSection == null) throw new NullArgumentException("configurationSection");
        this.configurationSection = configurationSection;
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
        return configurationSection.getString(path);
    }

    public String getString(String path, String def)
    {
        return configurationSection.getString(path, def);
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
        return configurationSection.getInt(path);
    }

    public int getInt(String path, int def)
    {
        return configurationSection.getInt(path, def);
    }

    public long getLong(String path)
    {
        return configurationSection.getLong(path);
    }

    public boolean getBoolean(String path, boolean def)
    {
        return configurationSection.getBoolean(path, def);
    }

    public ConfigurationSection getSection(String path)
    {
        ConfigurationSection result = this.configurationSection.getConfigurationSection(path);
        if (result == null) throw new IllegalArgumentException("Unknown path: " + getSection().getCurrentPath() + "." + path);
        return result;
    }
    public ConfigurationSection getSection(String path, ConfigurationSection def)
    {
        ConfigurationSection result = this.configurationSection.getConfigurationSection(path);
        if (result == null) return def;
        return result;
    }

    public List<String> getStringList(String path)
    {
        List<String> stringList = configurationSection.getStringList(path);
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

    public List<String> getColoredStringList(String path)
    {
        return Text.setColors(getStringList(path));
    }


    public boolean isList(String path)
    {
        return getSection().isList(path);
    }

    public MultilineMessage getMultilineMessage(String path)
    {
        return new MultilineMessage(getList(TextComponentConfig::new, path));
    }
}