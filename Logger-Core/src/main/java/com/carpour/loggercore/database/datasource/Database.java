package com.carpour.loggercore.database.datasource;

import com.carpour.loggercore.database.DataSourceInterface;
import com.carpour.loggercore.database.data.DatabaseCredentials;
import com.carpour.loggercore.database.data.Options;
import com.carpour.loggercore.database.entity.*;
import com.carpour.loggercore.database.utils.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.QueryHints;

import java.net.InetSocketAddress;
import java.time.Instant;
import java.util.List;

public final class Database implements DataSourceInterface {
    private final Options options;

    public Database(DatabaseCredentials databaseCredentials, Options options) {
        this.options = options;
        HibernateUtils.initializeHibernate(databaseCredentials, options);
    }

    @Override
    public void insertPlayerChat(String serverName, String playerName, String playerUUID, String worldName, String msg,
                                 boolean isStaff) {

        final Session session = HibernateUtils.getSession();
        final Transaction tx = session.beginTransaction();

        final EntityPlayer loggerPlayer = this.fetchEntityPlayer(playerName, playerUUID);

        final PlayerChat p = new PlayerChat();

        p.setDate(Instant.now());
        p.setServerName(serverName);
        p.setMessage(msg);
        p.setWorld(worldName);
        p.setEntityPlayer(loggerPlayer);
        p.isStaff(isStaff);
        session.persist(p);
        tx.commit();

    }

    @Override
    public void insertPlayerCommands(String serverName, String playerName, String playerUUID, String worldName, String command,
                                     boolean isStaff) {

        final Session session = HibernateUtils.getSession();
        final Transaction tx = session.beginTransaction();

        final EntityPlayer loggerPlayer = this.fetchEntityPlayer(playerName, playerUUID);

        final PlayerCommand p = new PlayerCommand();

        p.setDate(Instant.now());
        p.setServerName(serverName);
        p.setCommand(command);
        p.setWorld(worldName);
        p.setEntityPlayer(loggerPlayer);
        p.isStaff(isStaff);

        session.persist(p);
        tx.commit();

    }

    @Override
    public void insertPlayerSignText(String serverName, String playerName, String playerUUID, Coordinates coords, String lines,
                                     boolean isStaff) {

        final Session session = HibernateUtils.getSession();
        final Transaction tx = session.beginTransaction();

        final EntityPlayer loggerPlayer = this.fetchEntityPlayer(playerName, playerUUID);

        final PlayerSignText p = new PlayerSignText();

        p.setDate(Instant.now());
        p.setServerName(serverName);
        p.setCoordinates(coords);
        p.setWorld(coords.getWorldName());
        p.setLine(lines);
        p.setEntityPlayer(loggerPlayer);
        p.isStaff(isStaff);

        session.persist(p);
        tx.commit();

    }

    @Override
    public void insertPlayerDeath(
            String serverName, String playerName, String playerUUID, int level, String cause, String who,
            Coordinates coordinates, boolean isStaff
    ) {

        final Session session = HibernateUtils.getSession();
        final Transaction tx = session.beginTransaction();

        final EntityPlayer loggerPlayer = this.fetchEntityPlayer(playerName, playerUUID);

        final PlayerDeath p = new PlayerDeath();

        p.setDate(Instant.now());
        p.setServerName(serverName);
        p.setPlayerLevel(level);
        p.setWorld(coordinates.getWorldName());
        p.setX(coordinates.getX());
        p.setY(coordinates.getY());
        p.setZ(coordinates.getZ());
        p.setEntityPlayer(loggerPlayer);
        p.isStaff(isStaff);
        p.setCause(cause);
        p.setByWho(who);

        session.persist(p);
        tx.commit();

    }

    @Override
    public void insertPlayerTeleport(String serverName, String playerName, String playerUUID, Coordinates oldCoords,
                                     Coordinates newCoords, boolean isStaff) {

        final Session session = HibernateUtils.getSession();
        final Transaction tx = session.beginTransaction();

        final EntityPlayer loggerPlayer = this.fetchEntityPlayer(playerName, playerUUID);

        final PlayerTeleport p = new PlayerTeleport();

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
        p.isStaff(isStaff);

        session.persist(p);
        tx.commit();

    }

