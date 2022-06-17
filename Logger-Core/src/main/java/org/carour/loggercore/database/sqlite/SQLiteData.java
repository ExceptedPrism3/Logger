package org.carour.loggercore.database.sqlite;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.carour.loggercore.logging.LoggingOptions;

import java.net.InetSocketAddress;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SQLiteData {

	private SQLite sqLite;
	private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss:SSSXXX");

	LoggingOptions options;

	Logger logger;

	public SQLiteData(SQLite sqLite, LoggingOptions options, Logger logger) {
		this.sqLite = sqLite;
		this.options = options;
		this.logger = logger;
	}

	public void createTable() {

		PreparedStatement playerChat, playerCommands, consoleCommands, playerSignText, playerJoin, playerLeave, playerDeath, playerTeleport, blockPlace, blockBreak, TPS, RAM, playerKick, portalCreation, playerLevel, bucketPlace, anvil, serverStart, serverStop, itemDrop, enchant, bookEditing, afk, itemPickup, furnace;

		try {
			playerChat = sqLite.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Player_Chat " +
					"(Server_Name TEXT(30),Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY , World TEXT(100), Player_Name TEXT(20)," +
					"Message TEXT(256), Is_Staff INTEGER)");

			playerCommands = sqLite.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Player_Commands" +
					"(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, World TEXT(100),Player_Name TEXT(100)," +
					"Command TEXT(100),Is_Staff INTEGER)");

			consoleCommands = sqLite.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Console_Commands" +
					"(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY,Command TEXT(256))");

			playerSignText = sqLite.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Player_Sign_Text" +
					"(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY ,World TEXT(50)," +
					" X INTEGER, Y INTEGER, Z INTEGER," +
					"Player_Name TEXT(100), Line TEXT(100), Is_Staff INTEGER)");

			playerJoin = sqLite.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Player_Join" +
					"(Server_Name TEXT(30), Date TIMESTAMP DEFAULT(STRFTIME('%Y-%m-%d %H:%M:%f', 'now'))  PRIMARY KEY ,World TEXT(50)," +
					"Player_Name TEXT(50),X INTEGER, Y INTEGER, Z INTEGER, IP TEXT, Is_Staff INTEGER)");

			playerLeave = sqLite.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS  Player_Leave " +
					"(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY ,World TEXT(50), X INTEGER, Y INTEGER, Z INTEGER , Player_Name TEXT(100), Is_Staff INTEGER)");

			playerDeath = sqLite.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Player_Death" +
					"(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, World TEXT(100), Player_Name TEXT(100)," +
					"X INTEGER, Y INTEGER, Z INTEGER, Cause TEXT(100), By_Who TEXT(100), Item_Used TEXT(100), Is_Staff INTEGER)");

			playerTeleport = sqLite.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Player_Teleport" +
					"(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, World TEXT(100), Player_Name TEXT(100)," +
					"From_X INTEGER, From_Y INTEGER, From_Z INTEGER, To_X INTEGER, To_Y INTEGER, To_Z INTEGER, Is_Staff INTEGER)");

			blockPlace = sqLite.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Block_Place" +
					"(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, World TEXT(100), Player_Name TEXT(100)," +
					"Block TEXT(40),X INTEGER, Y INTEGER, Z INTEGER, Is_Staff INTEGER)");

			blockBreak = sqLite.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Block_Break" +
					"(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY , World TEXT(100), Player_Name TEXT(100)," +
					"Block TEXT(40), X INTEGER, Y INTEGER, Z INTEGER, Is_Staff INTEGER)");

			TPS = sqLite.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS TPS" +
					"(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, TPS INTEGER )");

			RAM = sqLite.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS RAM" +
					"(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY," +
					" Total_Memory INTEGER, Used_Memory INTEGER, Free_Memory INTEGER)");

			playerKick = sqLite.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Player_Kick" +
					"(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY,World TEXT(100), Player_Name TEXT(100)," +
					" X INTEGER, Y INTEGER, Z INTEGER, Reason TEXT(50), Is_Staff INTEGER )");

			portalCreation = sqLite.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Portal_Creation" +
					"(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, World TEXT(50), Caused_By TEXT(50))");

			playerLevel = sqLite.getConnection().prepareStatement("CREATE TABLE  IF NOT EXISTS Player_Level" +
					"(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, Player_Name TEXT(100), Is_Staff INTEGER )");

			bucketPlace = sqLite.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Bucket_Place" +
					"(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY ,World TEXT(100), Player_Name TEXT(100)," +
					"Bucket TEXT(40), X INTEGER, Y INTEGER, Z INTEGER, Is_Staff INTEGER)");

			anvil = sqLite.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Anvil" +
					"(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY ,Player_Name TEXT(100), New_Name TEXT(100), Is_Staff INTEGER)");

			serverStart = sqLite.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Server_Start" +
					"(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY)");

			serverStop = sqLite.getConnection().prepareStatement("CREATE TABLE  IF NOT EXISTS Server_Stop" +
					"(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY )");

			itemDrop = sqLite.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Item_Drop" +
					"(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, World TEXT(100), Player_Name TEXT(100)," +
					"Item TEXT(50), Amount INTEGER, X INTEGER, Y INTEGER, Z INTEGER, Enchantment TEXT(50), Changed_Name TEXT(50), Is_Staff INTEGER)");

			enchant = sqLite.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Enchanting" +
					"(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, World TEXT(100), Player_Name TEXT(100)," +
					"X INTEGER, Y INTEGER, Z INTEGER, Enchantment TEXT(50), Item TEXT(50), Cost INTEGER, Is_Staff INTEGER)");

			bookEditing = sqLite.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Book_Editing" +
					"(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, World TEXT(100), Player_Name TEXT(100)," +
					"Page_Count INTEGER, Page_Content TEXT(100), Signed_by TEXT(25), Is_Staff INTEGER)");

			afk = sqLite.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS AFK" +
					"(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, World TEXT(100), Player_Name TEXT(100)," +
					"X INTEGER, Y INTEGER, Z INTEGER, Is_Staff INTEGER)");

			itemPickup = sqLite.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Item_Pickup" +
					"(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, World TEXT(100), Player_Name TEXT(100)," +
					"Item TEXT(50), Amount INTEGER, X INTEGER, Y INTEGER, Z INTEGER, Changed_Name TEXT(100), Is_Staff INTEGER)");

			furnace = sqLite.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Furnace" +
					"(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, World TEXT(100), Player_Name TEXT(100)," +
					"Item TEXT(50), Amount INTEGER, X INTEGER, Y INTEGER, Z INTEGER, Is_Staff INTEGER)");

			playerChat.executeUpdate();
			playerCommands.executeUpdate();
			consoleCommands.executeUpdate();
			playerSignText.executeUpdate();
			playerDeath.executeUpdate();
			playerTeleport.executeUpdate();
			playerJoin.executeUpdate();
			playerLeave.executeUpdate();
			blockPlace.executeUpdate();
			blockBreak.executeUpdate();
			TPS.executeUpdate();
			RAM.executeUpdate();
			playerKick.executeUpdate();
			portalCreation.executeUpdate();
			playerLevel.executeUpdate();
			bucketPlace.executeUpdate();
			anvil.executeUpdate();
			serverStart.executeUpdate();
			serverStop.executeUpdate();
			itemDrop.executeUpdate();
			enchant.executeUpdate();
			bookEditing.executeUpdate();
			afk.executeUpdate();
			itemPickup.executeUpdate();
			furnace.executeUpdate();

		} catch (SQLException exception) {
			exception.printStackTrace();
		}
	}

	public void insertPlayerChat(String serverName, String playerName, String worldName, String message, boolean staff) {

		try {
			PreparedStatement playerChat = sqLite.getConnection()
					.prepareStatement("INSERT INTO Player_Chat (Server_Name,Date, World, Player_Name, Message, Is_Staff) VALUES (?,?,?,?,?,?)");
			playerChat.setString(1, serverName);
			playerChat.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
			playerChat.setString(3, playerName);
			playerChat.setString(4, worldName);
			playerChat.setString(5, message);
			playerChat.setBoolean(6, staff);

			playerChat.executeUpdate();
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}

	public void insertPlayerCommands(String serverName, String playerName, String worldName, String command, boolean staff) {
		try {
			PreparedStatement playerCommands = sqLite.getConnection()
					.prepareStatement("INSERT INTO Player_Commands(Server_Name,Date, World, Player_Name, Command, Is_Staff) VALUES (?,?,?,?,?,?)");
			playerCommands.setString(1, serverName);
			playerCommands.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
			playerCommands.setString(3, worldName);
			playerCommands.setString(4, playerName);
			playerCommands.setString(5, command);
			playerCommands.setBoolean(6, staff);

			playerCommands.executeUpdate();

		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}

	public void insertConsoleCommands(String servername, String commanmd) {
		try {
			PreparedStatement consoleCommands = sqLite.getConnection()
					.prepareStatement("INSERT INTO Console_Commands(Server_Name, Date, Command) VALUES (?,?,?)");
			consoleCommands.setString(1, servername);
			consoleCommands.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
			consoleCommands.setString(3, commanmd);

			consoleCommands.executeUpdate();
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}

	public void insertPlayerSignText(String serverName, String playerName, String worldName, int x, int y, int z, String lines, boolean isStaff) {
		try {
			PreparedStatement playerSignText = sqLite.getConnection().prepareStatement(
					"INSERT INTO Player_Sign_Text(Server_Name, Date, World, X, Y, Z, Player_Name, Line, Is_Staff) VALUES (?,?,?,?,?,?,?,?,?)");
			playerSignText.setString(1, serverName);
			playerSignText.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
			playerSignText.setString(3, worldName);
			playerSignText.setInt(4, x);
			playerSignText.setInt(5, y);
			playerSignText.setInt(6, z);
			playerSignText.setString(7, playerName);
			playerSignText.setString(8, lines);
			playerSignText.setBoolean(9, isStaff);

		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}

	public void insertPlayerDeath(String serverName, String playerName, String worldName, int x, int y, int z, String cause, String by_who, String using,
			boolean isStaff) {
		try {
			PreparedStatement playerDeath = sqLite.getConnection().prepareStatement(
					"INSERT INTO Player_Death(Server_Name, Date, World, Player_Name, X, Y, Z, Cause, By_Who, Item_Used, Is_Staff) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
			playerDeath.setString(1, serverName);
			playerDeath.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
			playerDeath.setString(3, worldName);
			playerDeath.setString(4, playerName);
			playerDeath.setInt(5, x);
			playerDeath.setInt(6, y);
			playerDeath.setInt(7, z);
			playerDeath.setString(8, cause);
			playerDeath.setString(9, by_who);
			playerDeath.setString(10, using);
			playerDeath.setBoolean(11, isStaff);

			playerDeath.executeUpdate();

		} catch (SQLException exception) {
			exception.printStackTrace();
		}
	}

	public void insertPlayerTeleport(String serverName, String playerName, String worldName, int fromX, int fromY, int fromZ, int toX, int toY, int toZ,
			boolean isStaff) {
		try {
			PreparedStatement playerTeleport = sqLite.getConnection().prepareStatement(
					"INSERT INTO Player_Teleport(Server_Name, Date, World, Player_Name, From_X, From_Y, From_Z, To_X, To_Y, To_Z, Is_Staff) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
			playerTeleport.setString(1, serverName);
			playerTeleport.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
			playerTeleport.setString(3, worldName);
			playerTeleport.setString(4, playerName);
			playerTeleport.setInt(5, fromX);
			playerTeleport.setInt(6, fromY);
			playerTeleport.setInt(7, fromZ);
			playerTeleport.setInt(8, toX);
			playerTeleport.setInt(9, toY);
			playerTeleport.setInt(10, toZ);
			playerTeleport.setBoolean(11, isStaff);

			playerTeleport.executeUpdate();
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
	}

	public void insertPlayerJoin(String serverName, String playerName, String worldName, int x, int y, int z, InetSocketAddress ipAddress, boolean isStaff) {
		try {
			PreparedStatement playerJoin = sqLite.getConnection()
					.prepareStatement("INSERT INTO Player_Join(Server_Name, World, Player_Name, X, Y, Z, IP, Is_Staff) VALUES (?,?,?,?,?,?,?)");
			playerJoin.setString(1, serverName);
			playerJoin.setString(2, worldName);
			playerJoin.setString(3, playerName);
			playerJoin.setInt(4, x);
			playerJoin.setInt(5, y);
			playerJoin.setInt(6, z);
			if (options.isPlayerIpLoggingEnabled()) {
				playerJoin.setString(6, Objects.requireNonNull(ipAddress.getAddress()).getHostAddress());
			} else {
				playerJoin.setString(6, null);
			}
			playerJoin.setBoolean(7, isStaff);

			playerJoin.executeUpdate();
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
	}

	public void insertPlayerLeave(String serverName, String playerName, String worldName, int x, int y, int z, InetSocketAddress ipAddress, boolean isStaff) {
		try {
			PreparedStatement playerLeave = sqLite.getConnection()
					.prepareStatement("INSERT INTO Player_Leave(Server_Name, Date, World, X, Y, Z, Player_Name, Is_Staff) VALUES (?,?,?,?,?,?,?,?)");
			playerLeave.setString(1, serverName);
			playerLeave.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
			playerLeave.setString(3, worldName);
			playerLeave.setInt(4, x);
			playerLeave.setInt(5, y);
			playerLeave.setInt(6, z);
			playerLeave.setString(7, playerName);
			playerLeave.setBoolean(8, isStaff);

			playerLeave.executeUpdate();
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
	}

	public void insertBlockPlace(String serverName, String playerName, String worldName, String block, int x, int y, int z, boolean isStaff) {
		try {
			PreparedStatement blockPlace = sqLite.getConnection()
					.prepareStatement("INSERT INTO Block_Place(Server_Name, Date, World, Player_Name,Block, X, Y, Z, Is_Staff) VALUES (?,?,?,?,?,?,?,?,?)");
			blockPlace.setString(1, serverName);
			blockPlace.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
			blockPlace.setString(3, worldName);
			blockPlace.setString(4, playerName);
			blockPlace.setString(5, block);
			blockPlace.setInt(6, x);
			blockPlace.setInt(7, y);
			blockPlace.setInt(8, z);
			blockPlace.setBoolean(9, isStaff);

			blockPlace.executeUpdate();
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
	}

	public void insertBlockBreak(String serverName, String playerName, String worldName, String block, int x, int y, int z, boolean isStaff) {
		try {
			PreparedStatement blockBreak = sqLite.getConnection()
					.prepareStatement("INSERT INTO Block_Break(Server_Name, Date, World, Player_Name, Block, X, Y, Z, Is_Staff) VALUES (?,?,?,?,?,?,?,?,?)");
			blockBreak.setString(1, serverName);
			blockBreak.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
			blockBreak.setString(3, worldName);
			blockBreak.setString(4, playerName);
			blockBreak.setString(5, block);
			blockBreak.setInt(6, x);
			blockBreak.setInt(7, y);
			blockBreak.setInt(8, z);
			blockBreak.setBoolean(9, isStaff);

			blockBreak.executeUpdate();
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
	}

	public void insertTPS(String serverName, double tps) {
		try {
			PreparedStatement tpsStatement = sqLite.getConnection().prepareStatement("INSERT INTO TPS(Server_Name, Date, TPS) VALUES (?,?,?)");

			tpsStatement.setString(1, serverName);
			tpsStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
			tpsStatement.setDouble(3, tps);

			tpsStatement.executeUpdate();
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
	}

	public void insertRAM(String serverName, long totalMemory, long usedMemory, long freeMemory) {
		try {
			PreparedStatement ramStatement = sqLite.getConnection()
					.prepareStatement("INSERT INTO RAM(Server_Name, Date, Total_Memory, Used_Memory, Free_Memory) VALUES (?,?,?,?,?)");
			ramStatement.setString(1, serverName);
			ramStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
			ramStatement.setDouble(3, totalMemory);
			ramStatement.setDouble(4, usedMemory);
			ramStatement.setDouble(5, freeMemory);

			ramStatement.executeUpdate();
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
	}

	public void insertPlayerKick(String serverName, String playerName, String worldName, int x, int y, int z, String reason, boolean isStaff) {
		try {
			PreparedStatement playerKickStatement = sqLite.getConnection()
					.prepareStatement("INSERT INTO Player_Kick(Server_Name, Date, World, Player_Name, X, Y, Z, Reason, Is_Staff) VALUES (?,?,?,?,?,?,?,?,?)");
			playerKickStatement.setString(1, serverName);
			playerKickStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
			playerKickStatement.setString(3, worldName);
			playerKickStatement.setString(4, playerName);
			playerKickStatement.setInt(5, x);
			playerKickStatement.setInt(6, y);
			playerKickStatement.setInt(7, z);
			playerKickStatement.setString(8, reason);
			playerKickStatement.setBoolean(9, isStaff);

			playerKickStatement.executeUpdate();
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}

	public void insertPortalCreate(String serverName, String worldName, String reason) {
		try {
			PreparedStatement portalCreateStatement = sqLite.getConnection()
					.prepareStatement("INSERT INTO Portal_Creation(Server_Name, Date, World, Caused_By) VALUES (?,?,?,?)");
			portalCreateStatement.setString(1, serverName);
			portalCreateStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
			portalCreateStatement.setString(3, worldName);
			portalCreateStatement.setString(4, reason);

			portalCreateStatement.executeUpdate();
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}

	public void insertLevelChange(String serverName, String playerName, boolean isStaff) {
		try {
			PreparedStatement levelChangeStatement = sqLite.getConnection()
					.prepareStatement("INSERT INTO Player_Level(Server_Name, Date, Player_Name, Is_Staff) VALUES (?,?,?,?)");
			levelChangeStatement.setString(1, serverName);
			levelChangeStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
			levelChangeStatement.setString(3, playerName);
			levelChangeStatement.setBoolean(4, isStaff);

			levelChangeStatement.executeUpdate();
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}

	public void insertBucketPlace(String serverName, String playerName, String worldName, int x, int y, int z, String bucket, boolean isStaff) {
		try {
			PreparedStatement bucketPlaceStatement = sqLite.getConnection()
					.prepareStatement("INSERT INTO Bucket_Place(Server_Name, Date, World, Player_Name, Bucket, X, Y, Z, Is_Staff) VALUES (?,?,?,?,?,?,?,?,?)");
			bucketPlaceStatement.setString(1, serverName);
			bucketPlaceStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
			bucketPlaceStatement.setString(3, worldName);
			bucketPlaceStatement.setString(4, playerName);
			bucketPlaceStatement.setString(5, bucket);
			bucketPlaceStatement.setInt(6, x);
			bucketPlaceStatement.setInt(7, y);
			bucketPlaceStatement.setInt(8, z);
			bucketPlaceStatement.setBoolean(9, isStaff);

			bucketPlaceStatement.executeUpdate();
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}

	public void insertAnvil(String serverName, String playerName, String newName, boolean isStaff) {
		try {
			PreparedStatement anvilStatement = sqLite.getConnection()
					.prepareStatement("INSERT INTO Anvil(Server_Name, Date, Player_Name,  New_Name, Is_Staff) VALUES (?,?,?,?,?)");
			anvilStatement.setString(1, serverName);
			anvilStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
			anvilStatement.setString(3, playerName);
			anvilStatement.setString(4, newName);
			anvilStatement.setBoolean(5, isStaff);

			anvilStatement.executeUpdate();
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}

	public void insertServerStart(String serverName) {
		try {
			PreparedStatement serverStartStatement = sqLite.getConnection().prepareStatement("INSERT INTO Server_Start(Server_Name, Date) VALUES (?,?)");
			serverStartStatement.setString(1, serverName);
			serverStartStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));

			serverStartStatement.executeUpdate();
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
	}

	public void insertServerStop(String serverName) {
		try {
			PreparedStatement serverStopStatement = sqLite.getConnection().prepareStatement("INSERT INTO Server_Stop(Server_Name, Date) VALUES (?,?)");
			serverStopStatement.setString(1, serverName);
			serverStopStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));

			serverStopStatement.executeUpdate();
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
	}

	public void insertItemDrop(String serverName, String playerName, String worldName, String item, int amount, int x, int y, int z, List<String> enchantment,
			String changedName, boolean isStaff) {
		try {
			PreparedStatement itemDropStatement = sqLite.getConnection().prepareStatement(
					"INSERT INTO Item_Drop(Server_Name, Date, World, Player_Name, Item, Amount, X, Y, Z, Enchantment, Changed_Name, Is_Staff) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");
			itemDropStatement.setString(1, serverName);
			itemDropStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
			itemDropStatement.setString(3, worldName);
			itemDropStatement.setString(4, playerName);
			itemDropStatement.setString(5, item);
			itemDropStatement.setInt(6, amount);
			itemDropStatement.setInt(7, x);
			itemDropStatement.setInt(8, y);
			itemDropStatement.setInt(9, z);
			itemDropStatement.setString(10, String.valueOf(enchantment));
			itemDropStatement.setString(11, changedName);
			itemDropStatement.setBoolean(12, isStaff);

			itemDropStatement.executeUpdate();
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
	}

	public void insertEnchant(String serverName, String playerName, String worldName, int x, int y, int z, List<String> enchantment, String item, int cost,
			boolean isStaff) {
		try {
			PreparedStatement enchantStatement = sqLite.getConnection().prepareStatement(
					"INSERT INTO Enchanting(Server_Name, Date, World, Player_Name, X, Y, Z, Enchantment, Item, Cost, Is_Staff) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
			enchantStatement.setString(1, serverName);
			enchantStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
			enchantStatement.setString(3, worldName);
			enchantStatement.setString(4, playerName);
			enchantStatement.setInt(5, x);
			enchantStatement.setInt(6, y);
			enchantStatement.setInt(7, z);
			enchantStatement.setString(8, String.valueOf(enchantment));
			enchantStatement.setString(9, item);
			enchantStatement.setInt(10, cost);
			enchantStatement.setBoolean(11, isStaff);

			enchantStatement.executeUpdate();
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
	}

	public void insertBook(String serverName, String world, String playername, int pages, List<String> content, String signed_by, boolean staff) {
		try {
			PreparedStatement book_editing = sqLite.getConnection().prepareStatement(
					"INSERT  INTO Book_Editing(Server_Name, Date, World, Player_Name, Page_Count, Page_Content, Signed_by, Is_Staff) VALUES(?,?,?,?,?,?,?,?)");
			book_editing.setString(1, serverName);
			book_editing.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
			book_editing.setString(3, world);
			book_editing.setString(4, playername);
			book_editing.setInt(5, pages);
			book_editing.setString(6, String.valueOf(content));
			book_editing.setString(7, signed_by);
			book_editing.setBoolean(8, staff);
			book_editing.executeUpdate();

		} catch (SQLException e) {

			e.printStackTrace();

		}
	}

	public void insertAFK(String serverName, String playerName, String worldName, int x, int y, int z, boolean isStaff) {

		try {
			PreparedStatement enchantStatement = sqLite.getConnection()
					.prepareStatement("INSERT INTO AFK (Server_Name, Date, World, Player_Name, X, Y, Z, Is_Staff) VALUES (?,?,?,?,?,?,?,?)");
			enchantStatement.setString(1, serverName);
			enchantStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
			enchantStatement.setString(3, worldName);
			enchantStatement.setString(4, playerName);
			enchantStatement.setInt(5, x);
			enchantStatement.setInt(6, y);
			enchantStatement.setInt(7, z);
			enchantStatement.setBoolean(8, isStaff);

			enchantStatement.executeUpdate();
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
	}

	public void insertItemPickup(String serverName, String playerName, String worldName, String item, int amount, int x, int y, int z, String changed_name,
			boolean isStaff) {
		try {
			PreparedStatement itemPickupStatement = sqLite.getConnection().prepareStatement(
					"INSERT INTO Item_Pickup(Server_Name, Date, World, Player_Name, Item, Amount, X, Y, Z, Changed_Name, Is_Staff) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
			itemPickupStatement.setString(1, serverName);
			itemPickupStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
			itemPickupStatement.setString(3, worldName);
			itemPickupStatement.setString(4, playerName);
			itemPickupStatement.setString(5, item);
			itemPickupStatement.setInt(6, amount);
			itemPickupStatement.setInt(7, x);
			itemPickupStatement.setInt(8, y);
			itemPickupStatement.setInt(9, z);
			itemPickupStatement.setString(10, changed_name);
			itemPickupStatement.setBoolean(11, isStaff);

			itemPickupStatement.executeUpdate();
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
	}

	public void insertFurnace(String serverName, String playerName, String worldName, String item, int amount, int x, int y, int z, boolean isStaff) {
		try {
			PreparedStatement furnaceStatement = sqLite.getConnection().prepareStatement(
					"INSERT INTO Furnace(Server_Name, Date, World, Player_Name, Item, Amount, X, Y, Z, Is_Staff) VALUES (?,?,?,?,?,?,?,?,?,?)");
			furnaceStatement.setString(1, serverName);
			furnaceStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
			furnaceStatement.setString(3, worldName);
			furnaceStatement.setString(4, playerName);
			furnaceStatement.setString(5, item);
			furnaceStatement.setInt(6, amount);
			furnaceStatement.setInt(7, x);
			furnaceStatement.setInt(8, y);
			furnaceStatement.setInt(9, z);
			furnaceStatement.setBoolean(10, isStaff);

			furnaceStatement.executeUpdate();
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
	}

	public void emptyTable() {

		int when = options.getTableCleanupTreshold();

		if (when <= 0)
			return;

		try {

			PreparedStatement player_Chat = sqLite.getConnection()
					.prepareStatement("DELETE FROM Player_Chat WHERE Date <= datetime('now','-" + when + " day')");

			PreparedStatement player_Commands = sqLite.getConnection()
					.prepareStatement("DELETE FROM Player_Commands WHERE Date <= datetime('now','-" + when + " day')");

			PreparedStatement console_Commands = sqLite.getConnection()
					.prepareStatement("DELETE FROM Console_Commands WHERE Date <= datetime('now','-" + when + " day')");

			PreparedStatement player_Sign_Text = sqLite.getConnection()
					.prepareStatement("DELETE FROM Player_Sign_Text WHERE Date <= datetime('now','-" + when + " day')");

			PreparedStatement player_Join = sqLite.getConnection()
					.prepareStatement("DELETE FROM Player_Join WHERE Date <= datetime('now','-" + when + " day')");

			PreparedStatement player_Leave = sqLite.getConnection()
					.prepareStatement("DELETE FROM Player_Leave WHERE Date <= datetime('now','-" + when + " day')");

			PreparedStatement player_Kick = sqLite.getConnection()
					.prepareStatement("DELETE FROM Player_Kick WHERE Date <= datetime('now','-" + when + " day')");

			PreparedStatement player_Death = sqLite.getConnection()
					.prepareStatement("DELETE FROM Player_Death WHERE Date <= datetime('now','-" + when + " day')");

			PreparedStatement player_Teleport = sqLite.getConnection()
					.prepareStatement("DELETE FROM Player_Teleport WHERE Date <= datetime('now','-" + when + " day')");

			PreparedStatement player_Level = sqLite.getConnection()
					.prepareStatement("DELETE FROM Player_Level WHERE Date <= datetime('now','-" + when + " day')");

			PreparedStatement block_Place = sqLite.getConnection()
					.prepareStatement("DELETE FROM Block_Place WHERE Date <= datetime('now','-" + when + " day')");

			PreparedStatement block_Break = sqLite.getConnection()
					.prepareStatement("DELETE FROM Block_Break WHERE Date <= datetime('now','-" + when + " day')");

			PreparedStatement portal_Creation = sqLite.getConnection()
					.prepareStatement("DELETE FROM Portal_Creation WHERE Date <= datetime('now','-" + when + " day')");

			PreparedStatement bucket_Place = sqLite.getConnection()
					.prepareStatement("DELETE FROM Bucket_Place WHERE Date <= datetime('now','-" + when + " day')");

			PreparedStatement anvil = sqLite.getConnection().prepareStatement("DELETE FROM Anvil WHERE Date <= datetime('now','-" + when + " day')");

			PreparedStatement TPS = sqLite.getConnection().prepareStatement("DELETE FROM TPS WHERE Date <= datetime('now','-" + when + " day')");

			PreparedStatement RAM = sqLite.getConnection().prepareStatement("DELETE FROM RAM WHERE Date <= datetime('now','-" + when + " day')");

			PreparedStatement server_Start = sqLite.getConnection()
					.prepareStatement("DELETE FROM Server_Start WHERE Date <= datetime('now','-" + when + " day')");

			PreparedStatement server_Stop = sqLite.getConnection()
					.prepareStatement("DELETE FROM Server_Stop WHERE Date <= datetime('now','-" + when + " day')");

			PreparedStatement item_Drop = sqLite.getConnection().prepareStatement("DELETE FROM Item_Drop WHERE Date <= datetime('now','-" + when + " day')");

			PreparedStatement enchanting = sqLite.getConnection().prepareStatement("DELETE FROM Enchanting WHERE Date <= datetime('now','-" + when + " day')");

			PreparedStatement book_Editing = sqLite.getConnection()
					.prepareStatement("DELETE FROM Book_Editing WHERE Date <= datetime('now','-" + when + " day')");

			PreparedStatement afk = sqLite.getConnection().prepareStatement("DELETE FROM AFK WHERE Date <= datetime('now','-" + when + " day')");

			PreparedStatement item_pickup = sqLite.getConnection()
					.prepareStatement("DELETE FROM Item_Pickup WHERE Date <= datetime('now','-" + when + " day')");

			PreparedStatement furnace = sqLite.getConnection().prepareStatement("DELETE FROM Furnace WHERE Date <= datetime('now','-" + when + " day')");

			player_Chat.executeUpdate();
			player_Commands.executeUpdate();
			console_Commands.executeUpdate();
			player_Sign_Text.executeUpdate();
			player_Join.executeUpdate();
			player_Leave.executeUpdate();
			player_Kick.executeUpdate();
			player_Death.executeUpdate();
			player_Teleport.executeUpdate();
			player_Level.executeUpdate();
			block_Place.executeUpdate();
			block_Break.executeUpdate();
			portal_Creation.executeUpdate();
			bucket_Place.executeUpdate();
			anvil.executeUpdate();
			TPS.executeUpdate();
			RAM.executeUpdate();
			server_Start.executeUpdate();
			server_Stop.executeUpdate();
			item_Drop.executeUpdate();
			enchanting.executeUpdate();
			book_Editing.executeUpdate();
			afk.executeUpdate();
			item_pickup.executeUpdate();
			furnace.executeUpdate();

		} catch (SQLException e) {

			e.printStackTrace();

			logger.severe("An error has occurred while cleaning the tables, if the error persists, contact the Authors!");

		}
	}
}