package ru.reosfire.temporarywhitelist.Data;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import ru.reosfire.temporarywhitelist.Configuration.Localization.MessagesConfig;
import ru.reosfire.temporarywhitelist.Lib.Yaml.YamlConfig;
import ru.reosfire.temporarywhitelist.TimeConverter;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.*;

public class YamlDataBase implements IDataProvider
{
    private final HashMap<String, PlayerData> data;
    private final File YamlDataFile;
    private final YamlConfiguration YamlDataConfig;
    private final MessagesConfig Messages;

    public YamlDataBase(MessagesConfig messages, File yamlFile) throws IOException, InvalidConfigurationException
    {
        Messages = messages;
        YamlDataFile = yamlFile;
        YamlDataConfig = YamlConfig.LoadOrCreate(YamlDataFile);

        data = new HashMap<>();
        ConfigurationSection players = YamlDataConfig.getConfigurationSection("Players");
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
    public void Add(String nick, long addedTime) throws IOException
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
        ConfigurationSection playersSection = YamlDataConfig.getConfigurationSection("Players");
        ConfigurationSection playerSection = playersSection.createSection(nick);
        playerSection.set("lastStartTime", StartTime);
        playerSection.set("permanent", Permanent);
        playerSection.set("timeAmount", TimeAmount);

        YamlDataConfig.save(YamlDataFile);
        data.put(nick, new PlayerData(StartTime, TimeAmount, false));
    }
    @Override
    public void Add(String nick) throws Exception
    {
        ConfigurationSection playersSection = YamlDataConfig.getConfigurationSection("Players");
        ConfigurationSection playerSection = playersSection.createSection(nick);
        long nowEpochSecond = Instant.now().getEpochSecond();
        playerSection.set("lastStartTime", nowEpochSecond);
        playerSection.set("permanent", true);
        playerSection.set("timeAmount", 0);

        YamlDataConfig.save(YamlDataFile);
        data.put(nick, new PlayerData(nowEpochSecond, 0, true));
    }

    @Override
    public void Remove(String nick) throws IOException
    {
        if (!data.containsKey(nick)) return;
        ConfigurationSection playersSection = YamlDataConfig.getConfigurationSection("Players");
        playersSection.set(nick, null);

        YamlDataConfig.save(YamlDataFile);
        data.remove(nick);
    }

    @Override
    public void SetPermanent(String player, boolean permanent) throws IOException
    {
        if(!data.containsKey(player)) return;
        ConfigurationSection playerSection = YamlDataConfig.getConfigurationSection("Players").getConfigurationSection(player);
        playerSection.set("permanent", permanent);

        YamlDataConfig.save(YamlDataFile);
        data.get(player).setPermanent(permanent);
    }

    @Override
    public String Check(String player)
    {
        if (!data.containsKey(player)) return Messages.DataBase.PlayerUndefined;
        PlayerData playerData = data.get(player);
        if(playerData.isPermanent()) return Messages.DataBase.SubscribeNeverEnd;
        long secondsAmount = playerData.subscriptionEndTime() - Instant.now().getEpochSecond();
        if(secondsAmount < 0) return Messages.DataBase.SubscribeEnd;
        return "действительна еще " + TimeConverter.ReadableTime(secondsAmount);
    }

    @Override
    public List<String> ActiveList(){
        List<String> active = new ArrayList<>();
        for (Map.Entry<String, PlayerData> player: data.entrySet())
        {
            if(player.getValue().isTimeOut() && !player.getValue().isPermanent()) continue;
            active.add(player.getKey());
        }
        return active;
    }
    @Override
    public List<String> AllList(){
        return new ArrayList<>(data.keySet());
    }
}