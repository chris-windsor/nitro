package thesugarchris.nitro.controllers;

import org.bukkit.entity.Player;
import thesugarchris.nitro.Nitro;
import thesugarchris.nitro.utils.Text;

import java.util.UUID;

public class ChatController {
    public static void updateNickname(Player p) {
        String playerNickname = PlayerDataController.nicknames.get(p.getUniqueId());
        p.setDisplayName(playerNickname);
        p.setPlayerListName(playerNickname);
    }

    public static void setNickname(Player p, String nickname) {
        String playerNickname = Text.createMsg(nickname);

        PlayerDataController.nicknames.replace(p.getUniqueId(), playerNickname);
        Nitro.getDatabase().update(String.format("INSERT INTO player_data (player, nickname) VALUES ('%s', '%s') ON DUPLICATE KEY UPDATE nickname = '%s'", p.getUniqueId(), playerNickname, playerNickname));

        p.setDisplayName(playerNickname);
        p.setPlayerListName(playerNickname);
        TabListController.updateAllPlayers();
    }

    public static String getNickname(Player p) {
        return PlayerDataController.nicknames.get(p.getUniqueId());
    }

    public static String getNickname(UUID uuid) {
        return PlayerDataController.nicknames.get(uuid);
    }
}
