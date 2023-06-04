package ru.reosfire.twl.data.providers;

import org.bukkit.configuration.ConfigurationSection;
import ru.reosfire.twl.data.PlayerData;

public class YamlPlayerData extends PlayerData {
    public YamlPlayerData(ConfigurationSection section)
    {
        super(section.getName(),
                section.getLong("lastStartTime"),
                section.getLong("timeAmount"),
                section.getBoolean("permanent")
        );
    }
}