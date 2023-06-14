package ru.reosfire.twl.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PreLoginEvent;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;
import ru.reosfire.twl.common.data.PlayerDatabase;
import ru.reosfire.twl.common.versioning.VersionChecker;

import java.time.Duration;

@Plugin(
        id = "temporary-white-list",
        name = "TemporaryWhiteList",
        version = "2.4" //TODO auto replace
)
public class TemporaryWhiteList {
    @Inject
    private Logger logger;
    @Inject
    private ProxyServer proxyServer;

    private PlayerDatabase database;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        logger.info("HelloWorld");
        proxyServer.getScheduler().buildTask(this,() -> {
            String currentVersion = proxyServer.getPluginManager().getPlugin("temporary-white-list").get().getDescription().getVersion().get();
            new VersionChecker(99914).printVersionCheckAsync(currentVersion, logger::info);
        }).delay(Duration.ofMillis(500)).schedule();
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