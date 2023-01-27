package io.github.thatsmusic99.spoofer;

import java.util.Arrays;

public class Utilities {

    public static String getStringFromArray(String[] args, int start) {
        String[] neededParts = Arrays.copyOfRange(args, start, args.length);
        return String.join(" ", neededParts);
    }
}
