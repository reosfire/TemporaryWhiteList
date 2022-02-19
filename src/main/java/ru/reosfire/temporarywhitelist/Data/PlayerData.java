package ru.reosfire.temporarywhitelist.Data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;

public class PlayerData
{
    public final boolean _undefined;
    private long _lastStartTime;
    private long _timeAmount;
    private boolean _permanent;

    public PlayerData(long lastStartTime, long timeAmount, boolean permanent)
    {
        _lastStartTime = lastStartTime;
        _timeAmount = timeAmount;
        _permanent = permanent;
        _undefined = false;
    }

    public PlayerData(ResultSet resultSet) throws SQLException
    {
        _undefined = !resultSet.next();
        if (_undefined) return;
        _permanent = resultSet.getBoolean("Permanent");
        _lastStartTime = resultSet.getLong("LastStartTime");
        _timeAmount = resultSet.getLong("TimeAmount");
    }

    public long getLastStartTime()
    {
        return _lastStartTime;
    }

    public long get_timeAmount()
    {
        return _timeAmount;
    }

    public boolean is_permanent()
    {
        return _permanent;
    }

    public void set_permanent(boolean _permanent)
    {
        this._permanent = _permanent;
    }

    public long subscriptionEndTime()
    {
        return _lastStartTime + _timeAmount;
    }

    public boolean isTimeOut()
    {
        return subscriptionEndTime() <= Instant.now().getEpochSecond();
    }
}