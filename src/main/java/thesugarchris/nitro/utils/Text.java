package thesugarchris.nitro.utils;

import org.bukkit.ChatColor;

public class Text {
    public static String createMsg(String s, Object... args) {
        return String.format(ChatColor.translateAlternateColorCodes('&', s), args);
    }

    public static int getLengthWithoutColors(String s) {
        return s.replaceAll("&\\w", "").length();
    }
}
