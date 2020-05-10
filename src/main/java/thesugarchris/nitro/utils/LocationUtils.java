package thesugarchris.nitro.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationUtils {
    public static Location stringToLocation(String loc) {
        String[] homeLocAsString = loc.split(",");
        return new Location(
                Bukkit.getWorld(homeLocAsString[0]),
                Double.parseDouble(homeLocAsString[1]),
                Double.parseDouble(homeLocAsString[2]),
                Double.parseDouble(homeLocAsString[3]),
                Float.parseFloat(homeLocAsString[4]),
                Float.parseFloat(homeLocAsString[5]));
    }

    public static String locationToString(Location loc) {
        return String.format("%s,%s,%s,%s,%s,%s", loc.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
    }
}
