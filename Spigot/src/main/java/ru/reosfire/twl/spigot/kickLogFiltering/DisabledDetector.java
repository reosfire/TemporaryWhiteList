package ru.reosfire.twl.spigot.kickLogFiltering;

public class DisabledDetector implements KickMessageDetector {
    @Override
    public boolean isKickMessage(String message) {
        return false;
    }
}