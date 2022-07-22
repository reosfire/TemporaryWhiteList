package ru.reosfire.temporarywhitelist.Configuration.Localization.CommandResults;

import org.bukkit.configuration.ConfigurationSection;
import ru.reosfire.temporarywhitelist.Lib.Yaml.Default.Text.MultilineMessage;
import ru.reosfire.temporarywhitelist.Lib.Yaml.YamlConfig;

public class ImportCommandResultConfig extends YamlConfig
{
    public final MultilineMessage Usage;
    public final MultilineMessage ImportFromSelf;
    public final MultilineMessage IncorrectTime;
    public final MultilineMessage IncorrectPermanent;
    public final MultilineMessage Success;
    public final MultilineMessage Error;
    public final MultilineMessage MinecraftDefaultUsage;
    public final MultilineMessage SelfSqlUsage;
    public final MultilineMessage SelfYamlUsage;
    public final MultilineMessage EasyWhiteListUsage;
    public final MultilineMessage EasyWhiteListPluginNotFound;

    public ImportCommandResultConfig(ConfigurationSection configurationSection)
    {
        super(configurationSection);
        Usage = getMultilineMessage("Usage");
        ImportFromSelf = getMultilineMessage("ImportFromSelf");
        IncorrectTime = getMultilineMessage("IncorrectTime");
        IncorrectPermanent = getMultilineMessage("IncorrectPermanent");
        Success = getMultilineMessage("Success");
        Error = getMultilineMessage("Error");
        MinecraftDefaultUsage = getMultilineMessage("MinecraftDefault.Usage");
        SelfSqlUsage = getMultilineMessage("SelfSql.Usage");
        SelfYamlUsage = getMultilineMessage("SelfYaml.Usage");
        EasyWhiteListUsage = getMultilineMessage("EasyWhiteList.Usage");
        EasyWhiteListPluginNotFound = getMultilineMessage("EasyWhiteList.PluginNotFound");
    }
}