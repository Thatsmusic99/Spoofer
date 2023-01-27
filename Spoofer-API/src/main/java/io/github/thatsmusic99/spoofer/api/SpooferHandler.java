package io.github.thatsmusic99.spoofer.api;

public class SpooferHandler {

    private static SpooferAPI api;

    public static void setInstance(SpooferAPI api) {
        SpooferHandler.api = api;
    }

    public static SpooferAPI getAPI() {
        return api;
    }
}
