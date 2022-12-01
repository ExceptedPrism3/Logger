package me.prism3.loggercore.database.queue;

import me.prism3.loggercore.database.DataSourceInterface;
import me.prism3.loggercore.database.data.Coordinates;
import me.prism3.loggercore.database.entity.*;
import me.prism3.loggercore.database.entity.enums.BucketActionType;
import me.prism3.loggercore.database.entity.enums.InteractionType;
import me.prism3.loggercore.database.entity.enums.ItemActionType;
import me.prism3.loggercore.database.entity.enums.PlayerConnectionType;

import java.net.InetSocketAddress;
import java.time.Instant;
import java.util.List;

public final class QueueManager extends Queue {

    public QueueManager(DataSourceInterface dataSource) {
        super(dataSource);
    }

    public void queuePlayerChat(String serverName, String playerName, String playerUUID, String worldName, String msg,
                                boolean isStaff) {

        final PlayerChat p = new PlayerChat();

        p.setDate(Instant.now());
        p.setServerName(serverName);
        p.setMessage(msg);
        p.setWorld(worldName);
        p.setEntityPlayer(new EntityPlayer(playerName, playerUUID));
        p.isStaff(isStaff);

        super.addItemToQueue(p);
    }

    public void queuePlayerCommands(String serverName, String playerName, String playerUUID, String worldName,
                                    String command, boolean isStaff) {

        final PlayerCommand p = new PlayerCommand();

        p.setDate(Instant.now());
        p.setServerName(serverName);
        p.setCommand(command);
        p.setWorld(worldName);
        p.setEntityPlayer(new EntityPlayer(playerName, playerUUID));
        p.isStaff(isStaff);

        super.addItemToQueue(p);
    }

    public void queuePlayerSignText(String serverName, String playerName, String playerUUID, Coordinates coords,
                                    String lines, boolean isStaff) {

        final PlayerSignText p = new PlayerSignText();

        p.setDate(Instant.now());
        p.setServerName(serverName);
        p.setCoordinates(coords);
        p.setWorld(coords.getWorldName());
        p.setLine(lines);
        p.setEntityPlayer(new EntityPlayer(playerName, playerUUID));
        p.isStaff(isStaff);

        super.addItemToQueue(p);
    }

    public void queuePlayerDeath(String serverName, String playerName, String playerUUID, int level, String cause,
                                 String who, Coordinates coordinates, boolean isStaff) {

        final PlayerDeath p = new PlayerDeath();

        p.setDate(Instant.now());
        p.setServerName(serverName);
        p.setPlayerLevel(level);
        p.setWorld(coordinates.getWorldName());
        p.setX(coordinates.getX());
        p.setY(coordinates.getY());
        p.setZ(coordinates.getZ());
        p.setEntityPlayer(new EntityPlayer(playerName, playerUUID));
        p.isStaff(isStaff);
        p.setCause(cause);
        p.setByWho(who);

        super.addItemToQueue(p);
    }

    public void queuePlayerTeleport(String serverName, String playerName, String playerUUID, Coordinates oldCoords,
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
        p.setEntityPlayer(new EntityPlayer(playerName, playerUUID));
        p.isStaff(isStaff);

        super.addItemToQueue(p);
    }

    public void queuePlayerJoin(String serverName, String playerName, String playerUUID, Coordinates coords,
                                InetSocketAddress ip, boolean isStaff) {

        final PlayerConnection p = new PlayerConnection();

        p.setDate(Instant.now());
        p.setServerName(serverName);
        p.setX(coords.getX());
        p.setY(coords.getY());
        p.setZ(coords.getZ());
        p.setWorld(coords.getWorldName());
        //TODO fix player ip
        if (true) {
            p.setIp(4L);
        }
        p.setEntityPlayer(new EntityPlayer(playerName, playerUUID));
        p.isStaff(isStaff);
        p.setPlayerConnectionType(PlayerConnectionType.PLAYER_JOIN);

        super.addItemToQueue(p);
    }

