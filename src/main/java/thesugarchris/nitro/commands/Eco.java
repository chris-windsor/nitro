package thesugarchris.nitro.commands;

import org.bukkit.entity.Player;
import thesugarchris.nitro.Nitro;
import thesugarchris.nitro.utils.Economy;
import thesugarchris.nitro.utils.RegisterAsCommand;
import thesugarchris.nitro.utils.Text;

public class Eco {
    @RegisterAsCommand(command = "/balance")
    public static void onBalance(Player p) {
        Double playerBalance = Economy.getPlayerBalance(p);
        p.sendMessage(Text.createMsg("&aYour current balance is: &2$%,.0f", playerBalance));
    }

    @RegisterAsCommand(command = "/eco set", parameters = 2)
    public static void onEcoSet(Player p, String[] args) {
        Player targetPlayer = Nitro.getPlugin().getServer().getPlayer(args[0]);
        if (targetPlayer == null) {
            p.sendMessage(Text.createMsg("&cCannot send money to offline player"));
        }

        Double amount = Double.valueOf(args[1]);

        Economy.setPlayerBalance(targetPlayer, amount);
        p.sendMessage(Text.createMsg("&aSet %s&a balance to: &2$%,.0f", targetPlayer.getDisplayName(), amount));
    }

    @RegisterAsCommand(command = "/pay", parameters = 2)
    public static void onPay(Player p, String[] args) {
        Player targetPlayer = Nitro.getPlugin().getServer().getPlayer(args[0]);
        if (targetPlayer == null) {
            p.sendMessage(Text.createMsg("&cCannot send money to offline player"));
        }

        Double amount = Double.valueOf(args[1]);

        if (Economy.playerHasAmount(p, amount)) {
            Economy.removeFromPlayerBalance(p, amount);
            Economy.addToPlayerBalance(targetPlayer, amount);
            p.sendMessage(Text.createMsg("&aSuccessfully sent &2$%,.0f &ato %s", amount, targetPlayer.getDisplayName()));
        } else {
            p.sendMessage(Text.createMsg("&cInsufficient funds in your account to pay %s&c $%,.0f", targetPlayer.getDisplayName(), amount));
        }
    }
}
