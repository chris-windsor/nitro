package thesugarchris.nitro.commands;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import thesugarchris.nitro.Nitro;
import thesugarchris.nitro.controllers.ChatController;
import thesugarchris.nitro.controllers.EconomyController;
import thesugarchris.nitro.utils.ItemBuilder;
import thesugarchris.nitro.utils.RegisterAsCommand;
import thesugarchris.nitro.utils.Text;

import java.util.HashMap;
import java.util.UUID;

public class Eco {
    @RegisterAsCommand(command = "balance <player?>")
    public void onBalance(Player p, String[] args) {
        Player playerToCheck = p;
        if (args.length == 1) {
            if (!p.hasPermission("eco.balance.others")) {
                p.sendMessage(Text.noPermMessage("check the balance of other players"));
                return;
            }
            playerToCheck = Nitro.getPlugin().getServer().getPlayer(args[0]);
            if (playerToCheck == null) {
                p.sendMessage(Text.createMsg("&cPlayer is offline"));
                return;
            }
        }
        Double playerBalance = EconomyController.getPlayerBalance(playerToCheck);
        p.sendMessage(Text.createMsg("&aYour current balance is: &2$%,.0f", playerBalance));
    }

    @RegisterAsCommand(command = "balancetop")
    public void onBalancetop(Player p, String[] args) {
        HashMap<UUID, Double> topBalances = EconomyController.getTopBalances();
        p.sendMessage(Text.createMsg("&6Top balances:"));
        topBalances.forEach((pUuid, balance) -> {
            p.sendMessage(Text.createMsg("&7%s &8-> &2$%s", ChatController.getNickname(pUuid), Text.formatMoney(balance)));
        });
    }

    @RegisterAsCommand(command = "eco set <player> <amount>", permission = "eco.set")
    public void onEcoSet(Player p, String[] args) {
        Player targetPlayer = Nitro.getPlugin().getServer().getPlayer(args[0]);
        if (targetPlayer == null) {
            p.sendMessage(Text.createMsg("&cCannot change balance of offline player"));
            return;
        }

        Double amount = Double.valueOf(args[1]);

        EconomyController.setPlayerBalance(targetPlayer, amount);
        p.sendMessage(Text.createMsg("&aSet %s&a balance to: &2$%,.0f", targetPlayer.getDisplayName(), amount));
    }

    @RegisterAsCommand(command = "eco add <player> <amount>", permission = "eco.set")
    public void onEcoAdd(Player p, String[] args) {
        Player targetPlayer = Nitro.getPlugin().getServer().getPlayer(args[0]);
        if (targetPlayer == null) {
            p.sendMessage(Text.createMsg("&cCannot change balance of offline player"));
            return;
        }

        Double amount = Double.valueOf(args[1]);

        EconomyController.addToPlayerBalance(targetPlayer, amount);
        p.sendMessage(Text.createMsg("&aAdded &2$%,.0f &ato %s&a balance", amount, targetPlayer.getDisplayName()));
    }

    @RegisterAsCommand(command = "eco take <player> <amount>", permission = "eco.set")
    public void onEcoTake(Player p, String[] args) {
        Player targetPlayer = Nitro.getPlugin().getServer().getPlayer(args[0]);
        if (targetPlayer == null) {
            p.sendMessage(Text.createMsg("&cCannot change balance of offline player"));
            return;
        }

        Double amount = Double.valueOf(args[1]);

        EconomyController.removeFromPlayerBalance(targetPlayer, amount);
        p.sendMessage(Text.createMsg("&aTook &2$%,.0f &afrom %s&a balance", amount, targetPlayer.getDisplayName()));
    }

    @RegisterAsCommand(command = "pay <player> <amount>")
    public void onPay(Player p, String[] args) {
        Player targetPlayer = Nitro.getPlugin().getServer().getPlayer(args[0]);
        if (targetPlayer == null) {
            p.sendMessage(Text.createMsg("&cCannot send money to offline player"));
            return;
        }

        Double amount = Double.valueOf(args[1]);

        if (EconomyController.playerHasAmount(p, amount)) {
            EconomyController.removeFromPlayerBalance(p, amount);
            EconomyController.addToPlayerBalance(targetPlayer, amount);
            p.sendMessage(Text.createMsg("&aSuccessfully sent &2$%,.0f &ato %s", amount, targetPlayer.getDisplayName()));
        } else {
            p.sendMessage(Text.createMsg("&cInsufficient funds in your account to pay %s&c $%,.0f", targetPlayer.getDisplayName(), amount));
        }
    }

    @RegisterAsCommand(command = "withdraw <amount>")
    public void onWithdraw(Player p, String[] args) {
        Double amount = Double.valueOf(args[0]);

        if (amount < 1) {
            p.sendMessage(Text.createMsg("&cCannot withdraw a banknote with that value"));
            return;
        }

        if (EconomyController.playerHasAmount(p, amount)) {
            if (p.getInventory().firstEmpty() != -1) {
                EconomyController.removeFromPlayerBalance(p, amount);

                String banknoteId = UUID.randomUUID().toString();
                ItemStack bankNote = new ItemBuilder(Material.PAPER)
                        .setName(Text.createMsg("&aBanknote (&2&o$%,.0f&a)", amount))
                        .addLore(Text.createMsg("&7%s", banknoteId))
                        .toItemStack();
                p.getInventory().addItem(bankNote);
                EconomyController.addBanknote(banknoteId, amount);

                p.sendMessage(Text.createMsg("&aAdded banknote with a value of &2$%,.0f &ato your inventory", amount));
            } else {
                p.sendMessage(Text.createMsg("&cCannot add banknote to your inventory because it is full"));
            }
        } else {
            p.sendMessage(Text.createMsg("&cInsufficient funds in your account to withdraw $%,.0f", amount));
        }
    }
}
