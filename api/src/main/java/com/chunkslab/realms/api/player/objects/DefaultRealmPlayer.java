package com.chunkslab.realms.api.player.objects;

import com.chunkslab.realms.api.RealmsAPI;
import com.chunkslab.realms.api.player.contexts.RealmPlayerContext;
import com.chunkslab.realms.api.player.data.DefaultRealmPlayerData;
import com.chunkslab.realms.api.player.data.RealmPlayerData;
import com.chunkslab.realms.api.realm.Realm;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

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
}
