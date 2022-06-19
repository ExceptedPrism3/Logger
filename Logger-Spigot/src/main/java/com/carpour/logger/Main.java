package com.carpour.logger;

import com.carpour.logger.commands.OnLogger;
import com.carpour.logger.discord.SpigotDiscordOptions;
import com.carpour.logger.events.*;
import com.carpour.logger.events.oncommands.OnCommand;
import com.carpour.logger.events.oninventories.OnFurnace;
import com.carpour.logger.serverside.*;
import com.carpour.logger.utils.*;
import com.carpour.logger.logging.SpigotLoggingOptions;
import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.IEssentials;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.carour.loggercore.database.mysql.MySQL;
import org.carour.loggercore.database.mysql.MySQLData;
import org.carour.loggercore.database.postgresql.PostgreSQL;
import org.carour.loggercore.database.postgresql.PostgreSQLData;
import org.carour.loggercore.database.sqlite.SQLite;
import org.carour.loggercore.database.sqlite.SQLiteData;
import org.carour.loggercore.discord.Discord;
import org.carour.loggercore.discord.DiscordOptions;
import org.carour.loggercore.logging.LoggingOptions;
import org.carour.loggercore.util.SqlConfiguration;

import java.io.File;
import java.util.Objects;

@Getter
@Setter
public class Main extends JavaPlugin {

	private static Main instance;

	private MySQL mySQL;
	private MySQLData mySQLData;

	private SQLite sqLite;
	private SQLiteData sqLiteData;

	private PostgreSQL postgreSQL;
	private PostgreSQLData postgreSQLData;

	private Start start;
  private Stop stop;

	public Discord discord;
	private DiscordOptions discordOptions;

	private LoggingOptions loggingOptions;

