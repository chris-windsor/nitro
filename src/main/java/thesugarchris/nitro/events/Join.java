package thesugarchris.nitro.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import thesugarchris.nitro.utils.ChatController;
import thesugarchris.nitro.utils.ScoreboardController;
import thesugarchris.nitro.utils.TabListController;
import thesugarchris.nitro.utils.Text;

public class Join implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        e.setJoinMessage(Text.createMsg("&a[+] &r%s", ChatController.getNickname(p)));

        ChatController.updateNickname(p);

        TabListController.updateAllPlayers();
        ScoreboardController.updateAllPlayers();
    }
}
