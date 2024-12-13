package com.chunkslab.realms.gui;

import com.chunkslab.realms.RealmsPlugin;
import com.chunkslab.realms.api.config.ConfigFile;
import com.chunkslab.realms.api.player.objects.RealmPlayer;
import com.chunkslab.realms.api.player.permissions.Permission;
import com.chunkslab.realms.api.realm.Realm;
import com.chunkslab.realms.api.upgrade.Upgrade;
import com.chunkslab.realms.gui.item.BackItem;
import com.chunkslab.realms.gui.item.ForwardItem;
import com.chunkslab.realms.gui.item.UpdatingItem;
import com.chunkslab.realms.util.ChatUtils;
import com.chunkslab.realms.util.ItemUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.inventory.ItemStack;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.builder.SkullBuilder;
import xyz.xenondevs.invui.util.MojangApiUtils;
import xyz.xenondevs.invui.window.Window;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class BansGui {

    public static void open(RealmPlayer player, Realm realm, RealmsPlugin plugin) {
        if (!player.hasPermission(Permission.REALM_MENU_BAN)) {
            ChatUtils.sendMessage(player.getBukkitPlayer(), ChatUtils.format(plugin.getPluginMessages().getNotEnoughPermission()));
            return;
        }
        ConfigFile config = plugin.getBansMenuConfig();

        ItemBuilder border = new ItemBuilder(ItemUtils.build(config, "items.#"));

        ItemStack memberItem = ItemUtils.build(config, "items.m", Placeholder.parsed("current", String.valueOf(realm.getMembersController().getMembersCount())), Placeholder.parsed("max", String.valueOf(realm.getUpgrade(Upgrade.Type.MEMBERS).value())));
        Item members = new UpdatingItem(20, () -> new ItemBuilder(memberItem), event -> {
            MembersGui.open(player, realm, plugin);
        });

        List<Item> slots = realm.getMembersController().getBans()
                .stream().map(member -> {
                    try {
                        SkullBuilder item = new SkullBuilder(SkullBuilder.HeadTexture.of(member.getBukkitOfflinePlayer()));
                        item.setDisplayName(
                                ChatUtils.formatForGui(
                                        PlaceholderAPI.setPlaceholders(member.getBukkitOfflinePlayer(), config.getString("items.x.member.name"))
                                ));
                        item.setLore(
                                ChatUtils.formatForGui(
                                        PlaceholderAPI.setPlaceholders(member.getBukkitOfflinePlayer(), config.getStringList("items.x.member.lore")),
                                        Placeholder.parsed("ban-date", Realm.DATE_FORMAT.format(member.getBanDate()))
                                ));
                        item.setCustomModelData(config.getInt("items.x.member.custom-model-data"));
                        return new UpdatingItem(20, () -> item, event -> {});
                    } catch (MojangApiUtils.MojangApiException | IOException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());

        int xCount = config.getStringList("structure").stream()
                .mapToInt(line -> (int) line.chars().filter(ch -> ch == 'x').count())
                .sum();

        int remainedSlot = xCount - slots.size();
        for (int i = 1; i <= remainedSlot; i++) {
            Item addItem = new UpdatingItem(20, () -> new ItemBuilder(ItemUtils.build(config, "items.x.add")), event -> {});
            slots.add(addItem);
        }

        Gui gui = PagedGui.items()
                .setStructure(config.getStringList("structure").toArray(new String[0]))
                .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                .addIngredient('#', border)
                .addIngredient('m', members)
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
}