package ru.reosfire.temporarywhitelist.lib.sql;

public interface ISqlConfiguration
{
    String getUser();
    String getPassword();
    String getConnectionString();
    long getMaxConnectionLifetime();
    void CheckRequirements() throws SqlRequirementsNotSatisfiedException;
}