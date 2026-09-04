package me.prism3.logger.utils.enums;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import me.prism3.logger.utils.VersionUtil;

import java.util.HashMap;
import java.util.Map;


/**
 * Friendly names for all vanilla enchants in MC 1.21.5.
 * Includes a fast lookup map keyed by the enchantment's NamespacedKey or legacy name.
 */
public enum FriendlyEnchants {

    // ===== BOW =====
    ARROW_DAMAGE("Power"),
    ARROW_FIRE("Flame"),
    ARROW_INFINITE("Infinite"),
    ARROW_KNOCKBACK("Punch"),

    // ===== CROSSBOW =====
    MULTISHOT("Multishot"),
    PIERCING("Piercing"),
    QUICK_CHARGE("Quick Charge"),

    // ===== SWORD/AXE =====
    DAMAGE_ALL("Sharpness"),
    DAMAGE_UNDEAD("Smite"),
    DAMAGE_ARTHROPODS("Bane of Arthropods"),
    FIRE_ASPECT("Fire Aspect"),
    KNOCKBACK("Knockback"),
    LOOT_BONUS_MOBS("Looting"),
    SWEEPING_EDGE("Sweeping Edge"),
    CLEAVING("Cleaving"), // Axe‑only

    // ===== MACE (Tricky Trials) =====
    BREACH("Breach"),
    DENSITY("Density"),
    WIND_BURST("Wind Burst"),

    // ===== ARMOR =====
    PROTECTION_ENVIRONMENTAL("Protection"),
    PROTECTION_FALL("Feather Falling"),
    PROTECTION_FIRE("Fire Protection"),
    PROTECTION_EXPLOSIONS("Blast Protection"),
    PROTECTION_PROJECTILE("Projectile Protection"),
    THORNS("Thorns"),
    DEPTH_STRIDER("Depth Strider"),
    FROST_WALKER("Frost Walker"),
    OXYGEN("Respiration"),
    WATER_WORKER("Aqua Affinity"),
    SOUL_SPEED("Soul Speed"),
    SWIFT_SNEAK("Swift Sneak"), // 1.19+

    // ===== TOOLS =====
    DIG_SPEED("Efficiency"),
    SILK_TOUCH("Silk Touch"),
    LOOT_BONUS_BLOCKS("Fortune"),

    // ===== TRIDENT =====
    IMPALING("Impaling"),
    CHANNELING("Channeling"),
    RIPTIDE("Riptide"),
    LOYALTY("Loyalty"),

    // ===== FISHING =====
    LUCK("Luck of the Sea"),
    LURE("Lure"),

    // ===== MISC =====
    DURABILITY("Unbreaking"),
    MENDING("Mending"),

    // ===== CURSES =====
    BINDING_CURSE("Curse of Binding", true),
    VANISHING_CURSE("Curse of Vanishing", true);

    private final String friendlyName;
    private final boolean isCurse;

    FriendlyEnchants(String friendlyName, boolean isCurse) {
        this.friendlyName = friendlyName;
        this.isCurse = isCurse;
    }

    FriendlyEnchants(String friendlyName) {
        this(friendlyName, false);
    }

    /** Friendly name getter */
    public String getFriendlyName() {
        return friendlyName;
    }

    /** Curse flag */
    public boolean isCurse() {
        return isCurse;
    }

    // ------------------------------------------------------------------------
    // STATIC LOOKUP
    // ------------------------------------------------------------------------

    private static final Map<String, FriendlyEnchants> LOOKUP = new HashMap<>();

    static {
        for (FriendlyEnchants fe : values()) {
            // map by enum name (legacy getName())
            LOOKUP.put(fe.name(), fe);
            // map by NamespacedKey (modern key)
            final Enchantment ench = Enchantment.getByName(fe.name());
            if (ench != null && VersionUtil.CURRENT.isModern()) {
                final NamespacedKey key = ench.getKey();
                LOOKUP.put(key.getKey().toUpperCase(), fe);
            }
        }
    }

    /**
     * Looks up a friendly enchant by its Bukkit Enchantment.
     * Falls back to raw key (upper‑cased) if not found.
     */
    public static String friendlyNameFor(final Enchantment ench) {
        if (ench == null) return "Unknown";
        try {
            final String raw = (VersionUtil.CURRENT != null && VersionUtil.CURRENT.isModern() && ench.getKey() != null)
                    ? ench.getKey().getKey().toUpperCase()
                    : (ench.getName() != null ? ench.getName().toUpperCase() : "UNKNOWN");

            final FriendlyEnchants fe = LOOKUP.get(raw);
            return fe != null ? fe.getFriendlyName() : raw;
        } catch (Throwable e) {
            try {
                return ench.getName() != null ? ench.getName() : "Unknown";
            } catch (Throwable ignored) {
                return "Unknown";
            }
        }
    }
}
