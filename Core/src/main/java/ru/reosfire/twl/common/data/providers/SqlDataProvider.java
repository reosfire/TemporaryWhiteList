package ru.reosfire.twl.common.data.providers;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ru.reosfire.twl.common.data.IDataProvider;
import ru.reosfire.twl.common.data.PlayerData;
import ru.reosfire.twl.common.lib.sql.ISqlConfiguration;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SqlDataProvider implements IDataProvider
{
    private final Configuration configuration;
    private final DataSource dataSource;

    public SqlDataProvider(Configuration configuration) throws SQLException
    {
        this.configuration = configuration;

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(configuration.sqlConfiguration.getConnectionString());
        hikariConfig.setUsername(configuration.sqlConfiguration.getUser());
        hikariConfig.setPassword(configuration.sqlConfiguration.getPassword());
        hikariConfig.setPoolName("TWL Hikari pool");
        hikariConfig.setMaxLifetime(configuration.sqlConfiguration.getMaxConnectionLifetime());

        dataSource = new HikariDataSource(hikariConfig);

        String createTableString = "CREATE TABLE IF NOT EXISTS `" + configuration.table + "` (" +
                "Player VARCHAR(32) NOT NULL UNIQUE, " +
                "Permanent BOOLEAN NOT NULL, " +
                "LastStartTime BIGINT NOT NULL, " +
                "TimeAmount BIGINT NOT NULL);";
        try (Connection connection = dataSource.getConnection())
        {
            try (Statement statement = connection.createStatement())
            {
                statement.executeUpdate(createTableString);
            }
        }
    }

    @Override
    public CompletableFuture<Void> update(PlayerData playerData)
    {
        return CompletableFuture.runAsync(() ->
        {
            String setRequest = "INSERT INTO " + configuration.table + " (Player, Permanent, LastStartTime, " +
                    "TimeAmount)" + "VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE Permanent=?, LastStartTime=?, " +
                    "TimeAmount=?;";

            try (Connection connection = dataSource.getConnection())
            {
                try (PreparedStatement statement = connection.prepareStatement(setRequest))
                {
                    statement.setString(1, playerData.Name);
                    statement.setBoolean(2, playerData.Permanent);
                    statement.setLong(3, playerData.StartTime);
                    statement.setLong(4, playerData.TimeAmount);
                    statement.setBoolean(5, playerData.Permanent);
                    statement.setLong(6, playerData.StartTime);
                    statement.setLong(7, playerData.TimeAmount);

                    statement.executeUpdate();
                }
            }
            catch (Exception e)
            {
                throw new RuntimeException("Error while updating player data for: " + playerData.Name, e);
            }
        });
    }

    @Override
    public CompletableFuture<Void> remove(String playerName)
    {
        return CompletableFuture.runAsync(() ->
        {
            String removeRequest = "DELETE FROM " + configuration.table + " WHERE Player=?;";
            try(Connection connection = dataSource.getConnection())
            {
                try(PreparedStatement statement = connection.prepareStatement(removeRequest))
                {
                    statement.setString(1, playerName);
                    statement.executeUpdate();
                }
            }
            catch (Exception e)
            {
                throw new RuntimeException("Error while removing player data about: " + playerName, e);
            }
        });
    }

    @Override
    public CompletableFuture<Void> clear() {
        return CompletableFuture.runAsync(() ->
        {
            String removeRequest = "DELETE FROM " + configuration.table + ";";
            try(Connection connection = dataSource.getConnection())
            {
                try(Statement statement = connection.createStatement())
                {
                    statement.executeUpdate(removeRequest);
                }
            }
            catch (Exception e)
            {
                throw new RuntimeException("Error while clearing", e);
            }
        });
    }

    @Override
    public PlayerData get(String playerName)
    {
        String selectRequest = "SELECT * FROM " + configuration.table + " WHERE Player=?;";
        try(Connection connection = dataSource.getConnection())
        {
            try(PreparedStatement statement = connection.prepareStatement(selectRequest))
            {
                statement.setString(1, playerName);
                try(ResultSet player = statement.executeQuery())
                {
                    if (!player.next()) return null;
                    return new PlayerData(player);
                }
            }
        }
        catch (Exception e)
        {
            return null;
        }
    }

    @Override
    public List<PlayerData> getAll()
    {
        String selectRequest = "SELECT * FROM " + configuration.table + ";";
        try(Connection connection = dataSource.getConnection())
        {
            try(PreparedStatement statement = connection.prepareStatement(selectRequest))
            {
                List<PlayerData> result = new ArrayList<>();

                try(ResultSet resultSet = statement.executeQuery(selectRequest))
                {
                    while (resultSet.next())
                    {
                        result.add(new PlayerData(resultSet));
                    }
                }
                return result;
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error while getting all data");
        }
    }


    public static class Configuration {
        private final String table;
        private final ISqlConfiguration sqlConfiguration;

        public Configuration(String table, ISqlConfiguration sqlConfiguration) {
            this.table = table;
            this.sqlConfiguration = sqlConfiguration;
        }
    }
}