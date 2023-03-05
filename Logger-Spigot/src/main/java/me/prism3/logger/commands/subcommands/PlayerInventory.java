package me.prism3.logger.commands.subcommands;

import me.prism3.logger.Main;
import me.prism3.logger.commands.SubCommand;
import me.prism3.logger.utils.FileHandler;
import me.prism3.logger.utils.Log;
import me.prism3.logger.utils.db.PlayerInventoryDB;
import me.prism3.logger.utils.playerdeathutils.InventoryToBase64;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.*;

import static me.prism3.logger.utils.Data.*;

public class PlayerInventory implements Listener, SubCommand {

    private File backupFile;
    private OfflinePlayer selectedPlayer;

    @Override
    public String getName() { return "playerinventory"; }

    @Override
    public String getDescription() { return "Opens a menu with all online players and their available inventory backups."; }

    @Override
    public String getSyntax() { return "/logger playerinventory"; }

    @Override
    public void perform(CommandSender commandSender, String[] args) {

        if (!(commandSender instanceof Player)) {

            Log.severe("This command can only be run in-game.");
            return;
        }

        final Player staff = (Player) commandSender;

        if (args.length == 1)
            this.mainMenu(staff);
        /*else {
            final OfflinePlayer selectedHooman = Bukkit.getOfflinePlayer(args[1]);
            this.backupMenu(selectedHooman.getName(), staff);
        }*/
    }

    @Override
    public List<String> getSubCommandsArgs(CommandSender commandSender, String[] args) { return Collections.emptyList(); }

