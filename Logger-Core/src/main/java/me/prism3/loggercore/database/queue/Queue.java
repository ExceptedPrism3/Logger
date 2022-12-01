package me.prism3.loggercore.database.queue;

import me.prism3.loggercore.database.DataSourceInterface;
import me.prism3.loggercore.database.entity.*;
import me.prism3.loggercore.database.utils.DateUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

class Queue {

    private final ConcurrentLinkedQueue<BlockInteraction> blockQueue = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<BucketAction> bucketQueue = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<GameMode> gameModeQueue = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<ItemAction> itemActionQueue = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<ConsoleCommand> consoleCommandQueue = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<PlayerConnection> playerConnectionQueue = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<PlayerChat> playerChatQueue = new ConcurrentLinkedQueue<>();

    private final ConcurrentLinkedQueue<Anvil> anvilQueue = new ConcurrentLinkedQueue<>();

    private final ConcurrentLinkedQueue<BookEditing> bookQueue = new ConcurrentLinkedQueue<>();

    private final ConcurrentLinkedQueue<Enchanting> enchantingQueue = new ConcurrentLinkedQueue<>();

    private final ConcurrentLinkedQueue<EntityDeath> entityDeathQueue = new ConcurrentLinkedQueue<>();

    private final ConcurrentLinkedQueue<PlayerDeath> playerDeathQueue = new ConcurrentLinkedQueue<>();

    private final ConcurrentLinkedQueue<PlayerKick> playerKickQueue = new ConcurrentLinkedQueue<>();

    private final ConcurrentLinkedQueue<PlayerLevel> playerLevelQueue = new ConcurrentLinkedQueue<>();

//    private final ConcurrentLinkedQueue<PlayerRegister> playerRegisterQueue = new ConcurrentLinkedQueue<>(); //TODO

    private final ConcurrentLinkedQueue<PlayerTeleport> playerTeleportQueue = new ConcurrentLinkedQueue<>();

    private final ConcurrentLinkedQueue<PlayerSignText> playerSignTextQueue = new ConcurrentLinkedQueue<>();

    private final ConcurrentLinkedQueue<PlayerCommand> playerCommandsQueue = new ConcurrentLinkedQueue<>();

    private final ConcurrentLinkedQueue<ChestInteraction> chestInteractionQueue = new ConcurrentLinkedQueue<>();

    private final ConcurrentLinkedQueue<Crafting> craftingQueue = new ConcurrentLinkedQueue<>();

    private final ConcurrentLinkedQueue<Furnace> furnaceQueue = new ConcurrentLinkedQueue<>();


    private final ConcurrentLinkedQueue<CommandBlock> commandBlockQueue = new ConcurrentLinkedQueue<>();


    private final ConcurrentLinkedQueue<Ram> ramQueue = new ConcurrentLinkedQueue<>();

    private final ConcurrentLinkedQueue<Rcon> rconQueue = new ConcurrentLinkedQueue<>();

    private final ConcurrentLinkedQueue<ServerAddress> serverAddresseQueue = new ConcurrentLinkedQueue<>();

    private final ConcurrentLinkedQueue<Tps> tpsQueue = new ConcurrentLinkedQueue<>();

    private final ConcurrentLinkedQueue<AdvancedBan> advancedBanQueue = new ConcurrentLinkedQueue<>();

    private final ConcurrentLinkedQueue<Afk> afkQueue = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<PlayerCount> playerCountQueue = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<PortalCreation> portalCreateQueue = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<PrimedTnt> primedTntQueue = new ConcurrentLinkedQueue<>();


    private final DataSourceInterface database;

    private final Timer timer = new Timer();
    private final TimerTask timerTask = new TimerTask() {
        @Override
        public void run() { flushQueue(); }
    };

    protected Queue(DataSourceInterface database) {

        this.database = database;

        try {

            this.enableRepeater();
            this.timer.scheduleAtFixedRate(this.timerTask, 500, 5000);

        } catch(Exception e) { e.printStackTrace(); }
    }

    protected static void insertBatchBlock(PreparedStatement stsm, ConcurrentLinkedQueue<BlockInteraction> queue) {

        try {

            final int size = queue.size();

            for (int i = 0; i < size; i++) {

                final BlockInteraction block = queue.poll();

                stsm.setString(1, block.getServerName());
                stsm.setString(2, block.getWorld());
                stsm.setString(3, block.getEntityPlayer().getPlayerName());
                stsm.setString(4, block.getBlock());
                stsm.setInt(5, block.getX());
                stsm.setInt(6, block.getY());
                stsm.setInt(7, block.getZ());
                stsm.setBoolean(8, block.isStaff());
                stsm.setString(9, block.getInteractionType().name());
                stsm.setString(10, DateUtils.formatInstant(block.getDate()));

                stsm.addBatch();
            }

            stsm.executeBatch();
            stsm.close();

        } catch (final Exception exception) { exception.printStackTrace(); }
    }

