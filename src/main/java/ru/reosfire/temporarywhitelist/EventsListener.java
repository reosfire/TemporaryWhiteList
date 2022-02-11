package ru.reosfire.temporarywhitelist;

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
    public EventsListener(Config configuration, IDataProvider dataProvider)
    {
        Configuration = configuration;
        DataProvider = dataProvider;
    }

    @EventHandler
    public void onJoin(PlayerLoginEvent event)
    {
        if(!Configuration.Enabled) return;
        if (!DataProvider.CanJoin(event.getPlayer().getDisplayName()))
        {
            if (!event.getPlayer().isOp())
            {
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, Text.Colorize(event.getPlayer(), Configuration.Messages.KickOnConnecting));
            }
        }
    }
}