package org.carour.loggercore.database.postgresql;

import java.net.InetSocketAddress;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.logging.Logger;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.carour.loggercore.database.mysql.MySQL;
import org.carour.loggercore.logging.LoggingOptions;
import org.postgresql.util.PGobject;

public class PostgreSQLData {

  PostgreSQL postgreSQL;

  LoggingOptions options;

  Logger logger;

  public PostgreSQLData(PostgreSQL postgreSQL, LoggingOptions options, Logger logger) {
    this.postgreSQL = postgreSQL;
    this.options = options;
    this.logger = logger;
  }

  public void createTable() {
    final PreparedStatement playerChat, playerCommands, consoleCommands, playerSignText, playerJoin,
        playerLeave, playerDeath, playerTeleport, blockPlace, blockBreak, tps, ram, playerKick,
        portalCreation, playerLevel, bucketPlace, anvil, serverStart, serverStop, itemDrop, enchant,
        bookEditing, afk, itemPickup, furnace;

    try {
      playerChat = postgreSQL.getConnection()
          .prepareStatement("CREATE TABLE IF NOT EXISTS player_chat " +
              "(server_name TEXT,date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIME,world TEXT,"
              + "player_name TEXT,message TEXT,is_staff BOOLEAN ,PRIMARY KEY (date))");

      playerCommands = postgreSQL.getConnection()
          .prepareStatement("CREATE TABLE IF NOT EXISTS player_commands "
              + "(server_name TEXT,date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIME,world TEXT,"
              + "player_name TEXT,command TEXT,is_staff BOOLEAN,PRIMARY KEY (date))");

      consoleCommands = postgreSQL.getConnection()
          .prepareStatement("CREATE TABLE IF NOT EXISTS console_commands "
              + "(server_name TEXT,date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIME,command TEXT,"
              + "PRIMARY KEY (date))");

      playerSignText = postgreSQL.getConnection()
          .prepareStatement("CREATE TABLE IF NOT EXISTS player_sign_text "
              + "(server_name TEXT,date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIME,world TEXT,"
              + "x DOUBLE PRECISION,y DOUBLE PRECISION,z DOUBLE PRECISION,player_name TEXT,"
              + "line TEXT,is_staff BOOLEAN,PRIMARY KEY (date))");

      playerJoin = postgreSQL.getConnection()
          .prepareStatement("CREATE TABLE IF NOT EXISTS player_join "
              + "(server_name TEXT,date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIME,world TEXT,"
              + "player_name TEXT,x DOUBLE PRECISION,y DOUBLE PRECISION,z DOUBLE PRECISION,"
              + "ip INET,is_staff BOOLEAN,PRIMARY KEY (date))");

      playerLeave = postgreSQL.getConnection()
          .prepareStatement("CREATE TABLE IF NOT EXISTS player_leave "
              + "(server_name TEXT,date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIME,world TEXT,"
              + "player_name TEXT,x DOUBLE PRECISION,y DOUBLE PRECISION,z DOUBLE PRECISION,"
              + "is_staff BOOLEAN,PRIMARY KEY (date))");

      playerDeath = postgreSQL.getConnection()
          .prepareStatement("CREATE TABLE IF NOT EXISTS player_death " +
              "(server_name TEXT, date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIME, world TEXT,"
              + "player_name TEXT, x DOUBLE PRECISION, y DOUBLE PRECISION, z DOUBLE PRECISION,"
              + " cause TEXT, killer TEXT,item_used TEXT, is_staff BOOLEAN, PRIMARY KEY(date))");

      playerTeleport = postgreSQL.getConnection()
          .prepareStatement("CREATE TABLE IF NOT EXISTS player_teleport"
              + "(server_name TEXT,date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIME,world TEXT,"
              + " player_name TEXT, from_x DOUBLE PRECISION,from_Y DOUBLE PRECISION,"
              + "from_z DOUBLE PRECISION,to_x DOUBLE PRECISION,to_y DOUBLE PRECISION,"
              + "to_z DOUBLE PRECISION, is_staff BOOLEAN, PRIMARY KEY (date))");

      blockPlace = postgreSQL.getConnection()
          .prepareStatement("CREATE TABLE IF NOT EXISTS block_place" +
              "(server_name TEXT, date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIME, world TEXT,"
              + " player_name TEXT, block TEXT,x DOUBLE PRECISION,y DOUBLE PRECISION,"
              + "z DOUBLE PRECISION, is_staff BOOLEAN,PRIMARY KEY (date))");

      blockBreak = postgreSQL.getConnection()
          .prepareStatement("CREATE TABLE IF NOT EXISTS block_break" +
              "(server_name TEXT, date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIME,world TEXT,"
              + "player_name TEXT, block TEXT,x DOUBLE PRECISION,y DOUBLE PRECISION,"
              + " z DOUBLE PRECISION, is_staff BOOLEAN, PRIMARY KEY (date))");

      tps = postgreSQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS tps" +
          "(server_name TEXT, date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIME, tps INTEGER,"
          + " PRIMARY KEY (date))");

      ram = postgreSQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS ram" +
          "(server_name TEXT, date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIME, total_memory INTEGER,"
          + " used_memory INTEGER, free_memory INTEGER, PRIMARY KEY (date))");

