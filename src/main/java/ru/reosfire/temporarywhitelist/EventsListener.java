package ru.reosfire.temporarywhitelist;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import ru.reosfire.temporarywhitelist.Configuration.Localization.MessagesConfig;
import ru.reosfire.temporarywhitelist.Data.PlayerDatabase;
import ru.reosfire.temporarywhitelist.Lib.Text.Text;

public class EventsListener implements Listener
{
    private final MessagesConfig _messages;
    private final PlayerDatabase _database;
    private final TemporaryWhiteList _pluginInstance;

    public EventsListener(MessagesConfig messages, PlayerDatabase database, TemporaryWhiteList pluginInstance)
    {
        _messages = messages;
        _database = database;
        _pluginInstance = pluginInstance;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLogin(PlayerLoginEvent event)
    {
        if (!_pluginInstance.isWhiteListEnabled()) return;
        Player player = event.getPlayer();

        if (_database.CanJoin(player.getName())) return;
        if (player.isOp()) return;
        if (player.hasPermission("TemporaryWhitelist.Bypass")) return;

        String message = String.join("\n", Text.Colorize(player, _messages.Kick.Connecting));
        event.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, message);
    }
}