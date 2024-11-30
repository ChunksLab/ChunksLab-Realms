package com.chunkslab.realms.gui;

import com.chunkslab.realms.RealmsPlugin;
import com.chunkslab.realms.api.config.ConfigFile;
import com.chunkslab.realms.api.player.objects.RealmPlayer;
import com.chunkslab.realms.api.realm.Realm;
import com.chunkslab.realms.api.realm.privacy.PrivacyOption;
import com.chunkslab.realms.api.util.PermissionUtils;
import com.chunkslab.realms.gui.item.UpdatingItem;
import com.chunkslab.realms.util.ChatUtils;
import com.chunkslab.realms.util.ItemUtils;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.inventory.ItemStack;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.window.Window;

public class SettingsGui {

    public static void open(RealmPlayer player, RealmsPlugin plugin) {
        ConfigFile config = plugin.getSettingsMenuConfig();

        Realm realm = player.getRealm();

        ItemBuilder border = new ItemBuilder(ItemUtils.build(config, "items.#"));

        ItemBuilder info = new ItemBuilder(ItemUtils.build(config, "items.?"));

        ItemStack memberItem = ItemUtils.build(config, "items.m", Placeholder.parsed("current", String.valueOf(realm.getMembersController().getMembersCount())), Placeholder.parsed("max", String.valueOf(PermissionUtils.getMax(player.getBukkitPlayer(), "chunkslab.realms.member", plugin.getPluginConfig().getSettings().getDefaultRealmMemberAmount()))));
        Item member = new UpdatingItem(20, () -> new ItemBuilder(memberItem), event -> {
            MembersGui.open(player, plugin);
        });

        Item reset = new UpdatingItem(20, () -> new ItemBuilder(ItemUtils.build(config, "items.r")), event -> {
            ChatUtils.sendMessage(player.getBukkitPlayer(), ChatUtils.format("<red>Maintenance!"));
        });

        Item privacy = new UpdatingItem(10, () -> new ItemBuilder(ItemUtils.build(config, "items.p", Placeholder.parsed("status", realm.getPrivacyOption().name()))), event -> {
            PrivacyOption privacyOption = realm.getPrivacyOption();

            PrivacyOption[] values = PrivacyOption.values();
            int nextIndex = (privacyOption.ordinal() + 1) % values.length;

            realm.setPrivacyOption(values[nextIndex]);
        });

        Gui gui = Gui.normal()
                .setStructure(config.getStringList("structure").toArray(new String[0]))
                .addIngredient('#', border)
                .addIngredient('?', info)
                .addIngredient('m', member)
                .addIngredient('r', reset)
                .addIngredient('p', privacy)
                .build();

        Window window = Window.single()
                .setViewer(player.getBukkitPlayer())
                .setGui(gui)
                .setTitle(ChatUtils.formatForGui(config.getString("title")))
                .build();

        window.open();
    }
}