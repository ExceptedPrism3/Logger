package me.prism3.logger.events.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.managers.PermissionManager;
import me.prism3.logger.managers.PlayerManager;
import me.prism3.logger.utils.enums.LogType;
import org.bukkit.Material;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.ChestedHorse;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;
import java.util.Map;

/**
 * Listens for player interactions with horses and mountable animals (mounting, dismounting, opening inventories).
 */
public class HorseInteractionListener implements Listener {

    private final LoggerAPI plugin;

    public HorseInteractionListener(final LoggerAPI plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onVehicleEnter(final VehicleEnterEvent event) {
        if (event.getEntered() instanceof Player && event.getVehicle() instanceof AbstractHorse) {
            final Player player = (Player) event.getEntered();
            final AbstractHorse horse = (AbstractHorse) event.getVehicle();
            logHorseEvent(player, horse, "mounted", "Player mounted vehicle");
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onVehicleExit(final VehicleExitEvent event) {
        if (event.getExited() instanceof Player && event.getVehicle() instanceof AbstractHorse) {
            final Player player = (Player) event.getExited();
            final AbstractHorse horse = (AbstractHorse) event.getVehicle();
            logHorseEvent(player, horse, "dismounted", "Player dismounted vehicle");
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInventoryOpen(final InventoryOpenEvent event) {
        if (event.getPlayer() instanceof Player && event.getInventory().getHolder() instanceof AbstractHorse) {
            final Player player = (Player) event.getPlayer();
            final AbstractHorse horse = (AbstractHorse) event.getInventory().getHolder();
            logHorseEvent(player, horse, "opened inventory of", "Player opened mount inventory");
        }
    }

    private void logHorseEvent(final Player player, final AbstractHorse horse, final String action, final String details) {
        if (player == null || PermissionManager.isExempt(player)) {
            return;
        }

        final Map<String, String> placeholders = PlayerManager.getPlayerManager(player).populatePlaceholders(player);

        final String rawType = horse.getType() != null ? horse.getType().name() : "HORSE";
        final String friendlyType = formatEntityName(rawType);
        final String horseName = (horse.getCustomName() != null && !horse.getCustomName().isEmpty())
                ? horse.getCustomName()
                : "Unnamed";
        final String horseUuid = horse.getUniqueId().toString();

        final AnimalTamer tamer = horse.getOwner();
        final String ownerName = (tamer != null && tamer.getName() != null)
                ? tamer.getName()
                : (horse.isTamed() ? "Unknown" : "None (Wild)");
        final String ownerUuid = (tamer != null && tamer.getUniqueId() != null)
                ? tamer.getUniqueId().toString()
                : "None";

        final boolean isTamed = horse.isTamed();
        final boolean hasSaddle = horse.getInventory().getSaddle() != null
                && horse.getInventory().getSaddle().getType() != Material.AIR;

        String armorType = "None";
        if (horse instanceof Horse) {
            final ItemStack armor = ((Horse) horse).getInventory().getArmor();
            if (armor != null && armor.getType() != Material.AIR) {
                armorType = formatEntityName(armor.getType().name());
            }
        }

        String extraDetails = details;
        if (horse instanceof ChestedHorse) {
            final boolean hasChest = ((ChestedHorse) horse).isCarryingChest();
            extraDetails += hasChest ? " [Chest: Yes]" : " [Chest: No]";
        }

        placeholders.put("action", action);
        placeholders.put("horse_type", friendlyType);
        placeholders.put("horse_name", horseName);
        placeholders.put("horse_uuid", horseUuid);
        placeholders.put("owner", ownerName);
        placeholders.put("owner_uuid", ownerUuid);
        placeholders.put("tamed", isTamed ? "Yes" : "No");
        placeholders.put("saddle", hasSaddle ? "Yes" : "No");
        placeholders.put("armor", armorType);
        placeholders.put("details", extraDetails);

        this.plugin.getLoggerManager().logEvent(LogType.PLAYER_HORSE_INTERACTION, player, placeholders);
    }

    private String formatEntityName(final String name) {
        if (name == null) return "Unknown";
        final String[] parts = name.toLowerCase(Locale.ROOT).split("_");
        final StringBuilder sb = new StringBuilder();
        for (final String part : parts) {
            if (part.isEmpty()) continue;
            if (sb.length() > 0) sb.append(' ');
            sb.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1));
        }
        return sb.toString();
    }
}
