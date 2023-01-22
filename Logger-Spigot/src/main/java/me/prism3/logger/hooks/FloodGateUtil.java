package me.prism3.logger.hooks;

public class FloodGateUtil {

    private FloodGateUtil() {}

    public static boolean getFloodGateAPI() {

        try {

            Class.forName("org.geysermc.floodgate.api.FloodgateApi").getMethod("getInstance");

            return true;
        } catch (final Exception ignored) { return false; }
    }
}