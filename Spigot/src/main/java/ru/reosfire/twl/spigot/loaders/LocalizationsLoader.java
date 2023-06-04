package ru.reosfire.twl.spigot.loaders;

import ru.reosfire.twl.spigot.TemporaryWhiteList;
import ru.reosfire.twl.spigot.configuration.localization.MessagesConfig;
import ru.reosfire.twl.spigot.lib.yaml.YamlConfig;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;

public class LocalizationsLoader
{
    private static final String[] translationsResources = new String[]
            {
                    "en.yml",
                    "ru.yml",
                    "pt.yml",
            };

    private final TemporaryWhiteList plugin;

    public LocalizationsLoader(TemporaryWhiteList pluginInstance)
    {
        plugin = pluginInstance;
    }

    public void copyDefaultTranslations()
    {
        File translationsDirectory = new File(plugin.getDataFolder(), "./translations/");

        if (!translationsDirectory.exists() && !translationsDirectory.mkdir())
            throw new RuntimeException("Directory for translations couldn't created.");

        for (String translationsResource : translationsResources)
        {
            File translationFile = new File(translationsDirectory, translationsResource);
            if (translationFile.exists()) continue;

            try (InputStream resource = plugin.getResource("translations/" + translationsResource))
            {
                Files.copy(resource, translationFile.toPath());
            }
            catch (Exception e)
            {
                throw new RuntimeException("Can't load " + translationsResource + " from plugin jar. Is it corrupted?", e);
            }
        }
    }

    public MessagesConfig loadMessages()
    {
        try
        {
            return new MessagesConfig(YamlConfig.loadOrCreate("translations/" + plugin.getConfiguration().Translation, plugin));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException("Can't load translation file", e);
        }
    }
}