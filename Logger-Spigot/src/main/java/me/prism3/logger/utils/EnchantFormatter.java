package me.prism3.logger.utils;

import me.prism3.logger.utils.enums.FriendlyEnchants;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import java.util.Map;
import java.util.stream.Collectors;


public final class EnchantFormatter {

    private EnchantFormatter() { /* no‑op */ }

    /** Format all enchants on an ItemStack. */
    public static String format(ItemStack stack) {
        return format(stack.getEnchantments());
    }

    /**
     * Map<Enchantment,level> → "FriendlyName:level, …",
     * custom enchants simply appear by their raw key.
     */
    public static String format(Map<Enchantment, Integer> enchants) {

        if (enchants.isEmpty())
            return "";

        return enchants.entrySet().stream()
                .map(e -> {
                    Enchantment ench = e.getKey();
                    int lvl = e.getValue();
                    String friendly = FriendlyEnchants.friendlyNameFor(ench);
                    return friendly + ":" + lvl;
                })
                .collect(Collectors.joining(", "));
    }
}
