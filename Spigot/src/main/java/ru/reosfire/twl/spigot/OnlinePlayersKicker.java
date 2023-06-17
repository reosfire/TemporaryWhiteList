package ru.reosfire.twl.spigot;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import ru.reosfire.twl.common.configuration.Config;
import ru.reosfire.twl.common.configuration.localization.MessagesConfig;
import ru.reosfire.twl.common.data.PlayerDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

public class OnlinePlayersKicker {
    private final TemporaryWhiteList pluginInstance;
    private final Config configuration;
    private final PlayerDatabase database;
    private final MessagesConfig messages;

    private BukkitTask checkerTask;
    private BukkitTask kickerTask;
    private final ConcurrentLinkedQueue<UUID> toKick = new ConcurrentLinkedQueue<>();

    public OnlinePlayersKicker(TemporaryWhiteList pluginInstance) {
        this.pluginInstance = pluginInstance;
        configuration = pluginInstance.getConfiguration();
        database = pluginInstance.getDatabase();
        messages = pluginInstance.getMessages();
    }

    public void start() {
        toKick.clear();
        runCheckerTask();
        runKickerTask();
    }

    public void stop() {
        if(checkerTask != null) checkerTask.cancel();
        if(kickerTask != null) kickerTask.cancel();
        toKick.clear();
    }

    private void runCheckerTask() {
        //TODO Smooth out load by check rolling queue.
        checkerTask = Bukkit.getScheduler().runTaskTimer(pluginInstance, () ->
        {
            List<PlayerInfo> potentialKickPlayersNames = new ArrayList<>();
            for (Player player: Bukkit.getOnlinePlayers()) {
                if (player.isOp()) continue;
                if (player.hasPermission("TemporaryWhitelist.Bypass")) continue;

                potentialKickPlayersNames.add(new PlayerInfo(player));
            }

            Bukkit.getScheduler().runTaskAsynchronously(pluginInstance, () -> {
                for (PlayerInfo player: potentialKickPlayersNames) {
                    if (database.canJoin(player.name)) continue;

                    toKick.add(player.uuid);
                }
            });
        },0, configuration.SubscriptionEndCheckTicks);
    }

    private void runKickerTask()
    {
        kickerTask = Bukkit.getScheduler().runTaskTimer(pluginInstance, () ->
        {
            while (!toKick.isEmpty())
            {
                Player player = Bukkit.getPlayer(toKick.poll());
                if (!player.isOnline()) continue;

                player.kickPlayer(String.join("\n", Text.colorize(player, messages.Kick.WhilePlaying)));
            }
        }, configuration.SubscriptionEndCheckTicks / 2, configuration.SubscriptionEndCheckTicks);
    }

    private static class PlayerInfo {
        private final String name;
        private final UUID uuid;

        private PlayerInfo(Player player) {
            this.name = player.getName();
            this.uuid = player.getUniqueId();
        }
    }
}