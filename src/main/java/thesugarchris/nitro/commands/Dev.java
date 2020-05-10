package thesugarchris.nitro.commands;

import org.bukkit.entity.Player;
import thesugarchris.nitro.controllers.HologramController;
import thesugarchris.nitro.utils.RegisterAsCommand;

public class Dev {
    @RegisterAsCommand(command = "hg create <id>")
    public void onHgCreate(Player p, String[] args) {
        HologramController.create(args[0], p.getLocation());
    }

    @RegisterAsCommand(command = "hg addline <id> <text>")
    public void onHgAddLine(Player p, String[] args) {
        HologramController.addLine(args[0], -1, false, args[1]);
    }

    @RegisterAsCommand(command = "hg setline <id> <line> <text>")
    public void onHgSetLine(Player p, String[] args) {
        HologramController.addLine(args[0], Integer.parseInt(args[1]), false, args[2]);
    }

    @RegisterAsCommand(command = "hg insertline <id> <line> <text>")
    public void onHgInsertLine(Player p, String[] args) {
        HologramController.addLine(args[0], Integer.parseInt(args[1]), true, args[2]);
    }

    @RegisterAsCommand(command = "hg removeline <id> <line>")
    public void onHgRemoveLine(Player p, String[] args) {
        HologramController.removeLine(args[0], Integer.parseInt(args[1]));
    }

    @RegisterAsCommand(command = "hg delete <id>")
    public void onHgDelete(Player p, String[] args) {
        HologramController.delete(args[0]);
    }

    @RegisterAsCommand(command = "hg move <id> <offset?>")
    public void onHgMove(Player p, String[] args) {
        HologramController.moveHologram(args[0], p.getLocation());
    }
}
