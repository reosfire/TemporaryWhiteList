package ru.reosfire.temporarywhitelist.Lib.Sql;

import ru.reosfire.temporarywhitelist.Lib.Sql.Selection.ISelectionAttribute;
import ru.reosfire.temporarywhitelist.Lib.Sql.Tables.TableColumn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlConnection
{
    private ISqlConfiguration _configuration = null;
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
                StringBuilder connectionBuilder = new StringBuilder("jdbc:mysql://");
                connectionBuilder.append(_configuration.getIp()).append(":").append(_configuration.getPort());
                connectionBuilder.append("/").append(_configuration.getDatabase());
                connectionBuilder.append("?useSSL=").append(_configuration.getUseSsl() ? "true" : "false");
                connectionBuilder.append("&useUnicode=").append(_configuration.getUseUnicode() ? "true" : "false");
                connectionBuilder.append("&autoReconnect=").append(_configuration.getAutoReconnect() ? "true" : "false");
                connectionBuilder.append("&failOverReadOnly=").append(_configuration.getFailOverReadOnly() ? "true" :
                        "false");
                connectionBuilder.append("&maxReconnects=").append(_configuration.getMaxReconnects());

                Class.forName("com.mysql.jdbc.Driver");
                _connection = DriverManager.getConnection(connectionBuilder.toString(), _configuration.getUser(),
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

    public void Close() throws SQLException
    {
        _connection.close();
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