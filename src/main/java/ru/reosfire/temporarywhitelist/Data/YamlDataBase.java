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
    private final MessagesConfig _messages;
    private final TimeConverter _timeConverter;

    private final HashMap<String, PlayerData> _data;
    private final File _yamlDataFile;
    private final YamlConfiguration _yamlDataConfig;

    public YamlDataBase(MessagesConfig messages, File yamlFile, TimeConverter converter) throws IOException, InvalidConfigurationException
    {
        _messages = messages;
        _timeConverter = converter;

        _yamlDataFile = yamlFile;
        _yamlDataConfig = YamlConfig.LoadOrCreate(_yamlDataFile);

        _data = new HashMap<>();
        ConfigurationSection players = _yamlDataConfig.getConfigurationSection("Players");
        for (String player : players.getKeys(false))
        {
            ConfigurationSection playerSection = players.getConfigurationSection(player);
            _data.put(player, new PlayerData(playerSection.getLong("lastStartTime"), playerSection.getLong("timeAmount"), playerSection.getBoolean("permanent")));
        }
    }

    @Override
    public boolean CanJoin(String playerDisplayName)
    {
        if (!_data.containsKey(playerDisplayName)) return false;
        PlayerData playerData = _data.get(playerDisplayName);
        return playerData.is_permanent() || playerData.subscriptionEndTime() > Instant.now().getEpochSecond();
    }

    @Override
    public void Add(String nick, long addedTime) throws IOException
    {
        long StartTime;
        long TimeAmount;
        boolean Permanent;
        if (_data.containsKey(nick) && !_data.get(nick).isTimeOut())
        {
            PlayerData playerData = _data.get(nick);
            TimeAmount = playerData.get_timeAmount() + addedTime;
            StartTime = playerData.getLastStartTime();
            Permanent = playerData.is_permanent();
        }
        else
        {
            TimeAmount = addedTime;
            StartTime = Instant.now().getEpochSecond();
            Permanent = false;
        }
        ConfigurationSection playersSection = _yamlDataConfig.getConfigurationSection("Players");
        ConfigurationSection playerSection = playersSection.createSection(nick);
        playerSection.set("lastStartTime", StartTime);
        playerSection.set("permanent", Permanent);
        playerSection.set("timeAmount", TimeAmount);

        _yamlDataConfig.save(_yamlDataFile);
        _data.put(nick, new PlayerData(StartTime, TimeAmount, false));
    }
    @Override
    public void Add(String nick) throws Exception
    {
        ConfigurationSection playersSection = _yamlDataConfig.getConfigurationSection("Players");
        ConfigurationSection playerSection = playersSection.createSection(nick);
        long nowEpochSecond = Instant.now().getEpochSecond();
        playerSection.set("lastStartTime", nowEpochSecond);
        playerSection.set("permanent", true);
        playerSection.set("timeAmount", 0);

        _yamlDataConfig.save(_yamlDataFile);
        _data.put(nick, new PlayerData(nowEpochSecond, 0, true));
    }

    @Override
    public void Remove(String nick) throws IOException
    {
        if (!_data.containsKey(nick)) return;
        ConfigurationSection playersSection = _yamlDataConfig.getConfigurationSection("Players");
        playersSection.set(nick, null);

        _yamlDataConfig.save(_yamlDataFile);
        _data.remove(nick);
    }

    @Override
    public void SetPermanent(String player, boolean permanent) throws IOException
    {
        if(!_data.containsKey(player)) return;
        ConfigurationSection playerSection = _yamlDataConfig.getConfigurationSection("Players").getConfigurationSection(player);
        playerSection.set("permanent", permanent);

        _yamlDataConfig.save(_yamlDataFile);
        _data.get(player).set_permanent(permanent);
    }

    @Override
    public String Check(String player)
    {
        if (!_data.containsKey(player)) return _messages.DataBase.PlayerUndefined;
        PlayerData playerData = _data.get(player);
        if(playerData.is_permanent()) return _messages.DataBase.SubscribeNeverEnd;
        long secondsAmount = playerData.subscriptionEndTime() - Instant.now().getEpochSecond();
        if(secondsAmount < 0) return _messages.DataBase.SubscribeEnd;
        return _timeConverter.ReadableTime(secondsAmount);
    }

    @Override
    public List<String> ActiveList(){
        List<String> active = new ArrayList<>();
        for (Map.Entry<String, PlayerData> player: _data.entrySet())
        {
            if(player.getValue().isTimeOut() && !player.getValue().is_permanent()) continue;
            active.add(player.getKey());
        }
        return active;
    }
    @Override
    public List<String> AllList(){
        return new ArrayList<>(_data.keySet());
    }
}