package ru.reosfire.temporarywhitelist.Data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;

public class PlayerData
{
    public final boolean undefined;
    private long LastStartTime;
    private long TimeAmount;
    private boolean Permanent;

    public PlayerData(long lastStartTime, long timeAmount, boolean permanent)
    {
        LastStartTime = lastStartTime;
        TimeAmount = timeAmount;
        Permanent = permanent;
        undefined = false;
    }

    public PlayerData(ResultSet resultSet) throws SQLException
    {
        undefined = !resultSet.next();
        if (undefined) return;
        Permanent = resultSet.getBoolean("Permanent");
        LastStartTime = resultSet.getLong("LastStartTime");
        TimeAmount = resultSet.getLong("TimeAmount");
    }

    public long getLastStartTime()
    {
        return LastStartTime;
    }

    public long getTimeAmount()
    {
        return TimeAmount;
    }

    public boolean isPermanent()
    {
        return Permanent;
    }

    public void setPermanent(boolean permanent)
    {
        Permanent = permanent;
    }

    public long subscriptionEndTime()
    {
        return LastStartTime + TimeAmount;
    }

    public boolean isTimeOut()
    {
        return subscriptionEndTime() <= Instant.now().getEpochSecond();
    }
}