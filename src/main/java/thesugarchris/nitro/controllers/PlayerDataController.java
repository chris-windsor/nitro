package thesugarchris.nitro.controllers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import thesugarchris.nitro.Nitro;
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

    public static void loadData() throws SQLException {
        ResultSet result = Nitro.getDatabase().query("SELECT * FROM player_data;");

        while (result.next()) {
            UUID playerUUID = UUID.fromString(result.getString("player"));
            String playerNickname = Text.createMsg(result.getString("nickname"));
            Double playerBalance = result.getDouble("balance");
            boolean playerIsMuted = result.getBoolean("is_muted");

            nicknames.put(playerUUID, playerNickname);
            balances.put(playerUUID, playerBalance);

            if (playerIsMuted) mutedPlayers.add(playerUUID);

            Player p = Bukkit.getPlayer(playerUUID);
            if (p != null) {
                ChatController.updateNickname(p);
            }
        }

        Nitro.getPlugin().getServer().getOnlinePlayers().forEach(player -> {
            if (!balances.containsKey(player.getUniqueId())) {
                balances.put(player.getUniqueId(), 0d);
            }
        });
    }
}
