package ru.reosfire.temporarywhitelist.Loaders;

import ru.reosfire.temporarywhitelist.Configuration.Config;
import ru.reosfire.temporarywhitelist.Configuration.Localization.MessagesConfig;
import ru.reosfire.temporarywhitelist.Lib.Yaml.YamlConfig;
import ru.reosfire.temporarywhitelist.TemporaryWhiteList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class LocalizationsLoader
{
    private final TemporaryWhiteList plugin;
    private final Config config;

    public LocalizationsLoader(TemporaryWhiteList pluginInstance)
    {
        plugin = pluginInstance;
        this.config = plugin.getConfiguration();
    }

    public void copyDefaultTranslations()
    {
        String[] translationsResources = new String[]
                {
                        "en.yml",
                        "ru.yml"
                };
        try
        {
            File translationsDirectory = new File(plugin.getDataFolder(), "./translations/");
            translationsDirectory.mkdir();

            for (String translationsResource : translationsResources)
            {
                File translationFile = new File(translationsDirectory, translationsResource);
                if (translationFile.exists()) continue;

                InputStream resource = plugin.getResource("translations/" + translationsResource);
                byte[] buffer = new byte[resource.available()];
                resource.read(buffer);

                FileOutputStream fileOutputStream = new FileOutputStream(translationFile);
                fileOutputStream.write(buffer);
                fileOutputStream.close();
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException();
        }
    }

    public MessagesConfig loadMessages()
    {
        try
        {
            return new MessagesConfig(YamlConfig.loadOrCreate("translations/" + config.Translation, plugin));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException("Can't load translation file");
        }
    }
}