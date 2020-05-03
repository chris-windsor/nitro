package thesugarchris.nitro.controllers;

import org.bukkit.entity.Player;
import thesugarchris.nitro.Nitro;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class EconomyController {
    public static Double getPlayerBalance(Player p) {
        return PlayerDataController.balances.getOrDefault(p.getUniqueId(), 0d);
    }

    public static boolean playerHasAmount(Player p, Double val) {
        return getPlayerBalance(p) >= val;
    }

    public static void setPlayerBalance(Player p, Double val) {
        PlayerDataController.balances.replace(p.getUniqueId(), val);
        Nitro.getDatabase().update(String.format("INSERT INTO player_data (player, balance) VALUES ('%s', %.0f) ON DUPLICATE KEY UPDATE balance = %.0f", p.getUniqueId(), val, val));
    }

    public static Double addToPlayerBalance(Player p, Double val) {
        UUID player = p.getUniqueId();
        Double newBalance = PlayerDataController.balances.get(player) + val;
        setPlayerBalance(p, newBalance);
        return newBalance;
    }

    public static Double removeFromPlayerBalance(Player p, Double val) {
        return addToPlayerBalance(p, -val);
    }

    public static void addBanknote(String uuid, Double val) {
        Nitro.getDatabase().update(String.format("INSERT INTO banknotes (id, value) VALUES ('%s', %.0f)", uuid, val));
    }

    public static Double removeBanknote(String uuid) {
        ResultSet result = Nitro.getDatabase().query(String.format("SELECT value FROM banknotes WHERE id = '%s'", uuid));
        try {
            if (result.next()) {
                Nitro.getDatabase().update(String.format("DELETE FROM banknotes WHERE id = '%s'", uuid));
                return result.getDouble("value");
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return 0d;
    }
}
