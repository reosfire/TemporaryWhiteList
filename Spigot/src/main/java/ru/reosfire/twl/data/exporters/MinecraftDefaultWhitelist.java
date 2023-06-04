package ru.reosfire.twl.data.exporters;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import ru.reosfire.twl.data.PlayerData;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class MinecraftDefaultWhitelist implements IDataExporter
{
    private final long defaultTimeAmount;
    private final boolean defaultPermanent;
    public MinecraftDefaultWhitelist(long defaultTimeAmount, boolean defaultPermanent)
    {
        this.defaultTimeAmount = defaultTimeAmount;
        this.defaultPermanent = defaultPermanent;
    }

    @Override
    public List<PlayerData> getAll()
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