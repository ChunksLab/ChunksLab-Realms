package com.chunkslab.realms.api.database;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum DatabaseType {

    YAML,
    ;

    private static final Set<DatabaseType> VALUES = Arrays.stream(DatabaseType.values()).collect(Collectors.toSet());

    public static DatabaseType get(String str) {
        for (DatabaseType value : VALUES) {
            if (str.equalsIgnoreCase(value.name()))
                return value;
        }

        return null;
    }

}