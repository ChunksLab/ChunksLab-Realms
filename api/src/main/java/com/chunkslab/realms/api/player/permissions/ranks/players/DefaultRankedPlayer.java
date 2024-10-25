package com.chunkslab.realms.api.player.permissions.ranks.players;

import com.chunkslab.realms.api.player.contexts.RealmPlayerContext;
import com.chunkslab.realms.api.player.objects.DefaultRealmPlayer;
import com.chunkslab.realms.api.player.permissions.ranks.Rank;
import lombok.Getter;
import lombok.Setter;

public class DefaultRankedPlayer extends DefaultRealmPlayer implements RankedPlayer {

    @Getter @Setter
    private Rank rank;

    public DefaultRankedPlayer(RealmPlayerContext context, Rank rank) {
        super(context);
        this.rank = rank;
    }
}