package ru.reosfire.temporarywhitelist.configuration.localization.commandResults;

import org.bukkit.configuration.ConfigurationSection;
import ru.reosfire.temporarywhitelist.lib.yaml.common.text.MultilineMessage;
import ru.reosfire.temporarywhitelist.lib.yaml.YamlConfig;

public class ListCommandResultsConfig extends YamlConfig
{
    public final MultilineMessage Usage;
    public final MultilineMessage IncorrectPage;
    public final MultilineMessage ListIsEmpty;
    public final MultilineMessage Header;
    public final MultilineMessage PlayerFormat;
    public final MultilineMessage PagesSwitch;

    public ListCommandResultsConfig(ConfigurationSection configurationSection)
    {
        super(configurationSection);

        Usage = getMultilineMessage("Usage");
        IncorrectPage = getMultilineMessage("IncorrectPage");
        ListIsEmpty = getMultilineMessage("ListIsEmpty");
        Header = getMultilineMessage("Header");
        PlayerFormat = getMultilineMessage("PlayerFormat");
        PagesSwitch = getMultilineMessage("PagesSwitch");
    }
}