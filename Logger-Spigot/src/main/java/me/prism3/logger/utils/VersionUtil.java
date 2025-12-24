package me.prism3.logger.utils;

import me.prism3.logger.utils.enums.NmsVersions;
import org.bukkit.Bukkit;


/**
 * Parses your server’s CraftBukkit package suffix into an MCVersions
 * and exposes it everywhere as a constant.
 */
public final class VersionUtil {

    /**
     * The detected server version, e.g. v1_12_R1, or the closest matching enum.
     */
    public static final NmsVersions SERVER_VERSION;

    static {
        String pkg = Bukkit.getServer().getClass().getPackage().getName();        // e.g. org.bukkit.craftbukkit.v1_16_R3
        String suffix = pkg.substring(pkg.lastIndexOf('.') + 1);                // e.g. v1_16_R3
        NmsVersions ver;
        try {
            ver = NmsVersions.valueOf(suffix);
        } catch (IllegalArgumentException ex) {
            // if Mojang or CraftBukkit ever adds new mappings you don’t know about,
            // just fall back to the latest you have in your enum:
            ver = NmsVersions.values()[NmsVersions.values().length - 1];
        }
        SERVER_VERSION = ver;
    }

    private VersionUtil() { /* no instances */ }

    /** True if running on 1.13 or newer */
    public static boolean isModern() {
        return SERVER_VERSION.isAtLeast(NmsVersions.v1_13_R1);
    }

    /** True if pre‑1.13 behavior (legacy) */
//    public static boolean isLegacy() {
//        return SERVER_VERSION.isLegacyVersion();
//    }
}
