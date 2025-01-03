package com.chunkslab.realms.gui;

import com.chunkslab.realms.RealmsPlugin;
import com.chunkslab.realms.api.config.ConfigFile;
import com.chunkslab.realms.api.player.ban.DefaultBannedPlayer;
import com.chunkslab.realms.api.player.objects.RealmPlayer;
import com.chunkslab.realms.api.player.permissions.Permission;
import com.chunkslab.realms.api.player.permissions.ranks.Rank;
import com.chunkslab.realms.api.player.permissions.ranks.players.RankedPlayer;
import com.chunkslab.realms.api.realm.Realm;
import com.chunkslab.realms.gui.item.UpdatingItem;
import com.chunkslab.realms.util.ChatUtils;
import com.chunkslab.realms.util.ItemUtils;
import lombok.SneakyThrows;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.builder.SkullBuilder;
import xyz.xenondevs.invui.window.Window;

public class RankGui {

    @SneakyThrows
    public static void open(RealmPlayer player, RankedPlayer member, Realm realm, RealmsPlugin plugin) {
        if (!player.hasPermission(Permission.REALM_MENU_RANK)) {
            ChatUtils.sendMessage(player.getBukkitPlayer(), ChatUtils.format(plugin.getPluginMessages().getNotEnoughPermission()));
            return;
        }
        ConfigFile config = plugin.getRankMenuConfig();

        ItemBuilder border = new ItemBuilder(ItemUtils.build(config, "items.#"));

        SkullBuilder skull = new SkullBuilder(SkullBuilder.HeadTexture.of(member.getBukkitOfflinePlayer()));
        skull.setDisplayName(ChatUtils.formatForGui(PlaceholderAPI.setPlaceholders(member.getBukkitOfflinePlayer(), config.getString("items.x.name"))));
        skull.setCustomModelData(config.getInt("items.x.custom-model-data"));

        Item visitor = new UpdatingItem(20, () -> new ItemBuilder(ItemUtils.build(config, "items.v")), event -> {
            realm.getMembersController().kick(member, player.getName());
            ChatUtils.sendMessage(player.getBukkitPlayer(), ChatUtils.format(plugin.getPluginMessages().getRoleModified()));
            player.getBukkitPlayer().closeInventory();
        });

        Item resident = new UpdatingItem(20, () -> new ItemBuilder(ItemUtils.build(config, "items.r")), event -> {
            Rank rank = plugin.getRankManager().getRank(Rank.Assignment.RESIDENT);
            realm.getMembersController().setMember(member, rank, member.getJoinDate(), true);
            ChatUtils.sendMessage(player.getBukkitPlayer(), ChatUtils.format(plugin.getPluginMessages().getRoleModified()));
            player.getBukkitPlayer().closeInventory();
        });

        Item trusted = new UpdatingItem(20, () -> new ItemBuilder(ItemUtils.build(config, "items.t")), event -> {
            Rank rank = plugin.getRankManager().getRank(Rank.Assignment.TRUSTED);
            realm.getMembersController().setMember(member, rank, member.getJoinDate(), true);
            ChatUtils.sendMessage(player.getBukkitPlayer(), ChatUtils.format(plugin.getPluginMessages().getRoleModified()));
            player.getBukkitPlayer().closeInventory();
        });

        Item ban = new UpdatingItem(20, () -> new ItemBuilder(ItemUtils.build(config, "items.b")), event -> {
            realm.getMembersController().kick(member, player.getName());
            realm.getMembersController().getBans().add(new DefaultBannedPlayer(member.getContext(), System.currentTimeMillis()));
            ChatUtils.sendMessage(player.getBukkitPlayer(), ChatUtils.format(plugin.getPluginMessages().getPlayerBanned(), Placeholder.unparsed("player", member.getName())));
            player.getBukkitPlayer().closeInventory();
        });

        Gui gui = Gui.normal()
                .setStructure(config.getStringList("structure").toArray(new String[0]))
                .addIngredient('#', border)
                .addIngredient('x', skull)
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