    //TODO add the rest of the insert Batches
    protected static void insertBucketBatch(PreparedStatement bucketStsm,
                                            ConcurrentLinkedQueue<BucketAction> bucketQueue) throws SQLException {

        final int size = bucketQueue.size();

        for (int i = 0; i < size; i++) {

            final BucketAction bucket = bucketQueue.poll();

            bucketStsm.setString(1, bucket.getServerName());
            bucketStsm.setString(2, bucket.getWorld());
            bucketStsm.setString(3, bucket.getEntityPlayer().getPlayerName());
            bucketStsm.setString(4, bucket.getBucket());
            bucketStsm.setInt(5, bucket.getX());
            bucketStsm.setInt(6, bucket.getY());
            bucketStsm.setInt(7, bucket.getZ());
            bucketStsm.setBoolean(8, bucket.isStaff());
            bucketStsm.setString(9, bucket.getBucketActionType().name());
            bucketStsm.setString(10, DateUtils.formatInstant(bucket.getDate()));

            bucketStsm.addBatch();
        }

        bucketStsm.executeBatch();
        bucketStsm.close();
    }

    protected static void insertItemActionBatch(PreparedStatement itemActionStsm,
                                                ConcurrentLinkedQueue<ItemAction> itemActionQueue) throws SQLException {
        final int size = itemActionQueue.size();

        for (int i = 0; i < size; i++) {

            final ItemAction itemAction = itemActionQueue.poll();

            itemActionStsm.setString(1, itemAction.getServerName());
            itemActionStsm.setString(2, itemAction.getWorld());
            itemActionStsm.setString(3, itemAction.getEntityPlayer().getPlayerName());
            itemActionStsm.setString(4, itemAction.getItem());
            itemActionStsm.setInt(5, itemAction.getX());
            itemActionStsm.setInt(6, itemAction.getY());
            itemActionStsm.setInt(7, itemAction.getZ());
            itemActionStsm.setString(8, itemAction.getChangedName());
            itemActionStsm.setBoolean(9, itemAction.isStaff());
            itemActionStsm.setString(10, itemAction.getItemActionType().name());
            itemActionStsm.setString(11, DateUtils.formatInstant(itemAction.getDate()));
            itemActionStsm.setInt(12, itemAction.getAmount());

            itemActionStsm.addBatch();
        }

        itemActionStsm.executeBatch();
        itemActionStsm.close();
    }

    protected static void insertConsoleCommandBatch(PreparedStatement consoleComamndStsm,
                                                    ConcurrentLinkedQueue<ConsoleCommand> consoleCommandQueue) throws SQLException {
        final int size = consoleCommandQueue.size();

        for (int i = 0; i < size; i++) {

            final ConsoleCommand consoleCommand = consoleCommandQueue.poll();

            consoleComamndStsm.setString(1, consoleCommand.getServerName());
            consoleComamndStsm.setString(2, consoleCommand.getCommand());
            consoleComamndStsm.setString(3, DateUtils.formatInstant(consoleCommand.getDate()));

            consoleComamndStsm.addBatch();
        }

        consoleComamndStsm.executeBatch();
        consoleComamndStsm.close();
    }

    protected static void insertGamemodeBatch(PreparedStatement stsm,
                                              ConcurrentLinkedQueue<GameMode> queue) throws SQLException {

        final int size = queue.size();

        for (int i = 0; i < size; i++) {

            final GameMode gamemode = queue.poll();

            stsm.setString(1, gamemode.getServerName());
            stsm.setString(2, gamemode.getWorld());
            stsm.setString(3, gamemode.getEntityPlayer().getPlayerName());
            stsm.setString(4, gamemode.getGameMode());
            stsm.setBoolean(5, gamemode.isStaff());
            stsm.setString(6, DateUtils.formatInstant(gamemode.getDate()));

            stsm.addBatch();
        }

        stsm.executeBatch();
        stsm.close();
    }

