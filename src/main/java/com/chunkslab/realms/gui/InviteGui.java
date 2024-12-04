package com.chunkslab.realms.gui;

import com.chunkslab.realms.RealmsPlugin;
import com.chunkslab.realms.api.config.ConfigFile;
import com.chunkslab.realms.api.player.objects.RealmPlayer;
import com.chunkslab.realms.api.player.permissions.ranks.Rank;
import com.chunkslab.realms.gui.item.UpdatingItem;
import com.chunkslab.realms.util.ChatUtils;
import com.chunkslab.realms.util.ItemUtils;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.window.Window;

public class InviteGui {

    public static void open(RealmPlayer inviter, RealmPlayer target, RealmsPlugin plugin) {
        ConfigFile config = plugin.getInviteMenuConfig();

        ItemBuilder border = new ItemBuilder(ItemUtils.build(config, "items.#"));

        ItemBuilder info = new ItemBuilder(ItemUtils.build(config, "items.?"));

        Item resident = new UpdatingItem(20, () -> new ItemBuilder(ItemUtils.build(config, "items.r")), event -> {
            Rank residentRank = plugin.getRankManager().getRank(Rank.Assignment.RESIDENT);
            plugin.getInviteManager().invitePlayer(inviter, target, residentRank);

            ChatUtils.sendMessage(inviter.getBukkitPlayer(), ChatUtils.format("<#85CC16>Player invited successfully."));
            inviter.getBukkitPlayer().closeInventory();
        });

        Item trusted = new UpdatingItem(20, () -> new ItemBuilder(ItemUtils.build(config, "items.t")), event -> {
            Rank trustedRank = plugin.getRankManager().getRank(Rank.Assignment.TRUSTED);
            plugin.getInviteManager().invitePlayer(inviter, target, trustedRank);

            ChatUtils.sendMessage(inviter.getBukkitPlayer(), ChatUtils.format("<#85CC16>Player invited successfully."));
            inviter.getBukkitPlayer().closeInventory();
        });

        Gui gui = Gui.normal()
                .setStructure(config.getStringList("structure").toArray(new String[0]))
                .addIngredient('#', border)
                .addIngredient('?', info)
                .addIngredient('r', resident)
                .addIngredient('t', trusted)
                .build();

        Window window = Window.single()
                .setViewer(inviter.getBukkitPlayer())
                .setGui(gui)
                .setTitle(ChatUtils.formatForGui(config.getString("title")))
                .build();

        window.open();
    }
}