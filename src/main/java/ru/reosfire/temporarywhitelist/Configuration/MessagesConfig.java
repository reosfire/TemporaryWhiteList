package ru.reosfire.temporarywhitelist.Configuration;

import org.bukkit.configuration.ConfigurationSection;
import ru.reosfire.temporarywhitelist.Lib.Yaml.YamlConfig;

public class MessagesConfig extends YamlConfig
{
    public final DatabaseMessagesConfig DataBase;
    public final String KickOnConnecting;
    public final String KickConnected;
    public final String NoPermission;
    public final String CheckMessageFormat;
    public final String WhiteListEnabledStatus;
    public final String WhiteListDisabledStatus;

    public MessagesConfig(ConfigurationSection configuration)
    {
        super(configuration);

        DataBase = new DatabaseMessagesConfig(getSection("DataBase"));
        KickOnConnecting = getColoredString("Kick.Connecting");
        KickConnected = getColoredString("Kick.WhileConnected");
        NoPermission = getColoredString("NoPermission");
        CheckMessageFormat = getColoredString("CheckMessageFormat");
        WhiteListEnabledStatus = getColoredString("WhiteListStatuses.Enabled");
        WhiteListDisabledStatus = getColoredString("WhiteListStatuses.Disabled");
    }
}