    protected static void insertPlayerConnectionBatch(PreparedStatement stsm,
                                                      ConcurrentLinkedQueue<PlayerConnection> queue) throws SQLException {

        final int size = queue.size();

        for (int i = 0; i < size; i++) {

            final PlayerConnection playerConnection = queue.poll();

            stsm.setString(1, playerConnection.getServerName());
            stsm.setString(2, playerConnection.getWorld());
            stsm.setString(3, playerConnection.getEntityPlayer().getPlayerName());
            stsm.setInt(4, playerConnection.getX());
            stsm.setInt(5, playerConnection.getY());
            stsm.setInt(6, playerConnection.getZ());
            if(playerConnection.getIp() != null)
                stsm.setLong(7, playerConnection.getIp());
            stsm.setBoolean(8, playerConnection.isStaff());
            stsm.setString(9, DateUtils.formatInstant(playerConnection.getDate()));
            stsm.setString(10, playerConnection.getPlayerConnectionType().name());

            stsm.addBatch();
        }

        stsm.executeBatch();
        stsm.close();
    }

    protected static void insertPlayerChatBatch(PreparedStatement stsm, ConcurrentLinkedQueue<PlayerChat> queue ) throws SQLException {

        final int size = queue.size();

        for (int i = 0; i < size; i++) {

            final PlayerChat playerChat = queue.poll();

            stsm.setString(1, playerChat.getServerName());
            stsm.setString(2, playerChat.getWorld());
            stsm.setString(3, playerChat.getEntityPlayer().getPlayerName());
            stsm.setString(4, playerChat.getMessage());
            stsm.setBoolean(5, playerChat.isStaff());
            stsm.setString(6, DateUtils.formatInstant(playerChat.getDate()));

            stsm.addBatch();
        }

        stsm.executeBatch();
        stsm.close();
    }

    protected static void insertAnvilBatch(PreparedStatement stsm, ConcurrentLinkedQueue<Anvil> queue ) throws SQLException {

        final int size = queue.size();

        for (int i = 0; i < size; i++) {

            final Anvil anvil = queue.poll();

            stsm.setString(1, anvil.getServerName());
            stsm.setString(2, anvil.getNewName());
            stsm.setString(3, anvil.getEntityPlayer().getPlayerName());
            stsm.setBoolean(4, anvil.isStaff());
            stsm.setString(5, DateUtils.formatInstant(anvil.getDate()));

            stsm.addBatch();
        }

        stsm.executeBatch();
        stsm.close();
    }

    protected static void insertBookEditingBatch(PreparedStatement stsm, ConcurrentLinkedQueue<BookEditing> queue ) throws SQLException {

        final int size = queue.size();

        for (int i = 0; i < size; i++) {

            final BookEditing bookEditing = queue.poll();

            stsm.setString(1, bookEditing.getServerName());
            stsm.setString(2, bookEditing.getWorld());
            stsm.setString(3, bookEditing.getEntityPlayer().getPlayerName());
            stsm.setInt(4, bookEditing.getPageCount());
            stsm.setString(5, bookEditing.getPageContent());
            stsm.setString(6, bookEditing.getSignedBy());
            stsm.setBoolean(7, bookEditing.isStaff());
            stsm.setString(8, DateUtils.formatInstant(bookEditing.getDate()));

            stsm.addBatch();
        }

        stsm.executeBatch();
        stsm.close();
    }

    protected static void insertEnchantingBatch(PreparedStatement stsm, ConcurrentLinkedQueue<Enchanting> queue ) throws SQLException {

        final int size = queue.size();

        for (int i = 0; i < size; i++) {

            final Enchanting enchanting = queue.poll();

            stsm.setString(1, enchanting.getServerName());
            stsm.setString(2, enchanting.getWorld());
            stsm.setString(3, enchanting.getEntityPlayer().getPlayerName());
            stsm.setInt(4, enchanting.getX());
            stsm.setInt(5, enchanting.getY());
            stsm.setInt(6, enchanting.getZ());
            stsm.setString(7, enchanting.getEnchantment());
            stsm.setInt(8, enchanting.getEnchantmentLevel());
            stsm.setString(9, enchanting.getItem());
            stsm.setInt(10, enchanting.getCost());
            stsm.setBoolean(11, enchanting.isStaff());
            stsm.setString(12, DateUtils.formatInstant(enchanting.getDate()));

            stsm.addBatch();
        }

        stsm.executeBatch();
        stsm.close();
    }