    @Override
    public void insertPlayerJoin(String serverName, String playerName, String playerUUID, Coordinates coords,
                                 InetSocketAddress ip, boolean isStaff) {

        final Session session = HibernateUtils.getSession();
        final Transaction tx = session.beginTransaction();
        final EntityPlayer loggerPlayer = this.fetchEntityPlayer(playerName, playerUUID);

        final PlayerJoin p = new PlayerJoin();

        p.setDate(Instant.now());
        p.setServerName(serverName);
        p.setX(coords.getX());
        p.setY(coords.getY());
        p.setZ(coords.getZ());
        p.setWorld(coords.getWorldName());
        //TODO
        if (this.options.isPlayerIPEnabled()) {
            p.setIp(4L);
        }
        p.setEntityPlayer(loggerPlayer);
        p.isStaff(isStaff);

        session.persist(p);
        tx.commit();

    }

    @Override
    public void insertPlayerLeave(String serverName, String playerName, String playerUUID, Coordinates coords, boolean isStaff) {

        final Session session = HibernateUtils.getSession();
        final Transaction tx = session.beginTransaction();

        final EntityPlayer loggerPlayer = this.fetchEntityPlayer(playerName, playerUUID);

        final PlayerLeave p = new PlayerLeave();

        p.setDate(Instant.now());
        p.setServerName(serverName);

        p.setWorld(coords.getWorldName());
        p.setX(coords.getX());
        p.setY(coords.getY());
        p.setZ(coords.getZ());
        p.setEntityPlayer(loggerPlayer);
        p.isStaff(isStaff);
        p.isStaff(isStaff);

        session.persist(p);
        tx.commit();

    }

    @Override
    public void insertBlockPlace(String serverName, String playerName, String playerUUID, String block, Coordinates coords,
                                 boolean isStaff) {

        final Session session = HibernateUtils.getSession();
        final Transaction tx = session.beginTransaction();

        final EntityPlayer loggerPlayer = this.fetchEntityPlayer(playerName, playerUUID);

        final BlockInteraction p = new BlockInteraction();
        p.setDate(Instant.now());
        p.setServerName(serverName);
        p.setWorld(coords.getWorldName());
        p.setX(coords.getX());
        p.setY(coords.getY());
        p.setZ(coords.getZ());
        p.setBlock(block);
        p.setEntityPlayer(loggerPlayer);
        p.isStaff(isStaff);
        p.setAsBlockPlace();

        session.persist(p);
        tx.commit();

    }

    @Override
    public void insertBlockBreak(String serverName, String playerName, String playerUUID, String blockName, Coordinates coords,
                                 boolean isStaff) {

        final Session session = HibernateUtils.getSession();
        final Transaction tx = session.beginTransaction();

        final EntityPlayer loggerPlayer = this.fetchEntityPlayer(playerName, playerUUID);

        final BlockInteraction p = new BlockInteraction();

        p.setDate(Instant.now());
        p.setServerName(serverName);
        p.setWorld(coords.getWorldName());
        p.setX(coords.getX());
        p.setY(coords.getY());
        p.setZ(coords.getZ());
        p.setBlock(blockName);
        p.setEntityPlayer(loggerPlayer);
        p.isStaff(isStaff);
        p.setAsBlockBreak();

        session.persist(p);
        tx.commit();

    }

    @Override
    public void insertTps(String serverName, double tpss) {

        final Session session = HibernateUtils.getSession();
        final Transaction tx = session.beginTransaction();

        final Tp p = new Tp();

        p.setDate(Instant.now());
        p.setServerName(serverName);
        p.setTps((int) tpss);

        session.persist(p);
        tx.commit();

    }

    @Override
    public void insertRam(String serverName, long tm, long um, long fm) {

        final Session session = HibernateUtils.getSession();
        final Transaction tx = session.beginTransaction();

        final Ram p = new Ram();

        p.setDate(Instant.now());
        p.setServerName(serverName);
        p.setTotalMemory((int) tm);
        p.setUsedMemory((int) um);
        p.setFreeMemory((int) fm);

        session.persist(p);
        tx.commit();

    }

    @Override
    public void insertPlayerKick(String serverName, String playerName, String playerUUID, Coordinates coords, String reason,
                                 boolean isStaff) {

        final Session session = HibernateUtils.getSession();
        final Transaction tx = session.beginTransaction();

        final EntityPlayer loggerPlayer = this.fetchEntityPlayer(playerName, playerUUID);

        final PlayerKick p = new PlayerKick();

        p.setDate(Instant.now());
        p.setServerName(serverName);
        p.setWorld(coords.getWorldName());
        p.setX(coords.getX());
        p.setY(coords.getY());
        p.setZ(coords.getZ());
        p.setReason(reason);
        p.setEntityPlayer(loggerPlayer);
        p.isStaff(isStaff);

        session.persist(p);
        tx.commit();

    }

