package ru.reosfire.temporarywhitelist.Lib.Sql;

import ru.reosfire.temporarywhitelist.Lib.Sql.Selection.ISelectionAttribute;
import ru.reosfire.temporarywhitelist.Lib.Sql.Tables.TableColumn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlConnection
{
    private ISqlConfiguration configuration = null;
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
                StringBuilder connectionBuilder = new StringBuilder("jdbc:mysql://");
                connectionBuilder.append(configuration.getIp()).append(":").append(configuration.getPort());
                connectionBuilder.append("/").append(configuration.getDatabase());
                connectionBuilder.append("?useSSL=").append(configuration.getUseSsl() ? "true" : "false");
                connectionBuilder.append("&useUnicode=").append(configuration.getUseUnicode() ? "true" : "false");
                connectionBuilder.append("&autoReconnect=").append(configuration.getAutoReconnect() ? "true" : "false");
                connectionBuilder.append("&failOverReadOnly=").append(configuration.getFailOverReadOnly() ? "true" :
                        "false");
                connectionBuilder.append("&maxReconnects=").append(configuration.getMaxReconnects());

                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection(connectionBuilder.toString(), configuration.getUser(),
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

    public void Close() throws SQLException
    {
        connection.close();
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

    public void AddTableColumn(String table, TableColumn column) throws SQLException
    {
        StringBuilder request = new StringBuilder("ALTER TABLE ").append(table).append(" ADD COLUMN ");
        request.append(column.toString());
        getConnection().createStatement().executeUpdate(request.append(";").toString());
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

    public void Insert(String table, String[] columns, String[] values) throws SQLException
    {
        StringBuilder request = new StringBuilder("INSERT INTO ").append(table).append(" (");
        for (int i = 0; i < columns.length; i++)
        {
            request.append(columns[i]);
            if (i + 1 < columns.length) request.append(", ");
        }
        request.append(") VALUES (");
        for (int i = 0; i < values.length; i++)
        {
            request.append("'").append(values[i]).append("'");
            if (i + 1 < values.length) request.append(", ");
        }
        request.append(");");
        getConnection().createStatement().executeUpdate(request.toString());
    }

    public void InsertOrUpdate()
    {

    }

    public void Update()
    {

    }

    public void Delete()
    {

    }
}