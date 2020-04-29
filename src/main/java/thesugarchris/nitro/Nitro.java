package thesugarchris.nitro;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import thesugarchris.nitro.commands.Eco;
import thesugarchris.nitro.commands.Mod;
import thesugarchris.nitro.commands.Nickname;
import thesugarchris.nitro.events.Chat;
import thesugarchris.nitro.events.Join;
import thesugarchris.nitro.events.Leave;
import thesugarchris.nitro.events.Prejoin;
import thesugarchris.nitro.utils.*;

import java.sql.SQLException;

public final class Nitro extends JavaPlugin {
    private static Plugin plugin;
    private static Database database = null;

    public static Plugin getPlugin() {
        return plugin;
    }

    public static Server getSrv() {
        return getPlugin().getServer();
    }

    public static Database getDatabase() {
        return database;
    }

    public static void log(String msg) {
        Bukkit.getConsoleSender().sendMessage("[Nitro] " + msg);
    }

    @Override
    public void onEnable() {
        plugin = this;

        try {
            database = new Database();
        } catch (SQLException | ClassNotFoundException exception) {
            exception.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }

        CommandRegistry commandRegistry = new CommandRegistry(this);
        commandRegistry.registerCommands(new Eco());
        commandRegistry.registerCommands(new Mod());
        commandRegistry.registerCommands(new Nickname());

        commandRegistry.getRegisteredCommands();

        try {
            Economy.loadBalances();
            ChatController.loadNicknames();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        TabListController.updateAllPlayers();
        ScoreboardController.updateAllPlayers();

        this.getServer().getPluginManager().registerEvents(new Prejoin(), this);
        this.getServer().getPluginManager().registerEvents(new Join(), this);
        this.getServer().getPluginManager().registerEvents(new Leave(), this);
        this.getServer().getPluginManager().registerEvents(new Chat(), this);

        // TODO: only update players when necessary, this is big waste of resources
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, ScoreboardController::updateAllPlayers, 0L, 20L);
    }

    @Override
    public void onDisable() {
        database.closeConnection();

        plugin = null;
    }
}
