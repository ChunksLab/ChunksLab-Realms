package com.chunkslab.realms.gui;

import com.chunkslab.realms.RealmsPlugin;
import com.chunkslab.realms.api.config.ConfigFile;
import com.chunkslab.realms.api.realm.Realm;
import com.chunkslab.realms.gui.item.BackItem;
import com.chunkslab.realms.gui.item.ForwardItem;
import com.chunkslab.realms.gui.item.UpdatingItem;
import com.chunkslab.realms.util.ChatUtils;
import com.chunkslab.realms.util.ItemUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;
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

public class RealmsGui {

    public static void open(Player player, RealmsPlugin plugin) {
        ConfigFile config = plugin.getRealmsMenuConfig();

        ItemBuilder border = new ItemBuilder(ItemUtils.build(config, "items.#"));

        List<Item> realms = plugin.getRealmManager().getRealms().stream().map(realm -> createRealmItem(player, config, realm)).toList();

        Gui gui = PagedGui.items()
                .setStructure(config.getStringList("structure").toArray(new String[0]))
                .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                .addIngredient('#', border)
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

    private static Item createRealmItem(Player player, ConfigFile config, Realm realm) {
        try {
            SkullBuilder item = new SkullBuilder(SkullBuilder.HeadTexture.of(
                    realm.getMembersController().getOwnerPlayer().getBukkitOfflinePlayer()));
            item.setDisplayName(ChatUtils.formatForGui(PlaceholderAPI.setPlaceholders(realm.getMembersController().getOwnerPlayer().getBukkitOfflinePlayer(), config.getString("items.x.name"))));
            item.setLore(ChatUtils.formatForGui(config.getStringList("items.x.lore"), Placeholder.unparsed("current-players", String.valueOf(1)), Placeholder.unparsed("creation-date", realm.getCreationDateFormatted())));
            item.setCustomModelData(config.getInt("items.x.custom-model-data"));
            return new UpdatingItem(
                    20,
                    () -> item,
                    event -> player.teleportAsync(realm.getSpawnLocation().getLocation())
            );
        } catch (MojangApiUtils.MojangApiException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}