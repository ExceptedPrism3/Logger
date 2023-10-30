package me.prism3.logger.events.inventories;

import me.prism3.logger.Main;
import me.prism3.logger.utils.enums.DiscordChannels;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.enums.LogCategory;
import me.prism3.logger.utils.enums.NmsVersions;
import me.prism3.loggercore.database.data.Coordinates;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.text.MessageFormat;
import java.time.ZonedDateTime;
import java.util.*;

import static me.prism3.logger.utils.Data.*;


public class OnChestInteraction implements Listener {

    private final Main main = Main.getInstance();

    private final List<Material> containerMaterials = new ArrayList<>(Arrays.asList(
            Material.CHEST,
            Material.TRAPPED_CHEST,
            Material.ENDER_CHEST,
            Material.DROPPER,
            Material.DISPENSER,
            Material.HOPPER,
            Material.BREWING_STAND,
            Material.FURNACE
    ));

    private void addAdditionalItems() {

        if (version.isAtLeast(NmsVersions.v1_11_R1)) {

            this.containerMaterials.add(Material.SHULKER_BOX);
            this.containerMaterials.add(Material.BLACK_SHULKER_BOX);
            this.containerMaterials.add(Material.BLUE_SHULKER_BOX);
            this.containerMaterials.add(Material.BROWN_SHULKER_BOX);
            this.containerMaterials.add(Material.CYAN_SHULKER_BOX);
            this.containerMaterials.add(Material.GRAY_SHULKER_BOX);
            this.containerMaterials.add(Material.GREEN_SHULKER_BOX);
            this.containerMaterials.add(Material.LIGHT_BLUE_SHULKER_BOX);
            this.containerMaterials.add(Material.LIGHT_GRAY_SHULKER_BOX);
            this.containerMaterials.add(Material.LIME_SHULKER_BOX);
            this.containerMaterials.add(Material.MAGENTA_SHULKER_BOX);
            this.containerMaterials.add(Material.ORANGE_SHULKER_BOX);
            this.containerMaterials.add(Material.PINK_SHULKER_BOX);
            this.containerMaterials.add(Material.PURPLE_SHULKER_BOX);
            this.containerMaterials.add(Material.RED_SHULKER_BOX);
            this.containerMaterials.add(Material.WHITE_SHULKER_BOX);
            this.containerMaterials.add(Material.YELLOW_SHULKER_BOX);
        }

        if (version.isAtLeast(NmsVersions.v1_14_R1)) {
            this.containerMaterials.add(Material.BARREL);
            this.containerMaterials.add(Material.BLAST_FURNACE);
        }
    }

