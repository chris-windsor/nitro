package thesugarchris.nitro.controllers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import thesugarchris.nitro.Nitro;
import thesugarchris.nitro.utils.LocationUtils;
import thesugarchris.nitro.utils.Text;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerDataController {
    protected static final HashMap<UUID, String> nicknames = new HashMap<>();
    protected static final HashMap<UUID, Double> balances = new HashMap<>();

    protected static final List<UUID> mutedPlayers = new ArrayList<>();

    protected static final HashMap<UUID, HashMap<String, Location>> homes = new HashMap<>();

    public static void loadData() {
        ResultSet playerDataResult = Nitro.getDatabase().query("SELECT * FROM player_data;");

        while (true) {
            try {
                if (!playerDataResult.next()) break;

                UUID playerUUID = UUID.fromString(playerDataResult.getString("player"));
                String playerNickname = Text.createMsg(playerDataResult.getString("nickname"));
                Double playerBalance = playerDataResult.getDouble("balance");
                boolean playerIsMuted = playerDataResult.getBoolean("is_muted");

                nicknames.put(playerUUID, playerNickname);
                balances.put(playerUUID, playerBalance);

                if (playerIsMuted) mutedPlayers.add(playerUUID);

                Player p = Bukkit.getPlayer(playerUUID);
                if (p != null) {
                    ChatController.updateNickname(p);
                }
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }

        Nitro.getPlugin().getServer().getOnlinePlayers().forEach(player -> {
            if (!balances.containsKey(player.getUniqueId())) {
                balances.put(player.getUniqueId(), 0d);
            }
        });

        ResultSet homesResult = Nitro.getDatabase().query("SELECT * FROM homes");

        while (true) {
            try {
                if (!homesResult.next()) break;

                UUID playerUUID = UUID.fromString(homesResult.getString("player"));
                String homeName = homesResult.getString("name");
                Location homeLoc = LocationUtils.stringToLocation(homesResult.getString("location"));

                if (!homes.containsKey(playerUUID)) {
                    homes.put(playerUUID, new HashMap<>());
                }

                homes.get(playerUUID).put(homeName, homeLoc);
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }
}