    @Override
    public void insertPortalCreate(String serverName, String worldName, String by) {

        final Session session = HibernateUtils.getSession();
        final Transaction tx = session.beginTransaction();

        final PortalCreation p = new PortalCreation();

        p.setDate(Instant.now());
        p.setServerName(serverName);
        p.setWorld(worldName);
        p.setCausedBy(by);

        session.persist(p);
        tx.commit();

    }

    @Override
    public void insertLevelChange(String serverName, String playerName, String playerUUID, boolean isStaff) {

        final Session session = HibernateUtils.getSession();
        final Transaction tx = session.beginTransaction();

        final EntityPlayer loggerPlayer = this.fetchEntityPlayer(playerName, playerUUID);

        final PlayerLevel p = new PlayerLevel();

        p.setDate(Instant.now());
        p.setServerName(serverName);
        p.setEntityPlayer(loggerPlayer);
        p.isStaff(isStaff);

        session.persist(p);
        tx.commit();

    }

    @Override
    public void insertBucketFill(String serverName, String playerName, String playerUUID, String bucket, Coordinates coords,
                                 boolean isStaff) {

        final Session session = HibernateUtils.getSession();
        final Transaction tx = session.beginTransaction();

        final EntityPlayer loggerPlayer = this.fetchEntityPlayer(playerName, playerUUID);

        final BucketFill b = new BucketFill();

        b.setDate(Instant.now());
        b.setServerName(serverName);
        b.setWorld(coords.getWorldName());
        b.setX(coords.getX());
        b.setY(coords.getY());
        b.setZ(coords.getZ());
        b.setBucket(bucket);
        b.setEntityPlayer(loggerPlayer);
        b.isStaff(isStaff);

        session.persist(b);
        tx.commit();

    }

    @Override
    public void insertBucketEmpty(String serverName, String playerName, String playerUUID, String bucket, Coordinates coords,
                                  boolean isStaff) {

        final Session session = HibernateUtils.getSession();
        final Transaction tx = session.beginTransaction();

        final EntityPlayer loggerPlayer = this.fetchEntityPlayer(playerName, playerUUID);

        final BucketEmpty b = new BucketEmpty();

        b.setDate(Instant.now());
        b.setServerName(serverName);
        b.setWorld(coords.getWorldName());
        b.setX(coords.getX());
        b.setY(coords.getY());
        b.setZ(coords.getZ());
        b.setBucket(bucket);
        b.setEntityPlayer(loggerPlayer);
        b.isStaff(isStaff);

        session.persist(b);
        tx.commit();

    }

    @Override
    public void insertAnvil(String serverName, String playerName, String playerUUID, String newName, boolean isStaff) {

        final Session session = HibernateUtils.getSession();
        final Transaction tx = session.beginTransaction();

        final EntityPlayer loggerPlayer = this.fetchEntityPlayer(playerName, playerUUID);

        final Anvil b = new Anvil();

        b.setDate(Instant.now());
        b.setServerName(serverName);
        b.setEntityPlayer(loggerPlayer);
        b.isStaff(isStaff);
        b.setNewName(newName);

        session.persist(b);
        tx.commit();

    }

    @Override
    public void insertServerStart(String serverName) {

        final Session session = HibernateUtils.getSession();
        final Transaction tx = session.beginTransaction();

        final ServerStart s = new ServerStart();
        s.setDate(Instant.now());
        s.setServerName(serverName);

        session.persist(s);
        tx.commit();

    }

    @Override
    public void insertServerStop(String serverName) {

        final Session session = HibernateUtils.getSession();
        final Transaction tx = session.beginTransaction();

        final ServerStop s = new ServerStop();
        s.setDate(Instant.now());
        s.setServerName(serverName);

        session.persist(s);
        tx.commit();

    }

    @Override
    public void insertItemDrop(
            String serverName, String playerName, String playerUUID, String item, int amount, Coordinates coords,
            List<String> enchantment, String changedName, boolean isStaff
    ) {

        final Session session = HibernateUtils.getSession();
        final Transaction tx = session.beginTransaction();

        final EntityPlayer loggerPlayer = this.fetchEntityPlayer(playerName, playerUUID);

        final ItemDrop b = new ItemDrop();

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
        b.isStaff(isStaff);

        session.persist(b);
        tx.commit();

    }

    @Override
    public void insertEnchant(
            String serverName, String playerName, String playerUUID, List<String> enchantment, int enchantmentLevel,
            String item, int cost, Coordinates coordinates, boolean isStaff
    ) {

        final Session session = HibernateUtils.getSession();
        final Transaction tx = session.beginTransaction();

        final EntityPlayer loggerPlayer = this.fetchEntityPlayer(playerName, playerUUID);

        final Enchanting e = new Enchanting();

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
        e.isStaff(isStaff);

        session.persist(e);
        tx.commit();

    }

