package ru.reosfire.temporarywhitelist.Data;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import ru.reosfire.temporarywhitelist.Lib.Yaml.YamlConfig;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class YamlDataProvider implements IDataProvider
{
    private final File _yamlDataFile;
    private YamlConfiguration _yamlDataConfig;

    public YamlDataProvider(File yamlFile) throws IOException, InvalidConfigurationException
    {
        _yamlDataFile = yamlFile;
        _yamlDataConfig = YamlConfig.LoadOrCreate(_yamlDataFile);
    }

    private void ReloadYaml()
    {
        try
        {
            _yamlDataConfig = YamlConfig.LoadOrCreate(_yamlDataFile);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error while reloading yaml data file", e);
        }
    }

    @Override
    public CompletableFuture<Void> Update(PlayerData playerData)
    {
        return CompletableFuture.runAsync(() ->
        {
            ReloadYaml();

            ConfigurationSection playersSection = _yamlDataConfig.getConfigurationSection("Players");

            ConfigurationSection playerSection = playersSection.getConfigurationSection(playerData.Name);
            if (playerSection == null) playerSection = playersSection.createSection(playerData.Name);


            playerSection.set("lastStartTime", playerData.StartTime);
            playerSection.set("permanent", playerData.Permanent);
            playerSection.set("timeAmount", playerData.TimeAmount);

            try
            {
                _yamlDataConfig.save(_yamlDataFile);
            }
            catch (Exception e)
            {
                throw new RuntimeException("Error while updating player data for: " + playerData.Name, e);
            }
        });
    }

    @Override
    public CompletableFuture<Void> Remove(String playerName)
    {
        return CompletableFuture.runAsync(() ->
        {
            ReloadYaml();

            ConfigurationSection playersSection = _yamlDataConfig.getConfigurationSection("Players");
            playersSection.set(playerName, null);

            try
            {
                _yamlDataConfig.save(_yamlDataFile);
            }
            catch (Exception e)
            {
                throw new RuntimeException("Error while removing player data about: " + playerName, e);
            }
        });
    }

    @Override
    public PlayerData Get(String playerName)
    {
        ReloadYaml();

        ConfigurationSection players = _yamlDataConfig.getConfigurationSection("Players");
        return new PlayerData(players.getConfigurationSection(playerName));
    }

    @Override
    public List<PlayerData> GetAll()
    {
        ReloadYaml();

        ArrayList<PlayerData> result = new ArrayList<>();

        ConfigurationSection players = _yamlDataConfig.getConfigurationSection("Players");
        for (String player : players.getKeys(false))
        {
            ConfigurationSection playerSection = players.getConfigurationSection(player);
            result.add(new PlayerData(playerSection));
        }

        return result;
    }
}