package ru.reosfire.temporarywhitelist.Lib.Sql;

public interface ISqlConfiguration
{
    String getIp();

    String getUser();

    String getPassword();

    String getDatabase();

    boolean getUseSsl();

    boolean getUseUnicode();

    boolean getAutoReconnect();

    boolean getFailOverReadOnly();

    int getMaxReconnects();

    int getPort();
}