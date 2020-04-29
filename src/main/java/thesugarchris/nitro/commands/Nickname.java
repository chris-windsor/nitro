package thesugarchris.nitro.commands;

import org.bukkit.entity.Player;
import thesugarchris.nitro.utils.ChatController;
import thesugarchris.nitro.utils.RegisterAsCommand;
import thesugarchris.nitro.utils.Text;

public class Nickname {
    @RegisterAsCommand(command = "nick <nickname>")
    public void onNick(Player p, String[] args) {
        int nickLength = Text.getLengthWithoutColors(args[0]);
        int maxNickLength = 24;
        if (nickLength > maxNickLength) {
            p.sendMessage(Text.createMsg("&cSorry but the max length of nicknames is %d", maxNickLength));
            return;
        }
        ChatController.setNickname(p, args[0]);
        p.sendMessage(Text.createMsg("&aYour nickname has been changed to:&r %s", args[0]));
    }
}
