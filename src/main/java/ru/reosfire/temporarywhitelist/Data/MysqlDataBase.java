package ru.reosfire.temporarywhitelist.Data;

import org.bukkit.Bukkit;
import ru.reosfire.temporarywhitelist.Configuration.Config;
import ru.reosfire.temporarywhitelist.Configuration.Localization.MessagesConfig;
import ru.reosfire.temporarywhitelist.Lib.Sql.Selection.Comparer;
import ru.reosfire.temporarywhitelist.Lib.Sql.Selection.Where;
import ru.reosfire.temporarywhitelist.Lib.Sql.SqlConnection;
import ru.reosfire.temporarywhitelist.Lib.Sql.Tables.ColumnFlag;
import ru.reosfire.temporarywhitelist.Lib.Sql.Tables.ColumnType;
import ru.reosfire.temporarywhitelist.Lib.Sql.Tables.TableColumn;
import ru.reosfire.temporarywhitelist.TimeConverter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class MysqlDataBase implements IDataProvider
{
    private final Config _configuration;
    private final MessagesConfig _messages;
    private final TimeConverter _timeConverter;
    private final SqlConnection _sqlConnection;
    private static final String[] AllColumns = new String[] {"*"};

    private final HashMap<String, PlayerData> _playerDataCache = new HashMap<>();
    private void UpdateCache(String nick) throws SQLException
    {
        ResultSet set = _sqlConnection.Select(_configuration.SqlTable, AllColumns, new Where("Player", Comparer.Equal, nick));
        _playerDataCache.put(nick, new PlayerData(set));
    }

    public MysqlDataBase(Config configuration, MessagesConfig messages, TimeConverter converter) throws SQLException
    {
        _configuration = configuration;
        _messages = messages;
        _timeConverter = converter;
        _sqlConnection = new SqlConnection(configuration.SqlConfiguration);
        _sqlConnection.CreateTable(_configuration.SqlTable,
                new TableColumn("Player", ColumnType.VarChar.setMax(32), ColumnFlag.Not_null, ColumnFlag.Unique),
                new TableColumn("Permanent", ColumnType.Boolean, ColumnFlag.Not_null),
                new TableColumn("LastStartTime", ColumnType.BigInt, ColumnFlag.Not_null),
                new TableColumn("TimeAmount", ColumnType.BigInt, ColumnFlag.Not_null));
    }

    @Override
    public boolean CanJoin(String nick)
    {
        try
        {
            if (!_playerDataCache.containsKey(nick)) UpdateCache(nick);
            PlayerData playerData = _playerDataCache.get(nick);

            if (playerData.undefined) return false;
            if (playerData.isPermanent()) return true;
            return !playerData.isTimeOut();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            Bukkit.getLogger().warning(e.toString());
            return false;
        }
    }

    @Override
    public void Add(String nick, long addedTime) throws Exception
    {
        long StartTime;
        long TimeAmount;
        boolean Permanent;
        ResultSet playerData = _sqlConnection.Select(_configuration.SqlTable, AllColumns, new Where("Player", Comparer.Equal, nick));

        long timeLeft;
        if (playerData.next())
        {
            timeLeft = playerData.getLong("LastStartTime") + playerData.getLong("TimeAmount") - Instant.now().getEpochSecond();
            if (timeLeft > 0)
            {
                TimeAmount = playerData.getLong("TimeAmount") + addedTime;
                StartTime = playerData.getLong("LastStartTime");
            }
            else
            {
                TimeAmount = addedTime;
                StartTime = Instant.now().getEpochSecond();
            }
            Permanent = playerData.getBoolean("Permanent");
        }
        else
        {
            TimeAmount = addedTime;
            StartTime = Instant.now().getEpochSecond();
            Permanent = false;
        }
        String setRequest = "INSERT INTO "+ _configuration.SqlTable +" (Player, Permanent, LastStartTime, TimeAmount)" + "VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE Permanent=?, LastStartTime=?, TimeAmount=?;";
        PreparedStatement statement = _sqlConnection.getConnection().prepareStatement(setRequest);
        statement.setString(1, nick);
        statement.setBoolean(2, Permanent);
        statement.setLong(3, StartTime);
        statement.setLong(4, TimeAmount);
        statement.setBoolean(5, Permanent);
        statement.setLong(6, StartTime);
        statement.setLong(7, TimeAmount);

        statement.executeUpdate();
        UpdateCache(nick);
    }

    @Override
    public void Add(String nick) throws Exception
    {
        long StartTime = Instant.now().getEpochSecond();
        long TimeAmount = 0;
        boolean Permanent = true;
        String setRequest = "INSERT INTO "+ _configuration.SqlTable +" (Player, Permanent, LastStartTime, TimeAmount)" + "VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE Permanent=?, LastStartTime=?, TimeAmount=?;";
        PreparedStatement statement = _sqlConnection.getConnection().prepareStatement(setRequest);
        statement.setString(1, nick);
        statement.setBoolean(2, Permanent);
        statement.setLong(3, StartTime);
        statement.setLong(4, TimeAmount);
        statement.setBoolean(5, Permanent);
        statement.setLong(6, StartTime);
        statement.setLong(7, TimeAmount);

        statement.executeUpdate();
        UpdateCache(nick);
    }

    @Override
    public void Remove(String nick) throws Exception
    {
        String removeRequest = "DELETE FROM "+ _configuration.SqlTable +" WHERE Player=?;";
        PreparedStatement statement = _sqlConnection.getConnection().prepareStatement(removeRequest);
        statement.setString(1, nick);
        statement.executeUpdate();
        UpdateCache(nick);
    }

    @Override
    public void SetPermanent(String nick, boolean permanent) throws Exception
    {
        String setRequest = "INSERT INTO "+ _configuration.SqlTable +" (Player, Permanent) VALUES (?, ?) ON DUPLICATE KEY UPDATE Permanent=?;";
        PreparedStatement statement = _sqlConnection.getConnection().prepareStatement(setRequest);
        statement.setString(1, nick);
        statement.setBoolean(2, permanent);
        statement.setBoolean(3, permanent);
        statement.executeUpdate(setRequest);
        UpdateCache(nick);
    }

    @Override
    public String Check(String nick) throws Exception
    {
        if (!_playerDataCache.containsKey(nick)) UpdateCache(nick);
        PlayerData playerData = _playerDataCache.get(nick);

        if (playerData.undefined) return _messages.DataBase.PlayerUndefined;
        if (playerData.isPermanent()) return _messages.DataBase.SubscribeNeverEnd;
        long secondsAmount = playerData.subscriptionEndTime() - Instant.now().getEpochSecond();
        if (secondsAmount < 0) return _messages.DataBase.SubscribeEnd;
        return _timeConverter.ReadableTime(secondsAmount);
    }

    @Override
    public List<String> ActiveList() throws Exception
    {
        ResultSet playerData = _sqlConnection.Select(_configuration.SqlTable, AllColumns);
        List<String> result = new LinkedList<>();
        while (playerData.next())
        {
            long timeLeft = playerData.getLong("LastStartTime") + playerData.getLong("TimeAmount") - Instant.now().getEpochSecond();
            if (!playerData.getBoolean("Permanent") && timeLeft > 0) continue;
            result.add(playerData.getString("Player"));
        }
        return result;
    }

    @Override
    public List<String> AllList() throws Exception
    {
        ResultSet playerData = _sqlConnection.Select(_configuration.SqlTable, AllColumns);
        List<String> result = new LinkedList<>();
        while (playerData.next())
        {
            result.add(playerData.getString("Player"));
        }
        return result;
    }
}