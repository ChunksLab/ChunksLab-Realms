package com.chunkslab.realms.api.role;

import lombok.Getter;

import java.util.Set;

@Getter
public enum RealmPermission {

    PLACE("place"),
    BREAK("break"),
    ;

    private final String section;

    RealmPermission(String section) {
        this.section = section;
    }

    public static final Set<RealmPermission> PERMISSIONS = Set.of(values());
}