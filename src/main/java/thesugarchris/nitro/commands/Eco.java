package thesugarchris.nitro.commands;

import org.bukkit.entity.Player;
import thesugarchris.nitro.Nitro;
import thesugarchris.nitro.utils.Economy;
import thesugarchris.nitro.utils.RegisterAsCommand;
import thesugarchris.nitro.utils.Text;

public class Eco {
    @RegisterAsCommand(command = "balance <player?>")
    public void onBalance(Player p, String[] args) {
        Player playerToCheck = p;
        if (args.length == 1) {
            if (!p.hasPermission("eco.balance.others")) {
                p.sendMessage(Text.noPermMessage("check the balance of other players."));
                return;
            }
            playerToCheck = Nitro.getPlugin().getServer().getPlayer(args[0]);
            if (playerToCheck == null) {
                p.sendMessage(Text.createMsg("&cPlayer is offline"));
                return;
            }
        }
        Double playerBalance = Economy.getPlayerBalance(playerToCheck);
        p.sendMessage(Text.createMsg("&aYour current balance is: &2$%,.0f", playerBalance));
    }

    @RegisterAsCommand(command = "eco set <player> <value>")
    public void onEcoSet(Player p, String[] args) {
        Player targetPlayer = Nitro.getPlugin().getServer().getPlayer(args[0]);
        if (targetPlayer == null) {
            p.sendMessage(Text.createMsg("&cCannot send money to offline player"));
            return;
        }

        Double amount = Double.valueOf(args[1]);

        Economy.setPlayerBalance(targetPlayer, amount);
        p.sendMessage(Text.createMsg("&aSet %s&a balance to: &2$%,.0f", targetPlayer.getDisplayName(), amount));
    }

    @RegisterAsCommand(command = "pay <player> <amount>")
    public void onPay(Player p, String[] args) {
        Player targetPlayer = Nitro.getPlugin().getServer().getPlayer(args[0]);
        if (targetPlayer == null) {
            p.sendMessage(Text.createMsg("&cCannot send money to offline player"));
            return;
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
