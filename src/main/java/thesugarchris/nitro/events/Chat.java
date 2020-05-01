package thesugarchris.nitro.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import thesugarchris.nitro.controllers.PlayerDataController;
import thesugarchris.nitro.utils.Text;

public class Chat implements Listener {
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();

        if (PlayerDataController.getMutedPlayers().contains(p.getUniqueId())) {
            p.sendMessage(Text.createMsg("&cYou are currently muted"));
            e.setCancelled(true);
        }

        e.setFormat(p.getDisplayName() + Text.createMsg(" &7> &r") + e.getMessage());
    }
}