    private void mainMenu(Player staff) {

        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {

            final Map<String, Integer> playerCounts = PlayerInventoryDB.getPlayerNames();

            // Calculate the number of rows needed based on the number of logs
            int numLogs = playerCounts.values().stream().reduce(0, Integer::sum);
            int numRows = (int) Math.ceil((double) numLogs / 9);
            numRows = Math.min(numRows, 6); // Limit to a maximum of 6 rows (54 slots)

            final Inventory inv = Bukkit.createInventory(null, numRows * 9, "Player Inventory Checker (Page 1)");

            for (Map.Entry<String, Integer> entry : playerCounts.entrySet()) {
                final String playerName = entry.getKey();
                final int count = entry.getValue();

                final ItemStack paper = this.createItemStack(Material.PAPER,
                        ChatColor.GOLD + "" + ChatColor.BOLD + playerName,
                        Collections.singletonList(ChatColor.WHITE + "Backup Available: " + ChatColor.AQUA + count));

                inv.addItem(paper);
            }

            staff.openInventory(inv);
        });
    }

    @EventHandler
    public void onClick(final InventoryClickEvent event) {

        if (event.getClickedInventory() == null ||
                !event.getView().getTitle().matches("(Player Inventory Checker \\(Page \\d+\\))|(Backup\\(s\\) of .*)|(Inventory of .*)"))
            return;

        event.setCancelled(true);
        final ItemStack clickedStack = event.getCurrentItem();

        if (clickedStack == null || !clickedStack.hasItemMeta())
            return;

        final String clickedItem = ChatColor.stripColor(clickedStack.getItemMeta().getDisplayName());
        final Player staff = (Player) event.getWhoClicked();

       this.handleClick(clickedItem, clickedStack, staff);
    }

    private void handleClick(String clickedItem, ItemStack clickedStack, Player staff) {
        
        for (String selectedPlayerBackup : PlayerInventoryDB.getDistinctPlayerNames()) {
            switch (clickedStack.getType()) {
                case CHEST:
                    for (String list : PlayerInventoryDB.getAllPlayerBackup(selectedPlayerBackup)) {
                        if (list.equalsIgnoreCase(clickedItem)) {
                            this.backupFile = new File(FileHandler.getPlayerDeathBackupLogFolder(), selectedPlayerBackup + File.separator + list);
                            this.restoreMenu(staff, list);
                            return;
                        }
                    }
                    break;
                case ENDER_CHEST:
                    this.backupMenu(this.selectedPlayer.getName(), staff);
                    break;
                case EMERALD_BLOCK:
                    this.addItem(staff);
                    break;
                case REDSTONE_BLOCK:
                    this.deleteFile(staff);
                    break;
                default:
                    if (clickedItem.equals(selectedPlayerBackup) && !clickedStack.getItemMeta().getLore().get(0).isEmpty()) {
                        this.selectedPlayer = Bukkit.getOfflinePlayer(selectedPlayerBackup);
                        this.backupMenu(selectedPlayerBackup, staff);
                        return;
                    }
                    break;
            }
        }
    }

    // Opening the second GUI after selecting the player and displaying all available backups
    private void backupMenu(final String selectedPlayer, final Player staff) {

        final int backupCount = PlayerInventoryDB.getPlayerBackupCount(selectedPlayer);

        if (backupCount == 0) {
            staff.sendMessage(ChatColor.translateAlternateColorCodes('&', pluginPrefix + "&cNo backup(s) have been found for &6" + selectedPlayer));
            return;
        }

        staff.closeInventory();

        int invSize = 9 * (int) Math.ceil(backupCount / 9.0);
        invSize = Math.min(invSize, 54);
        final Inventory secondInv = Bukkit.createInventory(staff, invSize, "Backup(s) of " + selectedPlayer);
        final List<String> backups = PlayerInventoryDB.getAllPlayerBackup(selectedPlayer);

        final ItemStack item = new ItemStack(Material.CHEST, 1);
        final ItemMeta meta = item.getItemMeta();

        for (String time : backups) {

            meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + time);
            meta.setLore(PlayerInventoryDB.getDetails(selectedPlayer));
            item.setItemMeta(meta);

            secondInv.addItem(item);
        }

        staff.openInventory(secondInv);
    }

    // Opening the last GUI based on the selected backup from the previous step, and displaying all items
    private void restoreMenu(final Player staff, final String date) {

        staff.closeInventory();

        final Inventory lastInv = Bukkit.createInventory(staff, 54, "Inventory of " + this.selectedPlayer.getName());

        final FileConfiguration f = YamlConfiguration.loadConfiguration(this.backupFile);

        System.out.println(PlayerInventoryDB.getPlayerInventoryData(date));

        final ItemStack[] invContent = InventoryToBase64.fromBase64(PlayerInventoryDB.getPlayerInventoryData(date));
        for (ItemStack item : invContent)
            lastInv.addItem(item);

        final ItemStack[] armorContent = InventoryToBase64.fromBase64(PlayerInventoryDB.getPlayerArmorData(date));
        for (ItemStack item : armorContent)
            lastInv.addItem(item);

        final String selectedPlayerObjectName = this.selectedPlayer.getName();

        lastInv.setItem(45, createItemStack(Material.ENDER_CHEST, ChatColor.AQUA + "Back", null));
//        lastInv.setItem(47, this.createSkull(selectedPlayerObjectName, PlayerFolder.getDetails(this.selectedPlayer.getName(), this.backupFile.getName())));
        lastInv.setItem(49, createItemStack(Material.EMERALD_BLOCK, ChatColor.AQUA + "Backup",
                Collections.singletonList(ChatColor.RED + "Clears player's current Inventory!")));
        lastInv.setItem(53, createItemStack(Material.REDSTONE_BLOCK, ChatColor.AQUA + "Delete",
                Collections.singletonList(ChatColor.RED + "Delete the Backup file.")));

        staff.openInventory(lastInv);
    }

    /*private ItemStack createSkull(final String playerName, final List<String> lores) {

        final ItemStack skull = new ItemStack(type, 1);

        if (!isNewVersion)
            skull.setDurability((short) 3);

        final SkullMeta meta = (SkullMeta) skull.getItemMeta();

        meta.setOwner(playerName);
        meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + playerName);
        meta.setLore(lores != null ? lores : Collections.singletonList(ChatColor.WHITE + "Backups Available: " + ChatColor.AQUA + PlayerFolder.backupCount(playerName)));
        skull.setItemMeta(meta);

        return skull;
    }*/

    private ItemStack createItemStack(final Material material, final String name, final List<String> lore) {

        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(name);

        if (lore != null)
            meta.setLore(lore);

        item.setItemMeta(meta);
        return item;
    }

    private void addItem(final Player staff) {

        if (!this.selectedPlayer.isOnline()) {

            staff.closeInventory();
            staff.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    pluginPrefix + "&cCould not restore the backup of " + this.selectedPlayer.getName() + " as his offline."));
            return;
        }

        final FileConfiguration f = YamlConfiguration.loadConfiguration(this.backupFile);

        final Player playerOnline = Bukkit.getPlayer(this.selectedPlayer.getUniqueId());

        final ItemStack[] invContent = InventoryToBase64.fromBase64(f.getString("inventory"));
        playerOnline.getInventory().setContents(invContent);

        final ItemStack[] armorContent = InventoryToBase64.fromBase64(f.getString("armor"));
        playerOnline.getInventory().setArmorContents(armorContent);

        staff.sendMessage(ChatColor.translateAlternateColorCodes('&', pluginPrefix + "Inventory Restored."));
        playerOnline.sendMessage(ChatColor.translateAlternateColorCodes('&', pluginPrefix + "&8&l[ &6&lYour inventory has been restored! &8&l]"));
    }

    // Deletes the selected backup file
    private void deleteFile(final Player staff) {

        if (this.backupFile != null && this.backupFile.exists()) {
            this.backupFile.delete();
            staff.closeInventory();
            staff.sendMessage(ChatColor.translateAlternateColorCodes('&', pluginPrefix + "&cThe backup file has been deleted."));
        }
    }
}
