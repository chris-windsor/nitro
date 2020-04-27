package thesugarchris.nitro.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import thesugarchris.nitro.utils.ChatController;
import thesugarchris.nitro.utils.ScoreboardController;
import thesugarchris.nitro.utils.TabListController;
import thesugarchris.nitro.utils.Text;

public class Leave implements Listener {
    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        e.setQuitMessage(Text.createMsg("&4[-] &r%s", ChatController.getNickname(p)));

        TabListController.updateAllPlayers();
        ScoreboardController.updateAllPlayers();
    }

    @EventHandler
    public void onKick(PlayerKickEvent e) {
        TabListController.updateAllPlayers();
        ScoreboardController.updateAllPlayers();
    }
}
