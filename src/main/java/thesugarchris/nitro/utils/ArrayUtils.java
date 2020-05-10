package thesugarchris.nitro.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class ArrayUtils {
    public static String joinValues(String[] strings, String glue) {
        String fmtString = Arrays.stream(strings).reduce("", (a, b) -> a + b + glue);
        return strings.length > 1 ? fmtString.substring(0, fmtString.length() - 2) : fmtString;
    }

    public static String joinValues(Set<String> strings, String glue) {
        return joinValues(strings.toArray(String[]::new), glue);
    }

    public static String joinValues(List<String> strings, String glue) {
        return joinValues(strings.toArray(String[]::new), glue);
    }
}
