package thesugarchris.nitro.controllers;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import thesugarchris.nitro.Nitro;
import thesugarchris.nitro.utils.LocationUtils;

import java.util.HashMap;
import java.util.UUID;

public class HomeController {
    public static HashMap<String, Location> getHomes(Player p) {
        return PlayerDataController.homes.get(p.getUniqueId());
    }

    public static Location getHomeLoc(Player p, String homeName) {
        return getHomes(p).get(homeName);
    }

    public static void addHome(Player p, String homeName) {
        UUID playerUUID = p.getUniqueId();
        if (!PlayerDataController.homes.containsKey(playerUUID)) {
            PlayerDataController.homes.put(playerUUID, new HashMap<>());
        }

        PlayerDataController.homes.get(playerUUID).put(homeName, p.getLocation());

        Location pLoc = p.getLocation();
        String locationAsString = LocationUtils.locationToString(pLoc);

        Nitro.getDatabase().update(String.format("INSERT INTO homes (player, name, location) VALUES ('%s', '%s', '%s') ON DUPLICATE KEY UPDATE location = '%s'", p.getUniqueId(), homeName, locationAsString, locationAsString));
    }
}
