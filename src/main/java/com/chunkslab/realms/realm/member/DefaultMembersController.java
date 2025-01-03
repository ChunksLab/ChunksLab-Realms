package com.chunkslab.realms.realm.member;

import com.chunkslab.realms.RealmsPlugin;
import com.chunkslab.realms.api.player.ban.BannedPlayer;
import com.chunkslab.realms.api.player.contexts.RealmPlayerContext;
import com.chunkslab.realms.api.player.objects.RealmPlayer;
import com.chunkslab.realms.api.player.permissions.ranks.Rank;
import com.chunkslab.realms.api.player.permissions.ranks.players.DefaultRankedPlayer;
import com.chunkslab.realms.api.player.permissions.ranks.players.RankedPlayer;
import com.chunkslab.realms.api.realm.Realm;
import com.chunkslab.realms.api.realm.member.MembersController;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

@RequiredArgsConstructor
public class DefaultMembersController implements MembersController {
    @Getter(AccessLevel.PROTECTED)
    private final Realm realm;

    @Override
    public @NotNull Set<RankedPlayer> getMembers() {
        return realm.getMembersData().getMembers();
    }

    @Override
    public @NotNull Set<RealmPlayer> getVisitors() {
        return realm.getMembersData().getVisitors();
    }

    @Override
    public @NotNull Set<BannedPlayer> getBans() {
        return realm.getMembersData().getBans();
    }

    @Override
    public int getMembersCount() {
        return getMembers().size();
    }

    @Override
    public int getBansCount() {
        return getBans().size();
    }

    @Override
    public int getVisitorsCount() {
        return getVisitors().size();
    }

    @Override
    public RankedPlayer getMember(RealmPlayer realmPlayer) {
        return getMembers().stream().filter(rankedPlayer -> rankedPlayer.getContext().equals(realmPlayer.getContext())).findFirst().orElse(null);
    }

    @Override
    public BannedPlayer getBanned(RealmPlayer realmPlayer) {
        return getBans().stream().filter(bannedPlayer -> bannedPlayer.getContext().equals(realmPlayer.getContext())).findFirst().orElse(null);
    }

    @Override
    public boolean isMember(@NotNull RealmPlayer player) {
        for (RankedPlayer member : getMembers())
            if (member.getContext().equals(player.getContext()))
                return true;
        return false;
    }

    @Override
    public boolean isMember(@NotNull RealmPlayerContext playerContext) {
        for (RankedPlayer member : getMembers())
            if (member.getContext().equals(playerContext))
                return true;
        return false;
    }

    @Override
    public boolean isVisitor(@NotNull RealmPlayer player) {
        for (RealmPlayer member : getVisitors())
            if (member.getContext().equals(player.getContext()))
                return true;
        return false;
    }

    @Override
    public boolean isBanned(@NotNull RealmPlayer player) {
        for (BannedPlayer member : getBans())
            if (member.getContext().equals(player.getContext()))
                return true;
        return false;
    }

    @Override
    public void setMember(@NotNull RealmPlayer player, @NotNull Rank rank, boolean onlyExisting) {
        setMember(player, rank, System.currentTimeMillis(), onlyExisting);
    }

    @Override
    public void setMember(@NotNull RealmPlayer player, @NotNull Rank rank, long joinDate, boolean onlyExisting) {
        if (onlyExisting && isMember(player)) return;
        removeMember(player);

        RankedPlayer member = new DefaultRankedPlayer(player.getContext(), rank, joinDate);
        getMembers().add(member);
    }

    @Override
    public void removeMember(RealmPlayer player) {
        getMembers().removeIf(member -> member.getContext().equals(player.getContext()));
    }

    @Override
    public @Nullable Rank getRank(@NotNull RealmPlayer player) {
        return getMembers().stream()
                .filter(member -> member.getContext().equals(player.getContext()))
                .map(RankedPlayer::getRank)
                .findFirst()
                .orElse(null);
    }

    @Override
    public @NotNull RealmPlayerContext getOwner() {
        for (RankedPlayer member : getMembers())
            if (member.getRank().assignment() == Rank.Assignment.OWNER)
                return member.getContext();
        return null;
    }

    @Override
    public @NotNull RealmPlayer getOwnerPlayer() {
        for (RankedPlayer member : getMembers())
            if (member.getRank().assignment() == Rank.Assignment.OWNER)
                return member;
        return null;
    }

    @Override
    public boolean isOwner(@NotNull RealmPlayerContext context) {
        return getOwner().equals(context);
    }

    @Override
    public void setOwner(@NotNull RealmPlayer player) {
        setMember(getOwnerPlayer(), RealmsPlugin.getInstance().getRankManager().getRank(Rank.Assignment.RESIDENT), true);
        setMember(player, RealmsPlugin.getInstance().getRankManager().getRank(Rank.Assignment.OWNER), false);
    }

    @Override
    public void join(@NotNull RealmPlayer player) {
        player.setRealmId(getRealm().getUniqueId());
        setMember(player, RealmsPlugin.getInstance().getRankManager().getRank(Rank.Assignment.RESIDENT),false);
        //TODO: SEND BROADCAST MESSAGE TO ALL MEMBER
    }

    @Override
    public void kick(@NotNull RealmPlayer player, @Nullable String kicker) {
        player.setRealmId(null);
        removeMember(player);
        //TODO: SEND BROADCAST MESSAGE TO ALL MEMBER
    }
}
