package thesugarchris.nitro.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import thesugarchris.nitro.Nitro;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class ChatController {
    private static final HashMap<UUID, String> nicknames = new HashMap<>();

    public static void loadNicknames() throws SQLException {
        ResultSet result = Nitro.getDatabase().query("SELECT * FROM player_data;");

        while (result.next()) {
            UUID playerUUID = UUID.fromString(result.getString("player"));
            String playerNickname = Text.createMsg(result.getString("nickname"));
            nicknames.put(playerUUID, playerNickname);

            Player p = Bukkit.getPlayer(playerUUID);
            if (p != null) {
                updateNickname(p);
            }
        }
    }

    public static void updateNickname(Player p) {
        String playerNickname = nicknames.get(p.getUniqueId());
        p.setDisplayName(playerNickname);
        p.setPlayerListName(playerNickname);
    }

    public static void setNickname(Player p, String nickname) {
        String playerNickname = Text.createMsg(nickname);

        nicknames.replace(p.getUniqueId(), playerNickname);
        Nitro.getDatabase().update(String.format("INSERT INTO player_data (player, nickname) VALUES ('%s', '%s') ON DUPLICATE KEY UPDATE nickname = '%s'", p.getUniqueId(), playerNickname, playerNickname));

        p.setDisplayName(playerNickname);
        p.setPlayerListName(playerNickname);
        TabListController.updateAllPlayers();
    }

    public static String getNickname(Player p) {
        return nicknames.get(p.getUniqueId());
    }
}
