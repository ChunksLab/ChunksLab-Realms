package com.chunkslab.realms.api.player.objects;

import com.chunkslab.realms.api.RealmsAPI;
import com.chunkslab.realms.api.location.ServerLocation;
import com.chunkslab.realms.api.player.contexts.RealmPlayerContext;
import com.chunkslab.realms.api.player.data.DefaultRealmPlayerData;
import com.chunkslab.realms.api.player.data.RealmPlayerData;
import com.chunkslab.realms.api.player.permissions.Permission;
import com.chunkslab.realms.api.player.permissions.ranks.Rank;
import com.chunkslab.realms.api.realm.Realm;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class DefaultRealmPlayer implements RealmPlayer {
    @Getter
    private final RealmPlayerContext context;
    @Getter
    private final RealmPlayerData data;
    @Getter @Setter
    private @Nullable UUID realmId;

    public DefaultRealmPlayer(RealmPlayerContext context) {
        this.context = context;
        this.data = new DefaultRealmPlayerData(context.getUniqueId());
    }

    @Nullable
    public Realm getRealm() {
        if (this.realmId == null) return null;

        return RealmsAPI.getInstance().getRealmManager().getRealm(realmId);
    }

    @Override
    public String getName() {
        return context.getName();
    }

    @Override
    public UUID getUniqueId() {
        return context.getUniqueId();
    }

    @Override
    public boolean isLocalPlayer() {
        return Bukkit.getPlayer(getName()) != null;
    }

    @Override
    public void teleport(ServerLocation location) {

    }

    @Override
    public List<Permission> getPermissions() {
        List<Permission> list = new ArrayList<>(RealmsAPI.getInstance().getRankManager().getCommon());
        // Load ranks
        Set<Rank> ranks = new HashSet<>();
        if (realmId != null) {
            ranks.add(getRealm().getMembersController().getRank(this));
        }
        ranks.forEach(rank -> {
            if (rank != null && rank.permissions() != null)
                list.addAll(rank.permissions());
        });
        return list;
    }

    @Override
    public boolean hasPermission(Permission permission) {
        return getPermissions().contains(permission);
    }

    @Override
    public boolean isOnline() {
        return RealmsAPI.getInstance().getPlayerManager().getPlayer(getUniqueId()) != null;
    }
}