    @Override
    public void insertBookEditing(
            String serverName, String playerName, String playerUUID, String worldName, int pages,
            List<String> content, String signedBy, boolean isStaff
    ) {

        final Session session = HibernateUtils.getSession();
        final Transaction tx = session.beginTransaction();

        final EntityPlayer loggerPlayer = this.fetchEntityPlayer(playerName, playerUUID);

        final BookEditing b = new BookEditing();
        b.setDate(Instant.now());
        b.setServerName(serverName);
        b.setWorld(worldName);
        b.setPageContent(content.toString());
        b.setPageCount(pages);
        b.setSignedBy(signedBy);
        b.setEntityPlayer(loggerPlayer);
        b.isStaff(isStaff);

        session.persist(b);
        tx.commit();

    }

    @Override
    public void insertAfk(String serverName, String playerName, String playerUUID, Coordinates coords, boolean isStaff) {


    }

    @Override
    public void insertWrongPassword(String serverName, String playerName, String playerUUID, String worldName, boolean isStaff) {


    }

    @Override
    public void insertItemPickup(
            String serverName, String playerName, String playerUUID, String item, int amount,
            Coordinates coords, String changedName, boolean isStaff
    ) {

        final Session session = HibernateUtils.getSession();
        final Transaction tx = session.beginTransaction();

        final EntityPlayer loggerPlayer = this.fetchEntityPlayer(playerName, playerUUID);

        final ItemPickup b = new ItemPickup();

        b.setDate(Instant.now());
        b.setServerName(serverName);
        b.setWorld(coords.getWorldName());
        b.setX(coords.getX());
        b.setY(coords.getY());
        b.setZ(coords.getZ());
        b.setChangedName(changedName);
        b.setItem(item);
        b.setAmount(amount);
        b.setEntityPlayer(loggerPlayer);
        b.isStaff(isStaff);

        session.persist(b);
        tx.commit();

    }

    @Override
    public void insertFurnace(String serverName, String playerName, String playerUUID, String item, int amount,
                              Coordinates coords, boolean isStaff) {

        final Session session = HibernateUtils.getSession();
        final Transaction tx = session.beginTransaction();

        final EntityPlayer loggerPlayer = this.fetchEntityPlayer(playerName, playerUUID);

        final Furnace b = new Furnace();

        b.setDate(Instant.now());
        b.setServerName(serverName);
        b.setWorld(coords.getWorldName());
        b.setX(coords.getX());
        b.setY(coords.getY());
        b.setZ(coords.getZ());
        b.setItem(item);
        b.setAmount(amount);
        b.setEntityPlayer(loggerPlayer);
        b.isStaff(isStaff);

        session.persist(b);
        tx.commit();

    }

    @Override
    public void insertRCON(String serverName, String ip, String command) {

        final Session session = HibernateUtils.getSession();
        final Transaction tx = session.beginTransaction();

        final Rcon b = new Rcon();
        b.setDate(Instant.now());
        b.setServerName(serverName);
        b.setIp(Long.getLong(ip));

        session.persist(b);
        tx.commit();

    }

    @Override
    public void insertGameMode(String serverName, String playerName, String playerUUID, String gameMode, String worldName,
                               boolean isStaff) {

        final Session session = HibernateUtils.getSession();
        final Transaction tx = session.beginTransaction();

        final EntityPlayer loggerPlayer = this.fetchEntityPlayer(playerName, playerUUID);

        final GameMode b = new GameMode();

        b.setDate(Instant.now());
        b.setServerName(serverName);
        b.setWorld(worldName);
        b.setGameMode(gameMode);
        b.setEntityPlayer(loggerPlayer);
        b.isStaff(isStaff);

        session.persist(b);
        tx.commit();

    }

    @Override
    public void insertPlayerCraft(String serverName, String playerName, String playerUUID, String item, int amount,
                                  Coordinates coords, boolean isStaff) {

        final Session session = HibernateUtils.getSession();
        final Transaction tx = session.beginTransaction();

        final EntityPlayer loggerPlayer = this.fetchEntityPlayer(playerName, playerUUID);

        final Crafting b = new Crafting();

        b.setDate(Instant.now());
        b.setServerName(serverName);
        b.setX(coords.getX());
        b.setY(coords.getY());
        b.setZ(coords.getZ());
        b.setAmount(amount);
        b.setItem(item);
        b.setWorld(coords.getWorldName());
        b.setEntityPlayer(loggerPlayer);
        b.isStaff(isStaff);

        session.persist(b);
        tx.commit();

    }

