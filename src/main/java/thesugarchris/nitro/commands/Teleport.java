package thesugarchris.nitro.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import thesugarchris.nitro.controllers.HomeController;
import thesugarchris.nitro.utils.ArrayUtils;
import thesugarchris.nitro.utils.RegisterAsCommand;
import thesugarchris.nitro.utils.Text;

public class Teleport {
    @RegisterAsCommand(command = "sethome <name>")
    public void onSetHome(Player p, String[] args) {
        HomeController.addHome(p, args[0]);
        p.sendMessage(Text.createMsg("&aAdded new home"));
    }

    @RegisterAsCommand(command = "home <name>")
    public void onHome(Player p, String[] args) {
        Location homeLoc = HomeController.getHomeLoc(p, args[0]);
        if (homeLoc == null) {
            p.sendMessage(Text.createMsg("&cHome does not exist"));
        } else {
            p.teleport(homeLoc);
            p.sendMessage(Text.createMsg("&aTeleported to &2%s", args[0]));
        }
    }

    @RegisterAsCommand(command = "homes")
    public void onHomes(Player p, String[] args) {
        String homeList = ArrayUtils.joinValues(HomeController.getHomes(p).keySet(), ", ");
        p.sendMessage(Text.createMsg("&2Homes: &a%s", Text.stripLastChar(homeList)));
    }

    @RegisterAsCommand(command = "tpall <permission?>")
    public void onTpAll(Player p, String[] args) {
        String permission = args.length > 0 && args[0] != null ? args[0] : "";
        Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission(permission)).forEach(player -> player.teleport(p.getLocation()));
    }
}
