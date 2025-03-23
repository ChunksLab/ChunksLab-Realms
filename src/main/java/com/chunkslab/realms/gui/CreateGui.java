package com.chunkslab.realms.gui;

import com.chunkslab.realms.RealmsPlugin;
import com.chunkslab.realms.api.biome.Biome;
import com.chunkslab.realms.api.config.ConfigFile;
import com.chunkslab.realms.api.player.objects.RealmPlayer;
import com.chunkslab.realms.api.player.permissions.Permission;
import com.chunkslab.realms.gui.item.BackItem;
import com.chunkslab.realms.gui.item.ForwardItem;
import com.chunkslab.realms.gui.item.UpdatingItem;
import com.chunkslab.realms.util.ChatUtils;
import com.chunkslab.realms.util.ItemUtils;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.window.Window;

public class CreateGui {

    public static void open(RealmPlayer player, RealmsPlugin plugin) {
        if (!player.hasPermission(Permission.REALM_MENU_CREATE)) {
            ChatUtils.sendMessage(player.getBukkitPlayer(), ChatUtils.format(plugin.getPluginMessages().getNotEnoughPermission()));
            return;
        }
        ConfigFile config = plugin.getCreateMenuConfig();

        ItemBuilder border = new ItemBuilder(ItemUtils.build(config, "items.#"));

        Biome plainsBiome = plugin.getBiomeManager().getBiome("plains");
        ItemBuilder plainsItem = new ItemBuilder(plainsBiome.getIcon());
        plainsItem.setDisplayName(ChatUtils.formatForGui(plainsBiome.getDisplayName()));
        plainsItem.setLore(ChatUtils.formatForGui(plainsBiome.getDescription()));
        Item plains = new UpdatingItem(20, () -> plainsItem, event -> {
            createFunction(player, plainsBiome, plugin);
        });

        Biome desertBiome = plugin.getBiomeManager().getBiome("desert");
        ItemBuilder desertItem = new ItemBuilder(desertBiome == null ? ItemUtils.build(config, "items.fallback") : desertBiome.getIcon());
        if (desertBiome != null) {
            desertItem.setDisplayName(ChatUtils.formatForGui(desertBiome.getDisplayName()));
            desertItem.setLore(ChatUtils.formatForGui(desertBiome.getDescription()));
        }
        Item desert = new UpdatingItem(20, () -> desertItem, event -> {
            createFunction(player, desertBiome, plugin);
        });

        Biome snowBiome = plugin.getBiomeManager().getBiome("snow");
        ItemBuilder snowItem = new ItemBuilder(snowBiome == null ? ItemUtils.build(config, "items.fallback") : snowBiome.getIcon());
        if (snowBiome != null) {
            snowItem.setDisplayName(ChatUtils.formatForGui(snowBiome.getDisplayName()));
            snowItem.setLore(ChatUtils.formatForGui(snowBiome.getDescription()));
        }
        Item snow = new UpdatingItem(20, () -> snowItem, event -> {
            createFunction(player, snowBiome, plugin);
        });

        Biome forestBiome = plugin.getBiomeManager().getBiome("forest");
        ItemBuilder forestItem = new ItemBuilder(forestBiome == null ? ItemUtils.build(config, "items.fallback") : forestBiome.getIcon());
        if (forestBiome != null) {
            forestItem.setDisplayName(ChatUtils.formatForGui(forestBiome.getDisplayName()));
            forestItem.setLore(ChatUtils.formatForGui(forestBiome.getDescription()));
        }
        Item forest = new UpdatingItem(20, () -> forestItem, event -> {
            createFunction(player, forestBiome, plugin);
        });

        Gui gui = PagedGui.items()
                .setStructure(config.getStringList("structure").toArray(new String[0]))
                .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                .addIngredient('#', border)
                .addIngredient('p', plains)
                .addIngredient('d', desert)
                .addIngredient('s', snow)
                .addIngredient('f', forest)
                .addIngredient('<', new BackItem(config))
                .addIngredient('>', new ForwardItem(config))
                .build();

        Window window = Window.single()
                .setViewer(player.getBukkitPlayer())
                .setGui(gui)
                .setTitle(ChatUtils.formatForGui(config.getString("title")))
                .build();

        window.open();
    }

    private static void createFunction(RealmPlayer realmPlayer, Biome biome, RealmsPlugin plugin) {
        if (biome == null) return;
        Player player = realmPlayer.getBukkitPlayer();
        player.closeInventory();
        ChatUtils.sendMessage(player, ChatUtils.format(plugin.getPluginMessages().getRealmCreating()));
        plugin.getRealmManager().createRealm(biome, realmPlayer).thenAccept(result -> {
            if (!result) {
                ChatUtils.sendMessage(player, ChatUtils.format(plugin.getPluginMessages().getProblemNotifyAdmin()));
                return;
            }

            plugin.getScheduler().runTaskSync(() -> player.teleportAsync(realmPlayer.getRealm().getSpawnLocation().getLocation()), player.getLocation());
            ChatUtils.sendMessage(player, ChatUtils.format(plugin.getPluginMessages().getRealmCreated()));
        });
    }
}