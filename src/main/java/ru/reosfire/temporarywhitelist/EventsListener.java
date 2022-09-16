package ru.reosfire.temporarywhitelist;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import ru.reosfire.temporarywhitelist.configuration.localization.MessagesConfig;
import ru.reosfire.temporarywhitelist.Data.PlayerDatabase;
import ru.reosfire.temporarywhitelist.lib.text.Text;

public class EventsListener implements Listener
{
    private final MessagesConfig messages;
    private final PlayerDatabase database;
    private final TemporaryWhiteList pluginInstance;

    public EventsListener(MessagesConfig messages, PlayerDatabase database, TemporaryWhiteList pluginInstance)
    {
        this.messages = messages;
        this.database = database;
        this.pluginInstance = pluginInstance;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLogin(PlayerLoginEvent event)
    {
        if (!pluginInstance.isWhiteListEnabled()) return;
        Player player = event.getPlayer();

        if (database.canJoin(player.getName())) return;
        if (player.isOp()) return;
        if (player.hasPermission("TemporaryWhitelist.Bypass")) return;

        String message = String.join("\n", Text.colorize(player, messages.Kick.Connecting));
        event.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, message);
    }
}