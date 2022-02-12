package ru.reosfire.temporarywhitelist;

public class TimeConverter
{
    public static String ReadableTime(long seconds)
    {
        StringBuilder timeBuilder = new StringBuilder();
        long daysInYear = 365;
        long hoursInDay = 24;
        long minutesInHour = 60;
        long secondsInMinute = 60;

        long resultSeconds = seconds % secondsInMinute;
        seconds = (seconds - resultSeconds) / secondsInMinute;

        long resultMinutes = seconds % minutesInHour;
        seconds = (seconds - resultMinutes) / minutesInHour;

        long resultHours = seconds % hoursInDay;
        seconds = (seconds - resultHours) / hoursInDay;

        long resultDays = seconds % daysInYear;
        seconds = (seconds - resultDays) / daysInYear;

        long resultYears = seconds;

        if (resultYears != 0) timeBuilder.append(resultYears).append("г,");
        if (resultDays != 0) timeBuilder.append(resultDays).append("дн,");
        if (resultHours != 0) timeBuilder.append(resultHours).append("ч,");
        if (resultMinutes != 0) timeBuilder.append(resultMinutes).append("мин,");
        timeBuilder.append(resultSeconds).append("сек.");
        return timeBuilder.toString();
    }

    public static long ParseTime(String time)
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
            }
        }
        return result;
    }
}