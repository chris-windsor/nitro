package thesugarchris.nitro;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import thesugarchris.nitro.commands.*;
import thesugarchris.nitro.controllers.HologramController;
import thesugarchris.nitro.controllers.PlayerDataController;
import thesugarchris.nitro.controllers.ScoreboardController;
import thesugarchris.nitro.controllers.TabListController;
import thesugarchris.nitro.events.ChatEvents;
import thesugarchris.nitro.events.ConnectionEvents;
import thesugarchris.nitro.events.InteractionEvents;
import thesugarchris.nitro.utils.CommandRegistry;
import thesugarchris.nitro.utils.Database;

import java.sql.SQLException;

public final class Nitro extends JavaPlugin {
    private static Plugin plugin;
    private static Database database = null;

    public static Plugin getPlugin() {
        return plugin;
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
        commandRegistry.registerCommands(new Dev());
        commandRegistry.registerCommands(new Eco());
        commandRegistry.registerCommands(new Etcetera());
        commandRegistry.registerCommands(new Mod());
        commandRegistry.registerCommands(new Nickname());
        commandRegistry.registerCommands(new Teleport());

        PlayerDataController.loadData();
        TabListController.updateAllPlayers();
        ScoreboardController.updateAllPlayers();
        HologramController.loadHolograms();

        getServer().getPluginManager().registerEvents(new ChatEvents(), this);
        getServer().getPluginManager().registerEvents(new InteractionEvents(), this);
        getServer().getPluginManager().registerEvents(new ConnectionEvents(), this);

        // TODO: only update players when necessary, this is big waste of resources
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, ScoreboardController::updateAllPlayers, 0L, 20L);
    }

    @Override
    public void onDisable() {
        database.closeConnection();

        plugin = null;
    }
}
