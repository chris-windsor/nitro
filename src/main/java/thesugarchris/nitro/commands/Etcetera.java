package thesugarchris.nitro.commands;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import thesugarchris.nitro.utils.RegisterAsCommand;
import thesugarchris.nitro.utils.Text;

import java.util.ArrayList;

public class Etcetera {
    @RegisterAsCommand(command = "top", permission = "etc.top")
    public void onTop(Player p, String[] args) {
        Block lookingAtBlock = p.getTargetBlockExact(64);
        if (lookingAtBlock == null) {
            p.sendMessage(Text.createMsg("&cLocation is too far away"));
            return;
        }
        Location newLoc = new Location(p.getWorld(), lookingAtBlock.getX(), p.getWorld().getHighestBlockYAt(lookingAtBlock.getLocation()) + 1, lookingAtBlock.getZ());
        p.teleport(newLoc);
        p.sendMessage(Text.createMsg("&eWoooosh"));
    }

    @RegisterAsCommand(command = "hat <entity?>", permission = "etc.hat")
    public void onHat(Player p, String[] args) {
        if (args.length == 0) {
            ArrayList<Entity> entities = new ArrayList<>();
            entities.add(p);
            entities.addAll(p.getNearbyEntities(2, 2, 2));
            for (int i = 0; i < entities.size() - 1; i++) {
                entities.get(i).addPassenger(entities.get(i + 1));
            }
            return;
        }

        if (args[0].equals("none")) {
            p.getPassengers().forEach(entity -> {
                if (!entity.getType().equals(EntityType.PLAYER)) {
                    entity.remove();
                } else {
                    p.eject();
                }
            });
        } else {
            Entity newEntity = p.getWorld().spawnEntity(p.getLocation(), EntityType.valueOf(args[0].toUpperCase()));
            newEntity.setSilent(true);
            newEntity.setInvulnerable(true);
            p.addPassenger(newEntity);
        }
    }

    @RegisterAsCommand(command = "craft")
    public void onCraft(Player p, String[] args) {
        p.openWorkbench(null, true);
    }

    @RegisterAsCommand(command = "echest")
    public void onEChest(Player p, String[] args) {
        p.openInventory(p.getEnderChest());
    }

    @RegisterAsCommand(command = "nuke <radius?>")
    public void onNuke(Player p, String[] args) {
        int radius = 10;
        if (args.length > 0) {
            radius = Integer.parseInt(args[0]);
            if (radius > 15) {
                p.sendMessage(Text.createMsg("&cMax nuke radius is 15"));
                return;
            }
        }
        Location pLoc = p.getLocation();
        int centerX = pLoc.getBlockX();
        int centerZ = pLoc.getBlockZ();
        for (int x = centerX - radius * 4; x < centerX + radius * 4; x += 4) {
            for (int z = centerZ - radius * 4; z < centerZ + radius * 4; z += 4) {
                Location tntLoc = new Location(p.getWorld(), x, p.getLocation().getY() + 4, z);
                p.getWorld().spawn(tntLoc, TNTPrimed.class);
            }
        }
        p.sendMessage(Text.createMsg("&cWatch out. Nuke incoming"));
    }

    @RegisterAsCommand(command = "flyspeed <speed>")
    public void onFlySpeed(Player p, String[] args) {
        p.setFlySpeed(Float.parseFloat(args[0]) / 10);
        p.sendMessage(Text.createMsg("&aSet flyspeed to: &2%s", args[0]));
    }
}
