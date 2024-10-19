package com.chunkslab.realms.api.upgrade;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record Upgrade(String name, Type type, int level, int value, double price) {

    public enum Type {

        MEMBERS, SIZE;

        public static final Set<Type> VALUES = new HashSet<>(List.of(values()));

    }

}