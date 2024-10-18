package com.chunkslab.realms;

import com.chunkslab.realms.api.RealmsAPI;
import com.chunkslab.realms.api.scheduler.IScheduler;
import com.chunkslab.realms.scheduler.Scheduler;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;

@Getter
public final class RealmsPlugin extends RealmsAPI {

    @Getter private static RealmsPlugin instance;

    private BukkitAudiences adventure;

    // managers
    @Setter private IScheduler scheduler = new Scheduler(this);

    @Override
    public void onLoad() {
        instance = this;
        RealmsAPI.setInstance(this);
        RealmsAPI.setDebugMode(true);
    }

    @Override
    public void onEnable() {
        adventure = BukkitAudiences.create(this);
    }

    @Override
    public void onDisable() {
        if (adventure != null) {
            adventure.close();
            adventure = null;
        }
    }
}
