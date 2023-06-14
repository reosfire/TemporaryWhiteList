package ru.reosfire.twl.common.lib.yaml;

import java.util.Map;

public class ConfigSection {
    public final Map<String, Object> data;
    public final String path;

    public ConfigSection(Map<String, Object> data, String path) {
        this.data = data;
        this.path = path;
    }

    public ConfigSection(Map<String, Object> data) {
        this(data, null);
    }

    public <T> T get(String path, Class<T> tClass) {
        return get(data, path, tClass);
    }
    public <T> T getOrDefault(String path, Class<T> tClass, T def) {
        return getOrDefault(data, path, tClass, def);
    }

    public ConfigSection getSubsection(String path) {
        Map<String, Object> resultData = (Map<String, Object>) get(path, Object.class);
        return new ConfigSection(resultData, appendPath(path));
    }
    public ConfigSection getSubsection(String path, ConfigSection def) {
        try {
            return getSubsection(path);
        } catch (Exception e) {
            return def;
        }
    }

    public String getString(String path) {
        return get(path, String.class);
    }
    public int getInt(String path) {
        return get(path, Integer.class);
    }
    public long getLong(String path) {
        try {
            return get(path, Long.class);
        } catch (Exception e) {
            return get(path, Integer.class);
        }
    }


    private <T> T get(Map<String, Object> current, String path, Class<T> tClass) {
        if (path == null) throw new IllegalArgumentException("Path can not be null");
        if (path.isEmpty()) throw new IllegalArgumentException("Path can not be empty");

        PathToken token = new PathToken(path);
        if (token.carry == null) {
            Object result = current.get(token.first);
            if (result == null) throw new RuntimeException("This path does not exist: " + appendPath(path));
            if (!tClass.isInstance(result)) throw new RuntimeException("Unexpected type on: " + appendPath(path));
            return (T) result;
        }
        else {
            Map<String, Object> child = (Map<String, Object>) current.get(token.first);
            if (child == null) throw new RuntimeException("This path does not exist" + appendPath(path));
            return get(child, token.carry, tClass);
        }
    }

    private <T> T getOrDefault(Map<String, Object> current, String path, Class<T> tClass, T def) {
        try {
            return get(current, path, tClass);
        } catch (Exception e) {
            return def;
        }
    }
    private String appendPath(String path) {
        if (this.path == null) return path;
        return this.path + path;
    }


    private static class PathToken {
        public final String first;
        public final String carry;

        public PathToken(String path) {
            int dotIndex = path.indexOf('.');
            if (dotIndex == 0) throw new IllegalArgumentException("Path can not start from dot character");

            if (dotIndex > 0) {
                first = path.substring(0, dotIndex);
                carry = path.substring(dotIndex + 1);
            }
            else {
                first = path;
                carry = null;
            }
        }
    }
}