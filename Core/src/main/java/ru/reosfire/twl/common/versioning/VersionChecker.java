package ru.reosfire.twl.common.versioning;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class VersionChecker
{
    private static final String SPIGOT_URL = "https://www.spigotmc.org/resources/temporarywhitelist.99914";

    private final URL url;

    public VersionChecker(int resourceId)
    {
        try
        {
            url = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceId);
        }
        catch (MalformedURLException e) {
            throw new RuntimeException("Invalid url in version checker", e);
        }
    }

    public String getVersionOnSpigot()
    {
        try (InputStream inputStream = url.openStream(); Scanner scanner = new Scanner(inputStream))
        {
            if (scanner.hasNext())
                return scanner.next();
            else throw new VersionCheckException("Spigot returned nothing");
        }
        catch (IOException exception)
        {
            throw new VersionCheckException();
        }
    }

    public CompletableFuture<String> getVersionOnSpigotAsync()
    {
        return CompletableFuture.supplyAsync(this::getVersionOnSpigot);
    }

    public void printVersionCheckAsync(String currentVersion, Consumer<String> printer) {
        getVersionOnSpigotAsync().whenComplete((version, exception) -> {
            if (exception != null) printer.accept("Error while checking latest plugin version.");
            else
            {
                if (version.equalsIgnoreCase(currentVersion))
                    printer.accept("Plugin is up to date. Please rate it: " + SPIGOT_URL);
                else
                    printer.accept("There is a new version (" + version + ") available: " + SPIGOT_URL);
            }
        });
    }
}