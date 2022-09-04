package me.prism3.loggercore.database.datasource;

import me.prism3.loggercore.database.DataInterface;
import me.prism3.loggercore.database.DataSourceInterface;
import me.prism3.loggercore.database.data.Coordinates;
import me.prism3.loggercore.database.data.Options;
import me.prism3.loggercore.database.data.Settings;
import me.prism3.loggercore.database.entity.enums.BucketActionType;
import me.prism3.loggercore.database.entity.enums.InteractionType;
import me.prism3.loggercore.database.entity.enums.ItemActionType;
import me.prism3.loggercore.database.entity.enums.PlayerConnectionType;
import me.prism3.loggercore.database.utils.HibernateUtils;
import me.prism3.loggercore.database.utils.Queue;
import me.prism3.loggercore.database.entity.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.net.InetSocketAddress;
import java.time.Instant;
import java.util.List;

public final class Database implements DataSourceInterface{

    private final Options options;
    private final Queue queue = new Queue();

    public Database(Settings databaseCredentials, Options options) {
        this.options = options;
        this.queue.setBatchSize(
                (Integer) this.options.getEnabledLogs().getOrDefault("loggercore.database.batchsize", 50));
        HibernateUtils.initializeHibernate(databaseCredentials, options);
    }

    @Override
    public void insertPlayerChat(String serverName, String playerName, String playerUUID,
                                 String worldName, String msg,
                                 boolean isStaff) {



        final PlayerChat p = new PlayerChat();

        p.setDate(Instant.now());
        p.setServerName(serverName);
        p.setMessage(msg);
        p.setWorld(worldName);
        p.setEntityPlayer(this.fetchEntityPlayer(playerName, playerUUID));
        p.isStaff(isStaff);
        this.queue.addItemToQueue(p);

    }

    @Override
    public void insertPlayerCommands(String serverName, String playerName, String playerUUID,
                                     String worldName, String command,
                                     boolean isStaff) {



        final PlayerCommand p = new PlayerCommand();

        p.setDate(Instant.now());
        p.setServerName(serverName);
        p.setCommand(command);
        p.setWorld(worldName);
        p.setEntityPlayer(this.fetchEntityPlayer(playerName, playerUUID));
        p.isStaff(isStaff);

        this.queue.addItemToQueue(p);

    }

    @Override
    public void insertPlayerSignText(String serverName, String playerName, String playerUUID,
                                     Coordinates coords, String lines,
                                     boolean isStaff) {



        final PlayerSignText p = new PlayerSignText();

        p.setDate(Instant.now());
        p.setServerName(serverName);
        p.setCoordinates(coords);
        p.setWorld(coords.getWorldName());
        p.setLine(lines);
        p.setEntityPlayer(this.fetchEntityPlayer(playerName, playerUUID));
        p.isStaff(isStaff);

        this.queue.addItemToQueue(p);

    }

    @Override
    public void insertPlayerDeath(
            String serverName, String playerName, String playerUUID, int level, String cause,
            String who,
            Coordinates coordinates, boolean isStaff) {



        final PlayerDeath p = new PlayerDeath();

        p.setDate(Instant.now());
        p.setServerName(serverName);
        p.setPlayerLevel(level);
        p.setWorld(coordinates.getWorldName());
        p.setX(coordinates.getX());
        p.setY(coordinates.getY());
        p.setZ(coordinates.getZ());
        p.setEntityPlayer(this.fetchEntityPlayer(playerName, playerUUID));
        p.isStaff(isStaff);
        p.setCause(cause);
        p.setByWho(who);

        this.queue.addItemToQueue(p);


    }

    @Override
    public void insertPlayerTeleport(String serverName, String playerName, String playerUUID,
                                     Coordinates oldCoords,
                                     Coordinates newCoords, boolean isStaff) {



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
        p.setEntityPlayer(this.fetchEntityPlayer(playerName, playerUUID));
        p.isStaff(isStaff);

        this.queue.addItemToQueue(p);


    }

    @Override
    public void insertPlayerJoin(String serverName, String playerName, String playerUUID,
                                 Coordinates coords,
                                 InetSocketAddress ip, boolean isStaff) {


        final PlayerConnection p = new PlayerConnection();

        p.setDate(Instant.now());
        p.setServerName(serverName);
        p.setX(coords.getX());
        p.setY(coords.getY());
        p.setZ(coords.getZ());
        p.setWorld(coords.getWorldName());
        //TODO
        if(this.options.isPlayerIPEnabled()) {
            p.setIp(4L);
        }
        p.setEntityPlayer(this.fetchEntityPlayer(playerName, playerUUID));
        p.isStaff(isStaff);
        p.setPlayerConnectionType(PlayerConnectionType.PLAYER_JOIN);

        this.queue.addItemToQueue(p);


    }