    public void queuePlayerLeave(String serverName, String playerName, String playerUUID, Coordinates coords,
                                 boolean isStaff) {

        final PlayerConnection p = new PlayerConnection();

        p.setDate(Instant.now());
        p.setServerName(serverName);

        p.setWorld(coords.getWorldName());
        p.setX(coords.getX());
        p.setY(coords.getY());
        p.setZ(coords.getZ());
        p.setEntityPlayer(new EntityPlayer(playerName, playerUUID));
        p.isStaff(isStaff);
        p.setPlayerConnectionType(PlayerConnectionType.PLAYER_LEAVE);

        super.addItemToQueue(p);
    }

    public void queueBlockInteraction(String serverName, String playerName, String playerUUID, String block,
                                      Coordinates coords, boolean isStaff, InteractionType type) {

        final BlockInteraction p = new BlockInteraction();

        p.setDate(Instant.now());
        p.setServerName(serverName);
        p.setWorld(coords.getWorldName());
        p.setX(coords.getX());
        p.setY(coords.getY());
        p.setZ(coords.getZ());
        p.setBlock(block);
        p.setEntityPlayer(new EntityPlayer(playerName, playerUUID));
        p.isStaff(isStaff);
        p.setInteractionType(type);

        super.addItemToQueue(p);
    }

    public void queueTps(String serverName, double tpss) {

        final Tps p = new Tps();

        p.setDate(Instant.now());
        p.setServerName(serverName);
        p.setTps((int) tpss);

        super.addItemToQueue(p);
    }

    public void queueRam(String serverName, long tm, long um, long fm) {

        final Ram p = new Ram();

        p.setDate(Instant.now());
        p.setServerName(serverName);
        p.setTotalMemory((int) tm);
        p.setUsedMemory((int) um);
        p.setFreeMemory((int) fm);

        super.addItemToQueue(p);
    }

    public void queuePlayerKick(String serverName, String playerName, String playerUUID, Coordinates coords,
                                String reason, boolean isStaff) {

        final PlayerKick p = new PlayerKick();

        p.setDate(Instant.now());
        p.setServerName(serverName);
        p.setWorld(coords.getWorldName());
        p.setX(coords.getX());
        p.setY(coords.getY());
        p.setZ(coords.getZ());
        p.setReason(reason);
        p.setEntityPlayer(new EntityPlayer(playerName, playerUUID));
        p.isStaff(isStaff);

        super.addItemToQueue(p);
    }

    public void queuePortalCreate(String serverName, String worldName, String by) {

        final PortalCreation p = new PortalCreation();

        p.setDate(Instant.now());
        p.setServerName(serverName);
        p.setWorld(worldName);
        p.setCausedBy(by);

        super.addItemToQueue(p);
    }

    public void queueLevelChange(String serverName, String playerName, String playerUUID, boolean isStaff) {

        final PlayerLevel p = new PlayerLevel();

        p.setDate(Instant.now());
        p.setServerName(serverName);
        p.setEntityPlayer(new EntityPlayer(playerName, playerUUID));
        p.isStaff(isStaff);

        super.addItemToQueue(p);
    }

    public void queueBucketFill(String serverName, String playerName, String playerUUID, String bucket,
                                Coordinates coords, boolean isStaff) {

        final BucketAction b = new BucketAction();

        b.setDate(Instant.now());
        b.setServerName(serverName);
        b.setWorld(coords.getWorldName());
        b.setX(coords.getX());
        b.setY(coords.getY());
        b.setZ(coords.getZ());
        b.setBucket(bucket);
        b.setEntityPlayer(new EntityPlayer(playerName, playerUUID));
        b.isStaff(isStaff);
        b.setBucketActionType(BucketActionType.BUCKET_FILL);

        super.addItemToQueue(b);
    }

    public void queueBucketEmpty(String serverName, String playerName, String playerUUID, String bucket,
                                 Coordinates coords, boolean isStaff) {

        final BucketAction b = new BucketAction();

        b.setDate(Instant.now());
        b.setServerName(serverName);
        b.setWorld(coords.getWorldName());
        b.setX(coords.getX());
        b.setY(coords.getY());
        b.setZ(coords.getZ());
        b.setBucket(bucket);
        b.setEntityPlayer(new EntityPlayer(playerName, playerUUID));
        b.isStaff(isStaff);
        b.setBucketActionType(BucketActionType.BUCKET_EMPTY);

        super.addItemToQueue(b);
    }

