package ru.reosfire.twl.spigot.kickLogFiltering;

public class HardDetector implements KickMessageDetector {
    @Override
    public boolean isKickMessage(String message) {
        if (message.startsWith("UUID of player ") && message.contains(" is ")) return true;
        if (message.startsWith("com.mojang.authlib.GameProfile@")) return true;
        return message.startsWith("Disconnecting com.mojang.authlib.GameProfile@");
    }
}