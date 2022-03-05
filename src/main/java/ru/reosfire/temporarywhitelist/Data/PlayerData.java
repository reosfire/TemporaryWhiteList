package ru.reosfire.temporarywhitelist.Data;

import org.bukkit.configuration.ConfigurationSection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;

public class PlayerData
{
    public final String Name;
    public final boolean Permanent;
    public final long StartTime;
    public final long TimeAmount;

    public PlayerData(String name, long startTime, long timeAmount, boolean permanent)
    {
        Name = name;
        StartTime = startTime;
        TimeAmount = timeAmount;
        Permanent = permanent;
    }

    public PlayerData(ResultSet resultSet) throws SQLException
    {
        Name = resultSet.getString("Player");
        Permanent = resultSet.getBoolean("Permanent");
        StartTime = resultSet.getLong("LastStartTime");
        TimeAmount = resultSet.getLong("TimeAmount");
    }

    public PlayerData(ConfigurationSection section)
    {
        Name = section.getName();
        Permanent = section.getBoolean("permanent");
        StartTime = section.getLong("lastStartTime");
        TimeAmount = section.getLong("timeAmount");
    }

    public long EndTime()
    {
        return StartTime + TimeAmount;
    }
    public boolean isTimedOut()
    {
        return EndTime() <= Instant.now().getEpochSecond();
    }
    public long TimeLeft()
    {
        return EndTime() - Instant.now().getEpochSecond();
    }
}