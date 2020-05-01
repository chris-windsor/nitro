package thesugarchris.nitro.controllers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import thesugarchris.nitro.Nitro;
import thesugarchris.nitro.utils.Text;

public class ScoreboardController {
    public static void updatePlayer(Player p) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = scoreboard.registerNewObjective("serverscoreboard", "dummy", Text.createMsg("&e&lNitro"));
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        Score line1 = obj.getScore(Text.createMsg("&7» Welcome"));
        line1.setScore(15);

        Score line2 = obj.getScore(ChatColor.WHITE + "" + ChatColor.YELLOW);
        line2.setScore(14);

        Team moneyCounter = scoreboard.registerNewTeam("moneyCounter");
        moneyCounter.addEntry(ChatColor.RED + "" + ChatColor.WHITE);
        moneyCounter.setPrefix(Text.createMsg("&7» &a$%s", Text.formatMoney(EconomyController.getPlayerBalance(p))));
        obj.getScore(ChatColor.RED + "" + ChatColor.WHITE).setScore(13);

        p.setScoreboard(scoreboard);
    }

    public static void updateAllPlayers() {
        Nitro.getPlugin().getServer().getOnlinePlayers().forEach(ScoreboardController::updatePlayer);
    }
}