    @Override
    public void insertPlayerLeave(String serverName, String playerName, String playerUUID,
                                  Coordinates coords, boolean isStaff) {



        final PlayerConnection p = new PlayerConnection();

        p.setDate(Instant.now());
        p.setServerName(serverName);

        p.setWorld(coords.getWorldName());
        p.setX(coords.getX());
        p.setY(coords.getY());
        p.setZ(coords.getZ());
        p.setEntityPlayer(this.fetchEntityPlayer(playerName, playerUUID));
        p.isStaff(isStaff);
        p.setPlayerConnectionType(PlayerConnectionType.PLAYER_LEAVE);
        this.queue.addItemToQueue(p);

    }

    @Override
    public void insertBlockPlace(String serverName, String playerName, String playerUUID,
                                 String block, Coordinates coords,
                                 boolean isStaff) {

        final BlockInteraction p = new BlockInteraction();
        p.setDate(Instant.now());
        p.setServerName(serverName);
        p.setWorld(coords.getWorldName());
        p.setX(coords.getX());
        p.setY(coords.getY());
        p.setZ(coords.getZ());
        p.setBlock(block);
        p.setEntityPlayer(this.fetchEntityPlayer(playerName, playerUUID));
        p.isStaff(isStaff);
        p.setInteractionType(InteractionType.BLOCK_PLACE);
        this.queue.addItemToQueue(p);

    }

    @Override
    public void insertBlockBreak(String serverName, String playerName, String playerUUID,
                                 String blockName, Coordinates coords,
                                 boolean isStaff) {


        final BlockInteraction p = new BlockInteraction();
        p.setDate(Instant.now());
        p.setServerName(serverName);
        p.setWorld(coords.getWorldName());
        p.setX(coords.getX());
        p.setY(coords.getY());
        p.setZ(coords.getZ());
        p.setBlock(blockName);
        p.setEntityPlayer(this.fetchEntityPlayer(playerName, playerUUID));
        p.isStaff(isStaff);
        p.setInteractionType(InteractionType.BLOCK_BREAK);
        this.queue.addItemToQueue(p);

    }

    @Override
    public void insertTps(String serverName, double tpss) {


        final Tps p = new Tps();

        p.setDate(Instant.now());
        p.setServerName(serverName);
        p.setTps((int) tpss);

        this.queue.addItemToQueue(p);

    }

    @Override
    public void insertRam(String serverName, long tm, long um, long fm) {


        final Ram p = new Ram();

        p.setDate(Instant.now());
        p.setServerName(serverName);
        p.setTotalMemory((int) tm);
        p.setUsedMemory((int) um);
        p.setFreeMemory((int) fm);

        this.queue.addItemToQueue(p);


    }

    @Override
    public void insertPlayerKick(String serverName, String playerName, String playerUUID,
                                 Coordinates coords, String reason,
                                 boolean isStaff) {



        final PlayerKick p = new PlayerKick();

        p.setDate(Instant.now());
        p.setServerName(serverName);
        p.setWorld(coords.getWorldName());
        p.setX(coords.getX());
        p.setY(coords.getY());
        p.setZ(coords.getZ());
        p.setReason(reason);
        p.setEntityPlayer(this.fetchEntityPlayer(playerName, playerUUID));
        p.isStaff(isStaff);

        this.queue.addItemToQueue(p);


    }

    @Override
    public void insertPortalCreate(String serverName, String worldName, String by) {


        final PortalCreation p = new PortalCreation();

        p.setDate(Instant.now());
        p.setServerName(serverName);
        p.setWorld(worldName);
        p.setCausedBy(by);

        this.queue.addItemToQueue(p);

    }

    @Override
    public void insertLevelChange(String serverName, String playerName, String playerUUID,
                                  boolean isStaff) {



        final PlayerLevel p = new PlayerLevel();

        p.setDate(Instant.now());
        p.setServerName(serverName);
        p.setEntityPlayer(this.fetchEntityPlayer(playerName, playerUUID));
        p.isStaff(isStaff);

        this.queue.addItemToQueue(p);

    }

