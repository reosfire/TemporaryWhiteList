package ru.reosfire.temporarywhitelist.Configuration;

import org.bukkit.configuration.file.YamlConfiguration;
import ru.reosfire.temporarywhitelist.Lib.Yaml.YamlConfig;

public class Messages extends YamlConfig
{
    public final String KickOnConnecting;
    public final String KickConnected;
    public final String NoPermission;
    public final String CheckMessageFormat;
    public final String PlayerUndefined;
    public final String SubscribeNeverEnd;
    public final String SubscribeEnd;
    public final String WhiteListEnabledStatus;
    public final String WhiteListDisabledStatus;

    public Messages(YamlConfiguration configuration)
    {
        super(configuration);
        KickOnConnecting = getString("Kick.Connecting");
        KickConnected = getString("Kick.WhileConnected");
        NoPermission = getString("NoPermission");
        CheckMessageFormat = getString("CheckMessageFormat");
        PlayerUndefined = getString("DataBase.PlayerUndefined");
        SubscribeNeverEnd = getString("DataBase.SubscribeNeverEnd");
        SubscribeEnd = getString("DataBase.SubscribeEnd");
        WhiteListEnabledStatus = getString("WhiteListStatuses.Enabled");
        WhiteListDisabledStatus = getString("WhiteListStatuses.Disabled");
    }
}