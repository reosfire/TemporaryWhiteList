package ru.reosfire.twl.common;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.apache.commons.lang.time.FastDateFormat;

public class TimeConverter
{
    private final String durationFormat;
    private final FastDateFormat dateTimeFormat;

    public TimeConverter(String durationFormat, String dateTimeFormat)
    {
        this.durationFormat = durationFormat;
        this.dateTimeFormat = FastDateFormat.getInstance(dateTimeFormat);
    }

    public String durationToString(long seconds)
    {
        return DurationFormatUtils.formatDuration(seconds * 1000, durationFormat);
    }
    public String dateTimeToString(long seconds)
    {
        return dateTimeFormat.format(seconds * 1000);
    }

    public long parseTime(String time)
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