    @Override
    public void insertBucketFill(String serverName, String playerName, String playerUUID,
                                 String bucket, Coordinates coords,
                                 boolean isStaff) {



        final BucketAction b = new BucketAction();

        b.setDate(Instant.now());
        b.setServerName(serverName);
        b.setWorld(coords.getWorldName());
        b.setX(coords.getX());
        b.setY(coords.getY());
        b.setZ(coords.getZ());
        b.setBucket(bucket);
        b.setEntityPlayer(this.fetchEntityPlayer(playerName, playerUUID));
        b.isStaff(isStaff);
        b.setBucketActionType(BucketActionType.BUCKET_FILL);

        this.queue.addItemToQueue(b);

    }

    @Override
    public void insertBucketEmpty(String serverName, String playerName, String playerUUID,
                                  String bucket, Coordinates coords,
                                  boolean isStaff) {



        final BucketAction b = new BucketAction();

        b.setDate(Instant.now());
        b.setServerName(serverName);
        b.setWorld(coords.getWorldName());
        b.setX(coords.getX());
        b.setY(coords.getY());
        b.setZ(coords.getZ());
        b.setBucket(bucket);
        b.setEntityPlayer(this.fetchEntityPlayer(playerName, playerUUID));
        b.isStaff(isStaff);
        b.setBucketActionType(BucketActionType.BUCKET_EMPTY);

        this.queue.addItemToQueue(b);

    }

    @Override
    public void insertAnvil(String serverName, String playerName, String playerUUID, String newName,
                            boolean isStaff) {



        final Anvil b = new Anvil();

        b.setDate(Instant.now());
        b.setServerName(serverName);
        b.setEntityPlayer(this.fetchEntityPlayer(playerName, playerUUID));
        b.isStaff(isStaff);
        b.setNewName(newName);

        this.queue.addItemToQueue(b);

    }

    @Override
    public void insertServerStart(String serverName) {


        final ServerStart s = new ServerStart();
        s.setDate(Instant.now());
        s.setServerName(serverName);

        this.queue.addItemToQueue(s);

    }

    @Override
    public void insertServerStop(String serverName) {


        final ServerStop s = new ServerStop();
        s.setDate(Instant.now());
        s.setServerName(serverName);

        this.queue.addItemToQueue(s);

    }

    @Override
    public void insertItemDrop(
            String serverName, String playerName, String playerUUID, String item, int amount,
            Coordinates coords,
            List<String> enchantment, String changedName, boolean isStaff) {



        final ItemAction b = new ItemAction();

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
        b.setEntityPlayer(this.fetchEntityPlayer(playerName, playerUUID));
        b.isStaff(isStaff);
        b.setItemActionType(ItemActionType.ITEM_DROP);

        this.queue.addItemToQueue(b);

    }

    @Override
    public void insertEnchant(
            String serverName, String playerName, String playerUUID, List<String> enchantment,
            int enchantmentLevel,
            String item, int cost, Coordinates coordinates, boolean isStaff) {



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
        e.setEntityPlayer(this.fetchEntityPlayer(playerName, playerUUID));
        e.isStaff(isStaff);

        this.queue.addItemToQueue(e);

    }

    @Override
    public void insertBookEditing(
            String serverName, String playerName, String playerUUID, String worldName, int pages,
            List<String> content, String signedBy, boolean isStaff) {



        final BookEditing b = new BookEditing();
        b.setDate(Instant.now());
        b.setServerName(serverName);
        b.setWorld(worldName);
        b.setPageContent(content.toString());
        b.setPageCount(pages);
        b.setSignedBy(signedBy);
        b.setEntityPlayer(this.fetchEntityPlayer(playerName, playerUUID));
        b.isStaff(isStaff);

        this.queue.addItemToQueue(b);

    }

    @Override
    public void insertAfk(String serverName, String playerName, String playerUUID,
                          Coordinates coords, boolean isStaff) {


    }

    @Override
    public void insertWrongPassword(String serverName, String playerName, String playerUUID,
                                    String worldName, boolean isStaff) {


    }

