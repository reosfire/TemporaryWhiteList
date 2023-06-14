package ru.reosfire.twl.common.configuration.localization;

import ru.reosfire.twl.common.lib.yaml.ConfigSection;
import ru.reosfire.twl.common.lib.yaml.YamlConfig;

public class MessagesConfig extends YamlConfig {
    public final PlayerStatusesConfig PlayerStatuses;
    public final KickMessagesConfig Kick;
    public final CommandResultsConfig CommandResults;
    public final String NoPermission;
    public final String WhiteListEnabledStatus;
    public final String WhiteListDisabledStatus;

    public MessagesConfig(ConfigSection configuration) {
        super(configuration);

        PlayerStatuses = new PlayerStatusesConfig(getSection("PlayerStatuses"));
        Kick = new KickMessagesConfig(getSection("Kick"));
        CommandResults = new CommandResultsConfig(getSection("CommandResults"));

        NoPermission = getString("NoPermission");
        WhiteListEnabledStatus = getString("WhiteListStatuses.Enabled");
        WhiteListDisabledStatus = getString("WhiteListStatuses.Disabled");
    }
}