    @Override
    public void insertVault(String serverName, String playerName, String playerUUID, double oldBal, double newBal,
                            boolean isStaff) {


    }

    @Override
    public void insertPlayerRegistration(String serverName, String playerName, String playerUUID, String joinDate) {

        final Session session = HibernateUtils.getSession();
        final Transaction tx = session.beginTransaction();

        final EntityPlayer loggerPlayer = this.fetchEntityPlayer(playerName, playerUUID);

        session.persist(loggerPlayer);
        tx.commit();

    }

    @Override
    public void insertPrimedTnt(String serverName, String playerName, String playerUUID, Coordinates coords, boolean isStaff) {

        final Session session = HibernateUtils.getSession();
        final Transaction tx = session.beginTransaction();

        final EntityPlayer loggerPlayer = this.fetchEntityPlayer(playerName, playerUUID);

        final PrimedTnt b = new PrimedTnt();

        b.setDate(Instant.now());
        b.setServerName(serverName);
        b.setX(coords.getX());
        b.setY(coords.getY());
        b.setZ(coords.getZ());
        b.setWorld(coords.getWorldName());
        b.setEntityPlayer(loggerPlayer);
        b.isStaff(isStaff);

        session.persist(b);
        tx.commit();
    }

    @Override
    public void insertLiteBans(
            String serverName, String executor, String command, String onWho, String duration,
            String reason, boolean isSilent
    ) {


    }

    @Override
    public void insertAdvanceBanData(
            String serverName, String type, String executor, String executedOn,
            String reason, long expirationDate
    ) {


    }

    @Override
    public void insertCommandBlock(String serverName, String msg) {

        final CommandBlock b = new CommandBlock();
        b.setServerName(serverName);
        b.setCommand(msg);

        this.save(b);

    }

    @Override
    public void insertWoodStripping(String serverName, String playerName, String playerUUID, String logName, Coordinates coords,
                                    boolean isStaff) {


    }

    @Override
    public void insertChestInteraction(String serverName, String playerName, String playerUUID, Coordinates coords,
                                       String[] items, boolean isStaff) {

        final Session session = HibernateUtils.getSession();
        final Transaction tx = session.beginTransaction();

        final EntityPlayer loggerPlayer = this.fetchEntityPlayer(playerName, playerUUID);

        final ChestInteraction b = new ChestInteraction();

        b.setDate(Instant.now());
        b.setServerName(serverName);
        b.setWorld(coords.getWorldName());
        b.setX(coords.getX());
        b.setY(coords.getY());
        b.setZ(coords.getZ());
        b.setEntityPlayer(loggerPlayer);
        b.isStaff(isStaff);
        b.setItems(String.join(",", items));

        session.persist(b);
        tx.commit();

    }

    @Override
    public void insertEntityDeath(String serverName, String playerName, String playerUUID, String mob, Coordinates coords,
                                  boolean isStaff) {

        final Session session = HibernateUtils.getSession();
        final Transaction tx = session.beginTransaction();

        final EntityPlayer loggerPlayer = this.fetchEntityPlayer(playerName, playerUUID);

        final EntityDeath b = new EntityDeath(serverName, coords, mob, loggerPlayer, isStaff);

        session.persist(b);
        tx.commit();
    }

    public void emptyTable() {


    }

    @Override
    public void insertConsoleCommand(String serverName, String msg) {

        final ConsoleCommand p = new ConsoleCommand(serverName, msg);

        this.save(p);

    }

    @Override
    public void insertServerReload(String serverName, String playerName, boolean isStaff) {

    }

    @Override
    public void insertPlayerLogin(String serverName, String playerName, String toString, InetSocketAddress playerIP,
                                  boolean hasPermission) {

    }


    private void save(Object obj) {
        final Session session;
        final Transaction transaction;
        session = HibernateUtils.getSession();
        transaction = session.beginTransaction();
        session.save(obj);
        transaction.commit();
    }

    public EntityPlayer fetchEntityPlayer(String playerName, String playerUUID) {
        return HibernateUtils.getSession()
                .createNamedQuery("EntityPlayer.findOneByName", EntityPlayer.class)
                .setParameter("playerName", playerName)
                .setHint(QueryHints.READ_ONLY, true)
                .getResultStream()
                .findFirst()
                .orElse(new EntityPlayer(playerName, playerUUID));

    }

    @Override
    public void disconnect()
    {
        HibernateUtils.closeSession();
        HibernateUtils.closeSessionFactory();
    }

}
