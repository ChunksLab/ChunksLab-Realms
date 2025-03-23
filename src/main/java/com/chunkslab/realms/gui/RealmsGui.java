package com.chunkslab.realms.gui;

import com.chunkslab.realms.RealmsPlugin;
import com.chunkslab.realms.api.config.ConfigFile;
import com.chunkslab.realms.api.location.ServerLocation;
import com.chunkslab.realms.api.player.objects.RealmPlayer;
import com.chunkslab.realms.api.player.permissions.Permission;
import com.chunkslab.realms.gui.item.BackItem;
import com.chunkslab.realms.gui.item.ForwardItem;
import com.chunkslab.realms.gui.item.UpdatingItem;
import com.chunkslab.realms.util.ChatUtils;
import com.chunkslab.realms.util.ItemUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;
import xyz.xenondevs.inventoryaccess.component.ComponentWrapper;
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

public class RealmsGui {

    public static void open(Player player, RealmsPlugin plugin) {
        ConfigFile config = plugin.getRealmsMenuConfig();

        RealmPlayer realmPlayer = plugin.getPlayerManager().getPlayer(player);

        if (!realmPlayer.hasPermission(Permission.REALM_MENU)) {
            ChatUtils.sendMessage(player, ChatUtils.format(plugin.getPluginMessages().getNotEnoughPermission()));
            return;
        }

        ItemBuilder border = new ItemBuilder(ItemUtils.build(config, "items.#"));

        ItemBuilder community = new ItemBuilder(ItemUtils.build(config, "items.c"));

        Item teleport = new UpdatingItem(20, () -> new ItemBuilder(ItemUtils.build(config, "items.t")), event -> {
            if (event.getClickType().isShiftClick()) {
                if (realmPlayer.getRealmId() == null) {
                    ChatUtils.sendMessage(player, ChatUtils.format(plugin.getPluginMessages().getNoRealm()));
                } else {
                    realmPlayer.getRealm().setSpawnLocation(ServerLocation.Builder.create(player.getLocation()).build());
                    ChatUtils.sendMessage(player, ChatUtils.format(plugin.getPluginMessages().getRealmSpawnSet()));
                }
            } else {
                if (realmPlayer.getRealmId() == null) {
                    ChatUtils.sendMessage(player, ChatUtils.format(plugin.getPluginMessages().getNoRealm()));
                } else {
                    player.teleportAsync(realmPlayer.getRealm().getSpawnLocation().getLocation());
                }
            }
        });

        Item settings = new UpdatingItem(20, () -> new ItemBuilder(ItemUtils.build(config, "items.s")), event -> {
            if (realmPlayer.getRealmId() == null) {
                ChatUtils.sendMessage(player, ChatUtils.format(plugin.getPluginMessages().getNoRealm()));
            } else {
                SettingsGui.open(realmPlayer, plugin);
            }
        });

        List<Item> realms = plugin.getRealmManager().getRealms()
                .stream().map(realm -> {
                    try {
                        SkullBuilder item = new SkullBuilder(realm.getMembersController().getOwner().getUniqueId());

                        ComponentWrapper displayName = ChatUtils.formatForGui(
                                PlaceholderAPI.setPlaceholders(
                                        realm.getMembersController().getOwnerPlayer().getBukkitOfflinePlayer(),
                                        config.getString("items.x.name")
                                )
                        );
                        item.setDisplayName(displayName);

                        List<ComponentWrapper> lore = ChatUtils.formatForGui(
                                config.getStringList("items.x.lore"),
                                Placeholder.parsed("current-visitors", String.valueOf(realm.getMembersController().getVisitorsCount())),
                                Placeholder.parsed("creation-date", realm.getCreationDateFormatted())
                        );
                        item.setLore(lore);

                        int customModelData = config.getInt("items.x.custom-model-data");
                        item.setCustomModelData(customModelData);
                        return new UpdatingItem(
                                20,
                                () -> item,
                                event -> player.teleportAsync(realm.getSpawnLocation().getLocation())
                        );
                    } catch (MojangApiUtils.MojangApiException | IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

        Gui gui = PagedGui.items()
                .setStructure(config.getStringList("structure").toArray(new String[0]))
                .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                .addIngredient('#', border)
                .addIngredient('c', community)
                .addIngredient('t', teleport)
                .addIngredient('s', settings)
                .addIngredient('<', new BackItem(config))
                .addIngredient('>', new ForwardItem(config))
                .setContent(realms)
                .build();

        Window window = Window.single()
                .setViewer(player)
                .setGui(gui)
                .setTitle(ChatUtils.formatForGui(config.getString("title")))
                .build();

        window.open();
    }
}