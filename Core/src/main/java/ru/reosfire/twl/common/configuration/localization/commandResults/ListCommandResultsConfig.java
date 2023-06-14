package ru.reosfire.twl.common.configuration.localization.commandResults;

import ru.reosfire.twl.common.lib.yaml.ConfigSection;
import ru.reosfire.twl.common.lib.yaml.YamlConfig;
import ru.reosfire.twl.common.lib.yaml.common.text.MultilineMessage;

public class ListCommandResultsConfig extends YamlConfig {
    public final MultilineMessage Usage;
    public final MultilineMessage IncorrectPage;
    public final MultilineMessage ListIsEmpty;
    public final MultilineMessage Header;
    public final MultilineMessage PlayerFormat;
    public final MultilineMessage PagesSwitch;

    public ListCommandResultsConfig(ConfigSection configurationSection) {
        super(configurationSection);

        Usage = getMultilineMessage("Usage");
        IncorrectPage = getMultilineMessage("IncorrectPage");
        ListIsEmpty = getMultilineMessage("ListIsEmpty");
        Header = getMultilineMessage("Header");
        PlayerFormat = getMultilineMessage("PlayerFormat");
        PagesSwitch = getMultilineMessage("PagesSwitch");
    }
}