    protected static void insertEntityDeathBatch(PreparedStatement stsm, ConcurrentLinkedQueue<EntityDeath> queue ) throws SQLException {

        final int size = queue.size();

        for (int i = 0; i < size; i++) {

            final EntityDeath entityDeath = queue.poll();

            stsm.setString(1, entityDeath.getServerName());
            stsm.setString(2, entityDeath.getWorld());
            stsm.setString(3, entityDeath.getEntityPlayer().getPlayerUniqueID());
            stsm.setString(4, entityDeath.getEntityPlayer().getPlayerName());
            stsm.setString(5, entityDeath.getMob());
            stsm.setInt(6, entityDeath.getX());
            stsm.setInt(7, entityDeath.getY());
            stsm.setInt(8, entityDeath.getZ());
            stsm.setBoolean(9, entityDeath.isStaff());
            stsm.setString(10, DateUtils.formatInstant(entityDeath.getDate()));

            stsm.addBatch();
        }

        stsm.executeBatch();
        stsm.close();
    }

    protected static void insertPlayerDeathBatch(PreparedStatement stsm, ConcurrentLinkedQueue<PlayerDeath> queue ) throws SQLException {

        final int size = queue.size();

        for (int i = 0; i < size; i++) {

            final PlayerDeath playerDeath = queue.poll();

            stsm.setString(1, playerDeath.getServerName());
            stsm.setString(2, playerDeath.getWorld());
            stsm.setString(3, playerDeath.getEntityPlayer().getPlayerName());
            stsm.setInt(4, playerDeath.getPlayerLevel());
            stsm.setInt(5, playerDeath.getX());
            stsm.setInt(6, playerDeath.getY());
            stsm.setInt(7, playerDeath.getZ());
            stsm.setString(8, playerDeath.getCause());
            stsm.setString(9, playerDeath.getByWho());
            stsm.setBoolean(10, playerDeath.isStaff());
            stsm.setString(11, DateUtils.formatInstant(playerDeath.getDate()));

            stsm.addBatch();
        }

        stsm.executeBatch();
        stsm.close();
    }

    protected static void insertPlayerKickBatch(PreparedStatement stsm, ConcurrentLinkedQueue<PlayerKick> queue ) throws SQLException {

        final int size = queue.size();

        for (int i = 0; i < size; i++) {

            final PlayerKick playerKick = queue.poll();

            stsm.setString(1, playerKick.getServerName());
            stsm.setString(2, playerKick.getWorld());
            stsm.setString(3, playerKick.getEntityPlayer().getPlayerName());
            stsm.setInt(4, playerKick.getX());
            stsm.setInt(5, playerKick.getY());
            stsm.setInt(6, playerKick.getZ());
            stsm.setString(7, playerKick.getReason());
            stsm.setBoolean(8, playerKick.isStaff());
            stsm.setString(9, DateUtils.formatInstant(playerKick.getDate()));

            stsm.addBatch();
        }

        stsm.executeBatch();
        stsm.close();
    }

    protected static void insertPlayerLevelBatch(PreparedStatement stsm, ConcurrentLinkedQueue<PlayerLevel> queue ) throws SQLException {

        final int size = queue.size();

        for (int i = 0; i < size; i++) {

            final PlayerLevel playerLevel = queue.poll();

            stsm.setString(1, playerLevel.getServerName());
            stsm.setString(2, playerLevel.getEntityPlayer().getPlayerName());
            stsm.setBoolean(3, playerLevel.isStaff());
            stsm.setString(4, DateUtils.formatInstant(playerLevel.getDate()));

            stsm.addBatch();
        }

        stsm.executeBatch();
        stsm.close();
    }

    protected static void insertPlayerTeleportBatch(PreparedStatement stsm, ConcurrentLinkedQueue<PlayerTeleport> queue ) throws SQLException {

        final int size = queue.size();

        for (int i = 0; i < size; i++) {

            final PlayerTeleport playerTeleport = queue.poll();

            stsm.setString(1, playerTeleport.getServerName());
            stsm.setString(2, playerTeleport.getWorld());
            stsm.setString(3, playerTeleport.getEntityPlayer().getPlayerName());
            stsm.setInt(4, playerTeleport.getFromX());
            stsm.setInt(5, playerTeleport.getFromY());
            stsm.setInt(6, playerTeleport.getFromZ());
            stsm.setInt(7, playerTeleport.getToX());
            stsm.setInt(8, playerTeleport.getToY());
            stsm.setInt(9, playerTeleport.getToZ());
            stsm.setBoolean(10, playerTeleport.isStaff());
            stsm.setString(11, DateUtils.formatInstant(playerTeleport.getDate()));

            stsm.addBatch();
        }

        stsm.executeBatch();
        stsm.close();
    }

