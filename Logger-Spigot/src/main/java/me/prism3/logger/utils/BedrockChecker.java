package me.prism3.logger.utils;

import me.prism3.logger.hooks.FloodGateUtil;
import org.geysermc.floodgate.api.FloodgateApi;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BedrockChecker {

    private BedrockChecker() {}

    private static final ConcurrentHashMap<UUID, Boolean> cache = new ConcurrentHashMap<>();

    private static final boolean FLOODGATE_API_ENABLED = FloodGateUtil.getFloodGateAPI();

    private static final FloodgateApi floodgateApi = FLOODGATE_API_ENABLED ? FloodgateApi.getInstance() : null;

    public static boolean isBedrock(UUID playerUUID) {
        if (!FLOODGATE_API_ENABLED || floodgateApi == null) return false;

        return cache.computeIfAbsent(playerUUID, floodgateApi::isFloodgatePlayer);
    }
}
