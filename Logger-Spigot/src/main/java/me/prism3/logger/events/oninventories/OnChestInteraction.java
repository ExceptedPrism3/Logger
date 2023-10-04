package me.prism3.logger.events.oninventories;

import me.prism3.logger.Main;
import me.prism3.logger.database.external.ExternalData;
import me.prism3.logger.database.sqlite.global.SQLiteData;
import me.prism3.logger.utils.BedrockChecker;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import me.prism3.logger.utils.Log;
import me.prism3.logger.utils.enums.NmsVersions;
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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.time.ZonedDateTime;
import java.util.*;

import static me.prism3.logger.utils.Data.version;


public class OnChestInteraction implements Listener {

    private final Main main = Main.getInstance();

    private final List<Material> containerMaterials = new ArrayList<>(Arrays.asList(
            Material.valueOf("CHEST"),
            Material.valueOf("TRAPPED_CHEST"),
            Material.valueOf("ENDER_CHEST"),
            Material.valueOf("DROPPER"),
            Material.valueOf("DISPENSER"),
            Material.valueOf("HOPPER"),
            Material.valueOf("BREWING_STAND"),
            Material.valueOf("FURNACE")
    ));

    private void addAdditionalItems() {

        if (version.isAtLeast(NmsVersions.v1_11_R1)) {

            this.containerMaterials.add(Material.valueOf("BLACK_SHULKER_BOX"));
            this.containerMaterials.add(Material.valueOf("BLUE_SHULKER_BOX"));
            this.containerMaterials.add(Material.valueOf("BROWN_SHULKER_BOX"));
            this.containerMaterials.add(Material.valueOf("CYAN_SHULKER_BOX"));
            this.containerMaterials.add(Material.valueOf("GRAY_SHULKER_BOX"));
            this.containerMaterials.add(Material.valueOf("GREEN_SHULKER_BOX"));
            this.containerMaterials.add(Material.valueOf("LIGHT_BLUE_SHULKER_BOX"));
            this.containerMaterials.add(Material.valueOf("LIME_SHULKER_BOX"));
            this.containerMaterials.add(Material.valueOf("MAGENTA_SHULKER_BOX"));
            this.containerMaterials.add(Material.valueOf("ORANGE_SHULKER_BOX"));
            this.containerMaterials.add(Material.valueOf("PINK_SHULKER_BOX"));
            this.containerMaterials.add(Material.valueOf("PURPLE_SHULKER_BOX"));
            this.containerMaterials.add(Material.valueOf("RED_SHULKER_BOX"));
            this.containerMaterials.add(Material.valueOf("WHITE_SHULKER_BOX"));
            this.containerMaterials.add(Material.valueOf("YELLOW_SHULKER_BOX"));
        }

        if (version.isAtLeast(NmsVersions.v1_13_R1)) {

            this.containerMaterials.add(Material.valueOf("SHULKER_BOX"));
            this.containerMaterials.add(Material.valueOf("LIGHT_GRAY_SHULKER_BOX"));
        }

        if (version.isAtLeast(NmsVersions.v1_14_R1)) {
            this.containerMaterials.add(Material.valueOf("BARREL"));
            this.containerMaterials.add(Material.valueOf("BLAST_FURNACE"));
        }
    }

    public OnChestInteraction() { this.addAdditionalItems();}

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(final PlayerInteractEvent event) {

        if (!event.isCancelled() && this.main.getConfig().getBoolean("Log-Player.Chest-Interaction")) {

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

                if (player.hasPermission(Data.loggerExempt) || BedrockChecker.isBedrock(player.getUniqueId())) return;

                UUID playerUUID = player.getUniqueId();
                String playerName = player.getName();
                String worldName = player.getWorld().getName();
                int x = clickedBlock.getX();
                int y = clickedBlock.getY();
                int z = clickedBlock.getZ();

                if (Data.isLogToFiles) {

                    if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                        if (!Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Chest-Interaction-Staff")).isEmpty()) {

                            this.main.getDiscord().staffChat(player, Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Chest-Interaction-Staff")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%uuid%", playerUUID.toString()).replace("%player%", playerName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%chest%", chestName).replace("%items%", Arrays.toString(items)), false);

                        }

                        try {

                            BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true));
                            out.write(Objects.requireNonNull(this.main.getMessages().get().getString("Files.Chest-Interaction-Staff")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%uuid%", playerUUID.toString()).replace("%player%", playerName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%chest%", chestName).replace("%items%", Arrays.toString(items)) + "\n");
                            out.close();

                        } catch (IOException e) {

                            Log.warning("An error occurred while logging into the appropriate file.");
                            e.printStackTrace();

                        }

                        if (Data.isExternal && this.main.getExternal().isConnected()) {

                            ExternalData.chestInteraction(Data.serverName, player, x, y, z, items, true);

                        }

                        if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                            SQLiteData.insertChestInteraction(Data.serverName, player, x, y, z, items, true);

                        }

                        return;

                    }

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getChestInteractionFile(), true));
                        out.write(Objects.requireNonNull(this.main.getMessages().get().getString("Files.Chest-Interaction")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%uuid%", playerUUID.toString()).replace("%player%", playerName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%chest%", chestName).replace("%items%", Arrays.toString(items)) + "\n");
                        out.close();

                    } catch (IOException e) {

                        Log.warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();
                    }
                }

                // Discord Integration
                if (!player.hasPermission(Data.loggerExemptDiscord)) {

                    if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                        if (!Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Chest-Interaction-Staff")).isEmpty()) {

                            this.main.getDiscord().staffChat(player, Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Chest-Interaction-Staff")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%uuid%", playerUUID.toString()).replace("%player%", playerName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%chest%", chestName).replace("%items%", Arrays.toString(items)), false);

                        }
                    } else {

                        if (!Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Chest-Interaction")).isEmpty()) {

                            this.main.getDiscord().chestInteraction(player, Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Chest-Interaction")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%uuid%", playerUUID.toString()).replace("%player%", playerName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%chest%", chestName).replace("%items%", Arrays.toString(items)), false);
                        }
                    }
                }

                // External
                if (Data.isExternal && this.main.getExternal().isConnected()) {

                    try {

                        ExternalData.chestInteraction(Data.serverName, player, x, y, z, items, player.hasPermission(Data.loggerStaffLog));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                // SQLite
                if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                    try {

                        SQLiteData.insertChestInteraction(Data.serverName, player, x, y, z, items, player.hasPermission(Data.loggerStaffLog));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
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