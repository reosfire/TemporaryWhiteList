package ru.reosfire.twl.common.lib.yaml;

import java.util.Map;

public interface IConfigCreator<T extends YamlConfig>
{
    T Create(ConfigSection configurationSection);

    default T Create(Map<String, Object> data) {
        return Create(new ConfigSection(data));
    }
}