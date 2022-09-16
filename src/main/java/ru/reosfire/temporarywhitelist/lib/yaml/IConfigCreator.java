package ru.reosfire.temporarywhitelist.lib.yaml;

import org.bukkit.configuration.ConfigurationSection;

public interface IConfigCreator<T extends YamlConfig>
{
    T Create(ConfigurationSection configurationSection);
}