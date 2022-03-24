package ru.reosfire.temporarywhitelist;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.reosfire.temporarywhitelist.Configuration.Localization.MessagesConfig;
import ru.reosfire.temporarywhitelist.Data.PlayerData;
import ru.reosfire.temporarywhitelist.Data.PlayerDatabase;

public class PlaceholdersExpansion extends PlaceholderExpansion
{
    private final MessagesConfig _messages;
    private final PlayerDatabase _database;
    private final TimeConverter _timeConverter;
    private final TemporaryWhiteList _pluginInstance;

    public PlaceholdersExpansion(MessagesConfig messages, PlayerDatabase database, TimeConverter timeConverter, TemporaryWhiteList pluginInstance)
    {
        _messages = messages;
        _database = database;
        _timeConverter = timeConverter;
        _pluginInstance = pluginInstance;
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
        return _pluginInstance.getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getIdentifier()
    {
        return "twl";
    }

    @Override
    public @NotNull String getVersion()
    {
        return _pluginInstance.getDescription().getVersion();
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params)
    {
        if (params.equals("plugin_status"))
        {
            return _pluginInstance.isWhiteListEnabled() ? _messages.WhiteListEnabledStatus :
                    _messages.WhiteListDisabledStatus;
        }

        if (player == null) return "";
        PlayerData playerData = _database.getPlayerData(player.getName());
        if (playerData == null) return _messages.PlayerStatuses.Undefined;

        if (params.equals("player_status"))
        {
            if (playerData.Permanent) return _messages.PlayerStatuses.NeverEnd;
            long timeLeft = playerData.TimeLeft();
            if (timeLeft < 0) return _messages.PlayerStatuses.Ended;
            return _timeConverter.DurationToString(timeLeft);
        }

        if (params.equals("start_time")) return _timeConverter.DateTimeToString(playerData.StartTime);
        if (params.equals("left_time")) return _timeConverter.DurationToString(Math.max(playerData.TimeLeft(), 0));
        if (params.equals("end_time")) return _timeConverter.DateTimeToString(playerData.EndTime());
        if (params.equals("permanent")) return Boolean.toString(playerData.Permanent);

        return super.onRequest(player, params);
    }
}