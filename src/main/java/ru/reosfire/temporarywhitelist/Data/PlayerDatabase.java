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
    private final boolean _ignoreCase;

    public IDataProvider getProvider()
    {
        return _provider;
    }

    public PlayerDatabase(IDataProvider provider, long refreshInterval, boolean ignoreCase)
    {
        _provider = provider;
        _refreshInterval = refreshInterval;
        _ignoreCase = ignoreCase;
        LoadAll();
    }

    public PlayerData getPlayerData(String name)
    {
        if (_ignoreCase) name = name.toLowerCase(Locale.ROOT);

        TryRefreshPlayer(name);
        return _playersData.get(name);
    }

    public CompletableFuture<Boolean> Update(PlayerData playerData)
    {
        if (playerData == null) throw new NullArgumentException("playerName");

        String name = playerData.Name;
        if (_ignoreCase) name = name.toLowerCase(Locale.ROOT);

        TryRefreshPlayer(name);

        PlayerData oldData = getPlayerData(name);
        if (oldData != null && oldData.isSame(playerData)) return CompletableFuture.completedFuture(false);
        String finalName = name;
        return _provider.Update(playerData).handle((res, ex) ->
        {
            if (ex != null) throw new RuntimeException(ex);
            _playersData.put(finalName, playerData);
            return true;
        });
    }

    public boolean CanJoin(String name)
    {
        if (_ignoreCase) name = name.toLowerCase(Locale.ROOT);

        PlayerData playerData = getPlayerData(name);
        if (playerData == null) return false;
        return playerData.CanJoin();
    }

    public CompletableFuture<Boolean> Add(String name, long addedTime)
    {
        if (_ignoreCase) name = name.toLowerCase(Locale.ROOT);

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
        if (_ignoreCase) name = name.toLowerCase(Locale.ROOT);

        long startTime = Instant.now().getEpochSecond();
        long timeAmount = 0;
        boolean permanent = true;

        return Update(new PlayerData(name, startTime, timeAmount, permanent));
    }

    public CompletableFuture<Boolean> Set(String name, long time)
    {
        if (_ignoreCase) name = name.toLowerCase(Locale.ROOT);

        long startTime = Instant.now().getEpochSecond();
        boolean permanent = false;

        return Update(new PlayerData(name, startTime, time, permanent));
    }

    public CompletableFuture<Boolean> Remove(String name)
    {
        if (_ignoreCase) name = name.toLowerCase(Locale.ROOT);

        TryRefreshPlayer(name);
        if (!_playersData.containsKey(name)) return CompletableFuture.completedFuture(false);
        String finalName = name;
        return _provider.Remove(name).thenRun(() -> _playersData.remove(finalName)).thenApply(res -> true);
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
            String name = playerData.Name;
            if (_ignoreCase) name = name.toLowerCase(Locale.ROOT);

            _playersData.put(name, playerData);
            _lastRefresh.put(name, nowTime);
        }
    }

    private void TryRefreshPlayer(String name)
    {
        if (_ignoreCase) name = name.toLowerCase(Locale.ROOT);

        long nowTime = Instant.now().getEpochSecond();
        long timePassed = nowTime - _lastRefresh.getOrDefault(name, nowTime);
        if (timePassed < _refreshInterval) return;

        PlayerData actualData = _provider.Get(name);
        if (actualData == null) _playersData.remove(name);
        else _playersData.put(name, actualData);

        _lastRefresh.put(name, nowTime);
    }
}