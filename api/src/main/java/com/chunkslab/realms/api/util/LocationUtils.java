package com.chunkslab.realms.api.util;

import com.chunkslab.realms.api.location.ServerLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationUtils {

    public static ServerLocation getServerLocation(String location) {
        if (location == null || location.isEmpty())
            return null;
        String[] sections = location.split(",");
        String server = sections[0];
        World world = Bukkit.getWorld(sections[1]);
        double x = Double.parseDouble(sections[2]);
        double y = Double.parseDouble(sections[3]);
        double z = Double.parseDouble(sections[4]);
        float yaw = Float.parseFloat(sections[5]);
        float pitch = Float.parseFloat(sections[6]);
        return ServerLocation.Builder.create(new Location(world, x, y, z, yaw, pitch)).server(server).build();
    }

    public static String getServerLocation(ServerLocation location) {
        return location == null ? "Unknown location!" : location.getServer() + "," + location.getLocation().getWorld().getName() + "," + location.getLocation().getX() + "," + location.getLocation().getY() + "," + location.getLocation().getZ() + "," + location.getLocation().getYaw() + "," + location.getLocation().getPitch();
    }

    public static Location getLocation(String location) {
        if (location == null || location.isEmpty())
            return null;
        String[] sections = location.split(",");
        double x = Double.parseDouble(sections[1]);
        double y = Double.parseDouble(sections[2]);
        double z = Double.parseDouble(sections[3]);
        float yaw = (sections.length > 5) ? Float.parseFloat(sections[4]) : 0.0F;
        float pitch = (sections.length > 4) ? Float.parseFloat(sections[5]) : 0.0F;
        return new Location(Bukkit.getWorld(sections[0]), x, y, z, yaw, pitch);
    }

    public static String getLocation(Location location) {
        return location == null ? "Unknown location!" : location.getWorld().getName() + "," + location.getX() + "," + location.getY() + "," + location.getZ() + "," + location.getYaw() + "," + location.getPitch();
    }

}
