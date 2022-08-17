package ru.reosfire.temporarywhitelist.Lib.Sql;

import ru.reosfire.temporarywhitelist.Lib.Sql.Selection.ISelectionAttribute;
import ru.reosfire.temporarywhitelist.Lib.Sql.Tables.TableColumn;

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

        if (connection != null && !connection.isClosed() && isConnectionAlive()) return;

        try
        {
            synchronized (this)
            {
                String connectionBuilder =
                        "jdbc:mysql://" + configuration.getIp() + ":" + configuration.getPort() + "/" + configuration.getDatabase()
                                + "?useSSL=" + (configuration.getUseSsl() ? "true" : "false")
                                + "&useUnicode=" + (configuration.getUseUnicode() ? "true" : "false")
                                + "&autoReconnect=" + (configuration.getAutoReconnect() ? "true" : "false")
                                + "&failOverReadOnly=" + (configuration.getFailOverReadOnly() ? "true" : "false")
                                + "&maxReconnects=" + configuration.getMaxReconnects();

                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection(connectionBuilder, configuration.getUser(),
                        configuration.getPassword());
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
            connection.createStatement().executeQuery("SELECT 1;");
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
        return connection;
    }

    public void createTable(String name, TableColumn... columns) throws SQLException
    {
        StringBuilder request = new StringBuilder("CREATE TABLE IF NOT EXISTS ").append(name).append(" (");
        for (int i = 0; i < columns.length; i++)
        {
            request.append(columns[i].toString());
            if (i + 1 < columns.length) request.append(", ");
        }
        request.append(");");
        try(Statement statement = getConnection().createStatement())
        {
            statement.execute(request.toString());
        }
    }

    public ResultSet select(String table, String[] columns, ISelectionAttribute... attributes) throws SQLException
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
            request.append(attribute.toSqlString());
        }
        try(Statement statement = getConnection().createStatement())
        {
            return statement.executeQuery(request.append(";").toString());
        }
    }
}