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

    private ConfigurationSection getPlayersSection()
    {
        ConfigurationSection playersSection = _yamlDataConfig.getConfigurationSection("Players");
        if (playersSection == null) playersSection = _yamlDataConfig.createSection("Players");
        return playersSection;
    }
    private ConfigurationSection getPlayerSection(String player)
    {
        ConfigurationSection playersSection = getPlayersSection();
        ConfigurationSection playerSection = playersSection.getConfigurationSection(player);
        if (playerSection == null) playerSection = playersSection.createSection(player);
        return playerSection;
    }

    @Override
    public CompletableFuture<Void> Update(PlayerData playerData)
    {
        return CompletableFuture.runAsync(() ->
        {
            synchronized (_yamlDataFile)
            {
                ReloadYaml();

                ConfigurationSection playerSection = getPlayerSection(playerData.Name);

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
            }
        });
    }

    @Override
    public CompletableFuture<Void> Remove(String playerName)
    {
        return CompletableFuture.runAsync(() ->
        {
            ReloadYaml();

            getPlayersSection().set(playerName, null);

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

        ConfigurationSection player = getPlayersSection().getConfigurationSection(playerName);
        if (player == null) return null;
        return new PlayerData(player);
    }

    @Override
    public List<PlayerData> GetAll()
    {
        ReloadYaml();

        ArrayList<PlayerData> result = new ArrayList<>();

        ConfigurationSection players = getPlayersSection();
        for (String player : players.getKeys(false))
        {
            result.add(new PlayerData(getPlayerSection(player)));
        }

        return result;
    }
}