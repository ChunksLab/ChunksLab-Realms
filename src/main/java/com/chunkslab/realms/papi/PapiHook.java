package com.chunkslab.realms.papi;

import com.chunkslab.realms.RealmsPlugin;
import com.chunkslab.realms.api.player.objects.RealmPlayer;
import com.chunkslab.realms.api.realm.Realm;
import com.chunkslab.realms.util.ChatUtils;
import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
public class PapiHook extends PlaceholderExpansion {

    private final RealmsPlugin plugin;

    @Override
    public @NotNull String getIdentifier() {
        return "realms";
    }

    @Override
    public @NotNull String getAuthor() {
        return String.join(", ", plugin.getDescription().getAuthors());
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        RealmPlayer realmPlayer = plugin.getPlayerManager().getPlayer(player);
        params = params.toUpperCase();
        if (params.startsWith("REALM")) {
            Realm realm = realmPlayer.getRealm();
            if (realm == null) return "NO REALM";
            if (params.split("_").length == 1)
                return realm.getUniqueId().toString();

            return switch (params.substring(params.indexOf("_") + 1)) {
                case "NAME" -> plugin.getPluginConfig().getSettings().getDefaultName().replace("<player>", realm.getMembersController().getOwner().getName());
                case "DESCRIPTION" -> plugin.getPluginConfig().getSettings().getDefaultDescription().replace("<player>", realm.getMembersController().getOwner().getName());
                case "BIOME" -> realm.getBiome();
                case "MEMBERS_COUNT" -> String.valueOf(realm.getMembersController().getMembersCount());
                case "VISITORS_COUNT" -> String.valueOf(realm.getMembersController().getVisitorsCount());
                case "BANS_COUNT" -> String.valueOf(realm.getMembersController().getBansCount());
                case "PRIVACY" -> realm.getPrivacyOption().name();
                case "CREATION_DATE" -> realm.getCreationDateFormatted();
                case "RANK_ID" -> realm.getMembersController().getRank(realmPlayer).id();
                case "RANK_DISPLAY" -> ChatUtils.fromLegacy(realm.getMembersController().getRank(realmPlayer).display());
                case "OWNER" -> realm.getMembersController().getOwner().getName();
                default -> "UNKNOWN PLACEHOLDER";
            };
        }
        return switch (params) {
            case "HAS_REALM" -> String.valueOf(realmPlayer.getRealmId() != null);
            case "IN_REALM" -> String.valueOf(plugin.getRealmManager().getRealm(player.getPlayer().getLocation()) != null);
            case "IS_VISITOR" -> String.valueOf(plugin.getRealmManager().getRealms().stream().anyMatch(realm -> realm.getMembersController().isVisitor(realmPlayer)));
            case "MESSAGE_PREFERENCE" -> realmPlayer.getData().getMessagePreference().name();
            case "BORDER_COLOR" -> realmPlayer.getData().getBorderColor().name();
            default -> "UNKNOWN PLACEHOLDER";
        };
    }
}
