package ru.reosfire.twl.common;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Node;
import ru.reosfire.twl.common.configuration.localization.MessagesConfig;
import ru.reosfire.twl.common.lib.yaml.ConfigSection;
import ru.reosfire.twl.common.lib.yaml.YamlUtils;

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

    private final File targetDirectory;
    private final ClassLoader classLoader;
    private final Yaml yaml = YamlUtils.createDefaultYaml();

    public LocalizationsLoader(File targetDirectory)
    {
        this.targetDirectory = targetDirectory;
        classLoader = getClass().getClassLoader();
    }

    public void copyDefaultTranslations()
    {
        if (!targetDirectory.exists() && !targetDirectory.mkdir())
            throw new RuntimeException("Directory for translations couldn't created.");

        for (String translationsResource : translationsResources)
        {
            File translationFile = new File(targetDirectory, translationsResource);
            if (translationFile.exists())
            {
                try (InputStream resource = getResource("translations/" + translationsResource);
                     Reader resourceReader = new InputStreamReader(resource, StandardCharsets.UTF_8);
                     Reader fileReader = new FileReader(translationFile, StandardCharsets.UTF_8)) {
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
                try (InputStream resource = getResource("translations/" + translationsResource)) {
                    Files.copy(resource, translationFile.toPath());
                }
                catch (Exception e) {
                    throw new RuntimeException("Can't load " + translationsResource + " from plugin jar. Is it corrupted?", e);
                }
            }
        }
    }

    public MessagesConfig loadMessages(String fileName)
    {
        try (InputStream inputStream = new FileInputStream(new File(targetDirectory, fileName)))
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

    public InputStream getResource(String filename) {
        if (filename == null) throw new IllegalArgumentException("Filename cannot be null");

        try {
            return classLoader.getResourceAsStream(filename);
        } catch (Exception e) {
            return null;
        }
    }
}