package ru.reosfire.temporarywhitelist;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
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

    @EventHandler
    public void onJoin(PlayerLoginEvent event)
    {
        if(!PluginInstance.isWhiteListEnabled()) return;
        Player player = event.getPlayer();
        if (!DataProvider.CanJoin(player.getDisplayName()))
        {
            if (!player.isOp())
            {
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, Text.Colorize(player, Configuration.Messages.KickOnConnecting));
            }
        }
    }
}