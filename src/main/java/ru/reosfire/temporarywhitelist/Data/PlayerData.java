package ru.reosfire.temporarywhitelist.Data;

import org.bukkit.configuration.ConfigurationSection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof PlayerData)) return false;
        PlayerData that = (PlayerData) o;
        return Permanent == that.Permanent && StartTime == that.StartTime && TimeAmount == that.TimeAmount && Name.equals(that.Name);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(Name, Permanent, StartTime, TimeAmount);
    }
}