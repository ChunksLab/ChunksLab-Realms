package com.chunkslab.realms.gui;

import com.chunkslab.realms.RealmsPlugin;
import com.chunkslab.realms.api.config.ConfigFile;
import com.chunkslab.realms.api.player.objects.RealmPlayer;
import com.chunkslab.realms.api.player.permissions.ranks.players.RankedPlayer;
import com.chunkslab.realms.api.realm.Realm;
import com.chunkslab.realms.api.util.PermissionUtils;
import com.chunkslab.realms.gui.item.BackItem;
import com.chunkslab.realms.gui.item.ForwardItem;
import com.chunkslab.realms.gui.item.UpdatingItem;
import com.chunkslab.realms.util.ChatUtils;
import com.chunkslab.realms.util.ItemUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.builder.SkullBuilder;
import xyz.xenondevs.invui.util.MojangApiUtils;
import xyz.xenondevs.invui.window.Window;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MembersGui {

    public static void open(RealmPlayer player, Realm realm, RealmsPlugin plugin) {
        ConfigFile config = plugin.getMembersMenuConfig();

        ItemBuilder border = new ItemBuilder(ItemUtils.build(config, "items.#"));

        List<Item> members = realm.getMembersController().getMembers()
                .stream().map(member -> createMemberItem(member, config, realm))
                .toList();

        List<Item> slots = new ArrayList<>(members);

        int currentMemberAmount = realm.getMembersController().getMembersCount();
        int maxMemberAmount = PermissionUtils.getMax(player.getBukkitPlayer(), "chunkslab.realms.member", plugin.getPluginConfig().getSettings().getDefaultRealmMemberAmount());

        if (currentMemberAmount < maxMemberAmount) {
            int remained = maxMemberAmount - currentMemberAmount;
            for (int i = 1; i <= remained; i++) {
                Item addMember = new UpdatingItem(20, () -> new ItemBuilder(ItemUtils.build(config, "items.x.add")), event -> {});
                slots.add(addMember);
            }
        }

        int xCount = config.getStringList("structure").stream()
                .mapToInt(line -> (int) line.chars().filter(ch -> ch == 'x').count())
                .sum();

        int remainedSlot = xCount - slots.size();
        for (int i = 1; i <= remainedSlot; i++) {
            Item lockedItem = new UpdatingItem(20, () -> new ItemBuilder(ItemUtils.build(config, "items.x.locked")), event -> {});
            slots.add(lockedItem);
        }

        Gui gui = PagedGui.items()
                .setStructure(config.getStringList("structure").toArray(new String[0]))
                .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                .addIngredient('#', border)
                .addIngredient('<', new BackItem(config))
                .addIngredient('>', new ForwardItem(config))
                .setContent(slots)
                .build();

        Window window = Window.single()
                .setViewer(player.getBukkitPlayer())
                .setGui(gui)
                .setTitle(ChatUtils.formatForGui(config.getString("title")))
                .build();

        window.open();
    }

    private static Item createMemberItem(RankedPlayer member, ConfigFile config, Realm realm) {
        try {
            SkullBuilder item = new SkullBuilder(SkullBuilder.HeadTexture.of(member.getBukkitOfflinePlayer()));
            item.setDisplayName(ChatUtils.formatForGui(PlaceholderAPI.setPlaceholders(member.getBukkitOfflinePlayer(), config.getString("items.x.member.name")), Placeholder.component("member-rank", member.getRank().display())));
            item.setLore(ChatUtils.formatForGui(PlaceholderAPI.setPlaceholders(member.getBukkitOfflinePlayer(), config.getStringList("items.x.member.lore")), Placeholder.parsed("last-online-date", Realm.DATE_FORMAT.format(member.getData().getLastLogout())), Placeholder.parsed("join-date", "Daha Join Date Datası Tutmuyon!")));
            item.setCustomModelData(config.getInt("items.x.member.custom-model-data"));
            return new UpdatingItem(20, () -> item, event -> System.out.println("s"));
        } catch (MojangApiUtils.MojangApiException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}
