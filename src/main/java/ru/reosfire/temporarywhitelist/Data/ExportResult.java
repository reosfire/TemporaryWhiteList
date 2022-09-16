package ru.reosfire.temporarywhitelist.Data;

import ru.reosfire.temporarywhitelist.lib.text.Replacement;

import java.time.Instant;
import java.util.*;

public class ExportResult
{
    public final List<PlayerData> Found;
    public final Set<PlayerData> WithoutError = new HashSet<>();
    public final long StartTime;

    public ExportResult(List<PlayerData> found)
    {
        Found = found;
        StartTime = Instant.now().toEpochMilli();
    }

    public int withErrorCount()
    {
        return Found.size() - WithoutError.size();
    }
    public List<PlayerData> withError()
    {
        if (WithoutError.size() == 0) return Found;

        List<PlayerData> result = new ArrayList<>();

        for (PlayerData playerData : Found)
        {
            if (WithoutError.contains(playerData)) continue;
            result.add(playerData);
        }

        return result;
    }

    public Replacement[] getReplacements()
    {
        return new Replacement[]
            {
                    new Replacement("{found_count}", Integer.toString(Found.size())),
                    new Replacement("{with_error_count}", Integer.toString(withErrorCount())),
                    new Replacement("{without_error_count}", Integer.toString(WithoutError.size())),
                    new Replacement("{found}", playersDataToString(Found)),
                    new Replacement("{with_error}", playersDataToString(withError())),
                    new Replacement("{without_error}", playersDataToString(WithoutError)),
                    new Replacement("{time_elapsed}", timeElapsedToString(getElapsed())),
            };
    }

    public long getElapsed()
    {
        return Instant.now().toEpochMilli() - StartTime;
    }

    private static String playersDataToString(Collection<PlayerData> players)
    {
        StringBuilder result = new StringBuilder();
        for (PlayerData player : players)
        {
            result.append(player.Name).append(" ");
        }
        if (result.length() > 0) result.deleteCharAt(result.length() - 1);

        return result.toString();
    }

    private static String timeElapsedToString(long ms)
    {
        long seconds = ms / 1000;
        long milliSeconds = ms % 1000;
        return seconds + "s " + milliSeconds + "ms";
    }
}