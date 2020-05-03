package thesugarchris.nitro.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {
    private ItemStack is;

    public ItemBuilder(Material m) {
        is = new ItemStack(m);
    }

    public ItemBuilder(Material m, int amount) {
        is = new ItemStack(m, amount);
    }

    public ItemBuilder(Material m, int amount, short durability) {
        is = new ItemStack(m, amount);
        ItemMeta meta = is.getItemMeta();
        if (meta != null) {
            ((Damageable) meta).setDamage(durability);
            is.setItemMeta(meta);
        }
    }

    public ItemBuilder setName(String name) {
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(name);
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder addLore(String line) {
        ItemMeta im = is.getItemMeta();
        List<String> lore = new ArrayList<>();

        if (im.hasLore()) {
            lore = im.getLore();
        }

        lore.add(line);
        im.setLore(lore);
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder setLoreLine(int idx, String line) {
        ItemMeta im = is.getItemMeta();
        List<String> lore = new ArrayList<>();

        if (im.hasLore()) {
            lore = im.getLore();
        }

        lore.set(idx, line);
        im.setLore(lore);
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        ItemMeta im = is.getItemMeta();
        im.setLore(lore);
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder removeLoreLine(int idx) {
        ItemMeta im = is.getItemMeta();
        List<String> lore = new ArrayList<>();

        if (im.hasLore()) {
            lore = im.getLore();
            if (idx < lore.size()) {
                lore.remove(idx);
            }
        }

        im.setLore(lore);
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder clearLore() {
        ItemMeta im = is.getItemMeta();
        List<String> lore = new ArrayList<>();
        im.setLore(lore);
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment ench, int level) {
        if (level > 5) {
            is.addUnsafeEnchantment(ench, level);
        } else {
            is.addEnchantment(ench, level);
        }
        return this;
    }

    private ItemBuilder addUnsafeEnchantment(Enchantment ench, int level) {
        is.addUnsafeEnchantment(ench, level);
        return this;
    }

    public ItemBuilder removeEnchantment(Enchantment ench) {
        is.removeEnchantment(ench);
        return this;
    }

    public ItemBuilder setSkullOwner(String owner) {
        SkullMeta im = (SkullMeta) is.getItemMeta();
        im.setOwningPlayer(Bukkit.getPlayerExact(owner));
        is.setItemMeta(im);
        return this;
    }

    public ItemStack toItemStack() {
        return is;
    }
}
