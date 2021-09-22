package com.carpour.logger;

import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Discord.DiscordFile;
import com.carpour.logger.Events.*;
import com.carpour.logger.Database.MySQL.*;
import com.carpour.logger.Utils.*;
import com.carpour.logger.Database.SQLite.SQLite;
import com.carpour.logger.Database.SQLite.SQLiteData;
import com.carpour.logger.Commands.CommandManager;
import com.carpour.logger.ServerSide.*;
import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.IEssentials;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class Main extends JavaPlugin {

    private static Main instance;

    public FileHandler fileHandler;

    public MySQL mySQL;
    public MySQLData mySQLData;

    private SQLite sqLite;
    private SQLiteData sqLiteData;

    public ASCIIArt icon;

    public Start start;
    public Stop stop;

    public Discord discord;

    public SQLite getSqLite() {
        return sqLite;
    }

    public SQLiteData getSqLiteData() {
        return sqLiteData;
    }

    @Override
    public void onEnable() {

        instance = this;

        saveDefaultConfig();
        getConfig().options().copyDefaults();


        DiscordFile.Setup();
        DiscordFile.values();
        DiscordFile.get().options().copyDefaults(true);
        DiscordFile.save();

        discord = new Discord();
        discord.run();

        if (getConfig().getBoolean("MySQL.Enable")) {
            mySQL = new MySQL();
            mySQL.connect();
            mySQLData = new MySQLData(this);
            if (mySQL.isConnected()) mySQLData.createTable();
            mySQLData.emptyTable();
        }

        if (getConfig().getBoolean("SQLite.Enable")) {
            sqLite = new SQLite();
            sqLite.connect();
            sqLiteData = new SQLiteData(this);
            if (sqLite.isConnected()) {
                sqLiteData.createTable();
            }
            sqLiteData.emptyTable();
        }

        if (getConfig().getBoolean("Log-to-Files") && (getConfig().getBoolean("SQLite.Enable"))){

            getLogger().warning("Logging to Files and SQLite are both enabled, it might impact your Server's Performance!");

        }

        new FileHandler(getDataFolder());
        fileHandler = new FileHandler(getDataFolder());
        fileHandler.deleteFiles();

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
        //getServer().getPluginManager().registerEvents(new onAnvil(), this);
        getServer().getPluginManager().registerEvents(new onItemDrop(), this);
        getServer().getPluginManager().registerEvents(new onEnchant(), this);
        getServer().getPluginManager().registerEvents(new onBook(), this);

        getServer().getScheduler().scheduleSyncRepeatingTask(this, new TPS(), 200L, getConfig().getInt("RAM-TPS-Checker"));
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new RAM(), 200L, getConfig().getInt("RAM-TPS-Checker"));

        Objects.requireNonNull(getCommand("logger")).setExecutor(new CommandManager());

        if (getAPI() != null){

            getServer().getPluginManager().registerEvents(new onAFK(), this);

            getServer().getLogger().info("[Logger] Essentials Plugin was Found!");

        }

        icon = new ASCIIArt();
        icon.Art();

        //bstats

        new Metrics(this, 12036);

        //Update Checker
        UpdateChecker updater = new UpdateChecker(this);
        updater.checkForUpdate();

        getServer().getConsoleSender().sendMessage("[Logger] " + ChatColor.GOLD + "Thank you " + ChatColor.GREEN + ChatColor.BOLD + "thelooter" + ChatColor.GOLD + " for the Contribution!");

        getServer().getConsoleSender().sendMessage("[Logger] " + ChatColor.GREEN + "Plugin Enabled!");

        start = new Start();
        start.run();
    }

    public static Main getInstance() { return instance; }

    public Essentials getAPI(){

        IEssentials essentials = (IEssentials) Bukkit.getPluginManager().getPlugin("Essentials");

        if (essentials instanceof Essentials){

            return (Essentials) essentials;

        }else return null;
    }

    @Override
    public void onDisable() {

        stop = new Stop();
        stop.run();

        if ((getConfig().getBoolean("MySQL.Enable")) && (mySQL.isConnected())) mySQL.disconnect();

        if ((getConfig().getBoolean("SQLite.Enable")) && (sqLite.isConnected())) sqLite.disconnect();

        discord.disconnect();

        getServer().getConsoleSender().sendMessage("[Logger] " + ChatColor.RED + "Plugin Disabled!");

    }
}