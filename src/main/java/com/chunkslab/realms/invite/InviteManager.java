package com.chunkslab.realms.invite;

import com.chunkslab.realms.RealmsPlugin;
import com.chunkslab.realms.api.invite.IInviteManager;
import com.chunkslab.realms.api.invite.Invite;
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

    private final Cache<UUID, Invite> inviteCache = CacheBuilder.newBuilder().expireAfterWrite(3, TimeUnit.MINUTES).build();

    @Override
    public void invitePlayer(RealmPlayer inviter, RealmPlayer target, Rank rank) {
        inviteCache.put(target.getUniqueId(), new Invite(inviter.getRealmId(), rank));

        Component component = ChatUtils.format(plugin.getPluginMessages().getInviteReceived(), Placeholder.unparsed("player", inviter.getName()));
        component = component.append(ChatUtils.format(plugin.getPluginMessages().getInviteAccept()).clickEvent(ClickEvent.runCommand("/realms accept")));
        component = component.append(ChatUtils.format(plugin.getPluginMessages().getInviteReject()).clickEvent(ClickEvent.runCommand("/realms deny")));

        if (target.getBukkitPlayer() != null) {
            ChatUtils.sendMessage(target.getBukkitPlayer(), component);
        }
    }

    @Override
    public CompletableFuture<Invite> getInvite(UUID uuid) {
        return CompletableFuture.completedFuture(inviteCache.getIfPresent(uuid));
    }

    @Override
    public void rejectInvite(RealmPlayer player) {
        inviteCache.invalidate(player.getUniqueId());
    }

    @Override
    public void acceptInvite(RealmPlayer player, Invite invite) {
        plugin.getRealmManager().getRealm(invite.getRealmId()).getMembersController().setMember(player, invite.getRank(), false);
        player.setRealmId(invite.getRealmId());
        inviteCache.invalidate(player.getUniqueId());
    }

}