package com.carpour.loggercore.database.datasource;

import com.carpour.loggercore.database.DataSourceInterface;
import com.carpour.loggercore.database.data.DatabaseCredentials;
import com.carpour.loggercore.database.entity.*;
import com.carpour.loggercore.database.utils.HibernateUtils;
import org.hibernate.Session;

import java.net.InetSocketAddress;
import java.time.Instant;
import java.util.List;

public final class Database implements DataSourceInterface {
public Database(DatabaseCredentials databaseCredentials)
{
    HibernateUtils.initializeHibernate(databaseCredentials);
}
    @Override
    public void insertPlayerChat(String serverName, EntityPlayer player, String worldName, String msg) {

        Session session = HibernateUtils.getSession();session.beginTransaction();

        PlayerChat p  = new PlayerChat();

        p.setDate(Instant.now());
        p.setServerName(serverName);
        p.setMessage(msg);
        p.setWorld(worldName);
        p.setIsStaff(player.isStaff());
        p.setPlayerName(player.getPlayerName());

        session.persist(p);
        session.getTransaction().commit();

    }

    @Override
    public void insertPlayerCommands(String serverName, EntityPlayer player, String worldName, String command) {

        Session session = HibernateUtils.getSession();session.beginTransaction();

        PlayerCommand p  = new PlayerCommand();

        p.setDate(Instant.now());
        p.setServerName(serverName);
        p.setCommand(command);
        p.setWorld(worldName);
        p.setIsStaff(player.isStaff());
        p.setPlayerName(player.getPlayerName());

        session.persist(p);
        session.getTransaction().commit();

    }

    @Override
    public void insertPlayerSignText(String serverName, EntityPlayer player, Coordinates coords, String lines) {

        Session session = HibernateUtils.getSession();session.beginTransaction();

        PlayerSignText p  = new PlayerSignText();

        p.setDate(Instant.now());
        p.setServerName(serverName);
        p.setCoordinates(coords);
        p.setWorld(coords.getWorldName());
        p.setIsStaff(player.isStaff());
        p.setPlayerName(player.getPlayerName());

        session.persist(p);
        session.getTransaction().commit();

    }

    @Override
    public void insertPlayerDeath(String serverName, EntityPlayer player, int level, String cause, String who, Coordinates coordinates) {

        Session session = HibernateUtils.getSession();session.beginTransaction();

        PlayerDeath p  = new PlayerDeath();

        p.setDate(Instant.now());
        p.setServerName(serverName);
        p.setPlayerLevel(level);
        p.setWorld(coordinates.getWorldName());
        p.setX(coordinates.getX());
        p.setY(coordinates.getY());
        p.setZ(coordinates.getZ());
        p.setIsStaff(player.isStaff());
        p.setPlayerName(player.getPlayerName());
        p.setCause(cause);
        p.setByWho(who);

        session.persist(p);
        session.getTransaction().commit();

    }

    @Override
    public void insertPlayerTeleport(String serverName, EntityPlayer player, Coordinates oldCoords, Coordinates newCoords) {

        Session session = HibernateUtils.getSession();session.beginTransaction();

        PlayerTeleport p  = new PlayerTeleport();

        p.setDate(Instant.now());
        p.setServerName(serverName);
        p.setFromX(oldCoords.getX());
        p.setFromY(oldCoords.getY());
        p.setFromZ(oldCoords.getZ());
        p.setWorld(oldCoords.getWorldName());
        p.setToX(newCoords.getX());
        p.setToY(newCoords.getY());
        p.setToZ(newCoords.getZ());
        p.setIsStaff(player.isStaff());
        p.setPlayerName(player.getPlayerName());

        session.persist(p);
        session.getTransaction().commit();

    }