      playerKick = postgreSQL.getConnection()
          .prepareStatement("CREATE TABLE IF NOT EXISTS player_kick" +
              "(server_name TEXT,date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIME,world TEXT,"
              + "player_name TEXT,x DOUBLE PRECISION,y DOUBLE PRECISION,z DOUBLE PRECISION,"
              + "reason TEXT,is_staff BOOLEAN,PRIMARY KEY (date))");

      portalCreation = postgreSQL.getConnection()
          .prepareStatement("CREATE TABLE IF NOT EXISTS portal_creation" +
              "(server_name TEXT ,date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIME,world TEXT,"
              + "caused_by TEXT,PRIMARY KEY (date))");

      playerLevel = postgreSQL.getConnection()
          .prepareStatement("CREATE TABLE IF NOT EXISTS player_level" +
              "(server_name TEXT,date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIME,player_name TEXT,"
              + "is_staff BOOLEAN,PRIMARY KEY (date))");

      bucketPlace = postgreSQL.getConnection()
          .prepareStatement("CREATE TABLE IF NOT EXISTS bucket_place" +
              "(Server_Name VARCHAR(30),Date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIME,world TEXT,"
              + "player_name TEXT,bucket TEXT,x DOUBLE PRECISION,y DOUBLE PRECISION,"
              + "z DOUBLE PRECISION,is_staff BOOLEAN,PRIMARY KEY (date))");

