package me.prism3.logger.commands.subcommands;

import me.prism3.logger.Main;
import me.prism3.logger.commands.SubCommand;
import me.prism3.logger.utils.FileHandler;
import me.prism3.logger.utils.playerdeathutils.InventoryToBase64;
import me.prism3.logger.utils.playerdeathutils.PlayerFolder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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
import java.util.stream.Collectors;

import static me.prism3.logger.utils.Data.pluginPrefix;

public class PlayerInventory implements Listener, SubCommand {

    private Player selectedPlayer;
    private Player selectedBy;
    private File backupFile;

    @Override
    public String getName() { return "playerinventory"; }

    @Override
    public String getDescription() { return "Opens a menu with all online players and their available inventory backups."; }

    @Override
    public String getSyntax() { return "/logger playerinventory"; }

    @Override
    public void perform(CommandSender commandSender, String[] args) { this.stepOne(commandSender); }

    @Override
    public List<String> getSubCommandsArgs(CommandSender commandSender, String[] args) { return Collections.emptyList(); }

    // Opening the first GUI with all online players and their available backups
    private void stepOne(CommandSender commandSender) {

        if (!(commandSender instanceof Player)) {

            Main.getInstance().getLogger().severe("This command can only be executed in game!");
            return;
        }

        final Player player = (Player) commandSender;

        final Inventory firstInv = Bukkit.createInventory(null, 27, "Player Inventory Checker");

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {

            final boolean isNewVersion = Arrays.stream(Material.values()).map(Material::name).collect(Collectors.toList()).contains("PLAYER_HEAD");

            final Material type = Material.matchMaterial(isNewVersion ? "PLAYER_HEAD" : "SKULL_ITEM");

            final ItemStack skull = new ItemStack(type, 1);

            if (!isNewVersion) skull.setDurability((short) 3);

            final SkullMeta meta = (SkullMeta) skull.getItemMeta();

            meta.setOwner(onlinePlayer.getName());
            meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + onlinePlayer.getName());
            final ArrayList<String> skullMeta = new ArrayList<>();
            skullMeta.add(ChatColor.WHITE + "Backups Available: " + ChatColor.AQUA + PlayerFolder.backupCount(onlinePlayer));
            meta.setLore(skullMeta);
            skull.setItemMeta(meta);

            firstInv.addItem(skull);
        }