    public OnChestInteraction() { this.addAdditionalItems(); }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryOpenEvent(final PlayerInteractEvent event) {

        if (event.isCancelled()) return;

        final Player player = event.getPlayer();
        final Block clickedBlock = event.getClickedBlock();

        if (clickedBlock != null && this.isContainer(clickedBlock.getType())) {

            final String chestName = this.getContainerType(clickedBlock);

            final BlockState blockState = clickedBlock.getState();

            String[] items = new String[0];

            // Handle Ender Chest
            if (clickedBlock.getType() == Material.ENDER_CHEST) {

                Inventory inventory = player.getEnderChest();

                items = Arrays.stream(inventory.getContents())
                        .filter(Objects::nonNull)
                        .map(stack -> MessageFormat.format("{0} x {1}", stack.getType(), stack.getAmount()))
                        .toArray(String[]::new);

            } else { // Handle other containers

                if (blockState instanceof InventoryHolder) {
                    InventoryHolder inventoryHolder = (InventoryHolder) blockState;
                    Inventory inventory = inventoryHolder.getInventory();

                    items = Arrays.stream(inventory.getContents())
                            .filter(Objects::nonNull)
                            .map(stack -> MessageFormat.format("{0} x {1}", stack.getType(), stack.getAmount()))
                            .toArray(String[]::new);
                }
            }

            UUID playerUUID = player.getUniqueId();
            String playerName = player.getName();
            String worldName = player.getWorld().getName();
            int x = clickedBlock.getX();
            int y = clickedBlock.getY();
            int z = clickedBlock.getZ();

            final Coordinates coordinates = new Coordinates(x, y, z, worldName);

            final Map<String, String> placeholders = new HashMap<>();
            placeholders.put("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now()));
            placeholders.put("%world%", worldName);
            placeholders.put("%uuid%", playerUUID.toString());
            placeholders.put("%player%", playerName);
            placeholders.put("%x%", String.valueOf(x));
            placeholders.put("%y%", String.valueOf(y));
            placeholders.put("%z%", String.valueOf(z));
            placeholders.put("%chest%", chestName);
            placeholders.put("%items%", Arrays.toString(items));

            // Log To Files
            if (Data.isLogToFiles) {
                if (Data.isStaffEnabled && player.hasPermission(loggerStaffLog)) {
                    this.main.getFileHandler().handleFileLog(LogCategory.STAFF,  "Files.Chest-Interaction-Staff", placeholders);
                } else {
                    this.main.getFileHandler().handleFileLog(LogCategory.CHEST_INTERACTION, "Files.Chest-Interaction", placeholders);
                }
            }

            // Discord Integration
            if (!player.hasPermission(loggerExemptDiscord) && this.main.getDiscordFile().get().getBoolean("Discord.Enable")) {

                if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    this.main.getDiscord().handleDiscordLog("Discord.Chest-Interaction-Staff", placeholders, DiscordChannels.STAFF, playerName, playerUUID);
                } else {

                    this.main.getDiscord().handleDiscordLog("Discord.Chest-Interaction", placeholders, DiscordChannels.CHEST_INTERACTION, playerName, playerUUID);
                }
            }

            // External
            if (Data.isExternal) {

                try {

//                Main.getInstance().getDatabase().getDatabaseQueue().queueChestInteraction(Data.serverName, playerName, playerUUID.toString(), coordinates, items, player.hasPermission(loggerStaffLog));

                } catch (final Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (Data.isSqlite) {

                try {

                    Main.getInstance().getDatabase().getDatabaseQueue().queueChestInteraction(Data.serverName, playerName, playerUUID.toString(), coordinates, Arrays.toString(items), player.hasPermission(loggerStaffLog));

                } catch (final Exception e) { e.printStackTrace(); }
            }
        }
    }

    private boolean isContainer(final Material material) {

        for (Material containerMaterial : this.containerMaterials)
            if (containerMaterial == material)
                return true;

        if (version.isAtLeast(NmsVersions.v1_11_R1))
            return material.name().endsWith("_SHULKER_BOX") || material == Material.SHULKER_BOX;

        return false;
    }

    private String getContainerType(final Block block) {

        final Material material = block.getType();

        if (block.getState() instanceof DoubleChest) {
            return "Double Chest";
        } else if (material == Material.CHEST) {
            return this.isDoubleChest(block) ? "Double Chest" : "Chest";
        } else if (material == Material.ENDER_CHEST) {
            return "Ender Chest";
        } else if (material == Material.TRAPPED_CHEST) {
            return "Trapped Chest";
        } else if (version.isAtLeast(NmsVersions.v1_14_R1) && material == Material.BARREL) {
            return "Barrel";
        } else if (version.isAtLeast(NmsVersions.v1_11_R1) && (material.name().endsWith("_SHULKER_BOX") || material == Material.SHULKER_BOX)) {
            return this.getShulkerBoxType(block);
        } else if (material == Material.DROPPER) {
            return "Dropper";
        } else if (material == Material.DISPENSER) {
            return "Dispenser";
        } else if (material == Material.HOPPER) {
            return "Hopper";
        } else if (material == Material.BREWING_STAND) {
            return "Brewing Stand";
        } else if (material == Material.FURNACE) {
            return "Furnace";
        } else if (version.isAtLeast(NmsVersions.v1_14_R1) && material == Material.BLAST_FURNACE) {
            return "Blast Furnace";
        }

        return "Unknown Container";
    }

    private boolean isDoubleChest(final Block block) {

        final InventoryHolder inventoryHolder = (InventoryHolder) block.getState();
        final Inventory inventory = inventoryHolder.getInventory();

        return inventory.getSize() == 54;
    }

    private String getShulkerBoxType(final Block block) {

        if (block.getState() instanceof ShulkerBox) {

            final ShulkerBox shulkerBox = (ShulkerBox) block.getState();
            final DyeColor color = shulkerBox.getColor();

            if (color != null)
                return color.name() + " Shulker Box";
        }

        return "Shulker Box";
    }
}
