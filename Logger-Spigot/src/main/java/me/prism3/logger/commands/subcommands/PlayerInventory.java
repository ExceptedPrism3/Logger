package me.prism3.logger.commands.subcommands;

import me.prism3.logger.commands.SubCommand;
import me.prism3.logger.utils.FileHandler;
import me.prism3.logger.utils.Log;
import me.prism3.logger.utils.playerdeathutils.InventoryToBase64;
import me.prism3.logger.utils.playerdeathutils.PlayerFolder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.File;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import static me.prism3.logger.utils.Data.*;

public class PlayerInventory implements Listener, SubCommand {

    private static final int ITEMS_PER_PAGE = 45;
    private static final String NEXT_PAGE = ChatColor.GREEN + "Next Page";
    private static final String PREVIOUS_PAGE = ChatColor.RED + "Previous Page";

    private File backupFile;
    private Inventory[] inventories;
    private List<Player> onlinePlayers;
    private int pageCount;
    private OfflinePlayer offlinePlayer;

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
        else {
            final OfflinePlayer selectedHooman = Bukkit.getOfflinePlayer(args[1]);
            this.backupMenu(selectedHooman.getName(), staff);
            this.offlinePlayer = selectedHooman;
        }
    }

    @Override
    public List<String> getSubCommandsArgs(CommandSender commandSender, String[] args) { return Collections.emptyList(); }

    // Opening the first GUI with all online players and their available backups
    private void mainMenu(final Player staff) {

        final int itemsPerRow = 9;
        final int playerCount = Bukkit.getOnlinePlayers().size();
        this.pageCount = (int) Math.ceil((double) playerCount / (itemsPerRow * 6));

        CompletableFuture.runAsync(() -> {

            final List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());

            if (this.inventories == null || this.onlinePlayers == null || this.onlinePlayers.size() != onlinePlayers.size()) {
                this.inventories = IntStream.range(0, this.pageCount)
                        .mapToObj(i -> Bukkit.createInventory(staff,
                                (int) Math.ceil((double) Math.min(playerCount - i * itemsPerRow * 6, itemsPerRow * 6) / itemsPerRow) * 9,
                                "Player Inventory Checker (Page " + (i + 1) + ")"))
                        .toArray(Inventory[]::new);

                this.onlinePlayers = onlinePlayers;

                IntStream.range(0, this.pageCount)
                        .forEach(i -> fillInventory(this.inventories[i], this.onlinePlayers.subList(i * itemsPerRow * 6, Math.min(this.onlinePlayers.size(), (i + 1) * itemsPerRow * 6)), itemsPerRow, i, this.pageCount));
            }

            staff.openInventory(this.inventories[0]);
        });
    }

    private void fillInventory(Inventory inventory, List<Player> onlinePlayers, int itemsPerRow, int currentPage, int totalPages) {

        final int[] count = {0};

        onlinePlayers.stream()
                .sequential()
                .map(this::createSkull)
                .forEach(skull -> {
                    inventory.setItem(count[0], skull);
                    count[0]++;
                    if (count[0] % itemsPerRow == 0) {
                        count[0] += itemsPerRow - 9;
                    }
                });
        if (onlinePlayers.size() >= itemsPerRow * 6) {
            // Add Next Page Button
            if (currentPage < totalPages - 1) {
                final ItemStack next = this.createItemStack(Material.ARROW, NEXT_PAGE, null);
                inventory.setItem(inventory.getSize() - 5, next);
            }

            // Add Previous Page Button
            if (currentPage > 0) {
                final ItemStack previous = this.createItemStack(Material.ARROW, PREVIOUS_PAGE, null);
                inventory.setItem(inventory.getSize() - 9, previous);
            }
        }
    }

    @EventHandler
    public void onClick(final InventoryClickEvent event) {

        if (event.getClickedInventory() == null
                || !event.getView().getTitle().matches("(Player Inventory Checker \\(Page \\d+\\))|(Backup\\(s\\) of .*)|(Inventory of .*)"))
            return;

        event.setCancelled(true);

        final ItemStack clickedStack = event.getCurrentItem();
        if (clickedStack == null || !clickedStack.hasItemMeta())
            return;

        final String clickedItem = ChatColor.stripColor(clickedStack.getItemMeta().getDisplayName());
        final Player staff = (Player) event.getWhoClicked();
        int currentPage = this.getCurrentPage(event.getView().getTitle());

        switch (clickedItem) {
            case "Next Page":
                if (currentPage + 1 > this.getTotalPages()) {
                    staff.sendMessage("You have reached the last page.");
                    return;
                }
                this.openPage(currentPage + 1, staff);
                break;
            case "Previous Page":
                final int previousPage = currentPage - 1;
                if (previousPage < 1) {
                    staff.sendMessage("You are already on the first page.");
                    return;
                }
                this.openPage(previousPage, staff);
                break;
            default:
                this.handleClick(clickedItem, clickedStack, staff);
                break;
        }
    }

    private void handleClick(String clickedItem, ItemStack clickedStack, Player staff) {

        if (!Bukkit.getOnlinePlayers().contains(this.offlinePlayer)) {

            for (String list : PlayerFolder.fileNames(this.offlinePlayer.getName())) {
                if (list.equalsIgnoreCase(clickedItem)) {
                    this.backupFile = new File(FileHandler.getPlayerDeathBackupLogFolder(), this.offlinePlayer + File.separator + list);
                    this.restoreMenu(this.offlinePlayer.getName(), staff);
                    return;
                }
            }
        }

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            switch (clickedStack.getType()) {
                case CHEST:
                    for (String list : PlayerFolder.fileNames(onlinePlayer.getName())) {
                        if (list.equalsIgnoreCase(clickedItem)) {
                            this.backupFile = new File(FileHandler.getPlayerDeathBackupLogFolder(), onlinePlayer.getName() + File.separator + list);
                            this.restoreMenu(onlinePlayer.getName(), staff);
                            return;
                        }
                    }
                    break;
                case ENDER_CHEST:
                    this.backupMenu(onlinePlayer.getName(), staff);
                    return;
                case EMERALD_BLOCK:
                    this.addItem(onlinePlayer, staff);
                    return;
                default:
                    if (clickedItem.equals(onlinePlayer.getName())) {
                        this.backupMenu(onlinePlayer.getName(), staff);
                        return;
                    }
                    break;
            }
        }
    }

    private int getCurrentPage(final String title) {
        final Matcher matcher = Pattern.compile("(\\d+)").matcher(title);
        return matcher.find() ? Integer.parseInt(matcher.group()) : -1;
    }

    private int getTotalPages() {
        // calculate and return the total number of pages needed to display all players
        int totalPlayers = Bukkit.getOnlinePlayers().size();
        return (int) Math.ceil(totalPlayers / (double) ITEMS_PER_PAGE);
    }

    private void openPage(final int pageNumber, final Player staff) {

        final Inventory inventory = Bukkit.createInventory(staff, 54, "Player Inventory Checker (Page " + (pageNumber) + ")");

        int startIndex = (pageNumber - 1) * ITEMS_PER_PAGE;
        int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, Bukkit.getOnlinePlayers().size());

        Bukkit.getOnlinePlayers().stream().skip(startIndex).limit(endIndex - startIndex)
                .forEach(player -> inventory.addItem(this.createSkull(player)));

        final ItemStack next = this.createItemStack(Material.ARROW, NEXT_PAGE, null);
        inventory.setItem(inventory.getSize() - 5, next);

        final ItemStack back = this.createItemStack(Material.ARROW, PREVIOUS_PAGE, null);
        inventory.setItem(inventory.getSize() - 9, back);

        staff.openInventory(inventory);
    }

    // Opening the second GUI after selecting the player and displaying all available backups
    private void backupMenu(final String selectedPlayer, final Player staff) {

        final int backupCount = PlayerFolder.backupCount(selectedPlayer);

        if (backupCount == 0) {
            staff.sendMessage(ChatColor.translateAlternateColorCodes('&', pluginPrefix + "&cNo backup(s) have been found for &6" + selectedPlayer));
            return;
        }

        staff.closeInventory();

        int invSize = 9 * (int) Math.ceil(backupCount / 9.0);
        invSize = Math.min(invSize, 54);
        final Inventory secondInv = Bukkit.createInventory(staff, invSize, "Backup(s) of " + selectedPlayer);
        final String[] files = PlayerFolder.fileNames(selectedPlayer);

        final ItemStack item = new ItemStack(Material.CHEST, 1);
        final ItemMeta meta = item.getItemMeta();

        for (String file : files) {

            meta.setDisplayName(file);
            item.setItemMeta(meta);

            secondInv.addItem(item);
        }

        staff.openInventory(secondInv);
    }

    // Opening the last GUI based on the selected backup from the previous step, and displaying all items
    private void restoreMenu(final String selectedPlayerName, final Player staff) {

        staff.closeInventory();

        final Inventory lastInv = Bukkit.createInventory(staff, 54, "Inventory of " + selectedPlayerName);

        final FileConfiguration f = YamlConfiguration.loadConfiguration(this.backupFile);

        final ItemStack[] invContent = InventoryToBase64.fromBase64(f.getString("inventory"));
        for (ItemStack item : invContent)
            lastInv.addItem(item);

        final ItemStack[] armorContent = InventoryToBase64.fromBase64(f.getString("armor"));
        for (ItemStack item : armorContent)
            lastInv.addItem(item);

        lastInv.setItem(45, createItemStack(Material.ENDER_CHEST, ChatColor.RED + "Back", null));
        lastInv.setItem(49, createItemStack(Material.EMERALD_BLOCK, ChatColor.AQUA + "Backup",
                Collections.singletonList(ChatColor.RED + "Clears player's current Inventory!")));

        staff.openInventory(lastInv);
    }

    private ItemStack createSkull(final Player onlinePlayer) {

        final ItemStack skull = new ItemStack(type, 1);

        if (!isNewVersion)
            skull.setDurability((short) 3);

        final SkullMeta meta = (SkullMeta) skull.getItemMeta();

        meta.setOwner(onlinePlayer.getName());

        meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + onlinePlayer.getName());

        final List<String> lore = new ArrayList<>(Collections.singletonList(ChatColor.WHITE + "Backups Available: " + ChatColor.AQUA + PlayerFolder.backupCount(onlinePlayer.getName())));

        meta.setLore(lore);

        skull.setItemMeta(meta);

        return skull;
    }

    private ItemStack createItemStack(final Material material, final String name, final List<String> lore) {

        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(name);

        if (lore != null)
            meta.setLore(lore);

        item.setItemMeta(meta);
        return item;
    }

    private void addItem(final Player selectedPlayer, final Player staff) {

        final FileConfiguration f = YamlConfiguration.loadConfiguration(this.backupFile);

        final ItemStack[] invContent = InventoryToBase64.fromBase64(f.getString("inventory"));
        selectedPlayer.getInventory().setContents(invContent);

        final ItemStack[] armorContent = InventoryToBase64.fromBase64(f.getString("armor"));
        selectedPlayer.getInventory().setArmorContents(armorContent);

        staff.sendMessage(ChatColor.translateAlternateColorCodes('&', pluginPrefix + "Inventory Restored."));
        selectedPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', pluginPrefix + "&8&l[ &6&lYour inventory has been restored! &8&l]"));
    }
}
