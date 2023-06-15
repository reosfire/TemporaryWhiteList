package ru.reosfire.twl.spigot.kickLogFiltering;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.message.Message;

public class KickLogFilter extends AbstractFilter {
    private final KickMessageDetector kickMessageDetector;

    public KickLogFilter(KickMessageDetector detector) {
        kickMessageDetector = detector;
    }

    @Override
    public Result filter(LogEvent event) {
        if (event == null) return Result.NEUTRAL;
        if (event.getMessage() == null) return Result.NEUTRAL;

        return filter(event.getMessage().getFormattedMessage());
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
        if (msg == null) return Result.NEUTRAL;

        return filter(msg.toString());
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
        return filter(msg);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
        if (msg == null) return Result.NEUTRAL;

        return filter(msg.getFormattedMessage());
    }

    private Result filter(String message) {
        return kickMessageDetector.isKickMessage(message) ? Result.DENY : Result.NEUTRAL;
    }
}