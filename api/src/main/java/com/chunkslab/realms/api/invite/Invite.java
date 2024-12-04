package com.chunkslab.realms.api.invite;

import com.chunkslab.realms.api.player.permissions.ranks.Rank;
import lombok.Getter;

import java.util.UUID;

@Getter
public class Invite {
    private final UUID realmId;
    private final Rank rank;

    public Invite(UUID realmId, Rank rank) {
        this.realmId = realmId;
        this.rank = rank;
    }

}
