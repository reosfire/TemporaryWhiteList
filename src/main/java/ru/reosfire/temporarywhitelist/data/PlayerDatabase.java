package ru.reosfire.temporarywhitelist.data;

import org.apache.commons.lang.NullArgumentException;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class PlayerDatabase implements IUpdatable
{
    private final IDataProvider provider;
    private final Map<String, PlayerData> playersData = new ConcurrentSkipListMap<>();
    private final Map<String, Long> lastRefresh = new ConcurrentHashMap<>();
    private long lastAllRefresh;
    private final long refreshInterval;
    private final boolean ignoreCase;

    public IDataProvider getProvider()
    {
        return provider;
    }

    public PlayerDatabase(IDataProvider provider, long refreshInterval, boolean ignoreCase)
    {
        this.provider = provider;
        this.refreshInterval = refreshInterval;
        this.ignoreCase = ignoreCase;
        loadAll();
    }

    public PlayerData getPlayerData(String name)
    {
        if (ignoreCase) name = name.toLowerCase(Locale.ROOT);

        tryRefreshPlayer(name);
        return playersData.get(name);
    }

    @Override
    public CompletableFuture<Boolean> update(PlayerData playerData)
    {
        if (playerData == null) throw new NullArgumentException("playerName");

        String name = playerData.Name;
        if (ignoreCase) name = name.toLowerCase(Locale.ROOT);

        tryRefreshPlayer(name);

        PlayerData oldData = getPlayerData(name);
        if (oldData != null && oldData.isSame(playerData)) return CompletableFuture.completedFuture(false);
        String finalName = name;
        return provider.update(playerData).handle((res, ex) ->
        {
            if (ex != null) throw new RuntimeException(ex);
            playersData.put(finalName, playerData);
            return true;
        });
    }

    public boolean canJoin(String name)
    {
        if (ignoreCase) name = name.toLowerCase(Locale.ROOT);

        PlayerData playerData = getPlayerData(name);
        if (playerData == null) return false;
        return playerData.canJoin();
    }

    public CompletableFuture<Boolean> add(String name, long addedTime)
    {
        if (ignoreCase) name = name.toLowerCase(Locale.ROOT);

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
            timeAmount = playerData.isTimedOut() ? addedTime : playerData.timeLeft() + addedTime;
            permanent = playerData.Permanent;
        }

        return update(new PlayerData(name, startTime, timeAmount, permanent));
    }

    public CompletableFuture<Boolean> setPermanent(String name)
    {
        if (ignoreCase) name = name.toLowerCase(Locale.ROOT);

        long startTime = Instant.now().getEpochSecond();
        long timeAmount = 0;
        boolean permanent = true;

        return update(new PlayerData(name, startTime, timeAmount, permanent));
    }

    public CompletableFuture<Boolean> set(String name, long time)
    {
        if (ignoreCase) name = name.toLowerCase(Locale.ROOT);

        long startTime = Instant.now().getEpochSecond();
        boolean permanent = false;

        return update(new PlayerData(name, startTime, time, permanent));
    }

    public CompletableFuture<Boolean> remove(String name)
    {
        if (ignoreCase) name = name.toLowerCase(Locale.ROOT);

        tryRefreshPlayer(name);
        if (!playersData.containsKey(name)) return CompletableFuture.completedFuture(false);
        String finalName = name;
        return provider.remove(name).thenRun(() -> playersData.remove(finalName)).thenApply(res -> true);
    }

    public List<PlayerData> activeList()
    {
        List<PlayerData> result = new ArrayList<>();

        for (PlayerData playerData : allList())
        {
            if (playerData.canJoin()) result.add(playerData);
        }
        return result;
    }

    public Collection<PlayerData> allList()
    {
        tryRefreshAll();
        return playersData.values();
    }

    private void loadAll()
    {
        long nowTime = Instant.now().getEpochSecond();
        playersData.clear();
        lastRefresh.clear();
        for (PlayerData playerData : provider.getAll())
        {
            String name = playerData.Name;
            if (ignoreCase) name = name.toLowerCase(Locale.ROOT);

            playersData.put(name, playerData);
            lastRefresh.put(name, nowTime);
        }
    }

    private void tryRefreshPlayer(String name)
    {
        if (refreshInterval < 0) return;

        long nowTime = Instant.now().getEpochSecond();
        if (refreshInterval != 0)
        {
            long timePassed = nowTime - lastRefresh.getOrDefault(name, 0L);
            if (timePassed < refreshInterval) return;
        }

        if (ignoreCase) name = name.toLowerCase(Locale.ROOT);

        PlayerData actualData = provider.get(name);
        if (actualData == null) playersData.remove(name);
        else playersData.put(name, actualData);

        lastRefresh.put(name, nowTime);
    }

    private void tryRefreshAll()
    {
        long nowTime = Instant.now().getEpochSecond();
        if (nowTime - lastAllRefresh < refreshInterval) return;
        loadAll();
        lastAllRefresh = nowTime;
    }
}