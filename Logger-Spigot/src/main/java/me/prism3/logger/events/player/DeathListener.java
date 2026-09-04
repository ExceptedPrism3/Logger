package me.prism3.logger.events.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.managers.PermissionManager;
import me.prism3.logger.managers.PlayerManager;
import me.prism3.logger.utils.EnchantFormatter;
import me.prism3.logger.utils.enums.LogType;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Logs detailed player‑death information: inventory, armor, killer (player or
 * mob), weapon, cause.
 */
public class DeathListener implements Listener {

    private final LoggerAPI plugin;

    /**
     * A mapping of EntityDamageEvent.DamageCause to human‑readable strings.
     * This is used to provide a more user‑friendly description of the cause of
     * death.
     */
    private static final Map<EntityDamageEvent.DamageCause, String> DAMAGE_CAUSE_MAP = new EnumMap<>(
            EntityDamageEvent.DamageCause.class);
    static {
        DAMAGE_CAUSE_MAP.put(EntityDamageEvent.DamageCause.FIRE, "Burned to Death");
        DAMAGE_CAUSE_MAP.put(EntityDamageEvent.DamageCause.DROWNING, "Drowned");
        DAMAGE_CAUSE_MAP.put(EntityDamageEvent.DamageCause.FALL, "Fell from a High Place");
        DAMAGE_CAUSE_MAP.put(EntityDamageEvent.DamageCause.SUFFOCATION, "Suffocated");
        DAMAGE_CAUSE_MAP.put(EntityDamageEvent.DamageCause.LAVA, "Burned in Lava");
        DAMAGE_CAUSE_MAP.put(EntityDamageEvent.DamageCause.FIRE_TICK, "Burned to Death");
        DAMAGE_CAUSE_MAP.put(EntityDamageEvent.DamageCause.PROJECTILE, "Shot");
        DAMAGE_CAUSE_MAP.put(EntityDamageEvent.DamageCause.ENTITY_ATTACK, "Struck Down");
        DAMAGE_CAUSE_MAP.put(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION, "Blown Up");
        DAMAGE_CAUSE_MAP.put(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION, "Blown Up");
    }

    /**
     * Constructor for the DeathListener.
     * Initializes the plugin instance.
     *
     * @param plugin the LoggerAPI plugin instance
     */
    public DeathListener(final LoggerAPI plugin) {
        this.plugin = plugin;
    }

    /**
     * Handles player death events.
     * Logs detailed information about the player, including inventory, armor,
     * killer, weapon, and cause of death.
     *
     * @param event the PlayerDeathEvent to handle
     */
    @EventHandler
    public void onPlayerDeath(final PlayerDeathEvent event) {

        final Player player = event.getEntity();

        // skip exempt players
        if (PermissionManager.isExempt(player))
            return;

        // base placeholders: name, uuid, location…
        final Map<String, String> placeholders = PlayerManager.getPlayerManager(player).populatePlaceholders(player);

        // add level, inventory, armor
        placeholders.put("level", Integer.toString(player.getLevel()));
        placeholders.put("inventory", formatItemStackArray(player.getInventory().getContents()));
        placeholders.put("armor", formatItemStackArray(player.getInventory().getArmorContents()));

        // examine last damage to determine killer and cause
        final EntityDamageEvent lastDamage = player.getLastDamageCause();

        // if the last damage was caused by an entity
        if (lastDamage instanceof EntityDamageByEntityEvent) {
            final EntityDamageByEntityEvent edbe = (EntityDamageByEntityEvent) lastDamage;
            final Entity damager = edbe.getDamager();

            // if a projectile, unwrap shooter
            if (damager instanceof Projectile) {
                final Projectile proj = (Projectile) damager;

                // if the projectile has a shooter, use that as the killer
                final Entity shooter = proj.getShooter() instanceof Entity ? (Entity) proj.getShooter() : null;

                // if the shooter is a player, use that as the killer
                if (shooter != null) {
                    placeholders.put("killer", shooter.getName());
                    // weapon: projectile type
                    placeholders.put("killer_weapon", proj.getType().name());
                    // if shooter holds weapon
                    if (shooter instanceof LivingEntity) {
                        final LivingEntity le = (LivingEntity) shooter;
                        final ItemStack hand = le.getEquipment().getItemInMainHand();
                        placeholders.put("killer_weapon", formatItemStack(hand));
                    }
                }
            } else {
                // direct mob or player
                placeholders.put("killer", damager.getName());
                if (damager instanceof Player) {
                    final Player p = (Player) damager;
                    final ItemStack weapon = p.getInventory().getItemInMainHand();
                    placeholders.put("killer_weapon", formatItemStack(weapon));
                } else if (damager instanceof LivingEntity) {
                    final LivingEntity le = (LivingEntity) damager;
                    final ItemStack hand = le.getEquipment().getItemInMainHand();
                    placeholders.put("killer_weapon", formatItemStack(hand));
                } else {
                    placeholders.put("killer_weapon", "Unknown");
                }
            }

            // human‑friendly damage cause
            final String cause = DAMAGE_CAUSE_MAP.getOrDefault(lastDamage.getCause(),
                    lastDamage.getCause().name().toLowerCase().replace('_', ' '));

            placeholders.put("death_cause", cause);
        } else {
            // no entity killer: environmental death
            placeholders.put("killer", "None");
            placeholders.put("killer_weapon", "None");

            final String cause = lastDamage != null
                    ? DAMAGE_CAUSE_MAP.getOrDefault(lastDamage.getCause(),
                            lastDamage.getCause().name().toLowerCase().replace('_', ' '))
                    : "Unknown";

            placeholders.put("death_cause", cause);
        }

        // fire off the log
        this.plugin.getLoggerManager().logEvent(LogType.PLAYER_DEATH, player, placeholders);
    }

    /**
     * Formats an array of ItemStacks into a human‑readable string.
     * Filters out null and AIR stacks, and formats each stack with its type and
     * amount.
     *
     * @param items the array of ItemStacks to format
     * @return a formatted string representing the ItemStacks
     */
    private String formatItemStackArray(final ItemStack[] items) {
        final List<String> formatted = Arrays.stream(items == null ? new ItemStack[0] : items)
                .filter(i -> i != null && i.getType() != Material.AIR)
                .map(this::formatItemStack)
                .collect(Collectors.toList());
        return formatted.isEmpty() ? "None" : String.join(", ", formatted);
    }

    /**
     * Formats a single ItemStack into a human‑readable string.
     * If the stack is null or AIR, returns "Empty".
     * Otherwise, formats the type and amount, and includes enchantments if present.
     *
     * @param item the ItemStack to format
     * @return a formatted string representing the ItemStack
     */
    private String formatItemStack(final ItemStack item) {

        if (item == null || item.getType() == Material.AIR)
            return "Empty";

        final String base = item.getType().name() + " x" + item.getAmount();
        final String ench = EnchantFormatter.format(item.getEnchantments());

        return ench.isEmpty() ? base : base + " (" + ench + ")";
    }
}