    public void queueAnvil(String serverName, String playerName, String playerUUID, String newName, boolean isStaff) {

        final Anvil b = new Anvil();

        b.setDate(Instant.now());
        b.setServerName(serverName);
        b.setEntityPlayer(new EntityPlayer(playerName, playerUUID));
        b.isStaff(isStaff);
        b.setNewName(newName);

        super.addItemToQueue(b);
    }

    public void queueServerStart(String serverName) {

        final ServerStart s = new ServerStart();
        s.setDate(Instant.now());
        s.setServerName(serverName);

        super.addItemToQueue(s);
    }

    public void queueServerStop(String serverName) {

        final ServerStop s = new ServerStop();
        s.setDate(Instant.now());
        s.setServerName(serverName);

        super.addItemToQueue(s);
    }


    public void queueEnchant(String serverName, String playerName, String playerUUID, List<String> enchantment,
                             int enchantmentLevel, String item, int cost, Coordinates coordinates, boolean isStaff) {

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
        e.setEntityPlayer(new EntityPlayer(playerName, playerUUID));
        e.isStaff(isStaff);

        super.addItemToQueue(e);
    }

    public void queueBookEditing(String serverName, String playerName, String playerUUID, String worldName,
                                 int pages, List<String> content, String signedBy, boolean isStaff) {

        final BookEditing b = new BookEditing();
        b.setDate(Instant.now());
        b.setServerName(serverName);
        b.setWorld(worldName);
        b.setPageContent(content.toString());
        b.setPageCount(pages);
        b.setSignedBy(signedBy);
        b.setEntityPlayer(new EntityPlayer(playerName, playerUUID));
        b.isStaff(isStaff);

        super.addItemToQueue(b);
    }

    public void queueAfk(String serverName, String playerName, String playerUUID, Coordinates coords, boolean isStaff) {
        final Afk afk = new Afk();
        afk.setServerName(serverName);
        afk.setEntityPlayer(new EntityPlayer(playerName, playerUUID));
        afk.setX(coords.getX());
        afk.setY(coords.getY());
        afk.setZ(coords.getZ());
        afk.isStaff(isStaff);
        afk.setWorld(coords.getWorldName());
        afk.setDate(Instant.now());
        this.addItemToQueue(afk);
    }

    public void queueWrongPassword(String serverName, String playerName, String playerUUID, String worldName, boolean isStaff) {

    }

    public void queueItemAction(String serverName, String playerName, String playerUUID, String item, int amount,
                                Coordinates coords, String enchantment, String changedName, boolean isStaff,
                                ItemActionType type) {

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
        b.setEntityPlayer(new EntityPlayer(playerName, playerUUID));
        b.isStaff(isStaff);
        b.setItemActionType(type);
        b.setEnchantment(enchantment);

        super.addItemToQueue(b);
    }

    public void queueFurnace(String serverName, String playerName, String playerUUID, String item, int amount,
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
        b.setEntityPlayer(new EntityPlayer(playerName, playerUUID));
        b.isStaff(isStaff);

        super.addItemToQueue(b);
    }

    public void queueRCON(String serverName, String command) {

        final Rcon b = new Rcon();
        b.setDate(Instant.now());
        b.setServerName(serverName);

        super.addItemToQueue(b);
    }

    public void queueGameMode(String serverName, String playerName, String playerUUID, String gameMode, String worldName,
                              boolean isStaff) {

        final GameMode b = new GameMode();

        b.setDate(Instant.now());
        b.setServerName(serverName);
        b.setWorld(worldName);
        b.setGameMode(gameMode);
        b.setEntityPlayer(new EntityPlayer(playerName, playerUUID));
        b.isStaff(isStaff);

        super.addItemToQueue(b);
    }

    public void queuePlayerCraft(String serverName, String playerName, String playerUUID, String item, int amount,
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
        b.setEntityPlayer(new EntityPlayer(playerName, playerUUID));
        b.isStaff(isStaff);

        super.addItemToQueue(b);
    }

    public void queueVault(String serverName, String playerName, String playerUUID, double oldBal, double newBal,
                           boolean isStaff) {

    }

    public void queuePlayerRegistration(String serverName, String playerName, String playerUUID, String joinDate) {

    }

