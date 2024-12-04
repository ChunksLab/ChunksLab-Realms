package com.chunkslab.realms.gui;

import com.chunkslab.realms.RealmsPlugin;
import com.chunkslab.realms.api.config.ConfigFile;
import com.chunkslab.realms.api.player.objects.RealmPlayer;
import com.chunkslab.realms.api.player.permissions.ranks.Rank;
import com.chunkslab.realms.api.player.permissions.ranks.players.RankedPlayer;
import com.chunkslab.realms.api.realm.Realm;
import com.chunkslab.realms.gui.item.UpdatingItem;
import com.chunkslab.realms.util.ChatUtils;
import com.chunkslab.realms.util.ItemUtils;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.window.Window;

public class RankGui {

    public static void open(RealmPlayer player, RankedPlayer member, Realm realm, RealmsPlugin plugin) {
        ConfigFile config = plugin.getRankMenuConfig();

        ItemBuilder border = new ItemBuilder(ItemUtils.build(config, "items.#"));

        Item visitor = new UpdatingItem(20, () -> new ItemBuilder(ItemUtils.build(config, "items.v")), event -> {
            realm.getMembersController().kick(member, player.getName());
            player.getBukkitPlayer().closeInventory();
        });

        Item resident = new UpdatingItem(20, () -> new ItemBuilder(ItemUtils.build(config, "items.r")), event -> {
            Rank rank = plugin.getRankManager().getRank(Rank.Assignment.RESIDENT);
            realm.getMembersController().setMember(member, rank, member.getJoinDate(), true);
            player.getBukkitPlayer().closeInventory();
        });

        Item trusted = new UpdatingItem(20, () -> new ItemBuilder(ItemUtils.build(config, "items.t")), event -> {
            Rank rank = plugin.getRankManager().getRank(Rank.Assignment.TRUSTED);
            realm.getMembersController().setMember(member, rank, member.getJoinDate(), true);
            player.getBukkitPlayer().closeInventory();
        });

        Item ban = new UpdatingItem(20, () -> new ItemBuilder(ItemUtils.build(config, "items.b")), event -> {
            realm.getMembersController().kick(member, player.getName());
            realm.getMembersController().getBans().add(member);
            player.getBukkitPlayer().closeInventory();
        });

        Gui gui = Gui.normal()
                .setStructure(config.getStringList("structure").toArray(new String[0]))
                .addIngredient('#', border)
                .addIngredient('v', visitor)
                .addIngredient('r', resident)
                .addIngredient('t', trusted)
                .addIngredient('b', ban)
                .build();

        Window window = Window.single()
                .setViewer(player.getBukkitPlayer())
                .setGui(gui)
                .setTitle(ChatUtils.formatForGui(config.getString("title")))
                .build();

        window.open();
    }
}