    @Override
    public void insertItemPickup(
            String serverName, String playerName, String playerUUID, String item, int amount,
            Coordinates coords, String changedName, boolean isStaff) {



        final ItemAction b = new ItemAction();

        b.setDate(Instant.now());
        b.setServerName(serverName);
        b.setWorld(coords.getWorldName());
        b.setX(coords.getX());
        b.setY(coords.getY());
        b.setZ(coords.getZ());
        b.setChangedName(changedName);
        b.setItem(item);
        b.setAmount(amount);
        b.setEntityPlayer(this.fetchEntityPlayer(playerName, playerUUID));
        b.isStaff(isStaff);
        b.setItemActionType(ItemActionType.ITEM_PICKUP);
        this.queue.addItemToQueue(b);

    }

    @Override
    public void insertFurnace(String serverName, String playerName, String playerUUID, String item,
                              int amount,
                              Coordinates coords, boolean isStaff) {



        final Furnace b = new Furnace();

        b.setDate(Instant.now());
        b.setServerName(serverName);
        b.setWorld(coords.getWorldName());
        b.setX(coords.getX());
        b.setY(coords.getY());
        b.setZ(coords.getZ());
        b.setItem(item);
        b.setAmount(amount);
        b.setEntityPlayer(this.fetchEntityPlayer(playerName, playerUUID));
        b.isStaff(isStaff);

        this.queue.addItemToQueue(b);

    }

    @Override
    public void insertRCON(String serverName, String ip, String command) {


        final Rcon b = new Rcon();
        b.setDate(Instant.now());
        b.setServerName(serverName);
        b.setIp(Long.getLong(ip));

        this.queue.addItemToQueue(b);

    }

    @Override
    public void insertGameMode(String serverName, String playerName, String playerUUID,
                               String gameMode, String worldName,
                               boolean isStaff) {



        final GameMode b = new GameMode();

        b.setDate(Instant.now());
        b.setServerName(serverName);
        b.setWorld(worldName);
        b.setGameMode(gameMode);
        b.setEntityPlayer(this.fetchEntityPlayer(playerName, playerUUID));
        b.isStaff(isStaff);

        this.queue.addItemToQueue(b);

    }

    @Override
    public void insertPlayerCraft(String serverName, String playerName, String playerUUID,
                                  String item, int amount,
                                  Coordinates coords, boolean isStaff) {



        final Crafting b = new Crafting();

        b.setDate(Instant.now());
        b.setServerName(serverName);
        b.setX(coords.getX());
        b.setY(coords.getY());
        b.setZ(coords.getZ());
        b.setAmount(amount);
        b.setItem(item);
        b.setWorld(coords.getWorldName());
        b.setEntityPlayer(this.fetchEntityPlayer(playerName, playerUUID));
        b.isStaff(isStaff);

        this.queue.addItemToQueue(b);

    }

    @Override
    public void insertVault(String serverName, String playerName, String playerUUID, double oldBal,
                            double newBal,
                            boolean isStaff) {


    }

    @Override
    public void insertPlayerRegistration(String serverName, String playerName, String playerUUID,
                                         String joinDate) {

    }

    @Override
    public void insertPrimedTnt(String serverName, String playerName, String playerUUID,
                                Coordinates coords, boolean isStaff) {



        final PrimedTnt b = new PrimedTnt();

        b.setDate(Instant.now());
        b.setServerName(serverName);
        b.setX(coords.getX());
        b.setY(coords.getY());
        b.setZ(coords.getZ());
        b.setWorld(coords.getWorldName());
        b.setEntityPlayer(this.fetchEntityPlayer(playerName, playerUUID));
        b.isStaff(isStaff);

        this.queue.addItemToQueue(b);
    }

    @Override
    public void insertLiteBans(
            String serverName, String executor, String command, String onWho, String duration,
            String reason, boolean isSilent) {

    }

    @Override
    public void insertAdvanceBanData(
            String serverName, String type, String executor, String executedOn,
            String reason, long expirationDate) {

    }

    @Override
    public void insertCommandBlock(String serverName, String msg) {

        final CommandBlock b = new CommandBlock();
        b.setServerName(serverName);
        b.setCommand(msg);

        this.save(b);

    }

    @Override
    public void insertWoodStripping(String serverName, String playerName, String playerUUID,
                                    String logName, Coordinates coords,
                                    boolean isStaff) {


    }

    @Override
    public void insertChestInteraction(String serverName, String playerName, String playerUUID,
                                       Coordinates coords,
                                       String[] items, boolean isStaff) {



        final ChestInteraction b = new ChestInteraction();

        b.setDate(Instant.now());
        b.setServerName(serverName);
        b.setWorld(coords.getWorldName());
        b.setX(coords.getX());
        b.setY(coords.getY());
        b.setZ(coords.getZ());
        b.setEntityPlayer(this.fetchEntityPlayer(playerName, playerUUID));
        b.isStaff(isStaff);
        b.setItems(String.join(",", items));

        this.queue.addItemToQueue(b);

    }

