package ru.reosfire.temporarywhitelist;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import ru.reosfire.temporarywhitelist.Configuration.Localization.MessagesConfig;
import ru.reosfire.temporarywhitelist.Data.IDataProvider;
import ru.reosfire.temporarywhitelist.Lib.Text.Text;

public class EventsListener implements Listener
{
    private final MessagesConfig _messages;
    private final IDataProvider _dataProvider;
    private final TemporaryWhiteList _pluginInstance;

    public EventsListener(MessagesConfig messages, IDataProvider dataProvider, TemporaryWhiteList pluginInstance)
    {
        _messages = messages;
        _dataProvider = dataProvider;
        _pluginInstance = pluginInstance;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(AsyncPlayerPreLoginEvent event)
    {
        if (!_pluginInstance.isWhiteListEnabled()) return;
        OfflinePlayer player = Bukkit.getOfflinePlayer(event.getUniqueId());

        if (_dataProvider.CanJoin(event.getName())) return;
        if (player.isOp()) return;

        String message = String.join("\n", Text.Colorize(player, _messages.Kick.Connecting));
        event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST, message);
    }
}