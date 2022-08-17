package ru.reosfire.temporarywhitelist.Lib.Sql;

import java.sql.*;

public class SqlConnection
{
    private final ISqlConfiguration configuration;
    private Connection connection = null;

    public SqlConnection(ISqlConfiguration config) throws SQLException
    {
        configuration = config;
        Open();
    }

    private void Open() throws SQLException
    {
        synchronized (this)
        {
            if (connection != null && !connection.isClosed() && isConnectionAlive()) return;

            try
            {
                configuration.CheckRequirements();
            }
            catch (SqlRequirementsNotSatisfiedException e)
            {
                throw new SQLException(e);
            }
            connection = DriverManager.getConnection(configuration.getConnectionString(), configuration.getUser(),
                    configuration.getPassword());
        }
    }

    private boolean isConnectionAlive()
    {
        try
        {
            connection.createStatement().executeQuery("SELECT 1;");
            return true;
        }
        catch (SQLException e)
        {
            return false;
        }
    }

    public Connection getConnection() throws SQLException
    {
        Open();
        return connection;
    }
}