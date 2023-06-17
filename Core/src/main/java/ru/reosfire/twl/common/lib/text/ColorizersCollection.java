package ru.reosfire.twl.common.lib.text;

import java.util.ArrayList;
import java.util.List;

public class ColorizersCollection {
    public static final ColorizersCollection shared = new ColorizersCollection();


    private final Object lock = new Object();
    private final List<IColorizer> colorizers = new ArrayList<>();

    private ColorizersCollection() {

    }

    public ColorizersCollection addColorizer(IColorizer colorizer) {
        synchronized (lock) {
            colorizers.add(colorizer);
        }
        return this;
    }

    public String apply(String input) {
        String result = input;

        synchronized (lock) {
            for (IColorizer colorizer : colorizers) {
                result = colorizer.colorize(result);
            }
        }

        return result;
    }
}