package ru.reosfire.temporarywhitelist;

import ru.reosfire.temporarywhitelist.Configuration.Config;
import ru.reosfire.temporarywhitelist.Configuration.MessagesConfig;
import ru.reosfire.temporarywhitelist.Lib.Yaml.YamlConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class LocalizationsLoader
{
    private final TemporaryWhiteList PluginInstance;
    private final Config Config;

    public LocalizationsLoader(TemporaryWhiteList pluginInstance, Config config)
    {
        PluginInstance = pluginInstance;
        Config = config;
    }

    public void CopyDefaultTranslations()
    {
        String[] translationsResources = new String[]
                {
                        "en.yml",
                        "ru.yml"
                };
        try
        {
            File translationsDirectory = new File(PluginInstance.getDataFolder(), "./translations/");
            translationsDirectory.mkdir();

            for (String translationsResource : translationsResources)
            {
                File translationFile = new File(translationsDirectory, translationsResource);
                if (translationFile.exists()) continue;

                InputStream resource = PluginInstance.getResource("translations/" + translationsResource);
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

    public MessagesConfig LoadMessages()
    {
        try
        {
            return new MessagesConfig(YamlConfig.LoadOrCreate("translations/" + Config.Translation, PluginInstance));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException("Can't load translation file");
        }
    }
}