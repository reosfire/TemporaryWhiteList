package ru.reosfire.twl.spigot.loaders;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Node;
import ru.reosfire.twl.common.configuration.localization.MessagesConfig;
import ru.reosfire.twl.common.lib.yaml.ConfigSection;
import ru.reosfire.twl.common.lib.yaml.YamlUtils;
import ru.reosfire.twl.spigot.TemporaryWhiteList;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

public class LocalizationsLoader
{
    private static final String[] translationsResources = new String[]
            {
                    "ru.yml",
                    "en.yml",
                    "pt.yml",
            };

    private final TemporaryWhiteList plugin;
    private final Yaml yaml = YamlUtils.createDumpYaml();

    public LocalizationsLoader(TemporaryWhiteList pluginInstance)
    {
        plugin = pluginInstance;
    }

    public void copyDefaultTranslations()
    {
        File translationsDirectory = new File(plugin.getDataFolder(), "translations/");

        if (!translationsDirectory.exists() && !translationsDirectory.mkdir())
            throw new RuntimeException("Directory for translations couldn't created.");

        for (String translationsResource : translationsResources)
        {
            File translationFile = new File(translationsDirectory, translationsResource);
            if (translationFile.exists())
            {
                try (InputStream resource = plugin.getResource("translations/" + translationsResource);
                     Reader resourceReader = new InputStreamReader(resource, StandardCharsets.UTF_8);
                     Reader fileReader = new FileReader(translationFile, StandardCharsets.UTF_8)) {
                    //new BufferedReader(resourceReader).lines().forEach(System.out::println);
                    Node fromResource = yaml.compose(resourceReader);
                    Node actual = yaml.compose(fileReader);

                    YamlUtils.mergeYaml(fromResource, actual);

                    try (Writer fileWriter = new FileWriter(translationFile, StandardCharsets.UTF_8)) {
                        yaml.serialize(actual, fileWriter);
                    }
                }
                catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            else
            {
                try (InputStream resource = plugin.getResource("translations/" + translationsResource)) {
                    Files.copy(resource, translationFile.toPath());
                }
                catch (Exception e) {
                    throw new RuntimeException("Can't load " + translationsResource + " from plugin jar. Is it corrupted?", e);
                }
            }
        }
    }

    public MessagesConfig loadMessages()
    {
        File file = new File(plugin.getDataFolder(), "translations/" + plugin.getConfiguration().Translation);
        try (InputStream inputStream = new FileInputStream(file))
        {
            Map<String, Object> loaded = yaml.load(inputStream);

            return new MessagesConfig(new ConfigSection(loaded));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException("Can't load translation file", e);
        }
    }
}