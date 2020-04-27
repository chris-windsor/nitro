package thesugarchris.nitro.commands;

import org.bukkit.entity.Player;
import thesugarchris.nitro.utils.ChatController;
import thesugarchris.nitro.utils.RegisterAsCommand;
import thesugarchris.nitro.utils.Text;

public class Nickname {
    @RegisterAsCommand(command = "/nick", parameters = 1)
    public void onNick(Player p, String[] args) {
        ChatController.setNickname(p, args[0]);
        int nickLength = Text.getLengthWithoutColors(args[0]);
        if (nickLength > 24) {
            p.sendMessage(Text.createMsg("&cSorry but the max length of nicknames is 24"));
            return;
        }
        p.sendMessage("Your nickname has been changed to: " + Text.createMsg(args[0]));
    }
}