    @Override
    public void insertPlayerJoin(String serverName, EntityPlayer player, Coordinates coords, InetSocketAddress ip) {

        Session session = HibernateUtils.getSession();session.beginTransaction();

        PlayerJoin p  = new PlayerJoin();

        p.setDate(Instant.now());
        p.setServerName(serverName);
        p.setX(coords.getX());
        p.setY(coords.getY());
        p.setZ(coords.getZ());
        p.setWorld(coords.getWorldName());
        //TODO
        //ip
        p.setIsStaff(player.isStaff());
        p.setPlayerName(player.getPlayerName());

        session.persist(p);
        session.getTransaction().commit();

    }

    @Override
    public void insertPlayerLeave(String serverName, EntityPlayer player, Coordinates coords) {

        Session session = HibernateUtils.getSession();session.beginTransaction();

        PlayerLeave p = new PlayerLeave();

        p.setDate(Instant.now());
        p.setServerName(serverName);

        p.setWorld(coords.getWorldName());
        p.setX(coords.getX());
        p.setY(coords.getY());
        p.setZ(coords.getZ());
        p.setIsStaff(player.isStaff());
        p.setPlayerName(player.getPlayerName());

        session.persist(p);
        session.getTransaction().commit();


    }
    @Override
    public void insertBlockPlace(String serverName, EntityPlayer player, String block, Coordinates coords) {

        Session session = HibernateUtils.getSession();session.beginTransaction();

        BlockPlace p = new BlockPlace();

        p.setDate(Instant.now());
        p.setServerName(serverName);

        p.setWorld(coords.getWorldName());
        p.setX(coords.getX());
        p.setY(coords.getY());
        p.setZ(coords.getZ());
        p.setBlock(block);
        p.setIsStaff(player.isStaff());
        p.setPlayerName(player.getPlayerName());

        session.persist(p);
        session.getTransaction().commit();

    }

    @Override
    public void insertBlockBreak(String serverName, EntityPlayer player, String blockName, Coordinates coords) {

        Session session = HibernateUtils.getSession();session.beginTransaction();

        BlockBreak p = new BlockBreak();

        p.setDate(Instant.now());
        p.setServerName(serverName);

        p.setWorld(coords.getWorldName());
        p.setX(coords.getX());
        p.setY(coords.getY());
        p.setZ(coords.getZ());
        p.setBlock(blockName);
        p.setIsStaff(player.isStaff());
        p.setPlayerName(player.getPlayerName());

        session.persist(p);
        session.getTransaction().commit();

    }

    @Override
    public void insertTps(String serverName, double tpss) {

        Session session = HibernateUtils.getSession();session.beginTransaction();

        Tp p = new Tp();

        p.setDate(Instant.now());
        p.setServerName(serverName);

        p.setTps((int) tpss);

        session.persist(p);
        session.getTransaction().commit();

    }

    @Override
    public void insertRam(String serverName, long tm, long um, long fm) {

        Session session = HibernateUtils.getSession();session.beginTransaction();

        Ram p = new Ram();

        p.setDate(Instant.now());
        p.setServerName(serverName);

        p.setTotalMemory((int) tm);
        p.setUsedMemory((int) um);
        p.setFreeMemory((int) fm);

        session.persist(p);
        session.getTransaction().commit();

    }

    @Override
    public void insertPlayerKick(String serverName, EntityPlayer player, Coordinates coords, String reason) {

        Session session = HibernateUtils.getSession();session.beginTransaction();

        PlayerKick p = new PlayerKick();

        p.setDate(Instant.now());
        p.setServerName(serverName);

        p.setWorld(coords.getWorldName());
        p.setX(coords.getX());
        p.setY(coords.getY());
        p.setZ(coords.getZ());
        p.setReason(reason);
        p.setIsStaff(player.isStaff());
        p.setPlayerName(player.getPlayerName());

        session.persist(p);
        session.getTransaction().commit();

    }

