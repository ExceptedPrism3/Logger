package me.prism3.logger.utils;

import me.prism3.logger.hooks.FloodGateUtil;
import org.geysermc.floodgate.api.FloodgateApi;

import java.util.UUID;

public class BedrockChecker {

    private BedrockChecker() {}

    private static final BedrockChecker INSTANCE = new BedrockChecker();
    private static final boolean FLOODGATE_API_ENABLED = FloodGateUtil.getFloodGateAPI();

    public static BedrockChecker getInstance() { return INSTANCE; }

    public static boolean isBedrock(UUID playerUUID) {
        if (!FLOODGATE_API_ENABLED) return false;
        return FloodgateApi.getInstance().isFloodgatePlayer(playerUUID);
    }
}
