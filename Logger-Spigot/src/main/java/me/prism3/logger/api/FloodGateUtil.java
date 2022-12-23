package me.prism3.logger.api;

public class FloodGateUtil {

    private FloodGateUtil() {}

    public static boolean getFloodGateAPI() {

        try {

            Class.forName("org.geysermc.floodgate.api.FloodgateApi");

        } catch (Exception ignored) { return false; }

        return true;
    }
}