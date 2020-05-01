package thesugarchris.nitro.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import thesugarchris.nitro.controllers.ChatController;
import thesugarchris.nitro.utils.RegisterAsCommand;
import thesugarchris.nitro.utils.Text;

public class Nickname {
    @RegisterAsCommand(command = "nick <player?|nickname> <nickname>", permission = "chat.nick")
    public void onNick(Player p, String[] args) {
        String nickname = args[0];
        Player targetPlayer = p;

        if (args.length == 2) {
            if (!p.hasPermission("chat.nick.others")) {
                p.sendMessage(Text.noPermMessage("change the nickname of other players"));
                return;
            }
            nickname = args[1];
            targetPlayer = Bukkit.getPlayer(args[0]);
            if (targetPlayer == null) {
                p.sendMessage(Text.createMsg("&cCannot change nickname of offline player"));
            }
        }

        if (!p.hasPermission("chat.usecolors")) {
            nickname = nickname.replaceAll("(?i)&[0-9A-FK-OR]", "");
        }

        if (p.hasPermission("chat.usecolors") && !p.hasPermission("chat.usedecoration")) {
            nickname = nickname.replaceAll("(?i)&[K-O]", "");
        }

        int nickLength = Text.getLengthWithoutColors(nickname);
        int maxNickLength = 24;

        if (nickLength > maxNickLength) {
            p.sendMessage(Text.createMsg("&cSorry but the max length of nicknames is %d", maxNickLength));
            return;
        }

        ChatController.setNickname(targetPlayer, nickname);
        targetPlayer.sendMessage(Text.createMsg("&aYour nickname has been changed to:&r %s", nickname));
        if (args.length == 2) {
            p.sendMessage(Text.createMsg("&aChanged %s &anickname to:&r %s", targetPlayer.getDisplayName(), nickname));
        }
    }
}
