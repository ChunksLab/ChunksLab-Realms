package com.chunkslab.realms.api.player.contexts;

import lombok.Data;

import java.util.UUID;

@Data
public class DefaultRealmPlayerContext implements RealmPlayerContext {
    private final String name;
    private final UUID uniqueId;

    @Override
    public boolean equals(RealmPlayerContext context) {
        return name.equals(context.getName()) && uniqueId.equals(context.getUniqueId());
    }
}
