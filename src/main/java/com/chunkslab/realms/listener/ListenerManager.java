package com.chunkslab.realms.listener;

import com.chunkslab.realms.RealmsPlugin;
import com.chunkslab.realms.api.listener.IListenerManager;
import com.chunkslab.realms.api.player.permissions.Permission;
import com.chunkslab.realms.listener.realm.BreakPermission;
import com.chunkslab.realms.listener.realm.PlacePermission;
import com.chunkslab.realms.listener.realm.SwitchPermission;
import com.chunkslab.realms.listener.realm.UsePermission;
import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
public class ListenerManager implements IListenerManager {

    private final Set<Listener> listeners = new HashSet<>();
    private final RealmsPlugin plugin;

    @Override
    public void enable() {
        register(new PlayerConnectionListener(plugin));
        register(new PlayerMoveListener(plugin));
        register(new PlayerTeleportListener(plugin));
        register(new PlayerRespawnListener(plugin));
        register(new PlacePermission(Permission.PROTECTION_PLACE));
        register(new BreakPermission(Permission.PROTECTION_BREAK));
        register(new UsePermission(Permission.PROTECTION_USE));
        register(new SwitchPermission(Permission.PROTECTION_SWITCH));
    }

    @Override
    public void disable() {
        for (Listener listener : listeners)
            HandlerList.unregisterAll(listener);
        listeners.clear();
    }

    @Override
    public void register(@NotNull Listener listener) {
        Preconditions.checkNotNull(listener, "Could not register a null listener");
        if (getListener(listener.getClass()).isPresent())
            throw new IllegalStateException(String.format("A listener \"%s\" already has been registered!", listener.getClass().getSimpleName()));
        listeners.add(listener);
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
    }

    @Override
    public Optional<Listener> getListener(@NotNull Class<? extends Listener> clazz) {
        return listeners.stream().filter(listener -> listener.getClass() == clazz).findFirst();
    }
}