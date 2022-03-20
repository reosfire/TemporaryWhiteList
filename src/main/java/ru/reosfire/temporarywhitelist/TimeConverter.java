package ru.reosfire.temporarywhitelist;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import ru.reosfire.temporarywhitelist.Configuration.Config;

public class TimeConverter
{
    private final Config _config;

    public TimeConverter(Config config)
    {
        _config = config;
    }

    public String DurationToString(long seconds)
    {
        return DurationFormatUtils.formatDuration(seconds * 1000, _config.DurationFormat);
    }
    public String DateTimeToString(long seconds)
    {
        return DateFormatUtils.format(seconds * 1000, _config.DateTimeFormat);
    }

    public long ParseTime(String time)
    {
        long secondsInYear = 31536000;
        long secondsInDay = 86400;
        long secondsInHour = 3600;
        long secondsInMinute = 60;

        long result = 0;
        String[] timeElements = time.split(",");
        for (String timeElement : timeElements)
        {
            String[] split = timeElement.split(":");
            switch (split[1])
            {
                case "s":
                    result += Long.parseLong(split[0]);
                    break;
                case "m":
                    result += Long.parseLong(split[0]) * secondsInMinute;
                    break;
                case "h":
                    result += Long.parseLong(split[0]) * secondsInHour;
                    break;
                case "d":
                    result += Long.parseLong(split[0]) * secondsInDay;
                    break;
                case "y":
                    result += Long.parseLong(split[0]) * secondsInYear;
                    break;
                default: throw new RuntimeException("Invalid time unit");
            }
        }
        return result;
    }
}