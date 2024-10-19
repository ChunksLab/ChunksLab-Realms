package com.chunkslab.realms.api.world;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum WorldManagerType {

    DEFAULT,
    SWM,
    ;

    private static final Set<WorldManagerType> VALUES = Arrays.stream(WorldManagerType.values()).collect(Collectors.toSet());

    public static WorldManagerType get(String str) {
        for (WorldManagerType value : VALUES) {
            if (str.equalsIgnoreCase(value.name()))
                return value;
        }

        return null;
    }

}