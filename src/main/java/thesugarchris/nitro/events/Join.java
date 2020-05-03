package thesugarchris.nitro.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import thesugarchris.nitro.controllers.ChatController;
import thesugarchris.nitro.controllers.ScoreboardController;
import thesugarchris.nitro.controllers.TabListController;
import thesugarchris.nitro.utils.Text;

public class Join implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        String nickname = ChatController.getNickname(p);
        e.setJoinMessage(Text.createMsg("&a[+] &r%s", nickname == null ? p.getName() : nickname));

        ChatController.updateNickname(p);

        TabListController.updateAllPlayers();
        ScoreboardController.updateAllPlayers();
    }
}
