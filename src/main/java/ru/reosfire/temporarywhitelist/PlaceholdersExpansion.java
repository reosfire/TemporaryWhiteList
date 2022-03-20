package ru.reosfire.temporarywhitelist;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
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
    public String onPlaceholderRequest(Player player, @NotNull String identifier)
    {
        if (player == null) return "";

        if (identifier.equals("status"))
        {
            return _pluginInstance.isWhiteListEnabled() ? _messages.WhiteListEnabledStatus :
                    _messages.WhiteListDisabledStatus;
        }

        PlayerData playerData = _database.getPlayerData(player.getName());

        if (identifier.equals("whitelist_status"))
        {
            if (playerData == null) return _messages.CheckStatuses.PlayerUndefined;
            if (playerData.Permanent) return _messages.CheckStatuses.SubscribeNeverEnd;
            long timeLeft = playerData.TimeLeft();
            if (timeLeft < 0) return _messages.CheckStatuses.SubscribeEnd;
            return _timeConverter.DurationToString(timeLeft);
        }

        return "";
    }
}