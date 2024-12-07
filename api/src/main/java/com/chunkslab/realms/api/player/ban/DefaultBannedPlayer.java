package com.chunkslab.realms.api.player.ban;

import com.chunkslab.realms.api.player.contexts.RealmPlayerContext;
import com.chunkslab.realms.api.player.objects.DefaultRealmPlayer;
import com.chunkslab.realms.api.player.permissions.ranks.Rank;
import lombok.Getter;
import lombok.Setter;

public class DefaultBannedPlayer extends DefaultRealmPlayer implements BannedPlayer {

    @Getter @Setter
    private long banDate;

    public DefaultBannedPlayer(RealmPlayerContext context, long banDate) {
        super(context);
        this.banDate = banDate;
    }
}