	@Override
	public void onEnable() {
		instance = this;

		saveDefaultConfig();
		getConfig().options().copyDefaults();

		Messages.Setup();
		Messages.get().options().copyDefaults(true);
		Messages.save();

		discordOptions = new SpigotDiscordOptions(new File(getDataFolder(), "discord.yml"));

		discord = new Discord(discordOptions, getLogger());
		discord.run();

		loggingOptions = new SpigotLoggingOptions(new File(getDataFolder(), "config.yml"));

		if (getConfig().getBoolean("MySQL.Enable")) {

			SqlConfiguration sqlConfiguration = new SqlConfiguration(
					getConfig().getString("MySQL.Host"),
					getConfig().getInt("MySQL.Port"),
					getConfig().getString("MySQL.Database"),
					getConfig().getString("MySQL.Username"),
					getConfig().getString("MySQL.Password")
			);

			mySQL = new MySQL(sqlConfiguration, getLogger());
			mySQL.connect();
			mySQLData = new MySQLData(mySQL, loggingOptions, getLogger());
			if (mySQL.isConnected())
				mySQLData.createTable();
			mySQLData.emptyTable();

		}

		if (getConfig().getBoolean("SQLite.Enable")) {

			sqLite = new SQLite(getDataFolder(), getLogger());
			sqLite.connect();
			sqLiteData = new SQLiteData(sqLite, loggingOptions, getLogger());
			if (sqLite.isConnected()) {
				sqLiteData.createTable();
			}
			sqLiteData.emptyTable();

		}

		if (getConfig().getBoolean("PostgreSQL.Enable")) {

			SqlConfiguration sqlConfiguration = new SqlConfiguration(getConfig().getString("PostgreSQL.Host"),
					Integer.parseInt(getConfig().getString("PostgreSQL.Port")),
					getConfig().getString("PostgreSQL.Database"),
					getConfig().getString("PostgreSQL.Username"),
					getConfig().getString("PostgreSQL.Password"));

			postgreSQL = new PostgreSQL(sqlConfiguration, getLogger());
			postgreSQL.connect();

			postgreSQLData = new PostgreSQLData(postgreSQL, loggingOptions, getLogger());

			if (postgreSQL.isConnected()) {
				postgreSQLData.createTable();
			}
			postgreSQLData.emptyTable();

		}

		if (getConfig().getBoolean("Log-to-Files") && getConfig().getBoolean("SQLite.Enable")) {

			getLogger().warning("Logging to Files and SQLite are both enabled, this might impact your Server's Performance!");

		}

		FileHandler fileHandler = new FileHandler(getDataFolder());
		fileHandler.deleteFiles();

		getServer().getPluginManager().registerEvents(new OnChat(), this);
		getServer().getPluginManager().registerEvents(new OnCommand(), this);
		getServer().getPluginManager().registerEvents(new Console(), this);
		getServer().getPluginManager().registerEvents(new OnSign(), this);
		getServer().getPluginManager().registerEvents(new OnPlayerJoin(), this);
		getServer().getPluginManager().registerEvents(new OnPlayerLeave(), this);
		getServer().getPluginManager().registerEvents(new OnPlayerKick(), this);
		getServer().getPluginManager().registerEvents(new OnPlayerDeath(), this);
		getServer().getPluginManager().registerEvents(new OnPlayerTeleport(), this);
		getServer().getPluginManager().registerEvents(new OnPlayerLevel(), this);
		getServer().getPluginManager().registerEvents(new OnBlockPlace(), this);
		getServer().getPluginManager().registerEvents(new OnBlockBreak(), this);
		getServer().getPluginManager().registerEvents(new PortalCreation(), this);
		getServer().getPluginManager().registerEvents(new OnBucketPlace(), this);
		getServer().getPluginManager().registerEvents(new OnAnvil(), this);
		getServer().getPluginManager().registerEvents(new OnItemPickup(), this);
		getServer().getPluginManager().registerEvents(new OnItemDrop(), this);
		getServer().getPluginManager().registerEvents(new OnEnchant(), this);
		getServer().getPluginManager().registerEvents(new OnBook(), this);

		getServer().getPluginManager().registerEvents(new OnFurnace(), this);
		//        getServer().getPluginManager().registerEvents(new onShulker(), this);
		//        getServer().getPluginManager().registerEvents(new OnChest(), this);

		getServer().getScheduler().scheduleSyncRepeatingTask(this, new TPS(), 200L, getConfig().getInt("RAM-TPS-Checker"));
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new RAM(), 200L, getConfig().getInt("RAM-TPS-Checker"));

		Objects.requireNonNull(getCommand("logger")).setExecutor(new OnLogger());

		if (getAPI() != null) {

			getServer().getPluginManager().registerEvents(new OnAFK(), this);

			getServer().getLogger().info("[Logger] Essentials Plugin was Found!");

		}

		new ASCIIArt().art();

		//bstats

		new Metrics(this, 12036);

		//Update Checker
		new UpdateChecker(this).checkForUpdate();

		getServer().getConsoleSender().sendMessage(
				"[Logger] " + ChatColor.GOLD + "Thank you " + ChatColor.GREEN + ChatColor.BOLD + "thelooter" + ChatColor.GOLD + " for the Contribution!");

		getServer().getConsoleSender().sendMessage("[Logger] " + ChatColor.GREEN + "Plugin Enabled!");

		start = new Start(this);
		start.run();
	}

	public static Main getInstance() {
		return instance;
	}


	public Essentials getAPI() {

		IEssentials essentials = (IEssentials) Bukkit.getPluginManager().getPlugin("Essentials");

		if (essentials instanceof Essentials) {

			return (Essentials) essentials;

		} else
			return null;
	}

	@Override
	public void onDisable() {

		stop = new Stop();
		stop.run();

		if (getConfig().getBoolean("MySQL.Enable") && mySQL.isConnected())
			mySQL.disconnect();

		if (getConfig().getBoolean("SQLite.Enable") && sqLite.isConnected())
			sqLite.disconnect();

		discord.disconnect();

		getServer().getConsoleSender().sendMessage("[Logger] " + ChatColor.RED + "Plugin Disabled!");

	}
}