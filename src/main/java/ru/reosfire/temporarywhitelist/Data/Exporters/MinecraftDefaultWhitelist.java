package ru.reosfire.temporarywhitelist.Data.Exporters;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import ru.reosfire.temporarywhitelist.Data.PlayerData;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class MinecraftDefaultWhitelist implements IDataExporter
{
    @Override
    public List<PlayerData> GetAll(long defaultTimeAmount, boolean defaultPermanent)
    {
        long currentTime = Instant.now().getEpochSecond();
        ArrayList<PlayerData> result = new ArrayList<>();
        for (OfflinePlayer player : Bukkit.getWhitelistedPlayers())
        {
            result.add(new PlayerData(player.getName(), currentTime, defaultTimeAmount, defaultPermanent));
        }

        return result;
    }
}