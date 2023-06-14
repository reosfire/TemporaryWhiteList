package ru.reosfire.twl.spigot;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.reosfire.twl.common.TimeConverter;
import ru.reosfire.twl.common.configuration.localization.MessagesConfig;
import ru.reosfire.twl.common.data.PlayerData;
import ru.reosfire.twl.common.data.PlayerDatabase;

public class PlaceholdersExpansion extends PlaceholderExpansion
{
    private final MessagesConfig messages;
    private final PlayerDatabase database;
    private final TimeConverter timeConverter;
    private final TemporaryWhiteList pluginInstance;

    public PlaceholdersExpansion(MessagesConfig messages, PlayerDatabase database, TimeConverter timeConverter, TemporaryWhiteList pluginInstance)
    {
        this.messages = messages;
        this.database = database;
        this.timeConverter = timeConverter;
        this.pluginInstance = pluginInstance;
    }

    @Override
    public boolean persist()
    {
        return true;
    }

    @Override
    public boolean canRegister()
    {
        return true;
    }

    @Override
    public @NotNull String getAuthor()
    {
        return pluginInstance.getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getIdentifier()
    {
        return "twl";
    }

    @Override
    public @NotNull String getVersion()
    {
        return pluginInstance.getDescription().getVersion();
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params)
    {
        if (params.equals("plugin_status"))
        {
            return pluginInstance.isWhiteListEnabled() ? messages.WhiteListEnabledStatus :
                    messages.WhiteListDisabledStatus;
        }

        if (player == null) return "";
        PlayerData playerData = database.getPlayerData(player.getName());
        if (playerData == null) return messages.PlayerStatuses.Undefined;

        if (params.equals("player_status"))
        {
            if (playerData.Permanent) return messages.PlayerStatuses.NeverEnd;
            long timeLeft = playerData.timeLeft();
            if (timeLeft < 0) return messages.PlayerStatuses.Ended;
            return timeConverter.durationToString(timeLeft);
        }

        if (params.equals("start_time")) return timeConverter.dateTimeToString(playerData.StartTime);
        if (params.equals("left_time")) return timeConverter.durationToString(Math.max(playerData.timeLeft(), 0));
        if (params.equals("end_time")) return timeConverter.dateTimeToString(playerData.endTime());
        if (params.equals("permanent")) return Boolean.toString(playerData.Permanent);

        return super.onRequest(player, params);
    }
}