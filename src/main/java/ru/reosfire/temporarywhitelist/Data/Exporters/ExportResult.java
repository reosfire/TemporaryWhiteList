package ru.reosfire.temporarywhitelist.Data.Exporters;

import ru.reosfire.temporarywhitelist.Data.PlayerData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExportResult
{
    public final List<PlayerData> Found;
    public final Set<PlayerData> WithoutError = new HashSet<>();

    public ExportResult(List<PlayerData> found)
    {
        Found = found;
    }

    public int WithErrorCount()
    {
        return Found.size() - WithoutError.size();
    }
    public List<PlayerData> WithError()
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
}