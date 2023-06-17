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

public class YamlDataProvider implements IDataProvider
{
    private final Yaml yaml = YamlUtils.createDumpYaml();
    private final File yamlDataFile;
    private Map<String, YamlPlayerData> yamlData;

    public YamlDataProvider(File yamlFile)
    {
        yamlDataFile = yamlFile;

        reloadYaml(yamlFile);
    }

    private void reloadYaml(File file)
    {
        Map<String, Map<String, YamlPlayerData>> loadedData = loadDataYaml(file);
        yamlData = loadedData.get("Players");

        if (yamlData == null) yamlData = new LinkedHashMap<>();
    }

    private Map<String, Map<String, YamlPlayerData>> loadDataYaml(File file) {
        synchronized (yamlDataFile)
        {
            try (Reader reader = new FileReader(file)) {
                return yaml.load(reader);
            }
            catch (Exception e) {
                throw new RuntimeException("Error while reloading yaml data file", e);
            }
        }
    }

    private void saveData() {
        synchronized (yamlDataFile) {
            try (Writer writer = new FileWriter(yamlDataFile)) {
                Map<String, Map<String, YamlPlayerData>> output = new LinkedHashMap<>();

                output.put("Players", yamlData);

                yaml.dump(output, writer);
            }
            catch (Exception e) {
                throw new RuntimeException("Error while saving yaml data file", e);
            }
        }
    }

    private YamlPlayerData getPlayerData(String player)
    {
        YamlPlayerData result = yamlData.get(player);
        if (result == null) {
            result = new YamlPlayerData();
            yamlData.put(player, result);
        }
        return result;
    }

    @Override
    public CompletableFuture<Void> update(PlayerData playerData)
    {
        return CompletableFuture.runAsync(() ->
        {
            synchronized (yamlDataFile)
            {
                reloadYaml(yamlDataFile);

                YamlPlayerData dataContainer = getPlayerData(playerData.Name);

                dataContainer.lastStartTime = playerData.StartTime;
                dataContainer.timeAmount = playerData.TimeAmount;
                dataContainer.permanent = playerData.Permanent;

                try
                {
                    saveData();
                }
                catch (Exception e)
                {
                    throw new RuntimeException("Error while updating player data for: " + playerData.Name, e);
                }
            }
        });
    }

    @Override
    public CompletableFuture<Void> remove(String playerName)
    {
        return CompletableFuture.runAsync(() ->
        {
            reloadYaml(yamlDataFile);

            yamlData.remove(playerName);

            synchronized (yamlDataFile)
            {
                try
                {
                    saveData();
                }
                catch (Exception e)
                {
                    throw new RuntimeException("Error while removing player data about: " + playerName, e);
                }
            }
        });
    }

    @Override
    public CompletableFuture<Void> clear() {
        //TODO clear logic
        throw new NotImplementedException();
    }

    @Override
    public PlayerData get(String playerName)
    {
        reloadYaml(yamlDataFile);

        YamlPlayerData playerData = yamlData.get(playerName);
        if (playerData == null) return null;
        return new PlayerData(playerName, playerData.lastStartTime, playerData.timeAmount, playerData.permanent);
    }

    @Override
    public List<PlayerData> getAll()
    {
        reloadYaml(yamlDataFile);

        ArrayList<PlayerData> result = new ArrayList<>();

        for (String key : yamlData.keySet())
        {
            YamlPlayerData playerData = yamlData.get(key);
            result.add(new PlayerData(key, playerData.lastStartTime, playerData.timeAmount, playerData.permanent));
        }

        return result;
    }

    private static class YamlPlayerData {
        public Long lastStartTime;
        public Long timeAmount;
        public Boolean permanent;
    }
}