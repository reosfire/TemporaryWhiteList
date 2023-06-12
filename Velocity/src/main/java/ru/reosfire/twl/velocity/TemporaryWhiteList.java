package ru.reosfire.twl.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PreLoginEvent;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import org.slf4j.Logger;

@Plugin(
        id = "temporary-white-list",
        name = "TemporaryWhiteList",
        version = "2.4" //TODO auto replace
)
public class TemporaryWhiteList {
    @Inject
    private Logger logger;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        logger.info("HelloWorld");
    }
    public void onConnect(ServerPreConnectEvent event) {
        event.setResult(ServerPreConnectEvent.ServerResult.denied());
        logger.info("HelloWorld");
    }

    public void onConnect(PreLoginEvent event) {
        //event.setResult(ResultedEvent.ComponentResult.denied());
        //event.setResult(ServerPreConnectEvent.ServerResult.denied());
        logger.info("HelloWorld");
    }
}