      anvil = postgreSQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS anvil" +
          "(server_name TEXT,date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIME,player_name TEXT,"
          + "new_name TEXT,is_staff BOOLEAN,PRIMARY KEY (date))");

      serverStart = postgreSQL.getConnection()
          .prepareStatement("CREATE  TABLE IF NOT EXISTS server_start" +
              "(server_name TEXT,date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIME,"
              + "PRIMARY KEY (date))");

      serverStop = postgreSQL.getConnection()
          .prepareStatement("CREATE TABLE IF NOT EXISTS server_stop" +
              "(server_name TEXT,date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIME,"
              + "PRIMARY KEY (date))");

      itemDrop = postgreSQL.getConnection()
          .prepareStatement("CREATE TABLE IF NOT EXISTS item_drop" +
              "(server_name TEXT,date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIME,world TEXT,"
              + "player_name TEXT,item TEXT,amount INTEGER,x DOUBLE PRECISION,y DOUBLE PRECISION,"
              + "z DOUBLE PRECISION,enchantment TEXT,changed_name TEXT,"
              + "is_staff BOOLEAN,PRIMARY KEY (date))");

      enchant = postgreSQL.getConnection().prepareStatement("CREATE TABLE "
          + "IF NOT EXISTS enchanting"
          + "(server_name TEXT,date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIME,world TEXT,"
          + "player_name TEXT,x DOUBLE PRECISION,y DOUBLE PRECISION,z DOUBLE PRECISION,"
          + "enchantment TEXT,item TEXT,cost INTEGER,is_staff BOOLEAN,PRIMARY KEY (date))");

      bookEditing = postgreSQL.getConnection()
          .prepareStatement("CREATE TABLE IF NOT EXISTS book_editing "
              + "(server_name TEXT,date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIME,world TEXT,"
              + "player_name TEXT,page_count INTEGER,page_content TEXT,signed_by TEXT,"
              + "is_staff BOOLEAN,PRIMARY KEY (date))");

      afk = postgreSQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS afk "
          + "(server_name TEXT,date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIME,world TEXT,"
          + "player_name TEXT,x DOUBLE PRECISION,y DOUBLE PRECISION,z DOUBLE PRECISION,"
          + "is_staff BOOLEAN,PRIMARY KEY (date))");

      itemPickup = postgreSQL.getConnection()
          .prepareStatement("CREATE TABLE IF NOT EXISTS item_pickup "
              + "(server_name TEXT,date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIME,world TEXT,"
              + "player_name TEXT,item TEXT,amount INTEGER,X DOUBLE PRECISION,y DOUBLE PRECISION,"
              + "z DOUBLE PRECISION,changed_name TEXT,is_staff BOOLEAN,PRIMARY KEY (date))");

      furnace = postgreSQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS furnace "
          + "(server_name TEXT,date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIME,world TEXT,"
          + "player_name TEXT,item TEXT,amount INTEGER,x DOUBLE PRECISION,y DOUBLE PRECISION,"
          + "z DOUBLE PRECISION,is_staff BOOLEAN,PRIMARY KEY (date))");

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
      tps.executeUpdate();
      ram.executeUpdate();
      playerKick.executeUpdate();
      portalCreation.executeUpdate();
      playerLevel.executeUpdate();
      bucketPlace.executeUpdate();
      anvil.executeUpdate();
      serverStart.executeUpdate();
      serverStop.executeUpdate();
      itemDrop.executeUpdate();
      bookEditing.executeUpdate();
      afk.executeUpdate();
      enchant.executeUpdate();
      itemPickup.executeUpdate();
      furnace.executeUpdate();

    } catch (SQLException exception) {
      exception.printStackTrace();
    }
  }

  public void insertPlayerChat(String serverName, String worldName, String playerName,
      String message, boolean isStaff) {
    try (PreparedStatement insertStatement = postgreSQL.getConnection().prepareStatement(
        "INSERT INTO player_chat(server_name,world,player_name,message,is_staff)"
            + " VALUES (?,?,?,?,?,?)")) {
      insertStatement.setString(1, serverName);
      insertStatement.setString(2, worldName);
      insertStatement.setString(3, playerName);
      insertStatement.setString(4, message);
      insertStatement.setBoolean(5, isStaff);
      insertStatement.executeUpdate();
    } catch (SQLException exception) {
      exception.printStackTrace();
    }
  }

  public void insertPlayerCommands(String serverName, String worldName, String playerName,
      String command, boolean isStaff) {
    try (PreparedStatement insertStatement = postgreSQL.getConnection().prepareStatement(
        "INSERT INTO player_commands(server_name,world,player_name,command,is_staff)"
            + " VALUES (?,?,?,?,?,?)")) {
      insertStatement.setString(1, serverName);
      insertStatement.setString(2, worldName);
      insertStatement.setString(3, playerName);
      insertStatement.setString(4, command);
      insertStatement.setBoolean(5, isStaff);

      insertStatement.executeUpdate();
    } catch (SQLException exception) {
      exception.printStackTrace();
    }
  }

  public void insertConsoleCommands(String serverName,
      String command) {
    try (PreparedStatement insertStatement = postgreSQL.getConnection().prepareStatement(
        "INSERT INTO console_commands(server_namecommand) VALUES (?,?)")) {
      insertStatement.setString(1, serverName);
      insertStatement.setString(2, command);

      insertStatement.executeUpdate();
    } catch (SQLException exception) {
      exception.printStackTrace();
    }
  }

  public void insertPlayerSignText(String serverName, String worldName, double x, double y,
      double z, String playerName, String lines, boolean isStaff) {
    try (PreparedStatement insertStatement = postgreSQL.getConnection().prepareStatement(
        "INSERT INTO player_sign_text(server_name,world,x,y,z,player_name,text,is_staff)"
            + " VALUES (?,?,?,?,?,?,?,?)")) {
      insertStatement.setString(1, serverName);
      insertStatement.setString(2, serverName);
      insertStatement.setDouble(3, x);
      insertStatement.setDouble(4, y);
      insertStatement.setDouble(5, z);
      insertStatement.setString(6, playerName);
      insertStatement.setString(7, lines);
      insertStatement.setBoolean(8, isStaff);

      insertStatement.executeUpdate();
    } catch (SQLException exception) {
      exception.printStackTrace();
    }
  }

  public void insertPlayerDeath(String serverName, String worldName, String playerName, double x,
      double y, double z, String cause, String killer, String itemUsed, boolean isStaff) {
    try (PreparedStatement insertStatement = postgreSQL.getConnection().prepareStatement(
        "INSERT INTO"
            + " player_death(server_name,world,player_name,x,y,z,cause,killer,item_used,is_staff)"
            + " VALUES (?,?,?,?,?,?,?,?,?,?)")) {

      insertStatement.setString(1, serverName);
      insertStatement.setString(2, worldName);
      insertStatement.setString(3, playerName);
      insertStatement.setDouble(4, x);
      insertStatement.setDouble(5, y);
      insertStatement.setDouble(6, z);
      insertStatement.setString(7, cause);
      insertStatement.setString(8, killer);
      insertStatement.setString(9, itemUsed);
      insertStatement.setBoolean(10, isStaff);

      insertStatement.executeUpdate();

    } catch (SQLException e) {
      logger.severe(ExceptionUtils.getStackTrace(e));
    }
  }

  public void insertPlayerTeleport(String serverName, String worldName, String playerName,
      double fromX, double fromY, double fromZ, double toX, double toY, double toZ,
      boolean isStaff) {
    try (PreparedStatement insertStatement = postgreSQL.getConnection().prepareStatement(
        "INSERT INTO player_teleport(server_name,world,player_name,from_x,from_y,from_z,to_x,"
            + "to_y,to_z,is_staff)"
            + " VALUES (?,?,?,?,?,?,?,?,?,?)")) {

      insertStatement.setString(1, serverName);
      insertStatement.setString(2, worldName);
      insertStatement.setString(3, playerName);
      insertStatement.setDouble(4, fromX);
      insertStatement.setDouble(5, fromY);
      insertStatement.setDouble(6, fromZ);
      insertStatement.setDouble(7, toX);
      insertStatement.setDouble(8, toY);
      insertStatement.setDouble(9, toZ);
      insertStatement.setBoolean(10, isStaff);

      insertStatement.executeUpdate();

    } catch (SQLException e) {
      logger.severe(ExceptionUtils.getStackTrace(e));
    }
  }

  public void insertPlayerJoin(String serverName, String worldName, String playerName, double x,
      double y, double z, InetSocketAddress ip, boolean isStaff) {

    PGobject ipObject = new PGobject();

    try (PreparedStatement insertStatement = postgreSQL.getConnection().prepareStatement(
        "INSERT INTO player_join(server_name,world,player_name,x,y,z,ip,is_staff)"
            + " VALUES (?,?,?,?,?,?,?,?)")) {
      if (options.isPlayerIpLoggingEnabled()) {
        ipObject.setType("inet");
        String ipString = ip.getAddress().toString();
        if (ipString.startsWith("/")) {
          ipString = ipString.substring(1);
        }

        ipString = ipString.substring(0, ipString.indexOf(":"));
        ipObject.setValue(ipString);

        insertStatement.setObject(7, ipObject);

      } else {
        insertStatement.setNull(7, Types.OTHER);
      }

      insertStatement.setString(1, serverName);
      insertStatement.setString(2, worldName);
      insertStatement.setString(3, playerName);
      insertStatement.setDouble(4, x);
      insertStatement.setDouble(5, y);
      insertStatement.setDouble(6, z);
      insertStatement.setBoolean(8, isStaff);

      insertStatement.executeUpdate();

    } catch (SQLException e) {
      logger.severe(ExceptionUtils.getStackTrace(e));
    }
  }

  public void insertPlayerLeave(String serverName, String worldName, String playerName, double x,
      double y, double z, boolean isStaff) {
    try (PreparedStatement insertStatement = postgreSQL.getConnection().prepareStatement(
        "INSERT INTO player_leave(server_name,world,player_name,x,y,z,is_staff)"
            + " VALUES (?,?,?,?,?,?,?)")) {

      insertStatement.setString(1, serverName);
      insertStatement.setString(2, worldName);
      insertStatement.setString(3, playerName);
      insertStatement.setDouble(4, x);
      insertStatement.setDouble(5, y);
      insertStatement.setDouble(6, z);
      insertStatement.setBoolean(7, isStaff);

      insertStatement.executeUpdate();
    } catch (SQLException e) {
      logger.severe(ExceptionUtils.getStackTrace(e));
    }
  }

  public void insertBlockPlace(String serverName, String worldName, String playerName, String block,
      double x, double y, double z, boolean isStaff) {
    try (PreparedStatement insertStatement = postgreSQL.getConnection().prepareStatement(
        "INSERT INTO block_place(server_name,world,player_name,block,x,y,z,is_staff)"
            + " VALUES (?,?,?,?,?,?,?,?)")) {

      insertStatement.setString(1, serverName);
      insertStatement.setString(2, worldName);
      insertStatement.setString(3, playerName);
      insertStatement.setString(4, block);
      insertStatement.setDouble(5, x);
      insertStatement.setDouble(6, y);
      insertStatement.setDouble(7, z);
      insertStatement.setBoolean(8, isStaff);

      insertStatement.executeUpdate();
    } catch (SQLException e) {
      logger.severe(ExceptionUtils.getStackTrace(e));
    }
  }

