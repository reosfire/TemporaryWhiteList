package ru.reosfire.temporarywhitelist.Data;

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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SqlDataProvider implements IDataProvider
{
    private static final String[] AllColumns = new String[] {"*"};

    private final Config _configuration;
    private final SqlConnection _sqlConnection;

    public SqlDataProvider(Config configuration) throws SQLException
    {
        _configuration = configuration;

        _sqlConnection = new SqlConnection(_configuration.SqlConfiguration);
        _sqlConnection.CreateTable(_configuration.SqlTable, new TableColumn("Player", ColumnType.VarChar.setMax(32),
                ColumnFlag.Not_null, ColumnFlag.Unique), new TableColumn("Permanent", ColumnType.Boolean,
                ColumnFlag.Not_null), new TableColumn("LastStartTime", ColumnType.BigInt, ColumnFlag.Not_null),
                new TableColumn("TimeAmount", ColumnType.BigInt, ColumnFlag.Not_null));
    }

    @Override
    public CompletableFuture<Void> Update(PlayerData playerData)
    {
        return CompletableFuture.runAsync(() ->
        {
            try
            {
                String setRequest = "INSERT INTO " + _configuration.SqlTable + " (Player, Permanent, LastStartTime, " +
                        "TimeAmount)" + "VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE Permanent=?, LastStartTime=?, " +
                        "TimeAmount=?;";
                PreparedStatement statement = _sqlConnection.getConnection().prepareStatement(setRequest);
                statement.setString(1, playerData.Name);
                statement.setBoolean(2, playerData.Permanent);
                statement.setLong(3, playerData.StartTime);
                statement.setLong(4, playerData.TimeAmount);
                statement.setBoolean(5, playerData.Permanent);
                statement.setLong(6, playerData.StartTime);
                statement.setLong(7, playerData.TimeAmount);

                statement.executeUpdate();
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
            try
            {
                String removeRequest = "DELETE FROM "+ _configuration.SqlTable +" WHERE Player=?;";
                PreparedStatement statement = _sqlConnection.getConnection().prepareStatement(removeRequest);
                statement.setString(1, playerName);
                statement.executeUpdate();
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
        try
        {
            ResultSet player = _sqlConnection.Select(_configuration.SqlTable, AllColumns, new Where("Player",
                    Comparer.Equal, playerName));
            if (player.isAfterLast()) return null;
            return new PlayerData(player);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    @Override
    public List<PlayerData> GetAll()
    {
        try
        {
            List<PlayerData> result = new ArrayList<>();
            ResultSet resultSet = _sqlConnection.Select(_configuration.SqlTable, AllColumns);

            while (resultSet.next())
            {
                result.add(new PlayerData(resultSet));
            }
            return result;
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error while getting all data");
        }
    }
}