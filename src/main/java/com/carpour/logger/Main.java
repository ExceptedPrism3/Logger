package com.carpour.logger;

import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Discord.DiscordFile;
import com.carpour.logger.Events.*;
import com.carpour.logger.MySQL.*;
import com.carpour.logger.Utils.*;
import com.carpour.logger.onCommands.LoggerCommand;
import com.carpour.logger.ServerSide.*;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Objects;

public class Main extends JavaPlugin {

    private static Main instance;

//    private Database db;
    public FileHandler FH;
    public MySQL SQL;
    public MySQLData Data;
    public ASCIIArt Icon;
    public Start start;
    public Stop stop;
    public Discord discord;

    public void onEnable() {

        instance = this;

        getConfig().options().copyDefaults();
        saveDefaultConfig();
        DiscordFile.Setup();
        DiscordFile.values();
        DiscordFile.get().options().copyDefaults(true);
        DiscordFile.save();
        discord = new Discord();
        discord.run();

//        this.db = new SQLite(this);
//        this.db.load();

        if (getConfig().getBoolean("MySQL.Enable")) {
            SQL = new MySQL();
            SQL.connect();
            Data = new MySQLData(this);
            if (SQL.isConnected()) Data.createTable();
            Data.emptyTable();
        }

        new FileHandler(getDataFolder());
        FH = new FileHandler(getDataFolder());
        FH.deleteFiles();

        getServer().getPluginManager().registerEvents(new onChat(), this);
        getServer().getPluginManager().registerEvents(new onCommand(), this);
        getServer().getPluginManager().registerEvents(new Console(), this);
        getServer().getPluginManager().registerEvents(new onSign(), this);
        getServer().getPluginManager().registerEvents(new onPlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new onPlayerLeave(), this);
        getServer().getPluginManager().registerEvents(new onPlayerKick(), this);
        getServer().getPluginManager().registerEvents(new onPlayerDeath(), this);
        getServer().getPluginManager().registerEvents(new onPlayerTeleport(), this);
        getServer().getPluginManager().registerEvents(new onPlayerLevel(), this);
        getServer().getPluginManager().registerEvents(new onBlockPlace(), this);
        getServer().getPluginManager().registerEvents(new onBlockBreak(), this);
        getServer().getPluginManager().registerEvents(new PortalCreation(), this);
        getServer().getPluginManager().registerEvents(new onBucketPlace(), this);
        getServer().getPluginManager().registerEvents(new onAnvil(), this);
        getServer().getPluginManager().registerEvents(new onItemDrop(), this);
        getServer().getPluginManager().registerEvents(new onEnchant(), this);

        getServer().getScheduler().scheduleSyncRepeatingTask(this, new TPS(), 200L, getConfig().getInt("RAM-TPS-Checker"));
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new RAM(), 200L, getConfig().getInt("RAM-TPS-Checker"));

        Objects.requireNonNull(getCommand("logger")).setExecutor(new LoggerCommand());

        Icon = new ASCIIArt();
        Icon.Art();

        //bstats

        new Metrics(this, 12036);

        //Update Checker
        UpdateChecker updater = new UpdateChecker(this);
        updater.checkForUpdate();

        getServer().getConsoleSender().sendMessage("[Logger] " + ChatColor.GREEN + "Plugin Enabled!");

        start = new Start();
        start.run();

        //getServer().getConsoleSender().sendMessage("\n\n[Logger] " + ChatColor.GOLD + "This is a DEV Build, please report any issues at " + ChatColor.BLUE + "https://discord.gg/MfR5mcpVfX\n\n");

    }

    public static Main getInstance() { return instance; }

    public void onDisable() {

        stop = new Stop();
        stop.run();

        if ((getConfig().getBoolean("MySQL.Enable")) && ((SQL.isConnected()))) SQL.disconnect();

        discord.disconnect();

        getServer().getConsoleSender().sendMessage("[Logger] " + ChatColor.RED + "Plugin Disabled!");

    }
}