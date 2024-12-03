package com.chunkslab.realms.invite;

import com.chunkslab.realms.RealmsPlugin;
import com.chunkslab.realms.api.invite.IInviteManager;
import com.chunkslab.realms.api.player.objects.RealmPlayer;
import com.chunkslab.realms.api.player.permissions.ranks.Rank;
import com.chunkslab.realms.util.ChatUtils;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class InviteManager implements IInviteManager {

    private final RealmsPlugin plugin;

    private final Cache<UUID, UUID> inviteCache = CacheBuilder.newBuilder().expireAfterWrite(3, TimeUnit.MINUTES).build();

    @Override
    public void invitePlayer(RealmPlayer inviter, RealmPlayer target) {
        inviteCache.put(target.getUniqueId(), inviter.getRealmId());

        Component component = ChatUtils.format("<yellow>You just got an invite from <player>. ", Placeholder.unparsed("player", inviter.getName()));
        component = component.append(ChatUtils.format("<green>ACCEPT").clickEvent(ClickEvent.runCommand("/realms accept " + inviter.getName())));
        component = component.append(ChatUtils.format("<dark_red>DENY").clickEvent(ClickEvent.runCommand("/realms deny " + inviter.getName())));

        if (target.getBukkitPlayer() != null) {
            target.getBukkitPlayer().sendMessage(component);
        }
    }

    @Override
    public CompletableFuture<UUID> getInvite(UUID uuid) {
        return CompletableFuture.completedFuture(inviteCache.getIfPresent(uuid));
    }

    @Override
    public void rejectInvite(RealmPlayer player) {
        inviteCache.invalidate(player.getUniqueId());
    }

    @Override
    public void acceptInvite(RealmPlayer player, UUID realmId) {
        plugin.getRealmManager().getRealm(realmId).getMembersController().setMember(player, plugin.getRankManager().getRank(Rank.Assignment.RESIDENT), false);
        player.setRealmId(realmId);
    }

}