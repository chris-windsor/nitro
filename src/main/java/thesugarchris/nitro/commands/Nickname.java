package thesugarchris.nitro.commands;

import org.bukkit.entity.Player;
import thesugarchris.nitro.utils.ChatController;
import thesugarchris.nitro.utils.RegisterAsCommand;
import thesugarchris.nitro.utils.Text;

public class Nickname {
    @RegisterAsCommand(command = "nick <player?|nickname> <nickname>")
    public void onNick(Player p, String[] args) {
        String nickname = args[0];

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

        ChatController.setNickname(p, nickname);
        p.sendMessage(Text.createMsg("&aYour nickname has been changed to:&r %s", nickname));
    }
}
