package ru.reosfire.twl;

import com.google.common.collect.ImmutableList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import ru.reosfire.twl.configuration.Config;
import ru.reosfire.twl.configuration.localization.MessagesConfig;
import ru.reosfire.twl.data.PlayerDatabase;
import ru.reosfire.twl.lib.text.Text;

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
        checkerTask = Bukkit.getScheduler().runTaskTimerAsynchronously(pluginInstance, () ->
        {
            for (Player player : ImmutableList.copyOf(pluginInstance.getServer().getOnlinePlayers()))
            {
                if (database.canJoin(player.getName())) continue;
                if (player.isOp()) continue;
                if (player.hasPermission("TemporaryWhitelist.Bypass")) continue;

                toKick.add(player.getUniqueId());
            }
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
}