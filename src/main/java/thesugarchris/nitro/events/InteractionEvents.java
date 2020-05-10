package thesugarchris.nitro.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import thesugarchris.nitro.controllers.EconomyController;
import thesugarchris.nitro.utils.Text;

import java.util.List;

public class InteractionEvents implements Listener {
    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if (e.getItem() == null) return;

        ItemStack item = e.getItem();
        ItemMeta itemMeta = item.getItemMeta();
        String itemTitle;
        List<String> itemLore;
        itemTitle = itemMeta.getDisplayName();
        itemLore = itemMeta.getLore();

        if (ChatColor.stripColor(itemTitle).startsWith("Banknote")) {
            Double banknoteValue = EconomyController.removeBanknote(ChatColor.stripColor(itemLore.get(0)));
            if (banknoteValue == 0d) {
                p.sendMessage(Text.createMsg("&cInvalid banknote"));
            } else {
                p.getInventory().remove(item);
                EconomyController.addToPlayerBalance(p, banknoteValue);
                p.sendMessage(Text.createMsg("&aRedeemed banknote for &2$%,.0f", banknoteValue));
            }
        }
    }
}
