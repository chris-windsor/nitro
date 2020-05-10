package thesugarchris.nitro.controllers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import thesugarchris.nitro.Nitro;
import thesugarchris.nitro.utils.ArrayUtils;
import thesugarchris.nitro.utils.LocationUtils;
import thesugarchris.nitro.utils.Text;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class HologramController {
    private static final HashMap<String, Hologram> holograms = new HashMap<>();

    final static class Hologram {
        public String id;
        public Location location;
        public List<String> lines;
        public List<ArmorStand> armorStands = new ArrayList<>();;

        public Hologram(String id, Location location, List<String> lines) {
            this.id = id;
            this.location = location;
            this.lines = lines;
        }
    }

    public static void loadHolograms() {
        holograms.values().forEach(hg -> hg.armorStands.forEach(Entity::remove));
        holograms.clear();

        ResultSet hologramsResult = Nitro.getDatabase().query("SELECT * FROM holograms");

        while (true) {
            try {
                if (!hologramsResult.next()) break;

                String id = hologramsResult.getString("id");
                String text = hologramsResult.getString("text");
                List<String> lines = new ArrayList<>();
                if (text != null) {
                    lines.addAll(Arrays.asList(text.split("\n")));
                }
                Location location = LocationUtils.stringToLocation(hologramsResult.getString("location"));

                Hologram newHg = new Hologram(id, location, lines);

                holograms.put(id, newHg);
                refreshHologram(newHg);
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }

    private static void saveHologram(String id, Hologram hg) {
        String location = LocationUtils.locationToString(hg.location);
        String text = ArrayUtils.joinValues(hg.lines, "\n");
        holograms.replace(id, hg);
        refreshHologram(hg);
        Nitro.getDatabase().update(String.format("UPDATE holograms SET location = '%s', text = '%s' WHERE id = '%s'", location, text, id));
    }

    private static void refreshHologram(Hologram hg) {
        hg.armorStands.forEach(Entity::remove);
        hg.armorStands.clear();

        World mainWorld = Bukkit.getWorld("world");
        Location hgLoc = hg.location;
        Double[] pos = new Double[]{0d};
        hg.lines.forEach(line -> {
            Nitro.log(pos[0] + "");
            ArmorStand newArmorstand = mainWorld.spawn(hgLoc, ArmorStand.class);
            newArmorstand.setCustomName(Text.createMsg(line));
            newArmorstand.setCustomNameVisible(true);
            newArmorstand.setVisible(false);
            newArmorstand.setGravity(false);
            newArmorstand.setCollidable(false);
            pos[0] += 0.25d;
            newArmorstand.teleport(hgLoc.subtract(new Vector(0, pos[0], 0)));
            hg.armorStands.add(newArmorstand);
        });
    }

    public static void create(String id, Location loc) {
        Hologram newHg = new Hologram(id, loc, new ArrayList<>());
        holograms.put(id, newHg);
        String locAsString = LocationUtils.locationToString(loc);
        Nitro.getDatabase().update(String.format("INSERT INTO holograms (id, location) VALUES ('%s', '%s')", id, locAsString));
    }

    public static void addLine(String id, int line, boolean insert, String text) {
        Hologram hg = holograms.get(id);

        if (line == -1) {
            hg.lines.add(text);
        } else {
            if (insert) {
                hg.lines.add(line, text);
            } else {
                hg.lines.set(line, text);
            }
        }

        saveHologram(id, hg);
    }

    public static void removeLine(String id, int line) {
        Hologram hg = holograms.get(id);
        hg.lines.remove(line);
        saveHologram(id, hg);
    }

    public static void moveHologram(String id, Location newLoc) {
        Hologram hg = holograms.get(id);
        hg.location = newLoc;
        saveHologram(id, hg);
    }

    public static void delete(String id) {
        holograms.get(id).armorStands.forEach(Entity::remove);;
        Nitro.getDatabase().update(String.format("DELETE FROM holograms WHERE id = '%s'", id));
    }
}