    @Override
    public void insertPortalCreate(String serverName, String worldName, String by) {

        Session session = HibernateUtils.getSession();session.beginTransaction();

        PortalCreation p = new PortalCreation();

        p.setDate(Instant.now());
        p.setServerName(serverName);
        p.setWorld(worldName);
        p.setCausedBy(by);

        session.persist(p);
        session.getTransaction().commit();

    }

    @Override
    public void insertLevelChange(String serverName, String playerName, boolean isStaff) {


    }

    @Override
    public void insertBucketFill(String serverName, EntityPlayer player, String bucket, Coordinates coords) {


    }

    @Override
    public void insertBucketEmpty(String serverName, EntityPlayer player, String bucket, Coordinates coords) {


    }

    @Override
    public void insertAnvil(String serverName, String playerName, String newName, boolean isStaff) {


    }

    @Override
    public void insertServerStart(String serverName) {
        Session session = HibernateUtils.getSession();session.beginTransaction();

        ServerStart s  = new ServerStart();
        s.setDate(Instant.now());
        s.setServerName(serverName);

        session.persist(s);
        session.getTransaction().commit();


    }

    @Override
    public void insertServerStop(String serverName) {
        Session session = HibernateUtils.getSession();session.beginTransaction();

        ServerStop s  = new ServerStop();
        s.setDate(Instant.now());
        s.setServerName(serverName);
        session.persist(s);
        session.getTransaction().commit();


    }

    @Override
    public void insertItemDrop(String serverName, EntityPlayer player, String item, int amount, Coordinates coords, List<String> enchantment, String changedName) {


    }

    @Override
    public void insertEnchant(String serverName, EntityPlayer player, List<String> enchantment, int enchantmentLevel,
                              String item, int cost, Coordinates coordinates) {


    }

    @Override
    public void insertBookEditing(String serverName, EntityPlayer player, String worldName, int pages, List<String> content, String signedBy) {


    }

    @Override
    public void insertAfk(String serverName, EntityPlayer player, Coordinates coords) {


    }

    @Override
    public void insertWrongPassword(String serverName, EntityPlayer player, String worldName) {


    }

    @Override
    public void insertItemPickup(String serverName, EntityPlayer player, String item, int amount, Coordinates coords, String changedName) {


    }

    @Override
    public void insertFurnace(String serverName, EntityPlayer player, String item, int amount, Coordinates coords) {


    }

    @Override
    public void insertRCON(String serverName, String ip, String command) {


    }

    @Override
    public void insertGameMode(String serverName, EntityPlayer player, String theGameMode, String worldName) {


    }

    @Override
    public void insertPlayerCraft(String serverName, EntityPlayer player, String item, int amount, Coordinates coordinates) {


    }

    @Override
    public void insertVault(String serverName, EntityPlayer player, double oldBal, double newBal) {


    }

    @Override
    public void insertPlayerRegistration(String serverName, EntityPlayer player, String joinDate) {


    }

    @Override
    public void insertPrimedTnt(String serverName, EntityPlayer player, Coordinates coords) {


    }

    @Override
    public void insertLiteBans(String serverName, String executor, String command, String onWho, String duration, String reason, boolean isSilent) {


    }

    @Override
    public void insertAdvanceBanData(String serverName, String type, String executor, String executedOn, String reason, long expirationDate) {


    }

    @Override
    public void insertCommandBlock(String serverName, String msg) {


    }

    @Override
    public void insertWoodStripping(String serverName, EntityPlayer player, String logName, Coordinates coords) {


    }

    @Override
    public void insertChestInteraction(String serverName, EntityPlayer player, Coordinates coords, String[] items) {


    }

    @Override
    public void insertEntityDeath(String serverName, EntityPlayer player, String mob, Coordinates coords) {


    }

    public void emptyTable() {


    }

    @Override
    public void insertConsoleCommand(String serverName, String msg) {

        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        ConsoleCommand p = new ConsoleCommand();
        p.setDate(Instant.now());
        p.setServerName(serverName);
        p.setCommand(msg);
        session.persist(p);
        session.getTransaction().commit();

    }

}
