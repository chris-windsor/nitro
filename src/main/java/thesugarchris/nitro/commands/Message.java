package thesugarchris.nitro.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import thesugarchris.nitro.controllers.ChatController;
import thesugarchris.nitro.utils.ArrayUtils;
import thesugarchris.nitro.utils.RegisterAsCommand;
import thesugarchris.nitro.utils.Text;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class Message {
    HashMap<UUID, UUID> lastMsgd = new HashMap<>();

    @RegisterAsCommand(command = "msg <player> <*>", forcedMin = 2, allowManyParams = true)
    public void onMsg(Player p, String[] args) {
        if (Bukkit.getPlayer(args[0]) == null) {
            p.sendMessage(Text.createMsg("&cPlayer is not online"));
            return;
        }

        String msg = ArrayUtils.joinValues(Arrays.asList(args).subList(1, args.length), " ");
        sendPM(p, Bukkit.getPlayer(args[0]), msg);
    }

    @RegisterAsCommand(command = "r <*>", forcedMin = 1, allowManyParams = true)
    public void onReply(Player p, String[] args) {
        String msg = ArrayUtils.joinValues(Arrays.asList(args), " ");
        UUID prevMsgUUID = lastMsgd.get(p.getUniqueId());
        if (prevMsgUUID != null) {
            if (Bukkit.getPlayer(prevMsgUUID) == null) {
                p.sendMessage(Text.createMsg("&cPlayer is not online"));
            } else {
                sendPM(p, Bukkit.getPlayer(prevMsgUUID), msg);
            }
        } else {
            p.sendMessage(Text.createMsg("&cYou have not been messaging anyone"));
        }
    }

    public void sendPM(Player sender, Player receiver, String msg) {
        if (receiver == null) {
            sender.sendMessage(Text.createMsg("&cCannot message offline player"));
        }

        sender.sendMessage(Text.createMsg("&6[&eme &7&l-> &r%s&6]&r %s", ChatController.getNickname(receiver), msg));
        receiver.sendMessage(Text.createMsg("&6[&r%s &7&l-> &eme&6]&r %s", ChatController.getNickname(sender), msg));

        if (lastMsgd.containsKey(sender.getUniqueId())) lastMsgd.replace(sender.getUniqueId(), receiver.getUniqueId());
        else lastMsgd.put(sender.getUniqueId(), receiver.getUniqueId());

        if (lastMsgd.containsKey(receiver.getUniqueId()))
            lastMsgd.replace(receiver.getUniqueId(), sender.getUniqueId());
        else lastMsgd.put(receiver.getUniqueId(), sender.getUniqueId());
    }
}