        player.openInventory(firstInv);
    }


    @EventHandler
    public void onClick(final InventoryClickEvent event) {

        if (event.getClickedInventory() == null) return;

        final String title = event.getView().getTitle();

        if (title.equals("Player Inventory Checker") || title.startsWith("Inventory") || title.startsWith("Backup(s)")) {

            event.setCancelled(true);

            this.selectedBy = (Player) event.getWhoClicked();

            if (event.getCurrentItem() == null || !event.getCurrentItem().hasItemMeta()) return;

            final String clickedItem = Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getDisplayName();

            boolean isPresent = false;

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {

                if (clickedItem.equals(ChatColor.GOLD + "" + ChatColor.BOLD + onlinePlayer.getName())) {

                    this.selectedPlayer = onlinePlayer;
                    this.stepTwo();
                    break;
                }

                if (event.getCurrentItem().getType() == Material.CHEST) {

                    for (String list : PlayerFolder.fileNames(onlinePlayer)) {

                        if (list.equalsIgnoreCase(clickedItem)) {

                            isPresent = true;
                            this.backupFile = new File(FileHandler.getPlayerDeathBackupLogFolder(), onlinePlayer.getName() + File.separator + list);
                            this.stepThree();
                            break;
                        }
                    }

                    if (isPresent) break;
                }

                if (event.getCurrentItem().getType() == Material.ENDER_CHEST) { this.stepTwo(); }

                if (event.getCurrentItem().getType() == Material.EMERALD_BLOCK) {

                    try {

                        this.addItem();

                    } catch (Exception except) {

                        this.selectedBy.sendMessage(ChatColor.translateAlternateColorCodes('&', pluginPrefix + "An error has occurred whilst restoring " + selectedPlayer + "'s Inventory"));
                        except.printStackTrace();
                    }
                    break;
                }
            }
        }
    }

    // Opening the second GUI after selecting the player and displaying all available backups
    private void stepTwo() {

        if (PlayerFolder.fileNames(this.selectedPlayer) == null || PlayerFolder.backupCount(this.selectedPlayer) == 0) return;

        this.selectedBy.closeInventory();

        int i = PlayerFolder.backupCount(this.selectedPlayer);

        final Inventory secondInv = Bukkit.createInventory(this.selectedBy, 27,  "Backup(s) of " + this.selectedPlayer.getName());

        final String[] files = PlayerFolder.fileNames(this.selectedPlayer);

        for (int e = 0; e < i; e++) {

            final ItemStack chest = new ItemStack(Material.CHEST);

            final ItemMeta chestMeta = chest.getItemMeta();

            assert chestMeta != null;
            chestMeta.setDisplayName(files[e]);

            chest.setItemMeta(chestMeta);

            secondInv.setItem(e, chest);
        }

        this.selectedBy.openInventory(secondInv);
    }

    // Opening the last GUI based on the selected backup from the previous step, and displaying all items
    public void stepThree() {

        this.selectedBy.closeInventory();

        final Inventory lastInv = Bukkit.createInventory(this.selectedBy, 54, "Inventory of " + this.selectedPlayer.getName());

        final FileConfiguration f = YamlConfiguration.loadConfiguration(this.backupFile);

        final ItemStack[] invContent = InventoryToBase64.stacksFromBase64(f.getString("inventory"));
        final ItemStack[] armorContent = InventoryToBase64.stacksFromBase64(f.getString("armor"));
        final ItemStack backButton = new ItemStack(Material.ENDER_CHEST);

        final ItemMeta backButtonMeta = backButton.getItemMeta();

        assert backButtonMeta != null;
        backButtonMeta.setDisplayName(ChatColor.RED + "Back");

        backButton.setItemMeta(backButtonMeta);

        final ItemStack backupButton = new ItemStack(Material.EMERALD_BLOCK);

        final ItemMeta backupButtonMeta = backupButton.getItemMeta();
        assert backupButtonMeta != null;
        backupButtonMeta.setDisplayName(ChatColor.AQUA + "Backup");
        final List<String> backupButtonLore = new ArrayList<>();
        backupButtonLore.add(ChatColor.RED + "Clears player's current Inventory!");
        backupButtonMeta.setLore(backupButtonLore);

        backupButton.setItemMeta(backupButtonMeta);

        for (int i = 0; i < Objects.requireNonNull(invContent).length; i++)
            lastInv.setItem(i, invContent[i]);

        if (armorContent.length != 0) {

            lastInv.setItem(36, armorContent[0]);
            lastInv.setItem(37, armorContent[1]);
            lastInv.setItem(38, armorContent[2]);
            lastInv.setItem(39, armorContent[3]);
        }

        lastInv.setItem(45, backButton);
        lastInv.setItem(49, backupButton);

        this.selectedBy.openInventory(lastInv);
    }

    private void addItem() {

        final FileConfiguration f = YamlConfiguration.loadConfiguration(this.backupFile);

        this.selectedPlayer.getInventory().clear();

        final ItemStack[] invContent = InventoryToBase64.stacksFromBase64(f.getString("inventory"));
        final ItemStack[] armorContent = InventoryToBase64.stacksFromBase64(f.getString("armor"));

        for (int i = 0; i < Objects.requireNonNull(invContent).length; i++)
            this.selectedPlayer.getInventory().setItem(i, invContent[i]);

        this.selectedPlayer.getInventory().setArmorContents(armorContent);

        this.selectedBy.sendMessage(ChatColor.translateAlternateColorCodes('&', pluginPrefix + "Inventory Restored."));
        this.selectedPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', pluginPrefix + "&8&l[ &6&lYour inventory has been restored! &8&l]"));
    }
}
