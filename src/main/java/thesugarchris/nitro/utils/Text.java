package thesugarchris.nitro.utils;

import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.Set;

public class Text {
    public static String createMsg(String s, Object... args) {
        return ChatColor.translateAlternateColorCodes('&', String.format(s, args));
    }

    public static int getLengthWithoutColors(String s) {
        return s.replaceAll("&\\w", "").length();
    }

    public static String stringFromArray(String[] strings) {
        return Arrays.stream(strings).reduce("", (a, b) -> a + b + ", ");
    }

    public static String stringFromArray(Set<String> strings) {
        return stringFromArray(strings.stream().toArray(String[]::new));
    }
}
