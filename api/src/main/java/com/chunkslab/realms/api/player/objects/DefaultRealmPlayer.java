package com.chunkslab.realms.api.player.objects;

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
    private transient @Nullable Realm realm;

    public DefaultRealmPlayer(RealmPlayerContext context) {
        this.context = context;
        this.data = new DefaultRealmPlayerData(context.getUniqueId());
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
