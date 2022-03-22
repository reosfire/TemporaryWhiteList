package ru.reosfire.temporarywhitelist.Configuration.Localization;

import org.bukkit.configuration.ConfigurationSection;
import ru.reosfire.temporarywhitelist.Lib.Yaml.YamlConfig;

public class MessagesConfig extends YamlConfig
{
    public final PlayerStatusesConfig PlayerStatuses;
    public final KickMessagesConfig Kick;
    public final CommandResultsConfig CommandResults;
    public final String NoPermission;
    public final String WhiteListEnabledStatus;
    public final String WhiteListDisabledStatus;

    public MessagesConfig(ConfigurationSection configuration)
    {
        super(configuration);

        PlayerStatuses = new PlayerStatusesConfig(getSection("PlayerStatuses"));
        Kick = new KickMessagesConfig(getSection("Kick"));
        CommandResults = new CommandResultsConfig(getSection("CommandResults"));

        NoPermission = getColoredString("NoPermission");
        WhiteListEnabledStatus = getColoredString("WhiteListStatuses.Enabled");
        WhiteListDisabledStatus = getColoredString("WhiteListStatuses.Disabled");
    }
}