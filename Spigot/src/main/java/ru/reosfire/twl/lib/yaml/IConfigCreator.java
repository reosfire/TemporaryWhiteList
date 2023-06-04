package ru.reosfire.twl.lib.yaml;

import org.bukkit.configuration.ConfigurationSection;

public interface IConfigCreator<T extends YamlConfig>
{
    T Create(ConfigurationSection configurationSection);
}