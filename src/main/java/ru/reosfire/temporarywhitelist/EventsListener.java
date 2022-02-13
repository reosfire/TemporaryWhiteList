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

import java.util.List;

public class EventsListener implements Listener
{
    private final MessagesConfig Messages;
    private final IDataProvider DataProvider;
    private final TemporaryWhiteList PluginInstance;
    public EventsListener(MessagesConfig messages, IDataProvider dataProvider, TemporaryWhiteList pluginInstance)
    {
        Messages = messages;
        DataProvider = dataProvider;
        PluginInstance = pluginInstance;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(AsyncPlayerPreLoginEvent event)
    {
        if(!PluginInstance.isWhiteListEnabled()) return;
        OfflinePlayer player = Bukkit.getOfflinePlayer(event.getUniqueId());

        if (!DataProvider.CanJoin(player.getName()))
        {
            if (!player.isOp())
            {
                String message = String.join("\n", Text.Colorize(player, Messages.Kick.Connecting));
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST, message);
            }
        }
    }
}