package thesugarchris.nitro.controllers;

import org.bukkit.entity.Player;
import thesugarchris.nitro.Nitro;

public class ModController {
    public static void addMutedPlayer(Player p) {
        PlayerDataController.mutedPlayers.add(p.getUniqueId());
        Nitro.getDatabase().update(String.format("INSERT INTO player_data (player, is_muted) VALUES ('%s', %d) ON DUPLICATE KEY UPDATE is_muted = %d", p.getUniqueId(), 1, 1));
    }

    public static void removeMutedPlayer(Player p) {
        PlayerDataController.mutedPlayers.remove(p.getUniqueId());
        Nitro.getDatabase().update(String.format("INSERT INTO player_data (player, is_muted) VALUES ('%s', %d) ON DUPLICATE KEY UPDATE is_muted = %d", p.getUniqueId(), 0, 0));
    }

    public static boolean isPlayerMuted(Player p) {
        return PlayerDataController.mutedPlayers.contains(p.getUniqueId());
    }
}
