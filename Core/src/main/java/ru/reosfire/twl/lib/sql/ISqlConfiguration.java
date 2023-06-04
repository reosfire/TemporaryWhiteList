package ru.reosfire.twl.lib.sql;

public interface ISqlConfiguration
{
    String getUser();
    String getPassword();
    String getConnectionString();
    long getMaxConnectionLifetime();
    void CheckRequirements() throws SqlRequirementsNotSatisfiedException;
}