    public void queuePrimedTnt(String serverName, String playerName, String playerUUID, Coordinates coords,
                               boolean isStaff) {

        final PrimedTnt b = new PrimedTnt();

        b.setDate(Instant.now());
        b.setServerName(serverName);
        b.setX(coords.getX());
        b.setY(coords.getY());
        b.setZ(coords.getZ());
        b.setWorld(coords.getWorldName());
        b.setEntityPlayer(new EntityPlayer(playerName, playerUUID));
        b.isStaff(isStaff);

        super.addItemToQueue(b);
    }

    public void queueLiteBans(String serverName, String executor, String command, String onWho, String duration,
                              String reason, boolean isSilent) {

    }

    public void queueAdvanceBanData(String serverName, String type, String executor, String executedOn,
                                    String reason, long expirationDate) {
        final AdvancedBan advancedBan = new AdvancedBan();
        advancedBan.setServerName(advancedBan.getServerName());
        advancedBan.setType(type);
        advancedBan.setExecutor(executor);
        advancedBan.setExecutedOn(executedOn);
        advancedBan.setReason(reason);
        advancedBan.setExpirationDate(expirationDate);
        advancedBan.setDate(Instant.now());
        this.addItemToQueue(advancedBan);

    }

    public void queueCommandBlock(String serverName, String msg) {

        final CommandBlock b = new CommandBlock();
        b.setServerName(serverName);
        b.setCommand(msg);
        b.setDate(Instant.now());

        super.addItemToQueue(b);
    }

    public void queueWoodStripping(String serverName, String playerName, String playerUUID, String logName,
                                   Coordinates coords, boolean isStaff) {

    }

    public void queueChestInteraction(String serverName, String playerName, String playerUUID, Coordinates coords,
                                      String[] items, boolean isStaff) {

        final ChestInteraction b = new ChestInteraction();

        b.setDate(Instant.now());
        b.setServerName(serverName);
        b.setWorld(coords.getWorldName());
        b.setX(coords.getX());
        b.setY(coords.getY());
        b.setZ(coords.getZ());
        b.setEntityPlayer(new EntityPlayer(playerName, playerUUID));
        b.isStaff(isStaff);
        b.setItems(String.join(",", items));

        super.addItemToQueue(b);
    }

    public void queueEntityDeath(String serverName, String playerName, String playerUUID, String mob, Coordinates coords,
                                 boolean isStaff) {

        final EntityDeath b = new EntityDeath(serverName, coords, mob, new EntityPlayer(playerName, playerUUID), isStaff);

        b.setDate(Instant.now());
        super.addItemToQueue(b);
    }

    public void queueConsoleCommand(String serverName, String msg) {

        final ConsoleCommand p = new ConsoleCommand(serverName, msg);
        p.setDate(Instant.now());

        super.addItemToQueue(p);
    }

    public void queueServerReload(String serverName, String playerName, boolean isStaff) {

    }

    public void queuePlayerLogin(String serverName, String playerName, String toString, InetSocketAddress playerIP,
                                 boolean hasPermission) {

    }
    public void queueServerAddress(String serverName, String playerName, String playerUUID, String domainName){
        final ServerAddress s = new ServerAddress();
        s.setServerName(serverName);
        s.setPlayerName(playerName);
        s.setPlayerUUID(playerUUID);
        s.setDomaineName(domainName);
        s.setDate(Instant.now());
        this.addItemToQueue(s);
    }


    public void queueServerSwitch(String serverName, String playerUUID, String playerName, String from,
                                  String destination, boolean isStaff) {

    }

    public void queuePAFFriendMessage(String serverName, String playerUUID, String playerName, String message,
                                      String receiver, boolean isStaff) {

    }

    public void queuePAFPartyMessage(String serverName, String playerUUID, String playerName, String message,
                                     String leader, List<String> partyMembers, boolean isStaff) {

    }

    public void queueLeverInteraction(String serverName, String playerUUID, String worldName, String playerName, int x,
                                      int y, int z, boolean isStaff) {

    }

    public void queueSpawnEgg(String serverName, String playerUUID, String worldName, String playerName, int x, int y,
                              int z, String entity, boolean isStaff) {

    }

    public void queueWorldGuard(String serverName, String playerUUID, String worldName, String playerName,
                                String regionName, boolean isStaff) {

    }

    public void queuePlayerCount(String serverName, int playerCount) {
        final PlayerCount pc = new PlayerCount();
        pc.setServerName(serverName);
        pc.setDate(Instant.now());
        this.addItemToQueue(pc);
    }
}
