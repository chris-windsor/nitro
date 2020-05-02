package thesugarchris.nitro.commands;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
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
}