    @Override
    public void insertEntityDeath(String serverName, String playerName, String playerUUID,
                                  String mob, Coordinates coords,
                                  boolean isStaff) {


        final EntityDeath b = new EntityDeath(serverName, coords, mob,
                this.fetchEntityPlayer(playerName, playerUUID), isStaff);
        this.queue.addItemToQueue(b);

    }

    @Override
    public void insertConsoleCommand(String serverName, String msg) {

        final ConsoleCommand p = new ConsoleCommand(serverName, msg);

        this.queue.addItemToQueue(p);

    }

    @Override
    public void insertServerReload(String serverName, String playerName, boolean isStaff) {

    }

    @Override
    public void insertPlayerLogin(String serverName, String playerName, String toString,
                                  InetSocketAddress playerIP,
                                  boolean hasPermission) {

    }

    @Override
    public void insertItemFramePlace(String serverName, String playerName, String playerUUID,
                                     Coordinates coords, boolean isStaff) {
        final BlockInteraction p = new BlockInteraction();
        p.setDate(Instant.now());
        p.setServerName(serverName);
        p.setWorld(coords.getWorldName());
        p.setX(coords.getX());
        p.setY(coords.getY());
        p.setZ(coords.getZ());
        //TODO
        p.setBlock(null);
        p.setEntityPlayer(this.fetchEntityPlayer(playerName, playerUUID));
        p.isStaff(isStaff);
        p.setInteractionType(InteractionType.BLOCK_PLACE);
        this.queue.addItemToQueue(p);
    }

    @Override
    public void insertItemFrameBreak(String serverName, String playerName, String playerUUID,
                                     Coordinates coords, boolean isStaff) {
        final BlockInteraction p = new BlockInteraction();
        p.setDate(Instant.now());
        p.setServerName(serverName);
        p.setWorld(coords.getWorldName());
        p.setX(coords.getX());
        p.setY(coords.getY());
        p.setZ(coords.getZ());
        //TODO
        p.setBlock(null);
        p.setEntityPlayer(this.fetchEntityPlayer(playerName, playerUUID));
        p.isStaff(isStaff);
        p.setInteractionType(InteractionType.BLOCK_BREAK);
        this.queue.addItemToQueue(p);
    }

    @Override
    public void insertServerSwitch(String serverName, String playerUUID, String playerName,
                                   String from, String destination, boolean isStaff) {

    }

    @Override
    public void insertPAFFriendMessage(String serverName, String playerUUID, String playerName,
                                       String message, String receiver, boolean isStaff) {

    }

    @Override
    public void insertPAFPartyMessage(String serverName, String playerUUID, String playerName,
                                      String message, String leader, List<String> partyMembers,
                                      boolean isStaff) {

    }


    @Override
    public void disconnect() {
        this.queue.flushCurrentQueue();
        HibernateUtils.closeSessionFactory();
    }

    @Override
    public List<PlayerChat> getPlayerChatByPlayerName(String playerName, int offset,
                                                      int limit) {
        Session session = HibernateUtils.getSession();
        Transaction tx = session.beginTransaction();
        final Query<PlayerChat> query = session.createQuery(
                "select p from PlayerChat p where p.entityPlayer.playerName=:playerName",
                PlayerChat.class);

        query.setParameter("playerName", playerName);
        query.setReadOnly(true);
        query.setCacheable(true);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        final List<PlayerChat> results = query.list();

        tx.commit();

        return results;
    }

    public Long countByTable(String action) {

        return 0L;

    }


    public void emptyTable() {


    }

    private void save(Object obj) {

        Transaction tx = null;

        try {
            final Session session;
            session = HibernateUtils.getSession();
            tx = session.beginTransaction();
            session.save(obj);
        }
        finally { if(tx != null) tx.commit(); }
    }

    public EntityPlayer fetchEntityPlayer(String playerName, String playerUUID) {
        return new EntityPlayer(playerName, playerUUID);
    }

    public void runTest() {
        for (int i = 0; i < 1500; i++) {
            this.insertConsoleCommand("Server Dzb", "bousni 1 2");
        }
    }
}
