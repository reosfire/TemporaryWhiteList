package ru.reosfire.temporarywhitelist.Data;

import org.apache.commons.lang.NullArgumentException;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class PlayerDatabase
{
    private final IDataProvider _provider;
    private final Map<String, PlayerData> _playersData = new ConcurrentSkipListMap<>();
    private final Map<String, Long> _lastRefresh = new ConcurrentHashMap<>();
    private final long _refreshInterval;

    public PlayerDatabase(IDataProvider provider, long refreshInterval)
    {
        _provider = provider;
        _refreshInterval = refreshInterval;
        LoadAll();
    }

    public PlayerData getPlayerData(String name)
    {
        TryRefreshPlayer(name);
        return _playersData.get(name);
    }

    public CompletableFuture<Boolean> Update(PlayerData playerData)
    {
        if (playerData == null) throw new NullArgumentException("playerName");

        TryRefreshPlayer(playerData.Name);

        PlayerData oldData = getPlayerData(playerData.Name);
        if (oldData != null && oldData.isSame(playerData)) return CompletableFuture.completedFuture(false);
        return _provider.Update(playerData).thenRun(() -> _playersData.put(playerData.Name, playerData))
                        .thenApply(res -> true);
    }

    public boolean CanJoin(String name)
    {
        PlayerData playerData = getPlayerData(name);
        if (playerData == null) return false;
        return playerData.CanJoin();
    }

    public CompletableFuture<Boolean> Add(String name, long addedTime)
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

    public CompletableFuture<Boolean> SetPermanent(String name)
    {
        long startTime = Instant.now().getEpochSecond();
        long timeAmount = 0;
        boolean permanent = true;

        return Update(new PlayerData(name, startTime, timeAmount, permanent));
    }

    public CompletableFuture<Boolean> Set(String name, long time)
    {
        long startTime = Instant.now().getEpochSecond();
        boolean permanent = false;

        return Update(new PlayerData(name, startTime, time, permanent));
    }

    public CompletableFuture<Boolean> Remove(String name)
    {
        TryRefreshPlayer(name);
        if (!_playersData.containsKey(name)) return CompletableFuture.completedFuture(false);
        return _provider.Remove(name).thenRun(() -> _playersData.remove(name)).thenApply(res -> true);
    }

    public List<PlayerData> ActiveList()
    {
        List<PlayerData> result = new ArrayList<>();

        for (PlayerData playerData : AllList())
        {
            if (playerData.CanJoin()) result.add(playerData);
        }
        return result;
    }

    public Collection<PlayerData> AllList()
    {
        return _playersData.values();
    }

    private void LoadAll()
    {
        long nowTime = Instant.now().getEpochSecond();
        _playersData.clear();
        _lastRefresh.clear();
        for (PlayerData playerData : _provider.GetAll())
        {
            _playersData.put(playerData.Name, playerData);
            _lastRefresh.put(playerData.Name, nowTime);
        }
    }

    private void TryRefreshPlayer(String name)
    {
        long nowTime = Instant.now().getEpochSecond();
        long timePassed = nowTime - _lastRefresh.getOrDefault(name, nowTime);
        if (timePassed < _refreshInterval) return;

        PlayerData actualData = _provider.Get(name);
        if (actualData == null) _playersData.remove(name);
        else _playersData.put(name, actualData);

        _lastRefresh.put(name, nowTime);
    }
}