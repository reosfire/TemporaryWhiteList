package ru.reosfire.twl.common.configuration.localization;

import ru.reosfire.twl.common.lib.yaml.ConfigSection;
import ru.reosfire.twl.common.lib.yaml.YamlConfig;
import ru.reosfire.twl.common.lib.yaml.common.text.MultilineMessage;

public class MessagesConfig extends YamlConfig {
    public final PlayerStatusesConfig PlayerStatuses;
    public final KickMessagesConfig Kick;
    public final CommandResultsConfig CommandResults;
    public final MultilineMessage NoPermission;
    public final MultilineMessage UnexpectedError;
    public final String WhiteListEnabledStatus;
    public final String WhiteListDisabledStatus;

    public MessagesConfig(ConfigSection configuration) {
        super(configuration);

        PlayerStatuses = new PlayerStatusesConfig(getSection("PlayerStatuses"));
        Kick = new KickMessagesConfig(getSection("Kick"));
        CommandResults = new CommandResultsConfig(getSection("CommandResults"));

        NoPermission = getMultilineMessage("NoPermission");
        UnexpectedError = getMultilineMessage("UnexpectedError");

        WhiteListEnabledStatus = getString("WhiteListStatuses.Enabled");
        WhiteListDisabledStatus = getString("WhiteListStatuses.Disabled");
    }
}