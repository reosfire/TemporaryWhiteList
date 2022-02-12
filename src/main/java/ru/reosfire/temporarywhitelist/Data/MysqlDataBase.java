package ru.reosfire.temporarywhitelist.Data;

import org.bukkit.Bukkit;
import ru.reosfire.temporarywhitelist.Configuration.Config;
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
    private final Config Configuration;
    private final SqlConnection sqlConnection;
    private static final String[] AllColumns = new String[] {"*"};

    private final HashMap<String, PlayerData> PlayerDataCache = new HashMap<>();
    private void UpdateCache(String nick) throws SQLException
    {
        ResultSet set = sqlConnection.Select(Configuration.SqlTable, AllColumns, new Where("Player", Comparer.Equal, nick));
        PlayerDataCache.put(nick, new PlayerData(set));
    }

    public MysqlDataBase(Config configuration) throws SQLException
    {
        Configuration = configuration;
        sqlConnection = new SqlConnection(configuration.SqlConfiguration);
        sqlConnection.CreateTable(Configuration.SqlTable,
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
            if (!PlayerDataCache.containsKey(nick)) UpdateCache(nick);
            PlayerData playerData = PlayerDataCache.get(nick);

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
        ResultSet playerData = sqlConnection.Select(Configuration.SqlTable, AllColumns, new Where("Player", Comparer.Equal, nick));

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
        String setRequest = "INSERT INTO "+ Configuration.SqlTable +" (Player, Permanent, LastStartTime, TimeAmount)" + "VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE Permanent=?, LastStartTime=?, TimeAmount=?;";
        PreparedStatement statement = sqlConnection.getConnection().prepareStatement(setRequest);
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
        String setRequest = "INSERT INTO "+ Configuration.SqlTable +" (Player, Permanent, LastStartTime, TimeAmount)" + "VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE Permanent=?, LastStartTime=?, TimeAmount=?;";
        PreparedStatement statement = sqlConnection.getConnection().prepareStatement(setRequest);
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
        String removeRequest = "DELETE FROM "+ Configuration.SqlTable +" WHERE Player=?;";
        PreparedStatement statement = sqlConnection.getConnection().prepareStatement(removeRequest);
        statement.setString(1, nick);
        statement.executeUpdate();
        UpdateCache(nick);
    }

    @Override
    public void SetPermanent(String nick, boolean permanent) throws Exception
    {
        String setRequest = "INSERT INTO "+ Configuration.SqlTable +" (Player, Permanent) VALUES (?, ?) ON DUPLICATE KEY UPDATE Permanent=?;";
        PreparedStatement statement = sqlConnection.getConnection().prepareStatement(setRequest);
        statement.setString(1, nick);
        statement.setBoolean(2, permanent);
        statement.setBoolean(3, permanent);
        statement.executeUpdate(setRequest);
        UpdateCache(nick);
    }

    @Override
    public String Check(String nick) throws Exception
    {
        if (!PlayerDataCache.containsKey(nick)) UpdateCache(nick);
        PlayerData playerData = PlayerDataCache.get(nick);

        if (playerData.undefined) return Configuration.Messages.DataBase.PlayerUndefined;
        if (playerData.isPermanent()) return Configuration.Messages.DataBase.SubscribeNeverEnd;
        long secondsAmount = playerData.subscriptionEndTime() - Instant.now().getEpochSecond();
        if (secondsAmount < 0) return Configuration.Messages.DataBase.SubscribeEnd;
        return TimeConverter.ReadableTime(secondsAmount);
    }

    @Override
    public List<String> ActiveList() throws Exception
    {
        ResultSet playerData = sqlConnection.Select(Configuration.SqlTable, AllColumns);
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
        ResultSet playerData = sqlConnection.Select(Configuration.SqlTable, AllColumns);
        List<String> result = new LinkedList<>();
        while (playerData.next())
        {
            result.add(playerData.getString("Player"));
        }
        return result;
    }
}