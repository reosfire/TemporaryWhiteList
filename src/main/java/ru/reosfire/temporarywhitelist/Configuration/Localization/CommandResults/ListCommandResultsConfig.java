package ru.reosfire.temporarywhitelist.Configuration.Localization.CommandResults;

import org.bukkit.configuration.ConfigurationSection;
import ru.reosfire.temporarywhitelist.Lib.Yaml.Default.Text.MultilineMessage;
import ru.reosfire.temporarywhitelist.Lib.Yaml.YamlConfig;

public class ListCommandResultsConfig extends YamlConfig
{
    public final MultilineMessage Usage;
    public final MultilineMessage IncorrectPage;
    public final MultilineMessage Header;
    public final MultilineMessage PlayerFormat;
    public final MultilineMessage PagesSwitch;

    public ListCommandResultsConfig(ConfigurationSection configurationSection)
    {
        super(configurationSection);

        Usage = getMultilineMessage("Usage");
        IncorrectPage = getMultilineMessage("IncorrectPage");
        Header = getMultilineMessage("Header");
        PlayerFormat = getMultilineMessage("PlayerFormat");
        PagesSwitch = getMultilineMessage("PagesSwitch");
    }
}