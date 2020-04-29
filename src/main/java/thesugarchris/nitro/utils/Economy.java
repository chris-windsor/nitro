package thesugarchris.nitro.utils;

import org.bukkit.entity.Player;
import thesugarchris.nitro.Nitro;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class Economy {
    private static final HashMap<UUID, Double> balances = new HashMap<>();

    public static void loadBalances() throws SQLException {
        ResultSet result = Nitro.getDatabase().query("SELECT * FROM player_data;");

        while (result.next()) {
            UUID playerUUID = UUID.fromString(result.getString("player"));
            Double playerBalance = result.getDouble("balance");
            balances.put(playerUUID, playerBalance);
        }

        // TODO: remove this
        Nitro.getPlugin().getServer().getOnlinePlayers().forEach(player -> {
            if (!balances.containsKey(player.getUniqueId())) {
                balances.put(player.getUniqueId(), 0d);
            }
        });
    }

    public static Double getPlayerBalance(Player p) {
        return balances.getOrDefault(p.getUniqueId(), 0d);
    }

    public static boolean playerHasAmount(Player p, Double val) {
        return getPlayerBalance(p) >= val;
    }

    public static void setPlayerBalance(Player p, Double val) {
        balances.replace(p.getUniqueId(), val);
        Nitro.getDatabase().update(String.format("INSERT INTO player_data (player, balance) VALUES ('%s', %.0f) ON DUPLICATE KEY UPDATE balance = %.0f", p.getUniqueId(), val, val));
    }

    public static Double addToPlayerBalance(Player p, Double val) {
        UUID player = p.getUniqueId();
        Double newBalance = balances.get(player) + val;
        setPlayerBalance(p, newBalance);
        return newBalance;
    }

    public static Double removeFromPlayerBalance(Player p, Double val) {
        return addToPlayerBalance(p, -val);
    }
}
