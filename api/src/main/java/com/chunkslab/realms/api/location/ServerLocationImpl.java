package com.chunkslab.realms.api.location;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@Getter
public class ServerLocationImpl implements ServerLocation {
    private final @NotNull String server;
    private @NotNull World world;
    private double x, y, z;
    private float yaw, pitch;

    public ServerLocationImpl(@NotNull String server, @NotNull Location location) {
        this.server = server;
        setLocation(location);
    }

    @Override
    public @NotNull Location getLocation() {
        return new Location(world, x, y, z, yaw, pitch);
    }

    @Override
    public void setLocation(Location location) {
        this.world = location.getWorld();
        this.x = location.getX(); this.y = location.getY(); this.z = location.getZ();
        this.yaw = location.getYaw(); this.pitch = location.getPitch();
    }
}