package com.chunkslab.realms.api.location;

import com.chunkslab.realms.api.RealmsAPI;
import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public interface ServerLocation {
    /**
     * Get the server name where this location is situated.
     * @return string name
     */
    @NotNull String getServer();

    /**
     * Get the classic Bukkit's location of this instance.
     * @return Bukkit's location
     */
    @NotNull
    Location getLocation();

    /**
     * Set the location of this instance.
     * @param location new location
     */
    void setLocation(Location location);

    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    class Builder {
        private String server;
        private Location location;

        public static ServerLocation.Builder create() {
            return new ServerLocation.Builder();
        }

        public static ServerLocation.Builder create(Location location) {
            return new ServerLocation.Builder().location(location);
        }

        public ServerLocation.Builder server(String server) {
            this.server = server;
            return this;
        }

        public ServerLocation.Builder location(Location location) {
            this.location = location;
            return this;
        }

        public ServerLocation build() {
            if (server == null) {
                if (RealmsAPI.getInstance().getModuleManager().isModulePresent("MultiServer"))
                    this.server = RealmsAPI.getInstance().getModuleManager().getModule("MultiServer").getConfig().getString("server-name");
                else
                    this.server = "server-1";
            }
            Preconditions.checkNotNull(location, "Specify a valid Bukkit's location to create a new instance of ServerLocation");
            return new ServerLocationImpl(server, location);
        }
    }
}