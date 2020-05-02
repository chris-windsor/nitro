package thesugarchris.nitro.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import thesugarchris.nitro.controllers.ModController;
import thesugarchris.nitro.utils.RegisterAsCommand;
import thesugarchris.nitro.utils.Text;

public class Mod {
    @RegisterAsCommand(command = "mute <player> <reason?> <time?>", permission = "mod.mute")
    public void onMute(Player p, String[] args) {
        Player targetPlayer = Bukkit.getPlayer(args[0]);
        if (targetPlayer == null) {
            p.sendMessage(Text.createMsg("&cCannot mute an offline player"));
            return;
        }
        if (ModController.isPlayerMuted(targetPlayer)) {
            p.sendMessage(Text.createMsg("&cPlayer is already muted"));
        } else {
            ModController.addMutedPlayer(targetPlayer);
            p.sendMessage(Text.createMsg("&aMuted player for %s", args[2]));
            targetPlayer.sendMessage(Text.createMsg("&aYou have been muted for %s", args[2]));
        }
    }

    @RegisterAsCommand(command = "unmute <player>", permission = "mod.mute")
    public void onUnmute(Player p, String[] args) {
        Player targetPlayer = Bukkit.getPlayer(args[0]);
        if (targetPlayer == null) {
            p.sendMessage(Text.createMsg("&cCannot mute an offline player"));
            return;
        }
        if (ModController.isPlayerMuted(targetPlayer)) {
            ModController.removeMutedPlayer(targetPlayer);
            p.sendMessage(Text.createMsg("&aUnmuted player"));
            targetPlayer.sendMessage(Text.createMsg("&aYou have been unmuted"));
        } else {
            p.sendMessage(Text.createMsg("&cPlayer is not currently muted"));
        }
    }

    @RegisterAsCommand(command = "kick <player> <reason?>", permission = "mod.kick")
    public void onKick(Player p, String[] args) {
        Player targetPlayer = Bukkit.getPlayer(args[0]);
        if (targetPlayer == null) {
            p.sendMessage(Text.createMsg("&cCannot kick an offline player"));
            return;
        }
        targetPlayer.kickPlayer(args[1]);
        p.sendMessage(Text.createMsg("&aKicked %s", targetPlayer.getDisplayName()));
    }

    @RegisterAsCommand(command = "ban <player> <reason?> <time?>", permission = "mod.ban")
    public void onBan(Player p, String[] args) {
        Player targetPlayer = Bukkit.getPlayer(args[0]);
        if (targetPlayer == null) {
            p.sendMessage(Text.createMsg("&cCannot ban an offline player"));
            return;
        }
    }
}
