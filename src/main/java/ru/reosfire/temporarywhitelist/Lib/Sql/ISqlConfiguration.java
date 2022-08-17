package ru.reosfire.temporarywhitelist.Lib.Sql;

public interface ISqlConfiguration
{
    String getUser();
    String getPassword();
    String getConnectionString();
    void CheckRequirements() throws SqlRequirementsNotSatisfiedException;
}