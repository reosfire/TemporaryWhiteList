package ru.reosfire.twl.spigot.data.providers;

import org.apache.commons.lang.NotImplementedException;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import ru.reosfire.twl.common.data.IDataProvider;
import ru.reosfire.twl.common.data.PlayerData;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class YamlDataProvider implements IDataProvider
{
    private final File yamlDataFile;
    private YamlConfiguration yamlDataConfig;

    public YamlDataProvider(File yamlFile) throws IOException, InvalidConfigurationException
    {
        yamlDataFile = yamlFile;
        yamlDataConfig = new YamlConfiguration();
        yamlDataConfig.load(yamlDataFile);
    }

    private void reloadYaml()
    {
        synchronized (yamlDataFile)
        {
            try
            {
                yamlDataConfig = new YamlConfiguration();
                yamlDataConfig.load(yamlDataFile);
            }
            catch (Exception e)
            {
                throw new RuntimeException("Error while reloading yaml data file", e);
            }
        }
    }

    private ConfigurationSection getPlayersSection()
    {
        ConfigurationSection playersSection = yamlDataConfig.getConfigurationSection("Players");
        if (playersSection == null) playersSection = yamlDataConfig.createSection("Players");
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
    public CompletableFuture<Void> update(PlayerData playerData)
    {
        return CompletableFuture.runAsync(() ->
        {
            synchronized (yamlDataFile)
            {
                reloadYaml();

                ConfigurationSection playerSection = getPlayerSection(playerData.Name);

                playerSection.set("lastStartTime", playerData.StartTime);
                playerSection.set("permanent", playerData.Permanent);
                playerSection.set("timeAmount", playerData.TimeAmount);

                try
                {
                    yamlDataConfig.save(yamlDataFile);
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
            reloadYaml();

            getPlayersSection().set(playerName, null);

            synchronized (yamlDataFile)
            {
                try
                {
                    yamlDataConfig.save(yamlDataFile);
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
        reloadYaml();

        ConfigurationSection player = getPlayersSection().getConfigurationSection(playerName);
        if (player == null) return null;
        return new YamlPlayerData(player);
    }

    @Override
    public List<PlayerData> getAll()
    {
        reloadYaml();

        ArrayList<PlayerData> result = new ArrayList<>();

        ConfigurationSection players = getPlayersSection();
        for (String player : players.getKeys(false))
        {
            result.add(new YamlPlayerData(getPlayerSection(player)));
        }

        return result;
    }
}