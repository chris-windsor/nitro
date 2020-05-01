package thesugarchris.nitro.utils;

import org.bukkit.ChatColor;

import java.util.TreeMap;

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

    private final static TreeMap<Double, String> moneyConversionMap = new TreeMap<>() {{
        put(1_000_000_000_000_000d, "Q");
        put(1_000_000_000_000d, "T");
        put(1_000_000_000d, "B");
        put(1_000_000d, "M");
    }};

    public static String formatMoney(Double amount) {
        if (amount < 1_000_000) {
            return String.format("%,.0f", amount);
        } else {
            Double floorVal = moneyConversionMap.floorKey(amount);
            String fmtSymbol = moneyConversionMap.get(floorVal);
            return String.format("%.2f%s", amount / floorVal, fmtSymbol);
        }
    }
}
