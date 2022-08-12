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
    public Database(DatabaseCredentials databaseCredentials) {
        HibernateUtils.initializeHibernate(databaseCredentials);
    }

    @Override
    public void insertPlayerChat(String serverName, EntityPlayer player, String worldName, String msg, boolean isStaff) {

        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        EntityPlayer loggerPlayer = HibernateUtils.getSession()
                .createNamedQuery("EntityPlayer.findOneByName", EntityPlayer.class)
                .setParameter("playerName", player.getPlayerName())
                .setHint("org.hibernate.cacheable", true)
                .getResultStream()
                .findFirst()
                .orElse(player);
        PlayerChat p = new PlayerChat();
        p.setDate(Instant.now());
        p.setServerName(serverName);
        p.setMessage(msg);
        p.setWorld(worldName);
        p.setEntityPlayer(loggerPlayer);
        session.persist(p);
        session.getTransaction().commit();

    }

    @Override
    public void insertPlayerCommands(String serverName, EntityPlayer player, String worldName, String command, boolean isStaff) {

        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        EntityPlayer loggerPlayer = session
                .createNamedQuery("EntityPlayer.findOneByName", EntityPlayer.class)
                .setParameter("playerName", player.getPlayerName())
                .getResultStream()
                .findFirst()
                .orElse(player);
        PlayerCommand p = new PlayerCommand();

        p.setDate(Instant.now());
        p.setServerName(serverName);
        p.setCommand(command);
        p.setWorld(worldName);
        p.setEntityPlayer(loggerPlayer);


        session.persist(p);
        session.getTransaction().commit();

    }

    @Override
    public void insertPlayerSignText(String serverName, EntityPlayer player, Coordinates coords, String lines, boolean isStaff) {

        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        EntityPlayer loggerPlayer = session
                .createNamedQuery("EntityPlayer.findOneByName", EntityPlayer.class)
                .setParameter("playerName", player.getPlayerName())
                .getResultStream()
                .findFirst()
                .orElse(player);
        PlayerSignText p = new PlayerSignText();

        p.setDate(Instant.now());
        p.setServerName(serverName);
        p.setCoordinates(coords);
        p.setWorld(coords.getWorldName());
        p.setLine(lines);
        p.setEntityPlayer(loggerPlayer);

        session.persist(p);
        session.getTransaction().commit();

    }

    @Override
    public void insertPlayerDeath(String serverName, EntityPlayer player, int level, String cause, String who, Coordinates coordinates, boolean isStaff) {

        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        EntityPlayer loggerPlayer = session
                .createNamedQuery("EntityPlayer.findOneByName", EntityPlayer.class)
                .setParameter("playerName", player.getPlayerName())
                .getResultStream()
                .findFirst()
                .orElse(player);
        PlayerDeath p = new PlayerDeath();

        p.setDate(Instant.now());
        p.setServerName(serverName);
        p.setPlayerLevel(level);
        p.setWorld(coordinates.getWorldName());
        p.setX(coordinates.getX());
        p.setY(coordinates.getY());
        p.setZ(coordinates.getZ());
        p.setEntityPlayer(loggerPlayer);
        p.setCause(cause);
        p.setByWho(who);

        session.persist(p);
        session.getTransaction().commit();

    }

    @Override
    public void insertPlayerTeleport(String serverName, EntityPlayer player, Coordinates oldCoords, Coordinates newCoords, boolean isStaff) {

        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        EntityPlayer loggerPlayer = session
                .createNamedQuery("EntityPlayer.findOneByName", EntityPlayer.class)
                .setParameter("playerName", player.getPlayerName())
                .getResultStream()
                .findFirst()
                .orElse(player);
        PlayerTeleport p = new PlayerTeleport();

        p.setDate(Instant.now());
        p.setServerName(serverName);
        p.setFromX(oldCoords.getX());
        p.setFromY(oldCoords.getY());
        p.setFromZ(oldCoords.getZ());
        p.setWorld(oldCoords.getWorldName());
        p.setToX(newCoords.getX());
        p.setToY(newCoords.getY());
        p.setToZ(newCoords.getZ());
        p.setEntityPlayer(loggerPlayer);

        session.persist(p);
        session.getTransaction().commit();

    }

    @Override
    public void insertPlayerJoin(String serverName, EntityPlayer player, Coordinates coords, InetSocketAddress ip, boolean isStaff) {

        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        EntityPlayer loggerPlayer = session
                .createNamedQuery("EntityPlayer.findOneByName", EntityPlayer.class)
                .setParameter("playerName", player.getPlayerName())
                .getResultStream()
                .findFirst()
                .orElse(player);
        PlayerJoin p = new PlayerJoin();

        p.setDate(Instant.now());
        p.setServerName(serverName);
        p.setX(coords.getX());
        p.setY(coords.getY());
        p.setZ(coords.getZ());
        p.setWorld(coords.getWorldName());
        //TODO
        //ip
        p.setEntityPlayer(loggerPlayer);

        session.persist(p);
        session.getTransaction().commit();

    }

    @Override
    public void insertPlayerLeave(String serverName, EntityPlayer player, Coordinates coords, boolean isStaff) {

        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        EntityPlayer loggerPlayer = session
                .createNamedQuery("EntityPlayer.findOneByName", EntityPlayer.class)
                .setParameter("playerName", player.getPlayerName())
                .getResultStream()
                .findFirst()
                .orElse(player);
        PlayerLeave p = new PlayerLeave();

        p.setDate(Instant.now());
        p.setServerName(serverName);

        p.setWorld(coords.getWorldName());
        p.setX(coords.getX());
        p.setY(coords.getY());
        p.setZ(coords.getZ());
        p.setEntityPlayer(loggerPlayer);

        session.persist(p);
        session.getTransaction().commit();


    }

    @Override
    public void insertBlockPlace(String serverName, EntityPlayer player, String block, Coordinates coords, boolean isStaff) {

        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        EntityPlayer loggerPlayer = session
                .createNamedQuery("EntityPlayer.findOneByName", EntityPlayer.class)
                .setParameter("playerName", player.getPlayerName())
                .getResultStream()
                .findFirst()
                .orElse(player);
        BlockPlace p = new BlockPlace();

        p.setDate(Instant.now());
        p.setServerName(serverName);

        p.setWorld(coords.getWorldName());
        p.setX(coords.getX());
        p.setY(coords.getY());
        p.setZ(coords.getZ());
        p.setBlock(block);
        p.setEntityPlayer(loggerPlayer);

        session.persist(p);
        session.getTransaction().commit();

    }

    @Override
    public void insertBlockBreak(String serverName, EntityPlayer player, String blockName, Coordinates coords, boolean isStaff) {

        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        EntityPlayer loggerPlayer = session
                .createNamedQuery("EntityPlayer.findOneByName", EntityPlayer.class)
                .setParameter("playerName", player.getPlayerName())
                .getResultStream()
                .findFirst()
                .orElse(player);
        BlockBreak p = new BlockBreak();

        p.setDate(Instant.now());
        p.setServerName(serverName);

        p.setWorld(coords.getWorldName());
        p.setX(coords.getX());
        p.setY(coords.getY());
        p.setZ(coords.getZ());
        p.setBlock(blockName);
        p.setEntityPlayer(loggerPlayer);


        session.persist(p);
        session.getTransaction().commit();

    }

    @Override
    public void insertTps(String serverName, double tpss) {

        Session session = HibernateUtils.getSession();
        session.beginTransaction();

        Tp p = new Tp();

        p.setDate(Instant.now());
        p.setServerName(serverName);

        p.setTps((int) tpss);

        session.persist(p);
        session.getTransaction().commit();

    }

    @Override
    public void insertRam(String serverName, long tm, long um, long fm) {

        Session session = HibernateUtils.getSession();
        session.beginTransaction();

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
    public void insertPlayerKick(String serverName, EntityPlayer player, Coordinates coords, String reason, boolean isStaff) {

        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        EntityPlayer loggerPlayer = session
                .createNamedQuery("EntityPlayer.findOneByName", EntityPlayer.class)
                .setParameter("playerName", player.getPlayerName())
                .getResultStream()
                .findFirst()
                .orElse(player);
        PlayerKick p = new PlayerKick();

        p.setDate(Instant.now());
        p.setServerName(serverName);

        p.setWorld(coords.getWorldName());
        p.setX(coords.getX());
        p.setY(coords.getY());
        p.setZ(coords.getZ());
        p.setReason(reason);
        p.setEntityPlayer(loggerPlayer);

        session.persist(p);
        session.getTransaction().commit();

    }

    @Override
    public void insertPortalCreate(String serverName, String worldName, String by) {

        Session session = HibernateUtils.getSession();
        session.beginTransaction();

        PortalCreation p = new PortalCreation();

        p.setDate(Instant.now());
        p.setServerName(serverName);
        p.setWorld(worldName);
        p.setCausedBy(by);

        session.persist(p);
        session.getTransaction().commit();

    }

    @Override
    public void insertLevelChange(String serverName, EntityPlayer player, boolean isStaff) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        EntityPlayer loggerPlayer = session
                .createNamedQuery("EntityPlayer.findOneByName", EntityPlayer.class)
                .setParameter("playerName", player.getPlayerName())
                .getResultStream()
                .findFirst()
                .orElse(player);
        PlayerLevel p = new PlayerLevel();

        p.setDate(Instant.now());
        p.setServerName(serverName);

        p.setEntityPlayer(loggerPlayer);

        session.persist(p);
        session.getTransaction().commit();

    }

    @Override
    public void insertBucketFill(String serverName, EntityPlayer player, String bucket, Coordinates coords, boolean isStaff) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        EntityPlayer loggerPlayer = session
                .createNamedQuery("EntityPlayer.findOneByName", EntityPlayer.class)
                .setParameter("playerName", player.getPlayerName())
                .getResultStream()
                .findFirst()
                .orElse(player);
        BucketFill b = new BucketFill();

        b.setDate(Instant.now());
        b.setServerName(serverName);

        b.setWorld(coords.getWorldName());
        b.setX(coords.getX());
        b.setY(coords.getY());
        b.setZ(coords.getZ());
        b.setBucket(bucket);
        b.setEntityPlayer(loggerPlayer);

        session.persist(b);
        session.getTransaction().commit();

    }

    @Override
    public void insertBucketEmpty(String serverName, EntityPlayer player, String bucket, Coordinates coords, boolean isStaff) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        EntityPlayer loggerPlayer = session
                .createNamedQuery("EntityPlayer.findOneByName", EntityPlayer.class)
                .setParameter("playerName", player.getPlayerName())
                .getResultStream()
                .findFirst()
                .orElse(player);
        BucketEmpty b = new BucketEmpty();

        b.setDate(Instant.now());
        b.setServerName(serverName);

        b.setWorld(coords.getWorldName());
        b.setX(coords.getX());
        b.setY(coords.getY());
        b.setZ(coords.getZ());
        b.setBucket(bucket);
        b.setEntityPlayer(loggerPlayer);

        session.persist(b);
        session.getTransaction().commit();

    }

    @Override
    public void insertAnvil(String serverName, EntityPlayer player, String newName, boolean isStaff) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        EntityPlayer loggerPlayer = session
                .createNamedQuery("EntityPlayer.findOneByName", EntityPlayer.class)
                .setParameter("playerName", player.getPlayerName())
                .getResultStream()
                .findFirst()
                .orElse(player);
        Anvil b = new Anvil();

        b.setDate(Instant.now());
        b.setServerName(serverName);
        b.setEntityPlayer(loggerPlayer);
        b.setNewName(newName);

        session.persist(b);
        session.getTransaction().commit();

    }

    @Override
    public void insertServerStart(String serverName) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();

        ServerStart s = new ServerStart();
        s.setDate(Instant.now());
        s.setServerName(serverName);

        session.persist(s);
        session.getTransaction().commit();


    }

    @Override
    public void insertServerStop(String serverName) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();

        ServerStop s = new ServerStop();
        s.setDate(Instant.now());
        s.setServerName(serverName);
        session.persist(s);
        session.getTransaction().commit();


    }

    @Override
    public void insertItemDrop(String serverName, EntityPlayer player, String item, int amount, Coordinates coords, List<String> enchantment, String changedName, boolean isStaff) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        EntityPlayer loggerPlayer = session
                .createNamedQuery("EntityPlayer.findOneByName", EntityPlayer.class)
                .setParameter("playerName", player.getPlayerName())
                .getResultStream()
                .findFirst()
                .orElse(player);
        ItemDrop b = new ItemDrop();

        b.setDate(Instant.now());
        b.setServerName(serverName);

        b.setWorld(coords.getWorldName());
        b.setX(coords.getX());
        b.setY(coords.getY());
        b.setZ(coords.getZ());
        b.setChangedName(changedName);
        b.setItem(item);
        b.setEnchantment(enchantment.toString());
        b.setAmount(amount);
        b.setEntityPlayer(loggerPlayer);

        session.persist(b);
        session.getTransaction().commit();

    }

    @Override
    public void insertEnchant(String serverName, EntityPlayer player, List<String> enchantment, int enchantmentLevel,
                              String item, int cost, Coordinates coordinates, boolean isStaff) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        EntityPlayer loggerPlayer = session
                .createNamedQuery("EntityPlayer.findOneByName", EntityPlayer.class)
                .setParameter("playerName", player.getPlayerName())
                .getResultStream()
                .findFirst()
                .orElse(player);
        Enchanting e = new Enchanting();

        e.setDate(Instant.now());
        e.setServerName(serverName);
        e.setWorld(coordinates.getWorldName());
        e.setX(coordinates.getX());
        e.setY(coordinates.getY());
        e.setZ(coordinates.getZ());
        e.setItem(item);
        e.setCost(cost);
        e.setEnchantment(enchantment.toString());
        e.setEnchantmentLevel(enchantmentLevel);
        e.setEntityPlayer(loggerPlayer);

        session.persist(e);
        session.getTransaction().commit();

    }

    @Override
    public void insertBookEditing(String serverName, EntityPlayer player, String worldName, int pages, List<String> content, String signedBy, boolean isStaff) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        EntityPlayer loggerPlayer = session
                .createNamedQuery("EntityPlayer.findOneByName", EntityPlayer.class)
                .setParameter("playerName", player.getPlayerName())
                .getResultStream()
                .findFirst()
                .orElse(player);
        BookEditing b = new BookEditing();
        b.setDate(Instant.now());
        b.setServerName(serverName);
        b.setWorld(worldName);
        b.setPageContent(content.toString());
        b.setPageCount(pages);
        b.setSignedBy(signedBy);
        b.setEntityPlayer(loggerPlayer);

        session.persist(b);
        session.getTransaction().commit();

    }

    @Override
    public void insertAfk(String serverName, EntityPlayer player, Coordinates coords, boolean isStaff) {


    }

    @Override
    public void insertWrongPassword(String serverName, EntityPlayer player, String worldName, boolean isStaff) {


    }

    @Override
    public void insertItemPickup(String serverName, EntityPlayer player, String item, int amount, Coordinates coords, String changedName, boolean isStaff) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        EntityPlayer loggerPlayer = session
                .createNamedQuery("EntityPlayer.findOneByName", EntityPlayer.class)
                .setParameter("playerName", player.getPlayerName())
                .getResultStream()
                .findFirst()
                .orElse(player);
        ItemPickup b = new ItemPickup();

        b.setDate(Instant.now());
        b.setServerName(serverName);

        b.setWorld(coords.getWorldName());
        b.setX(coords.getX());
        b.setY(coords.getY());
        b.setZ(coords.getZ());
        b.setChangedName(changedName);
        b.setItem(item);
        b.setEntityPlayer(loggerPlayer);

        session.persist(b);
        session.getTransaction().commit();

    }

    @Override
    public void insertFurnace(String serverName, EntityPlayer player, String item, int amount, Coordinates coords, boolean isStaff) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        EntityPlayer loggerPlayer = session
                .createNamedQuery("EntityPlayer.findOneByName", EntityPlayer.class)
                .setParameter("playerName", player.getPlayerName())
                .getResultStream()
                .findFirst()
                .orElse(player);
        Furnace b = new Furnace();

        b.setDate(Instant.now());
        b.setServerName(serverName);

        b.setWorld(coords.getWorldName());
        b.setX(coords.getX());
        b.setY(coords.getY());
        b.setZ(coords.getZ());
        b.setItem(item);
        b.setAmount(amount);
        b.setEntityPlayer(loggerPlayer);

        session.persist(b);
        session.getTransaction().commit();

    }

    @Override
    public void insertRCON(String serverName, String ip, String command) {

        Session session = HibernateUtils.getSession();
        session.beginTransaction();

        Rcon b = new Rcon();
        b.setDate(Instant.now());
        b.setServerName(serverName);
        b.setIp(Long.getLong(ip));

        session.persist(b);
        session.getTransaction().commit();
    }

    @Override
    public void insertGameMode(String serverName, EntityPlayer player, String gameMode, String worldName, boolean isStaff) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        EntityPlayer loggerPlayer = session
                .createNamedQuery("EntityPlayer.findOneByName", EntityPlayer.class)
                .setParameter("playerName", player.getPlayerName())
                .getResultStream()
                .findFirst()
                .orElse(player);
        GameMode b = new GameMode();

        b.setDate(Instant.now());
        b.setServerName(serverName);
        b.setWorld(worldName);
        b.setGameMode(gameMode);
        b.setEntityPlayer(loggerPlayer);

        session.persist(b);
        session.getTransaction().commit();

    }

    @Override
    public void insertPlayerCraft(String serverName, EntityPlayer player, String item, int amount, Coordinates coords, boolean isStaff) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        EntityPlayer loggerPlayer = session
                .createNamedQuery("EntityPlayer.findOneByName", EntityPlayer.class)
                .setParameter("playerName", player.getPlayerName())
                .getResultStream()
                .findFirst()
                .orElse(player);
        Crafting b = new Crafting();

        b.setDate(Instant.now());
        b.setServerName(serverName);
        b.setX(coords.getX());
        b.setY(coords.getY());
        b.setZ(coords.getZ());
        b.setAmount(amount);
        b.setItem(item);
        b.setWorld(coords.getWorldName());
        b.setEntityPlayer(loggerPlayer);

        session.persist(b);
        session.getTransaction().commit();

    }

    @Override
    public void insertVault(String serverName, EntityPlayer player, double oldBal, double newBal, boolean isStaff) {


    }

    @Override
    public void insertPlayerRegistration(String serverName, EntityPlayer player, String joinDate) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        EntityPlayer loggerPlayer = session
                .createNamedQuery("EntityPlayer.findOneByName", EntityPlayer.class)
                .setParameter("playerName", player.getPlayerName())
                .getResultStream()
                .findFirst()
                .orElse(player);
        session.persist(loggerPlayer);
        session.getTransaction().commit();
    }

    @Override
    public void insertPrimedTnt(String serverName, EntityPlayer player, Coordinates coords, boolean isStaff) {

        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        EntityPlayer loggerPlayer = session
                .createNamedQuery("EntityPlayer.findOneByName", EntityPlayer.class)
                .setParameter("playerName", player.getPlayerName())
                .getResultStream()
                .findFirst()
                .orElse(player);
        PrimedTnt b = new PrimedTnt();

        b.setDate(Instant.now());
        b.setServerName(serverName);
        b.setX(coords.getX());
        b.setY(coords.getY());
        b.setZ(coords.getZ());
        b.setWorld(coords.getWorldName());
        b.setEntityPlayer(loggerPlayer);

        session.persist(b);
        session.getTransaction().commit();
    }

    @Override
    public void insertLiteBans(String serverName, String executor, String command, String onWho, String duration, String reason, boolean isSilent) {


    }

    @Override
    public void insertAdvanceBanData(String serverName, String type, String executor, String executedOn, String reason, long expirationDate) {


    }

    @Override
    public void insertCommandBlock(String serverName, String msg) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();

        CommandBlock b = new CommandBlock();

        b.setDate(Instant.now());
        b.setServerName(serverName);
        b.setCommand(msg);

        session.persist(b);
        session.getTransaction().commit();

    }

    @Override
    public void insertWoodStripping(String serverName, EntityPlayer player, String logName, Coordinates coords, boolean isStaff) {


    }

    @Override
    public void insertChestInteraction(String serverName, EntityPlayer player, Coordinates coords, String[] items, boolean isStaff) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        EntityPlayer loggerPlayer = session
                .createNamedQuery("EntityPlayer.findOneByName", EntityPlayer.class)
                .setParameter("playerName", player.getPlayerName())
                .getResultStream()
                .findFirst()
                .orElse(player);
        ChestInteraction b = new ChestInteraction();

        b.setDate(Instant.now());
        b.setServerName(serverName);

        b.setWorld(coords.getWorldName());
        b.setX(coords.getX());
        b.setY(coords.getY());
        b.setZ(coords.getZ());
        b.setEntityPlayer(loggerPlayer);
        b.setItems(String.join(",", items));
        session.persist(b);
        session.getTransaction().commit();

    }

    @Override
    public void insertEntityDeath(String serverName, EntityPlayer player, String mob, Coordinates coords) {

        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        EntityPlayer loggerPlayer = session
                .createNamedQuery("EntityPlayer.findOneByName", EntityPlayer.class)
                .setParameter("playerName", player.getPlayerName())
                .getResultStream()
                .findFirst()
                .orElse(player);
        EntityDeath b = new EntityDeath();

        b.setDate(Instant.now());
        b.setServerName(serverName);

        b.setWorld(coords.getWorldName());
        b.setX(coords.getX());
        b.setY(coords.getY());
        b.setZ(coords.getZ());
        b.setMob(mob);
        b.setEntityPlayer(loggerPlayer);

        session.persist(b);
        session.getTransaction().commit();
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
