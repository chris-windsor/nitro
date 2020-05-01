package thesugarchris.nitro.controllers;

import org.bukkit.entity.Player;
import thesugarchris.nitro.Nitro;
import thesugarchris.nitro.utils.Text;

public class TabListController {
    public static void updatePlayer(Player p) {
        String header = Text.createMsg("&r                            \n");
        String footer = Text.createMsg("&r                            \n");

        header += Text.createMsg("&r&7Welcome &r%s\n", p.getDisplayName());
        header += Text.createMsg("&r ");

        footer += Text.createMsg("&rPlayers: %d/%d\n", Nitro.getPlugin().getServer().getOnlinePlayers().size(), Nitro.getPlugin().getServer().getMaxPlayers());
        footer += Text.createMsg("&r ");

        p.setPlayerListHeaderFooter(header, footer);
    }

    public static void updateAllPlayers() {
        Nitro.getPlugin().getServer().getOnlinePlayers().forEach(TabListController::updatePlayer);
    }
}
