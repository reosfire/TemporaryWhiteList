package ru.reosfire.twl.common.data.providers;

import org.apache.commons.lang.NotImplementedException;
import org.yaml.snakeyaml.Yaml;
import ru.reosfire.twl.common.data.IDataProvider;
import ru.reosfire.twl.common.data.PlayerData;
import ru.reosfire.twl.common.lib.yaml.YamlUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class YamlDataProvider implements IDataProvider
{
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Yaml yaml = YamlUtils.createDumpYaml();
    private final File yamlDataFile;
    private Map<String, Map<String, Object>> playersData;

    public YamlDataProvider(File yamlFile) {
        yamlDataFile = yamlFile;

        reloadPlayersData(yamlFile);
    }

    private void reloadPlayersData(File file) {
        Map<String, Map<String, Map<String, Object>>> loadedData = loadYamlFile(file);
        if (loadedData == null) playersData = new LinkedHashMap<>();
        else {
            playersData = loadedData.get("Players");
            if (playersData == null) playersData = new LinkedHashMap<>();
        }
    }

    private Map<String, Map<String, Map<String, Object>>> loadYamlFile(File file) {
        lock.writeLock().lock();

        try (Reader reader = new FileReader(file)) {
            return yaml.load(reader);
        }
        catch (Exception e) {
            throw new RuntimeException("Error while reloading yaml data file", e);
        }
        finally {
            lock.writeLock().unlock();
        }
    }

    private void saveData() {
        lock.writeLock().lock();

        try (Writer writer = new FileWriter(yamlDataFile)) {
            Map<String, Map<String, Map<String, Object>>> output = new LinkedHashMap<>();

            output.put("Players", playersData);

            yaml.dump(output, writer);
        }
        catch (Exception e) {
            throw new RuntimeException("Error while saving yaml data file", e);
        }
        finally {
            lock.writeLock().unlock();
        }
    }

    private Map<String, Object> getPlayerData(String player) {
        return playersData.computeIfAbsent(player, k -> new LinkedHashMap<>());
    }

    @Override
    public CompletableFuture<Void> update(PlayerData playerData) {
        return CompletableFuture.runAsync(() -> {
            reloadPlayersData(yamlDataFile);

            lock.writeLock().lock();

            try {
                Map<String, Object> dataContainer = getPlayerData(playerData.Name);

                dataContainer.put("lastStartTime", playerData.StartTime);
                dataContainer.put("timeAmount", playerData.TimeAmount);
                dataContainer.put("permanent", playerData.Permanent);

                saveData();
            }
            catch (Exception e) {
                throw new RuntimeException("Error while updating player data for: " + playerData.Name, e);
            }
            finally {
                lock.writeLock().unlock();
            }
        });
    }

    @Override
    public CompletableFuture<Void> remove(String playerName) {
        return CompletableFuture.runAsync(() -> {
            reloadPlayersData(yamlDataFile);

            lock.writeLock().lock();
            try {
                playersData.remove(playerName);

                saveData();
            }
            catch (Exception e)
            {
                throw new RuntimeException("Error while removing player data about: " + playerName, e);
            }
            finally {
                lock.writeLock().unlock();
            }
        });
    }

    @Override
    public CompletableFuture<Void> clear() {
        //TODO clear logic
        throw new NotImplementedException();
    }

    @Override
    public PlayerData get(String playerName) {
        reloadPlayersData(yamlDataFile);

        lock.readLock().lock();
        try {
            Map<String, Object> playerData = playersData.get(playerName);
            if (playerData == null) return null;

            return playerDataFromMap(playerName, playerData);
        }
        finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<PlayerData> getAll() {
        reloadPlayersData(yamlDataFile);

        lock.readLock().lock();
        try {
            ArrayList<PlayerData> result = new ArrayList<>();

            for (String key : playersData.keySet()) {
                result.add(playerDataFromMap(key, playersData.get(key)));
            }

            return result;
        }
        finally {
            lock.readLock().unlock();
        }
    }

    private PlayerData playerDataFromMap(String name, Map<String, Object> map) {
        long lastStartTime = getLong(map, "lastStartTime");
        long timeAmount = getLong(map, "timeAmount");
        boolean permanent = (Boolean) map.get("permanent");
        return new PlayerData(name, lastStartTime, timeAmount, permanent);
    }

    private long getLong(Map<String, Object> map, String path) {
        Object result = map.get(path);
        if (result instanceof Integer) return (Integer) result;
        else if (result instanceof Long) return (Long) result;
        else throw new RuntimeException(path + "is not an Int or Long");
    }
}