package ru.reosfire.temporarywhitelist;

import com.google.common.collect.ImmutableList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import ru.reosfire.temporarywhitelist.configuration.Config;
import ru.reosfire.temporarywhitelist.configuration.localization.MessagesConfig;
import ru.reosfire.temporarywhitelist.data.PlayerDatabase;
import ru.reosfire.temporarywhitelist.lib.text.Text;

public class OnlinePlayersKicker {
    private final TemporaryWhiteList pluginInstance;
    private final Config configuration;
    private final PlayerDatabase database;
    private final MessagesConfig messages;
    private BukkitTask kickerTask;

    public OnlinePlayersKicker(TemporaryWhiteList pluginInstance) {
        this.pluginInstance = pluginInstance;
        configuration = pluginInstance.getConfiguration();
        database = pluginInstance.getDatabase();
        messages = pluginInstance.getMessages();
    }

    public void start() {
        runKickerTask();
    }

    public void stop() {
        if(kickerTask != null) kickerTask.cancel();
    }

    private void runKickerTask()
    {
        //TODO async db calls. Smooth out load by check queue. + may be move this logic to other class
        kickerTask = Bukkit.getScheduler().runTaskTimer(pluginInstance, () ->
        {
            for (Player player : ImmutableList.copyOf(pluginInstance.getServer().getOnlinePlayers()))
            {
                if (database.canJoin(player.getName())) continue;
                if (player.isOp()) continue;
                if (player.hasPermission("TemporaryWhitelist.Bypass")) continue;

                player.kickPlayer(String.join("\n", Text.colorize(player, messages.Kick.WhilePlaying)));
            }
        }, 0, configuration.SubscriptionEndCheckTicks);
    }
}