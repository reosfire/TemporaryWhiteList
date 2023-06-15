package ru.reosfire.twl.spigot.kickLogFiltering;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.Plugin;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

public class SoftDetector implements Listener, KickMessageDetector {
    private final Set<String> blacklist = new ConcurrentSkipListSet<>();
    private final Plugin plugin;

    public SoftDetector(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void OnKick(PlayerLoginEvent event) {
        if (event.getResult() != PlayerLoginEvent.Result.KICK_WHITELIST) return;

        addToBlacklist(event.getPlayer().getUniqueId().toString());
    }

    private void addToBlacklist(String uuid) {
        blacklist.add(uuid);
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> blacklist.remove(uuid), 20);
    }

    @Override
    public boolean isKickMessage(String message) {
        if (message == null) return false;
        if (blacklist.isEmpty()) return false;

        return blacklist.contains(takeId(message));
    }

    private String takeId(String rawString) {
        String firstTry = searchAfter(rawString, " is ");
        if (firstTry.length() == 36) return firstTry;

        return searchAfter(rawString, "id=");
    }

    private String searchAfter(String in, String after) {
        int idIndex = in.indexOf(after);
        if (idIndex < 0) return in;

        idIndex += after.length();

        if (idIndex + 36 > in.length()) return in;
        return in.substring(idIndex, idIndex + 36);
    }
}