package com.chunkslab.realms.realm;

import com.chunkslab.realms.RealmsPlugin;
import com.chunkslab.realms.api.location.ServerLocation;
import com.chunkslab.realms.api.player.objects.RealmPlayer;
import com.chunkslab.realms.api.realm.Realm;
import com.chunkslab.realms.api.realm.bank.RealmBank;
import com.chunkslab.realms.api.realm.member.MembersController;
import com.chunkslab.realms.api.realm.member.data.MembersData;
import com.chunkslab.realms.api.realm.privacy.PrivacyOption;
import com.chunkslab.realms.api.upgrade.Upgrade;
import com.chunkslab.realms.realm.bank.DefaultRealmBank;
import com.chunkslab.realms.realm.member.DefaultMembersController;
import com.chunkslab.realms.realm.member.data.DefaultMembersData;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter @Setter
public class DefaultRealm implements Realm {
    private UUID uniqueId;
    private final long creationDate = System.currentTimeMillis();
    private MembersData membersData = new DefaultMembersData();
    private String biome;
    private final Map<Upgrade.Type, Upgrade> upgrades = new HashMap<>();
    private Map<RealmPlayer, Integer> ratings = new HashMap<>();
    private ServerLocation centerLocation;
    private ServerLocation spawnLocation;
    private PrivacyOption privacyOption = PrivacyOption.ANYONE;

    public DefaultRealm(String biome) {
        this.uniqueId = UUID.randomUUID();
        this.biome = biome;
    }

    @Override
    public @NotNull MembersController getMembersController() {
        return new DefaultMembersController(this);
    }

    @Override
    public @NotNull RealmBank getRealmBank() {
        return new DefaultRealmBank(this);
    }

    @Override
    public Upgrade getUpgrade(Upgrade.Type type) {
        return upgrades.getOrDefault(type, RealmsPlugin.getInstance().getUpgradeManager().getUpgrade(type, 0));
    }

    @Override
    public int getRating(RealmPlayer player) {
        return this.ratings.getOrDefault(player, -1);
    }

    @Override
    public void setUpgrade(Upgrade upgrade) {
        upgrades.put(upgrade.type(), upgrade);
    }

    @Override
    public void addRating(RealmPlayer player, int rating) {
        this.ratings.put(player, rating);
    }
}