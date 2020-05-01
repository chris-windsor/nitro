package thesugarchris.nitro.commands;

import org.bukkit.entity.Player;
import thesugarchris.nitro.utils.RegisterAsCommand;

public class Mod {
    @RegisterAsCommand(command = "mute <player> <reason?> <time?>", permission = "mod.mute")
    public void onMute(Player p, String[] args) {

    }

    @RegisterAsCommand(command = "kick <player> <reason?>", permission = "mod.kick")
    public void onKick(Player p, String[] args) {

    }

    @RegisterAsCommand(command = "ban <player> <reason?> <time?>", permission = "mod.ban")
    public void onBan(Player p, String[] args) {

    }
}
