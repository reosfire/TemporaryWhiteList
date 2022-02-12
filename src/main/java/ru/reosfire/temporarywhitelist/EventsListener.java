package ru.reosfire.temporarywhitelist;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import ru.reosfire.temporarywhitelist.Configuration.Config;
import ru.reosfire.temporarywhitelist.Data.IDataProvider;
import ru.reosfire.temporarywhitelist.Lib.Text.Text;

public class EventsListener implements Listener
{
    private final Config Configuration;
    private final IDataProvider DataProvider;
    private final TemporaryWhiteList PluginInstance;
    public EventsListener(Config configuration, IDataProvider dataProvider, TemporaryWhiteList pluginInstance)
    {
        Configuration = configuration;
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
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Text.Colorize(player, Configuration.Messages.KickOnConnecting));
            }
        }
    }
}