package ru.reosfire.temporarywhitelist.Data;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import ru.reosfire.temporarywhitelist.TemporaryWhiteList;
import ru.reosfire.temporarywhitelist.TimeConverter;

import java.io.File;
import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class YamlDataBase implements IDataProvider
{
    private HashMap<String, PlayerData> data = new HashMap<>();
    private YamlConfiguration yamlDataFile;

    public YamlDataBase(YamlConfiguration yamlConfiguration)
    {
        yamlDataFile = yamlConfiguration;
        Load(yamlConfiguration);
    }

    public void Load(YamlConfiguration yamlConfiguration)
    {
        data = new HashMap<>();
        ConfigurationSection players = yamlConfiguration.getConfigurationSection("Players");
        for (String player : players.getKeys(false))
        {
            ConfigurationSection playerSection = players.getConfigurationSection(player);
            data.put(player, new PlayerData(playerSection.getLong("lastStartTime"), playerSection.getLong("timeAmount"), playerSection.getBoolean("permanent")));
        }
    }

    @Override
    public boolean CanJoin(String playerDisplayName)
    {
        if (!data.containsKey(playerDisplayName)) return false;
        PlayerData playerData = data.get(playerDisplayName);
        return playerData.isPermanent() || playerData.subscriptionEndTime() > Instant.now().getEpochSecond();
    }

    @Override
    public void Add(String nick, long addedTime) throws Exception
    {
        long StartTime;
        long TimeAmount;
        boolean Permanent;
        if (data.containsKey(nick) && !data.get(nick).isTimeOut())
        {
            PlayerData playerData = data.get(nick);
            TimeAmount = playerData.getTimeAmount() + addedTime;
            StartTime = playerData.getLastStartTime();
            Permanent = playerData.isPermanent();
        }
        else
        {
            TimeAmount = addedTime;
            StartTime = Instant.now().getEpochSecond();
            Permanent = false;
        }
        ConfigurationSection playersSection = yamlDataFile.getConfigurationSection("Players");
        ConfigurationSection playerSection = playersSection.createSection(nick);
        playerSection.set("lastStartTime", StartTime);
        playerSection.set("permanent", Permanent);
        playerSection.set("timeAmount", TimeAmount);

        File dataFile = new File(TemporaryWhiteList.getSingleton().getDataFolder(), TemporaryWhiteList.getConfiguration().DataFile);
        try
        {
            yamlDataFile.save(dataFile);
            data.put(nick, new PlayerData(StartTime, TimeAmount, false));
        }
        catch (Exception e)
        {
            throw new Exception();
        }
    }
    @Override
    public void Add(String nick) throws Exception
    {
        ConfigurationSection playersSection = yamlDataFile.getConfigurationSection("Players");
        ConfigurationSection playerSection = playersSection.createSection(nick);
        long nowEpochSecond = Instant.now().getEpochSecond();
        playerSection.set("lastStartTime", nowEpochSecond);
        playerSection.set("permanent", true);
        playerSection.set("timeAmount", 0);

        try
        {
            File dataFile = new File(TemporaryWhiteList.getSingleton().getDataFolder(), TemporaryWhiteList.getConfiguration().DataFile);
            yamlDataFile.save(dataFile);
            data.put(nick, new PlayerData(nowEpochSecond, 0, true));
        }
        catch (Exception e)
        {
            throw new Exception();
        }
    }

    @Override
    public void Remove(String nick) throws Exception
    {
        if (!data.containsKey(nick)) return;
        ConfigurationSection playersSection = yamlDataFile.getConfigurationSection("Players");
        playersSection.set(nick, null);

        try
        {
            File dataFile = new File(TemporaryWhiteList.getSingleton().getDataFolder(), TemporaryWhiteList.getConfiguration().DataFile);
            yamlDataFile.save(dataFile);
            data.remove(nick);
        }
        catch (Exception e)
        {
            throw new Exception();
        }
    }

    @Override
    public void SetPermanent(String player, boolean permanent) throws Exception
    {
        if(!data.containsKey(player)) return;
        ConfigurationSection playerSection = yamlDataFile.getConfigurationSection("Players").getConfigurationSection(player);
        playerSection.set("permanent", permanent);

        try
        {
            File dataFile = new File(TemporaryWhiteList.getSingleton().getDataFolder(), TemporaryWhiteList.getConfiguration().DataFile);
            yamlDataFile.save(dataFile);
            data.get(player).setPermanent(permanent);
        }
        catch (Exception e)
        {
            throw new Exception();
        }
    }

    @Override
    public String Check(String player)
    {
        if (!data.containsKey(player)) return TemporaryWhiteList.getMessages().PlayerUndefined;
        PlayerData playerData = data.get(player);
        if(playerData.isPermanent()) return TemporaryWhiteList.getMessages().SubscribeNeverEnd;
        long secondsAmount = playerData.subscriptionEndTime() - Instant.now().getEpochSecond();
        if(secondsAmount < 0) return TemporaryWhiteList.getMessages().SubscribeEnd;
        return "действительна еще " + TimeConverter.ReadableTime(secondsAmount);
    }

    @Override
    public List<String> ActiveList(){
        List<String> active = new LinkedList();
        for (Map.Entry<String, PlayerData> player: data.entrySet())
        {
            if(player.getValue().isTimeOut() && !player.getValue().isPermanent()) continue;
            active.add(player.getKey());
        }
        return active;
    }
    @Override
    public List<String> AllList(){
        return new LinkedList(data.keySet());
    }
}