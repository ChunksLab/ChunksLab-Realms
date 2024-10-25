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