    protected static void insertPlayerSignTextBatch(PreparedStatement stsm, ConcurrentLinkedQueue<PlayerSignText> queue ) throws SQLException {

        final int size = queue.size();

        for (int i = 0; i < size; i++) {

            final PlayerSignText playerSignText = queue.poll();

            stsm.setString(1, playerSignText.getServerName());
            stsm.setString(2, playerSignText.getWorld());
            stsm.setInt(3, playerSignText.getX());
            stsm.setInt(4, playerSignText.getY());
            stsm.setInt(5, playerSignText.getZ());
            stsm.setString(6, playerSignText.getEntityPlayer().getPlayerName());
            stsm.setString(7, playerSignText.getLine());
            stsm.setBoolean(8, playerSignText.isStaff());
            stsm.setString(9, DateUtils.formatInstant(playerSignText.getDate()));

            stsm.addBatch();
        }

        stsm.executeBatch();
        stsm.close();
    }

    protected static void insertPlayerCommandBatch(PreparedStatement stsm, ConcurrentLinkedQueue<PlayerCommand> queue ) throws SQLException {

        final int size = queue.size();

        for (int i = 0; i < size; i++) {

            final PlayerCommand playerCommand = queue.poll();

            stsm.setString(1, playerCommand.getServerName());
            stsm.setString(2, playerCommand.getWorld());
            stsm.setString(3, playerCommand.getEntityPlayer().getPlayerName());
            stsm.setString(4, playerCommand.getCommand());
            stsm.setBoolean(5, playerCommand.isStaff());
            stsm.setString(6, DateUtils.formatInstant(playerCommand.getDate()));

            stsm.addBatch();
        }

        stsm.executeBatch();
        stsm.close();
    }

    protected static void insertChestInteractionBatch(PreparedStatement stsm, ConcurrentLinkedQueue<ChestInteraction> queue ) throws SQLException {

        final int size = queue.size();

        for (int i = 0; i < size; i++) {

            final ChestInteraction chestInteraction = queue.poll();

            stsm.setString(1, chestInteraction.getServerName());
            stsm.setString(2, chestInteraction.getWorld());
            stsm.setString(3, chestInteraction.getEntityPlayer().getPlayerUniqueID());
            stsm.setString(4, chestInteraction.getEntityPlayer().getPlayerName());
            stsm.setInt(5, chestInteraction.getX());
            stsm.setInt(6, chestInteraction.getY());
            stsm.setInt(7, chestInteraction.getZ());
            stsm.setString(8, chestInteraction.getItems());
            stsm.setBoolean(9, chestInteraction.isStaff());
            stsm.setString(10, DateUtils.formatInstant(chestInteraction.getDate()));

            stsm.addBatch();
        }

        stsm.executeBatch();
        stsm.close();
    }

    protected static void insertCraftingBatch(PreparedStatement stsm, ConcurrentLinkedQueue<Crafting> queue ) throws SQLException {

        final int size = queue.size();

        for (int i = 0; i < size; i++) {

            final Crafting crafting = queue.poll();

            stsm.setString(1, crafting.getServerName());
            stsm.setString(2, crafting.getWorld());
            stsm.setString(3, crafting.getEntityPlayer().getPlayerName());
            stsm.setString(4, crafting.getItem());
            stsm.setInt(5, crafting.getAmount());
            stsm.setInt(6, crafting.getX());
            stsm.setInt(7, crafting.getY());
            stsm.setInt(8, crafting.getZ());
            stsm.setBoolean(9, crafting.isStaff());
            stsm.setString(10, DateUtils.formatInstant(crafting.getDate()));

            stsm.addBatch();
        }

        stsm.executeBatch();
        stsm.close();
    }

    protected static void insertFurnaceBatch(PreparedStatement stsm, ConcurrentLinkedQueue<Furnace> queue ) throws SQLException {

        final int size = queue.size();

        for (int i = 0; i < size; i++) {

            final Furnace furnace = queue.poll();

            stsm.setString(1, furnace.getServerName());
            stsm.setString(2, furnace.getWorld());
            stsm.setString(3, furnace.getEntityPlayer().getPlayerName());
            stsm.setString(4, furnace.getItem());
            stsm.setInt(5, furnace.getAmount());
            stsm.setInt(6, furnace.getX());
            stsm.setInt(7, furnace.getY());
            stsm.setInt(8, furnace.getZ());
            stsm.setBoolean(9, furnace.isStaff());
            stsm.setString(10, DateUtils.formatInstant(furnace.getDate()));

            stsm.addBatch();
        }

        stsm.executeBatch();
        stsm.close();
    }

