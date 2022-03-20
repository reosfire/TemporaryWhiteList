package ru.reosfire.temporarywhitelist.Configuration.Localization.CommandResults;

import org.bukkit.configuration.ConfigurationSection;
import ru.reosfire.temporarywhitelist.Lib.Yaml.Default.Wrappers.Text.MultilineMessage;
import ru.reosfire.temporarywhitelist.Lib.Yaml.YamlConfig;

public class CheckCommandResultsConfig extends YamlConfig
{
    public final MultilineMessage Format;

    public CheckCommandResultsConfig(ConfigurationSection configurationSection)
    {
        super(configurationSection);

        Format = getMultilineMessage("Format");
    }
}