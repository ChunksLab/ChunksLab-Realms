package com.chunkslab.realms.api.player.permissions;

import com.chunkslab.realms.api.player.objects.RealmPlayer;
import com.google.common.base.Preconditions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@NoArgsConstructor
@AllArgsConstructor
public enum Permission {

    REALM_COMMAND,
    REALM_COMMAND_CREATE,
    REALM_COMMAND_SETTINGS,
    REALM_COMMAND_MEMBER,
    REALM_COMMAND_MEMBERS,
    REALM_COMMAND_INVITE,
    REALM_COMMAND_ACCEPT,
    REALM_COMMAND_DENY,
    REALM_COMMAND_BAN,
    REALM_COMMAND_UNBAN,
    REALM_COMMAND_REMOVE,
    REALM_COMMAND_TELEPORT,
    REALM_COMMAND_SETSPAWN,
    REALM_COMMAND_BORDER,

    REALM_MENU,
    REALM_MENU_SETTINGS,
    REALM_MENU_MEMBERS,
    REALM_MENU_INVITE,
    REALM_MENU_RANK,
    REALM_MENU_BAN,

    PROTECTION_PLACE,
    PROTECTION_BREAK,
    PROTECTION_USE,
    PROTECTION_SWITCH,
    PROTECTION_FLY,

    ADMIN_COMMAND_RELOAD("towny.admin.reload"),
    ADMIN_COMMAND_MODULE("towny.admin.module"),
    ;

    @Getter
    private @Nullable String bukkitPermission;

    public static @Nullable String getPermissionNodeValue(@NotNull String node, @NotNull RealmPlayer player) {
        Preconditions.checkNotNull(player.getBukkitPlayer(), "Player must be online.");
        for (PermissionAttachmentInfo info : player.getBukkitPlayer().getEffectivePermissions())
            if (info.getPermission().startsWith(node)) {
                String[] args = info.getPermission().split("\\.");
                return args[args.length - 1];
            }
        return null;
    }
}
