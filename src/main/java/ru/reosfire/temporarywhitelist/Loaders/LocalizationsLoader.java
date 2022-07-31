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
    private final TemporaryWhiteList _pluginInstance;
    private final Config _config;

    public LocalizationsLoader(TemporaryWhiteList pluginInstance, Config config)
    {
        _pluginInstance = pluginInstance;
        _config = config;
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
            File translationsDirectory = new File(_pluginInstance.getDataFolder(), "./translations/");
            translationsDirectory.mkdir();

            for (String translationsResource : translationsResources)
            {
                File translationFile = new File(translationsDirectory, translationsResource);
                if (translationFile.exists()) continue;

                InputStream resource = _pluginInstance.getResource("translations/" + translationsResource);
                byte[] buffer = new byte[resource.available()];
                resource.read(buffer);

                FileOutputStream fileOutputStream = new FileOutputStream(translationFile);
                fileOutputStream.write(buffer);
                fileOutputStream.close();
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public MessagesConfig LoadMessages()
    {
        try
        {
            return new MessagesConfig(YamlConfig.LoadOrCreate("translations/" + _config.Translation, _pluginInstance));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException("Can't load translation file", e);
        }
    }
}