    protected static void insertCommandBlockBatch(PreparedStatement stsm, ConcurrentLinkedQueue<CommandBlock> queue ) throws SQLException {

        final int size = queue.size();

        for (int i = 0; i < size; i++) {

            final CommandBlock commandBlock = queue.poll();

            stsm.setString(1, commandBlock.getServerName());
            stsm.setString(2, commandBlock.getCommand());
            stsm.setString(3, DateUtils.formatInstant(commandBlock.getDate()));

            stsm.addBatch();
        }

        stsm.executeBatch();
        stsm.close();
    }

    protected static void insertRamBatch(PreparedStatement stsm, ConcurrentLinkedQueue<Ram> queue ) throws SQLException {

        final int size = queue.size();

        for (int i = 0; i < size; i++) {

            final Ram ram = queue.poll();

            stsm.setString(1, ram.getServerName());
            stsm.setInt(2, ram.getTotalMemory());
            stsm.setInt(3, ram.getUsedMemory());
            stsm.setInt(4, ram.getFreeMemory());
            stsm.setString(5, DateUtils.formatInstant(ram.getDate()));

            stsm.addBatch();
        }

        stsm.executeBatch();
        stsm.close();
    }

    protected static void insertRconBatch(PreparedStatement stsm, ConcurrentLinkedQueue<Rcon> queue ) throws SQLException {

        final int size = queue.size();

        for (int i = 0; i < size; i++) {

            final Rcon rcon = queue.poll();

            stsm.setString(1, rcon.getServerName());
            stsm.setString(2, rcon.getCommand());
            stsm.setString(3, DateUtils.formatInstant(rcon.getDate()));

            stsm.addBatch();
        }

        stsm.executeBatch();
        stsm.close();
    }

    protected static void insertTpsBatch(PreparedStatement stsm, ConcurrentLinkedQueue<Tps> queue) throws SQLException {

        final int size = queue.size();

        for (int i = 0; i < size; i++) {

            final Tps tps = queue.poll();

            stsm.setString(1, tps.getServerName());
            stsm.setInt(2, tps.getTps());
            stsm.setString(3, DateUtils.formatInstant(tps.getDate()));

            stsm.addBatch();
        }

        stsm.executeBatch();
        stsm.close();
    }

    protected static void insertAfkBatch(PreparedStatement stsm, ConcurrentLinkedQueue<Afk> queue) throws SQLException {

        final int size = queue.size();

        for (int i = 0; i < size; i++) {

            final Afk afk = queue.poll();

            stsm.setString(1, afk.getServerName());
            stsm.setString(2, afk.getWorld());
            stsm.setString(3, afk.getEntityPlayer().getPlayerName());
            stsm.setInt(4, afk.getX());
            stsm.setInt(5, afk.getY());
            stsm.setInt(6, afk.getZ());
            stsm.setBoolean(7, afk.isStaff());
            stsm.setString(8, DateUtils.formatInstant(afk.getDate()));

            stsm.addBatch();
        }

        stsm.executeBatch();
        stsm.close();
    }

    protected static void insertPlayerCountBatch(PreparedStatement stsm, ConcurrentLinkedQueue<PlayerCount> queue) throws SQLException {

        final int size = queue.size();

        for (int i = 0; i < size; i++) {

            final PlayerCount playerCount = queue.poll();

            stsm.setString(1, playerCount.getServerName());
            stsm.setString(2, DateUtils.formatInstant(playerCount.getDate()));

            stsm.addBatch();
        }

        stsm.executeBatch();
        stsm.close();
    }

    protected static void insertAdvancedBanBatch(PreparedStatement stsm, ConcurrentLinkedQueue<AdvancedBan> queue) throws SQLException {

        final int size = queue.size();

        for (int i = 0; i < size; i++) {

            final AdvancedBan advancedBan = queue.poll();

            stsm.setString(1, advancedBan.getServerName());
            stsm.setString(2, advancedBan.getType());
            stsm.setString(3, advancedBan.getExecutor());
            stsm.setString(4, advancedBan.getExecutedOn());
            stsm.setString(5, advancedBan.getReason());
            stsm.setLong(6, advancedBan.getExpirationDate());
            stsm.setString(7, DateUtils.formatInstant(advancedBan.getDate()));

            stsm.addBatch();
        }

        stsm.executeBatch();
        stsm.close();
    }

