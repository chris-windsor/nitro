package thesugarchris.nitro.utils;

import org.bukkit.ChatColor;

public class Text {
    public static String createMsg(String s, Object... args) {
        return ChatColor.translateAlternateColorCodes('&', String.format(s, args));
    }

    public static int getLengthWithoutColors(String s) {
        return s.replaceAll("(?i)&[0-9A-FK-OR]", "").length();
    }

    public static String noPermMessage(String s, Object... args) {
        return createMsg("&cInsufficient permissions to " + s, args);
    }
}
