package ru.reosfire.twl.spigot.configuration.localization.commandResults;

import org.bukkit.configuration.ConfigurationSection;
import ru.reosfire.twl.spigot.lib.yaml.YamlConfig;
import ru.reosfire.twl.spigot.lib.yaml.common.text.MultilineMessage;

public class ReloadCommandResultsConfig extends YamlConfig
{
    public final MultilineMessage Success;
    public final MultilineMessage Error;

    public ReloadCommandResultsConfig(ConfigurationSection configurationSection)
    {
        super(configurationSection);

        Success = getMultilineMessage("Success");
        Error = getMultilineMessage("Error");
    }
}