    protected static void insertServerAddressBatch(PreparedStatement stsm, ConcurrentLinkedQueue<ServerAddress> queue) throws SQLException {

        final int size = queue.size();

        for (int i = 0; i < size; i++) {

            final ServerAddress serverAddress = queue.poll();

            stsm.setString(1, serverAddress.getServerName());
            stsm.setString(2, serverAddress.getPlayerName());
            stsm.setString(3, serverAddress.getPlayerUUID());
            stsm.setString(4, serverAddress.getDomaineName());
            stsm.setString(5, DateUtils.formatInstant(serverAddress.getDate()));


            stsm.addBatch();
        }

        stsm.executeBatch();
        stsm.close();
    }
    protected static void insertPortalCreateBatch(PreparedStatement stsm, ConcurrentLinkedQueue<PortalCreation> queue) throws SQLException {

        final int size = queue.size();

        for (int i = 0; i < size; i++) {

            final PortalCreation portalCreation = queue.poll();

            stsm.setString(1, portalCreation.getServerName());
            stsm.setString(2, portalCreation.getWorld());
            stsm.setString(3, portalCreation.getCausedBy());
            stsm.setString(4, DateUtils.formatInstant(portalCreation.getDate()));


            stsm.addBatch();
        }

        stsm.executeBatch();
        stsm.close();
    }
    /**
     *         return connection.prepareStatement("INSERT INTO primed_tnt (server_name, world, player_uuid, player_name," +
                " x, y, z, is_staff, date) VALUES(?,?,?,?,?,?,?,?,?)");
     */
    protected static void insertPrimedTntBatch(PreparedStatement stsm, ConcurrentLinkedQueue<PrimedTnt> queue) throws SQLException {

        final int size = queue.size();

        for (int i = 0; i < size; i++) {

            final PrimedTnt primedTnt = queue.poll();

            stsm.setString(1, primedTnt.getServerName());
            stsm.setString(2, primedTnt.getWorld());
            stsm.setString(3, primedTnt.getEntityPlayer().getPlayerUniqueID());
            stsm.setString(4, primedTnt.getEntityPlayer().getPlayerName());
            stsm.setInt(5, primedTnt.getX());
            stsm.setInt(6, primedTnt.getY());
            stsm.setInt(7, primedTnt.getZ());
            stsm.setBoolean(8, primedTnt.isStaff());
            stsm.setString(9, DateUtils.formatInstant(primedTnt.getDate()));


            stsm.addBatch();
        }

        stsm.executeBatch();
        stsm.close();
    }

    protected void addItemToQueue(Object item) {

        if (item instanceof BlockInteraction) {
            blockQueue.add((BlockInteraction) item);
        } else if (item instanceof BucketAction) {
            bucketQueue.add((BucketAction) item);
        } else if (item instanceof GameMode) {
            gameModeQueue.add((GameMode) item);
        } else if (item instanceof ItemAction) {
            itemActionQueue.add((ItemAction) item);
        } else if (item instanceof ConsoleCommand) {
            consoleCommandQueue.add((ConsoleCommand) item);
        } else if (item instanceof PlayerConnection) {
            playerConnectionQueue.add((PlayerConnection) item);

        } else if (item instanceof PlayerChat) {
            playerChatQueue.add((PlayerChat) item );

        } else if (item instanceof Anvil) {
            anvilQueue.add((Anvil) item );

        } else if (item instanceof BookEditing) {
            bookQueue.add((BookEditing) item );

        } else if (item instanceof Enchanting) {
            enchantingQueue.add((Enchanting) item);

        } else if (item instanceof EntityDeath) {
            entityDeathQueue.add((EntityDeath) item);

        } else if (item instanceof PlayerDeath) {
            playerDeathQueue.add((PlayerDeath) item);

        } else if (item instanceof PlayerKick) {
            playerKickQueue.add((PlayerKick) item);

        } else if (item instanceof PlayerLevel) {
            playerLevelQueue.add((PlayerLevel) item);

        } else if (item instanceof PlayerTeleport) {
            playerTeleportQueue.add((PlayerTeleport) item);

        } else if (item instanceof PlayerSignText) {
            playerSignTextQueue.add((PlayerSignText) item);

        } else if (item instanceof PlayerCommand) {
            playerCommandsQueue.add((PlayerCommand) item);

        } else if (item instanceof ChestInteraction) {
            chestInteractionQueue.add((ChestInteraction) item);

        } else if (item instanceof Crafting) {
            craftingQueue.add((Crafting) item);

        } else if (item instanceof Furnace) {
            furnaceQueue.add((Furnace) item);

        } else if (item instanceof CommandBlock) {
            commandBlockQueue.add((CommandBlock) item);

        } else if (item instanceof Ram) {
            ramQueue.add((Ram) item);

        } else if (item instanceof Rcon) {
            rconQueue.add((Rcon) item);

        } else if (item instanceof Tps) {
            tpsQueue.add((Tps) item);
        } else if (item instanceof Afk) {
            afkQueue.add((Afk) item);
        } else if (item instanceof PlayerCount) {
            playerCountQueue.add((PlayerCount) item);
        } else if (item instanceof AdvancedBan) {
            advancedBanQueue.add((AdvancedBan) item);
        } else if (item instanceof ServerAddress) {
            serverAddresseQueue.add((ServerAddress) item);
        } else if(item instanceof PortalCreation) {
            portalCreateQueue.add((PortalCreation) item);
        } else if(item instanceof PrimedTnt) {
            primedTntQueue.add((PrimedTnt) item);
        }
         else {
            throw new RuntimeException("Unidentified Object type! " + item.getClass());
        }
    }

