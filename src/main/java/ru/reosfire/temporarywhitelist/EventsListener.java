package ru.reosfire.temporarywhitelist;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import ru.reosfire.temporarywhitelist.Lib.Text.Text;

public class EventsListener implements Listener
{
    @EventHandler
    public void onJoin(PlayerLoginEvent event)
    {
        if(!TemporaryWhiteList.getConfiguration().Enabled) return;
        if (!TemporaryWhiteList.getDataProvider().CanJoin(event.getPlayer().getDisplayName()))
        {
            if (!event.getPlayer().isOp())
            {
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, Text.Colorize(event.getPlayer(), TemporaryWhiteList.getMessages().KickOnConnecting));
            }
        }
    }
}