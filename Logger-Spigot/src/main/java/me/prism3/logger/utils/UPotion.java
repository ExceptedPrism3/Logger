package me.prism3.logger.utils;

import me.prism3.logger.utils.enums.UMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

public class UPotion {

    private static final String V = Bukkit.getVersion();
    private final ItemStack potion;
    private final Object potionData;

    public UPotion(UMaterial.PotionBase base, PotionType type, boolean extended, boolean upgraded) {

        final String bn = base.name();

        if (V.contains("1.8")) {
            this.potion = type.name().equals("WATER") ? new Potion(type).toItemStack(1) : new Potion(type, upgraded ? 2 : 1, bn.equals("SPLASH")).toItemStack(1);
            this.potionData = this.potion.getItemMeta();
        } else {
            final ItemStack is = new ItemStack(Material.matchMaterial(bn.equals("NORMAL") ? "POTION" : bn.equals("ARROW") ? V.contains("1.8") || V.contains("1.9") || V.contains("1.11") ? "ARROW" : "TIPPED_ARROW" : bn + "_POTION"));
            final boolean a = !is.getType().equals(Material.ARROW);
            org.bukkit.inventory.meta.PotionMeta pm = null;
            org.bukkit.potion.PotionData pd = null;
            if (a) {
                pm = (org.bukkit.inventory.meta.PotionMeta) is.getItemMeta();
                pd = new org.bukkit.potion.PotionData(type, type.isExtendable() && extended, type.isUpgradeable() && upgraded);
            }
            this.potionData = pd;
            if (a) {
                pm.setBasePotionData(pd);
                is.setItemMeta(pm);
            }
            this.potion = is;
        }
    }

    public ItemStack getItemStack() { return this.potion.clone(); }

    public Object getPotionData() { return this.potionData; }
}