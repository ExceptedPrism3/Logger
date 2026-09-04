package me.prism3.logger.events.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.managers.PermissionManager;
import me.prism3.logger.managers.PlayerManager;
import me.prism3.logger.utils.enums.LogType;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.EnumMap;
import java.util.Map;


public class WoodStripListener implements Listener { //TODO TO TEST

    private final LoggerAPI plugin;

    /** Precomputed mapping of all LOG/WOOD → STRIPPED_* materials. */
    private static final Map<Material, Material> STRIP_MAP = new EnumMap<>(Material.class);

    static {
        for (Material m : Material.values()) {

            final String name = m.name();

            if (name.endsWith("_LOG") || name.endsWith("_WOOD")) {
                // try the straightforward stripped_<name> first
                try {
                    STRIP_MAP.put(m, Material.valueOf("STRIPPED_" + name));
                } catch (final IllegalArgumentException ex) {
                    // handle stems in Nether & Warped
                    if (m == Material.CRIMSON_STEM)     STRIP_MAP.put(m, Material.STRIPPED_CRIMSON_STEM);
                    else if (m == Material.WARPED_STEM) STRIP_MAP.put(m, Material.STRIPPED_WARPED_STEM);
                }
            }
        }
    }

    public WoodStripListener(final LoggerAPI plugin) { this.plugin = plugin; }

    @EventHandler(ignoreCancelled = true)
    public void onWoodStripped(final PlayerInteractEvent event) {

        // only RIGHT‑CLICK_BLOCK with an axe
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        final Player player = event.getPlayer();

        if (PermissionManager.isExempt(player))
            return;

        final ItemStack held = player.getInventory().getItemInMainHand();

        final Material tool = held != null ? held.getType() : null;

        if (tool == null || !tool.name().endsWith("_AXE"))
            return;

        final Block block = event.getClickedBlock();

        if (block == null)
            return;

        final Material before = block.getType();
        final Material after  = STRIP_MAP.get(before);

        // not a strippable block
        if (after == null)
            return;

        // change the block (no physics update)
        block.setType(after, false);

        final Map<String,String> placeholders = PlayerManager.getPlayerManager(player).populatePlaceholders(player);

        placeholders.put("block_before", before.name());
        placeholders.put("block_after",  after.name());
        placeholders.put("used_tool",   tool.name());
        placeholders.put("block_x",      String.valueOf(block.getX()));
        placeholders.put("block_y",      String.valueOf(block.getY()));
        placeholders.put("block_z",      String.valueOf(block.getZ()));

        this.plugin.getLoggerManager().logEvent(LogType.PLAYER_WOOD_STRIP, player, placeholders);
    }
}
