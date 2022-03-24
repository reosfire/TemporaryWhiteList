package ru.reosfire.temporarywhitelist.Lib.Sql;

import ru.reosfire.temporarywhitelist.Lib.Sql.Selection.ISelectionAttribute;
import ru.reosfire.temporarywhitelist.Lib.Sql.Tables.TableColumn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlConnection
{
    private final ISqlConfiguration _configuration;
    private Connection _connection = null;

    public SqlConnection(ISqlConfiguration config) throws SQLException
    {
        _configuration = config;
        Open();
    }

    private void Open() throws SQLException
    {

        if (_connection != null && !_connection.isClosed() && isConnectionAlive()) return;

        try
        {
            synchronized (this)
            {
                String connectionBuilder =
                        "jdbc:mysql://" + _configuration.getIp() + ":" + _configuration.getPort() + "/" + _configuration.getDatabase()
                                + "?useSSL=" + (_configuration.getUseSsl() ? "true" : "false")
                                + "&useUnicode=" + (_configuration.getUseUnicode() ? "true" : "false")
                                + "&autoReconnect=" + (_configuration.getAutoReconnect() ? "true" : "false")
                                + "&failOverReadOnly=" + (_configuration.getFailOverReadOnly() ? "true" : "false")
                                + "&maxReconnects=" + _configuration.getMaxReconnects();

                Class.forName("com.mysql.jdbc.Driver");
                _connection = DriverManager.getConnection(connectionBuilder, _configuration.getUser(),
                        _configuration.getPassword());
            }
        }
        catch (ClassNotFoundException e)
        {
            throw new SQLException("Can't find com.mysql.jdbc.Driver class");
        }
    }

    private boolean isConnectionAlive()
    {
        boolean alive;
        try
        {
            _connection.createStatement().executeQuery("SELECT 1;");
            alive = true;
        }
        catch (SQLException e)
        {
            alive = false;
        }
        return alive;
    }

    public Connection getConnection() throws SQLException
    {
        Open();
        return _connection;
    }

    public void CreateTable(String name, TableColumn... columns) throws SQLException
    {
        StringBuilder request = new StringBuilder("CREATE TABLE IF NOT EXISTS ").append(name).append(" (");
        for (int i = 0; i < columns.length; i++)
        {
            request.append(columns[i].toString());
            if (i + 1 < columns.length) request.append(", ");
        }
        request.append(");");
        getConnection().createStatement().executeUpdate(request.toString());
    }

    public ResultSet Select(String table, String[] columns, ISelectionAttribute... attributes) throws SQLException
    {
        StringBuilder request = new StringBuilder("SELECT ");
        for (int i = 0; i < columns.length; i++)
        {
            request.append(columns[i]);
            if (i + 1 < columns.length) request.append(",");
        }
        request.append(" FROM ").append(table);
        for (ISelectionAttribute attribute : attributes)
        {
            request.append(" ");
            request.append(attribute.ToString());
        }
        return getConnection().createStatement().executeQuery(request.append(";").toString());
    }
}