public void insertBlockBreak(String serverName, String worldName, String playerName, String block,
      double x, double y, double z, boolean isStaff) {
    try (PreparedStatement insertStatement = postgreSQL.getConnection().prepareStatement(
        "INSERT INTO block_break(server_name,world,player_name,block,x,y,z,is_staff)"
            + " VALUES (?,?,?,?,?,?,?,?)")) {

      insertStatement.setString(1, serverName);
      insertStatement.setString(2, worldName);
      insertStatement.setString(3, playerName);
      insertStatement.setString(4, block);
      insertStatement.setDouble(5, x);
      insertStatement.setDouble(6, y);
      insertStatement.setDouble(7, z);
      insertStatement.setBoolean(8, isStaff);

      insertStatement.executeUpdate();
    } catch (SQLException e) {
      logger.severe(ExceptionUtils.getStackTrace(e));
    }
  }

  public void insertTps(String serverName, int tps) {
    try (PreparedStatement insertStatement = postgreSQL.getConnection().prepareStatement(
        "INSERT INTO tps(server_name,tps)" + " VALUES (?,?)")) {

      insertStatement.setString(1, serverName);
      insertStatement.setInt(2, tps);

      insertStatement.executeUpdate();
    } catch (SQLException e) {
      logger.severe(ExceptionUtils.getStackTrace(e));
    }
  }

  public void insertRam(String serverName, int ram) {
    try (PreparedStatement insertStatement = postgreSQL.getConnection().prepareStatement(
        "INSERT INTO ram(server_name,ram)" + " VALUES (?,?)")) {

      insertStatement.setString(1, serverName);
      insertStatement.setInt(2, ram);

      insertStatement.executeUpdate();
    } catch (SQLException e) {
      logger.severe(ExceptionUtils.getStackTrace(e));
    }
  }

  public void emptyTable() {

    int when = options.getTableCleanupTreshold();

    if (when <= 0) {
      return;
    }

    try {

      PreparedStatement playerChat = postgreSQL.getConnection().prepareStatement(
          "DELETE FROM player_chat WHERE date < NOW() - INTERVAL " + when + " DAY");

      PreparedStatement playerCommands = postgreSQL.getConnection().prepareStatement(
          "DELETE FROM player_commands WHERE Date < NOW() - INTERVAL " + when + " DAY");

      PreparedStatement consoleCommands = postgreSQL.getConnection().prepareStatement(
          "DELETE FROM console_commands WHERE DATE < NOW() - INTERVAL " + when + " DAY");

      PreparedStatement playerSignText = postgreSQL.getConnection().prepareStatement(
          "DELETE FROM player_sign_text WHERE Date < NOW() - INTERVAL " + when + " DAY");

      PreparedStatement playerJoin = postgreSQL.getConnection().prepareStatement(
          "DELETE FROM player_join WHERE DATE < NOW() - INTERVAL " + when + " DAY");

      PreparedStatement playerLeave = postgreSQL.getConnection().prepareStatement(
          "DELETE FROM player_leave WHERE Date < NOW() - INTERVAL " + when + " DAY");

      PreparedStatement playerKick = postgreSQL.getConnection().prepareStatement(
          "DELETE FROM player_kick WHERE DATE < NOW() - INTERVAL " + when + " DAY");

      PreparedStatement playerDeath = postgreSQL.getConnection().prepareStatement(
          "DELETE FROM player_death WHERE Date < NOW() - INTERVAL " + when + " DAY");

      PreparedStatement playerTeleport = postgreSQL.getConnection().prepareStatement(
          "DELETE FROM player_teleport WHERE DATE < NOW() - INTERVAL " + when + " DAY");

      PreparedStatement playerLevel = postgreSQL.getConnection().prepareStatement(
          "DELETE FROM player_level WHERE Date < NOW() - INTERVAL " + when + " DAY");

      PreparedStatement blockPlace = postgreSQL.getConnection().prepareStatement(
          "DELETE FROM block_place WHERE DATE < NOW() - INTERVAL " + when + " DAY");

      PreparedStatement blockBreak = postgreSQL.getConnection().prepareStatement(
          "DELETE FROM block_break WHERE Date < NOW() - INTERVAL " + when + " DAY");

      PreparedStatement portalCreation = postgreSQL.getConnection().prepareStatement(
          "DELETE FROM portal_creation WHERE DATE < NOW() - INTERVAL " + when + " DAY");

      PreparedStatement bucketPlace = postgreSQL.getConnection().prepareStatement(
          "DELETE FROM bucket_place WHERE Date < NOW() - INTERVAL " + when + " DAY");

      PreparedStatement anvil = postgreSQL.getConnection()
          .prepareStatement("DELETE FROM anvil WHERE DATE < NOW() - INTERVAL " + when + " DAY");

      PreparedStatement tps = postgreSQL.getConnection()
          .prepareStatement("DELETE FROM tps WHERE Date < NOW() - INTERVAL " + when + " DAY");

      PreparedStatement ram = postgreSQL.getConnection()
          .prepareStatement("DELETE FROM ram WHERE DATE < NOW() - INTERVAL " + when + " DAY");

      PreparedStatement serverStart = postgreSQL.getConnection().prepareStatement(
          "DELETE FROM server_start WHERE Date < NOW() - INTERVAL " + when + " DAY");

      PreparedStatement serverStop = postgreSQL.getConnection().prepareStatement(
          "DELETE FROM server_stop WHERE DATE < NOW() - INTERVAL " + when + " DAY");

      PreparedStatement itemDrop = postgreSQL.getConnection()
          .prepareStatement(
              "DELETE FROM item_drop WHERE Date < NOW() - INTERVAL " + when + " DAY");

      PreparedStatement enchanting = postgreSQL.getConnection().prepareStatement(
          "DELETE FROM enchanting WHERE DATE < NOW() - INTERVAL " + when + " DAY");

      PreparedStatement bookEditing = postgreSQL.getConnection().prepareStatement(
          "DELETE FROM book_editing WHERE DATE < NOW() - INTERVAL " + when + " DAY");

      PreparedStatement afk = postgreSQL.getConnection()
          .prepareStatement("DELETE FROM afk WHERE DATE < NOW() - INTERVAL " + when + " DAY");

      PreparedStatement itemPickup = postgreSQL.getConnection().prepareStatement(
          "DELETE FROM item_pickup WHERE DATE < NOW() - INTERVAL " + when + " DAY");

      PreparedStatement furnace = postgreSQL.getConnection()
          .prepareStatement("DELETE FROM furnace WHERE DATE < NOW() - INTERVAL " + when + " DAY");

      playerChat.executeUpdate();
      playerCommands.executeUpdate();
      consoleCommands.executeUpdate();
      playerSignText.executeUpdate();
      playerJoin.executeUpdate();
      playerLeave.executeUpdate();
      playerKick.executeUpdate();
      playerDeath.executeUpdate();
      playerTeleport.executeUpdate();
      playerLevel.executeUpdate();
      blockPlace.executeUpdate();
      blockBreak.executeUpdate();
      portalCreation.executeUpdate();
      bucketPlace.executeUpdate();
      anvil.executeUpdate();
      tps.executeUpdate();
      ram.executeUpdate();
      serverStart.executeUpdate();
      serverStop.executeUpdate();
      itemDrop.executeUpdate();
      enchanting.executeUpdate();
      bookEditing.executeUpdate();
      afk.executeUpdate();
      itemPickup.executeUpdate();
      furnace.executeUpdate();

    } catch (SQLException e) {

      logger.severe(
          "An error has occurred while cleaning the tables, if the error persists, contact the Authors!");
      logger.severe(ExceptionUtils.getStackTrace(e));

    }
  }

}
