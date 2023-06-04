package ru.reosfire.twl.configuration.localization.commandResults;

import org.bukkit.configuration.ConfigurationSection;
import ru.reosfire.twl.lib.yaml.YamlConfig;
import ru.reosfire.twl.lib.yaml.common.text.MultilineMessage;

public class ImportCommandResultConfig extends YamlConfig
{
    public final MultilineMessage Usage;
    public final MultilineMessage ImportFromSelf;
    public final MultilineMessage IncorrectTime;
    public final MultilineMessage IncorrectPermanent;
    public final MultilineMessage Success;
    public final MultilineMessage Error;
    public final MultilineMessage SuccessfullyStarted;
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
        SuccessfullyStarted = getMultilineMessage("SuccessfullyStarted");
        MinecraftDefaultUsage = getMultilineMessage("MinecraftDefault.Usage");
        SelfSqlUsage = getMultilineMessage("SelfSql.Usage");
        SelfYamlUsage = getMultilineMessage("SelfYaml.Usage");
        EasyWhiteListUsage = getMultilineMessage("EasyWhiteList.Usage");
        EasyWhiteListPluginNotFound = getMultilineMessage("EasyWhiteList.PluginNotFound");
    }
}