package ru.reosfire.twl.spigot.data.providers;

import org.bukkit.configuration.ConfigurationSection;
import ru.reosfire.twl.common.data.PlayerData;

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