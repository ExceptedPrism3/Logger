package me.prism3.logger.utils.enums;

import org.bukkit.enchantments.Enchantment;

public enum FriendlyEnchants  {

    ARROW_DAMAGE("Power"),
    ARROW_FIRE("Flame"),
    ARROW_INFINITE("Infinite"),
    ARROW_KNOCKBACK("Punch"),
    BINDING_CURSE("Curse of Binding",true),
    CHANNELING("Channeling"),
    DAMAGE_ALL("Sharpness"),
    DAMAGE_ARTHROPODS("Bane of Arthropods"),
    DAMAGE_UNDEAD("Smite"),
    DEPTH_STRIDER("Depth Strider"),
    DIG_SPEED("Efficiency"),
    DURABILITY("Unbreaking"),
    FIRE_ASPECT("Fire Aspect"),
    FROST_WALKER("Frost Walker"),
    IMPALING("Impaling"),
    KNOCKBACK("Knockback"),
    LOOT_BONUS_BLOCKS("Fortune"),
    LOOT_BONUS_MOBS("Looting"),
    LOYALTY("Loyalty"),
    LUCK("Luck of the Sea"),
    LURE("Lure"),
    MENDING("Mending"),
    MULTISHOT("Multishot"),
    OXYGEN("Respiration"),
    PIERCING("Piercing"),
    PROTECTION_ENVIRONMENTAL("Protection"),
    PROTECTION_EXPLOSIONS("Blast Protection"),
    PROTECTION_FALL("Feather Falling"),
    PROTECTION_FIRE("Fire Protection"),
    PROTECTION_PROJECTILE("Projectile Protection"),
    QUICK_CHARGE("Quick Charge"),
    RIPTIDE("Riptide"),
    SILK_TOUCH("Silk Touch"),
    SOUL_SPEED("Soul Speed"),
    SWEEPING_EDGE("Sweeping Edge"),
    THORNS("Thorns"),
    VANISHING_CURSE("Curse of Vanishing",true),
    WATER_WORKER("Aqua Affinity");

    private final String friendlyName;
    private final boolean isCurse;

    FriendlyEnchants(String friendlyName, boolean isCurse) {
        this.friendlyName = friendlyName;
        this.isCurse = isCurse;
    }

    FriendlyEnchants(String friendlyName) {
        this(friendlyName,false);
    }

    @SuppressWarnings("deprecation")
    public static FriendlyEnchants getFriendlyEnchantment(Enchantment ench) {
        return FriendlyEnchants.valueOf(ench.getName());
    }

    public String getFriendlyName() { return friendlyName; }

    public boolean isCurse() { return isCurse; }
}
