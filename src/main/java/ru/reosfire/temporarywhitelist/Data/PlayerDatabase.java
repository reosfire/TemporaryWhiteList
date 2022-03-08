package ru.reosfire.temporarywhitelist.Data;

import org.bukkit.OfflinePlayer;
import ru.reosfire.temporarywhitelist.Configuration.Localization.MessagesConfig;
import ru.reosfire.temporarywhitelist.TimeConverter;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerDatabase
{
    private final IDataProvider _provider;
    private final MessagesConfig _messages;
    private final TimeConverter _timeConverter;
    private final Map<String, PlayerData> _playersData = new ConcurrentHashMap<>();

    public PlayerDatabase(IDataProvider provider, MessagesConfig messagesConfig, TimeConverter timeConverter)
    {
        _provider = provider;
        _messages = messagesConfig;
        _timeConverter = timeConverter;
        LoadAll();
    }

    public PlayerData getPlayerData(String name)
    {
        return _playersData.get(name);
    }
    public CompletableFuture<Void> Update(PlayerData playerData)
    {
        return _provider.Update(playerData).thenRun(() ->
        {
            _playersData.put(playerData.Name, playerData);
        });
    }

    public boolean CanJoin(String name)
    {
        PlayerData playerData = getPlayerData(name);
        if (playerData == null) return false;
        if (playerData.Permanent) return true;

        return !playerData.isTimedOut();
    }
    public boolean CanJoin(OfflinePlayer player)
    {
        return CanJoin(player.getName());
    }

    public CompletableFuture<Void> Add(String name, long addedTime)
    {
        PlayerData playerData = getPlayerData(name);

        long startTime = Instant.now().getEpochSecond();
        long timeAmount;
        boolean permanent;

        if (playerData == null)
        {
            timeAmount = addedTime;
            permanent = false;
        }
        else
        {
            timeAmount = playerData.isTimedOut() ? addedTime : playerData.TimeLeft() + addedTime;
            permanent = playerData.Permanent;
        }

        return Update(new PlayerData(name, startTime, timeAmount, permanent));
    }
    public CompletableFuture<Void> Add(String name)
    {
        long startTime = Instant.now().getEpochSecond();
        long timeAmount = 0;
        boolean permanent = true;

        return Update(new PlayerData(name, startTime, timeAmount, permanent));
    }

    public CompletableFuture<Void> Set(String name, long time)
    {
        long startTime = Instant.now().getEpochSecond();
        boolean permanent = false;

        return Update(new PlayerData(name, startTime, time, permanent));
    }

    public CompletableFuture<Void> Remove(String name)
    {
        return _provider.Remove(name).thenRun(() -> _playersData.remove(name));
    }

    public List<PlayerData> ActiveList()
    {
        List<PlayerData> result = new ArrayList<>();

        for (PlayerData playerData : AllList())
        {
            if (CanJoin(playerData.Name)) result.add(playerData);
        }

        return result;
    }
    public List<PlayerData> AllList()
    {
        return new ArrayList<>(_playersData.values());
    }

    public String Check(String name)
    {
        if (!_playersData.containsKey(name)) return _messages.DataBase.PlayerUndefined;
        PlayerData playerData = _playersData.get(name);
        if(playerData.Permanent) return _messages.DataBase.SubscribeNeverEnd;
        long timeLeft = playerData.TimeLeft();
        if(timeLeft < 0) return _messages.DataBase.SubscribeEnd;
        return _timeConverter.ReadableTime(timeLeft);
    }

    private void LoadAll()
    {
        for (PlayerData playerData : _provider.GetAll())
        {
            _playersData.put(playerData.Name, playerData);
        }
    }
}