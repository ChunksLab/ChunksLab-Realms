package com.chunkslab.realms.api.player.permissions.ranks;

import com.chunkslab.realms.api.player.permissions.Permission;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record Rank(String id, int power, @Nullable Component display, @Nullable Assignment assignment, @Nullable List<Permission> permissions) {
    public boolean hasPermission(Permission permission) {
        return permissions == null || permissions.contains(permission);
    }

    @Override
    public Component display() {
        return display == null ? Component.empty() : display;
    }

    public enum Assignment {
        RESIDENT, TRUSTED, OWNER
    }
}
