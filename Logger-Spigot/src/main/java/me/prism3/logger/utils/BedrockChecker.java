package me.prism3.logger.utils;

import me.prism3.logger.api.FloodGateUtil;
import org.geysermc.floodgate.api.FloodgateApi;

import java.util.UUID;

public class BedrockChecker {

    private BedrockChecker() {}

    public static boolean isBedrock(UUID playerUUID) {

        if (!FloodGateUtil.getFloodGateAPI()) return false;

        return FloodgateApi.getInstance().isFloodgatePlayer(playerUUID);
    }
}