    protected void enableRepeater() { this.timer.scheduleAtFixedRate(this.timerTask, 0, 10000); }

    protected void disableRepeater() {

        this.timer.cancel();

        this.flushQueue();
    }

    public void flushQueue() {

        try (final Connection connection = this.database.getConnection()) {

            connection.setAutoCommit(false);

            Queue.insertBatchBlock(database.getBlockInteractionStsm(connection), this.blockQueue);
            Queue.insertBucketBatch(database.getBucketActionStsm(connection), this.bucketQueue);
            Queue.insertGamemodeBatch(database.getGamemodeStsm(connection), this.gameModeQueue);
            Queue.insertItemActionBatch(database.getItemActionStsm(connection), this.itemActionQueue);
            Queue.insertConsoleCommandBatch(database.getConsoleCommandStsm(connection), this.consoleCommandQueue);
            Queue.insertPlayerConnectionBatch(database.getPlayerConnectionStsm(connection), this.playerConnectionQueue);
            Queue.insertPlayerChatBatch(database.getPlayerChatStsm(connection), this.playerChatQueue);
            Queue.insertAnvilBatch(database.getAnvilStsm(connection), this.anvilQueue);
            Queue.insertBookEditingBatch(database.getBookEditingStsm(connection), this.bookQueue);
            Queue.insertEnchantingBatch(database.getEnchantStsm(connection), this.enchantingQueue);
            Queue.insertEntityDeathBatch(database.getEntityDeathStsm(connection), this.entityDeathQueue);
            Queue.insertPlayerDeathBatch(database.getPlayerDeathStsm(connection), this.playerDeathQueue);
            Queue.insertPlayerKickBatch(database.getPlayerKickStsm(connection), this.playerKickQueue);
            Queue.insertPlayerLevelBatch(database.getLevelChangeStsm(connection), this.playerLevelQueue);
            Queue.insertPlayerTeleportBatch(database.getPlayerTeleportStsm(connection), this.playerTeleportQueue);
            Queue.insertPlayerSignTextBatch(database.getPlayerSignTextStsm(connection), this.playerSignTextQueue);
            Queue.insertPlayerCommandBatch(database.getPlayerCommandStsm(connection), this.playerCommandsQueue);
            Queue.insertChestInteractionBatch(database.getChestInteractionStsm(connection), this.chestInteractionQueue);
            Queue.insertCraftingBatch(database.getPlayerCraftStsm(connection), this.craftingQueue);
            Queue.insertFurnaceBatch(database.getFurnaceStsm(connection), this.furnaceQueue);
            Queue.insertCommandBlockBatch(database.getCommandBlockStsm(connection), this.commandBlockQueue);
            Queue.insertRamBatch(database.getRAMStsm(connection), this.ramQueue);
//            Queue.insertRconBatch(database.getrcon(connection), this.rconQueue); //TODO FIX
            Queue.insertTpsBatch(database.getTpsStsm(connection), this.tpsQueue);
            Queue.insertAfkBatch(database.getAfkStsm(connection), this.afkQueue);
            Queue.insertPlayerCountBatch(database.getPlayerCountStsm(connection), this.playerCountQueue);
            Queue.insertAdvancedBanBatch(database.getAdvancedDataStsm(connection), this.advancedBanQueue);
            Queue.insertServerAddressBatch(database.getServerAddressStsm(connection), this.serverAddresseQueue);
            Queue.insertPortalCreateBatch(database.getPortalCreateStsm(connection), this.portalCreateQueue);
            Queue.insertPrimedTntBatch(database.getPrimedTntStsm(connection), this.primedTntQueue);

            connection.commit();

        } catch (final Exception exception) { exception.printStackTrace(); }
    }
}

