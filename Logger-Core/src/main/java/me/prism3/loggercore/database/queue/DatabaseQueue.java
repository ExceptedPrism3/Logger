package me.prism3.loggercore.database.queue;

import me.prism3.loggercore.database.AbstractDataSource;
import me.prism3.loggercore.database.data.Coordinates;
import me.prism3.loggercore.database.entity.*;
import me.prism3.loggercore.database.entity.enums.*;
import me.prism3.loggercore.database.utils.DataBaseUtils;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class DatabaseQueue {

    private final AbstractDataSource database;
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
    private final ConcurrentLinkedQueue<LiteBans> liteBansQueue = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<StandCrystal> standCrystalQueue = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<ArmorStandAction> armorStandQueue = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<LeverInteraction> leverInteractionQueue = new ConcurrentLinkedQueue<>();

    private final Timer timer = new Timer();
    private final TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            flushQueue();
        }
    };

    public DatabaseQueue(@NotNull AbstractDataSource database) {

        this.database = database;

        try {

            this.enableRepeater();

        } catch (final Exception e) { e.printStackTrace(); }
    }

    private static void insertBatchBlock(PreparedStatement stsm, ConcurrentLinkedQueue<BlockInteraction> queue) {

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
                stsm.setString(10, DataBaseUtils.formatInstant(block.getDate()));

                stsm.addBatch();
            }

            stsm.executeBatch();
            stsm.close();

        } catch (final Exception exception) { exception.printStackTrace(); }
    }

    private static void insertBucketBatch(PreparedStatement bucketStsm,
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
            bucketStsm.setString(10, DataBaseUtils.formatInstant(bucket.getDate()));

            bucketStsm.addBatch();
        }

        bucketStsm.executeBatch();
        bucketStsm.close();
    }

    private static void insertItemActionBatch(PreparedStatement itemActionStsm,
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
            itemActionStsm.setString(11, DataBaseUtils.formatInstant(itemAction.getDate()));
            itemActionStsm.setInt(12, itemAction.getAmount());

            itemActionStsm.addBatch();
        }

        itemActionStsm.executeBatch();
        itemActionStsm.close();
    }

    private static void insertConsoleCommandBatch(PreparedStatement consoleComamndStsm,
                                                  ConcurrentLinkedQueue<ConsoleCommand> consoleCommandQueue) throws SQLException {
        final int size = consoleCommandQueue.size();

        for (int i = 0; i < size; i++) {

            final ConsoleCommand consoleCommand = consoleCommandQueue.poll();

            consoleComamndStsm.setString(1, consoleCommand.getServerName());
            consoleComamndStsm.setString(2, consoleCommand.getCommand());
            consoleComamndStsm.setString(3, DataBaseUtils.formatInstant(consoleCommand.getDate()));

            consoleComamndStsm.addBatch();
        }

        consoleComamndStsm.executeBatch();
        consoleComamndStsm.close();
    }

    private static void insertGameModeBatch(PreparedStatement stsm,
                                            ConcurrentLinkedQueue<GameMode> queue) throws SQLException {

        final int size = queue.size();

        for (int i = 0; i < size; i++) {

            final GameMode gamemode = queue.poll();

            stsm.setString(1, gamemode.getServerName());
            stsm.setString(2, gamemode.getWorld());
            stsm.setString(3, gamemode.getEntityPlayer().getPlayerName());
            stsm.setString(4, gamemode.getGameMode());
            stsm.setBoolean(5, gamemode.isStaff());
            stsm.setString(6, DataBaseUtils.formatInstant(gamemode.getDate()));

            stsm.addBatch();
        }

        stsm.executeBatch();
        stsm.close();
    }

    private static void insertPlayerConnectionBatch(PreparedStatement stsm,
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
            if (playerConnection.getIp() != null)
                stsm.setLong(7, playerConnection.getIp());
            stsm.setBoolean(8, playerConnection.isStaff());
            stsm.setString(9, DataBaseUtils.formatInstant(playerConnection.getDate()));
            stsm.setString(10, playerConnection.getPlayerConnectionType().name());

            stsm.addBatch();
        }

        stsm.executeBatch();
        stsm.close();
    }

    private static void insertPlayerChatBatch(PreparedStatement stsm,
                                              ConcurrentLinkedQueue<PlayerChat> queue) throws SQLException {

        final int size = queue.size();

        for (int i = 0; i < size; i++) {

            final PlayerChat playerChat = queue.poll();

            stsm.setString(1, playerChat.getServerName());
            stsm.setString(2, playerChat.getWorld());
            stsm.setString(3, playerChat.getEntityPlayer().getPlayerName());
            stsm.setString(4, playerChat.getMessage());
            stsm.setBoolean(5, playerChat.isStaff());
            stsm.setString(6, DataBaseUtils.formatInstant(playerChat.getDate()));

            stsm.addBatch();
        }

        stsm.executeBatch();
        stsm.close();
    }

    private static void insertAnvilBatch(PreparedStatement stsm, ConcurrentLinkedQueue<Anvil> queue) throws SQLException {

        final int size = queue.size();

        for (int i = 0; i < size; i++) {

            final Anvil anvil = queue.poll();

            stsm.setString(1, anvil.getServerName());
            stsm.setString(2, anvil.getNewName());
            stsm.setString(3, anvil.getEntityPlayer().getPlayerName());
            stsm.setBoolean(4, anvil.isStaff());
            stsm.setString(5, DataBaseUtils.formatInstant(anvil.getDate()));

            stsm.addBatch();
        }

        stsm.executeBatch();
        stsm.close();
    }

    private static void insertBookEditingBatch(PreparedStatement stsm,
                                               ConcurrentLinkedQueue<BookEditing> queue) throws SQLException {

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
            stsm.setString(8, DataBaseUtils.formatInstant(bookEditing.getDate()));

            stsm.addBatch();
        }

        stsm.executeBatch();
        stsm.close();
    }

    private static void insertEnchantingBatch(PreparedStatement stsm,
                                              ConcurrentLinkedQueue<Enchanting> queue) throws SQLException {

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
            stsm.setString(12, DataBaseUtils.formatInstant(enchanting.getDate()));

            stsm.addBatch();
        }

        stsm.executeBatch();
        stsm.close();
    }

    private static void insertEntityDeathBatch(PreparedStatement stsm,
                                               ConcurrentLinkedQueue<EntityDeath> queue) throws SQLException {

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
            stsm.setString(10, DataBaseUtils.formatInstant(entityDeath.getDate()));

            stsm.addBatch();
        }

        stsm.executeBatch();
        stsm.close();
    }

    private static void insertPlayerDeathBatch(PreparedStatement stsm,
                                               ConcurrentLinkedQueue<PlayerDeath> queue) throws SQLException {

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
            stsm.setString(11, DataBaseUtils.formatInstant(playerDeath.getDate()));

            stsm.addBatch();
        }

        stsm.executeBatch();
        stsm.close();
    }

    private static void insertPlayerKickBatch(PreparedStatement stsm,
                                              ConcurrentLinkedQueue<PlayerKick> queue) throws SQLException {

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
            stsm.setString(9, DataBaseUtils.formatInstant(playerKick.getDate()));

            stsm.addBatch();
        }

        stsm.executeBatch();
        stsm.close();
    }

    private static void insertPlayerLevelBatch(PreparedStatement stsm,
                                               ConcurrentLinkedQueue<PlayerLevel> queue) throws SQLException {

        final int size = queue.size();

        for (int i = 0; i < size; i++) {

            final PlayerLevel playerLevel = queue.poll();

            stsm.setString(1, playerLevel.getServerName());
            stsm.setString(2, playerLevel.getEntityPlayer().getPlayerName());
            stsm.setBoolean(3, playerLevel.isStaff());
            stsm.setString(4, DataBaseUtils.formatInstant(playerLevel.getDate()));

            stsm.addBatch();
        }

        stsm.executeBatch();
        stsm.close();
    }

    private static void insertPlayerTeleportBatch(PreparedStatement stsm,
                                                  ConcurrentLinkedQueue<PlayerTeleport> queue) throws SQLException {

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
            stsm.setString(11, DataBaseUtils.formatInstant(playerTeleport.getDate()));

            stsm.addBatch();
        }

        stsm.executeBatch();
        stsm.close();
    }

    private static void insertPlayerSignTextBatch(PreparedStatement stsm,
                                                  ConcurrentLinkedQueue<PlayerSignText> queue) throws SQLException {

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
            stsm.setString(9, DataBaseUtils.formatInstant(playerSignText.getDate()));

            stsm.addBatch();
        }

        stsm.executeBatch();
        stsm.close();
    }

    private static void insertPlayerCommandBatch(PreparedStatement stsm,
                                                 ConcurrentLinkedQueue<PlayerCommand> queue) throws SQLException {

        final int size = queue.size();

        for (int i = 0; i < size; i++) {

            final PlayerCommand playerCommand = queue.poll();

            stsm.setString(1, playerCommand.getServerName());
            stsm.setString(2, playerCommand.getWorld());
            stsm.setString(3, playerCommand.getEntityPlayer().getPlayerName());
            stsm.setString(4, playerCommand.getCommand());
            stsm.setBoolean(5, playerCommand.isStaff());
            stsm.setString(6, DataBaseUtils.formatInstant(playerCommand.getDate()));

            stsm.addBatch();
        }

        stsm.executeBatch();
        stsm.close();
    }

    private static void insertChestInteractionBatch(PreparedStatement stsm,
                                                    ConcurrentLinkedQueue<ChestInteraction> queue) throws SQLException {

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
            stsm.setString(10, DataBaseUtils.formatInstant(chestInteraction.getDate()));

            stsm.addBatch();
        }

        stsm.executeBatch();
        stsm.close();
    }

    private static void insertCraftingBatch(PreparedStatement stsm, ConcurrentLinkedQueue<Crafting> queue) throws SQLException {

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
            stsm.setString(10, DataBaseUtils.formatInstant(crafting.getDate()));

            stsm.addBatch();
        }

        stsm.executeBatch();
        stsm.close();
    }

    private static void insertFurnaceBatch(PreparedStatement stsm, ConcurrentLinkedQueue<Furnace> queue) throws SQLException {

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
            stsm.setString(10, DataBaseUtils.formatInstant(furnace.getDate()));

            stsm.addBatch();
        }

        stsm.executeBatch();
        stsm.close();
    }

    private static void insertCommandBlockBatch(PreparedStatement stsm,
                                                ConcurrentLinkedQueue<CommandBlock> queue) throws SQLException {

        final int size = queue.size();

        for (int i = 0; i < size; i++) {

            final CommandBlock commandBlock = queue.poll();

            stsm.setString(1, commandBlock.getServerName());
            stsm.setString(2, commandBlock.getCommand());
            stsm.setString(3, DataBaseUtils.formatInstant(commandBlock.getDate()));

            stsm.addBatch();
        }

        stsm.executeBatch();
        stsm.close();
    }

    private static void insertRamBatch(PreparedStatement stsm, ConcurrentLinkedQueue<Ram> queue) throws SQLException {

        final int size = queue.size();

        for (int i = 0; i < size; i++) {

            final Ram ram = queue.poll();

            stsm.setString(1, ram.getServerName());
            stsm.setInt(2, ram.getTotalMemory());
            stsm.setInt(3, ram.getUsedMemory());
            stsm.setInt(4, ram.getFreeMemory());
            stsm.setString(5, DataBaseUtils.formatInstant(ram.getDate()));

            stsm.addBatch();
        }

        stsm.executeBatch();
        stsm.close();
    }

    private static void insertRconBatch(PreparedStatement stsm, ConcurrentLinkedQueue<Rcon> queue) throws SQLException {

        final int size = queue.size();

        for (int i = 0; i < size; i++) {

            final Rcon rcon = queue.poll();

            stsm.setString(1, rcon.getServerName());
            stsm.setString(2, rcon.getCommand());
            stsm.setString(3, DataBaseUtils.formatInstant(rcon.getDate()));

            stsm.addBatch();
        }

        stsm.executeBatch();
        stsm.close();
    }

    private static void insertTpsBatch(PreparedStatement stsm, ConcurrentLinkedQueue<Tps> queue) throws SQLException {

        final int size = queue.size();

        for (int i = 0; i < size; i++) {

            final Tps tps = queue.poll();

            stsm.setString(1, tps.getServerName());
            stsm.setInt(2, tps.getTps());
            stsm.setString(3, DataBaseUtils.formatInstant(tps.getDate()));

            stsm.addBatch();
        }

        stsm.executeBatch();
        stsm.close();
    }

    private static void insertAfkBatch(PreparedStatement stsm, ConcurrentLinkedQueue<Afk> queue) throws SQLException {

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
            stsm.setString(8, DataBaseUtils.formatInstant(afk.getDate()));

            stsm.addBatch();
        }

        stsm.executeBatch();
        stsm.close();
    }

    private static void insertPlayerCountBatch(PreparedStatement stsm,
                                               ConcurrentLinkedQueue<PlayerCount> queue) throws SQLException {

        final int size = queue.size();

        for (int i = 0; i < size; i++) {

            final PlayerCount playerCount = queue.poll();

            stsm.setString(1, playerCount.getServerName());
            stsm.setString(2, DataBaseUtils.formatInstant(playerCount.getDate()));

            stsm.addBatch();
        }

        stsm.executeBatch();
        stsm.close();
    }

    private static void insertAdvancedBanBatch(PreparedStatement stsm,
                                               ConcurrentLinkedQueue<AdvancedBan> queue) throws SQLException {

        final int size = queue.size();

        for (int i = 0; i < size; i++) {

            final AdvancedBan advancedBan = queue.poll();

            stsm.setString(1, advancedBan.getServerName());
            stsm.setString(2, advancedBan.getType());
            stsm.setString(3, advancedBan.getExecutor());
            stsm.setString(4, advancedBan.getExecutedOn());
            stsm.setString(5, advancedBan.getReason());
            stsm.setLong(6, advancedBan.getExpirationDate());
            stsm.setString(7, DataBaseUtils.formatInstant(advancedBan.getDate()));

            stsm.addBatch();
        }

        stsm.executeBatch();
        stsm.close();
    }

    private static void insertLiteBansBatch(PreparedStatement stsm, ConcurrentLinkedQueue<LiteBans> queue) throws SQLException {

        final int size = queue.size();

        for (int i = 0; i < size; i++) {

            final LiteBans litebans = queue.poll();

            stsm.setString(1, litebans.getServerName());
            stsm.setString(2, litebans.getSender());
            stsm.setString(3, litebans.getCommand());
            stsm.setString(4, litebans.getOnWho());
            stsm.setString(5, litebans.getReason());
            stsm.setString(6, litebans.getDuration());
            stsm.setBoolean(7, litebans.getSilent());
            stsm.setString(8, DataBaseUtils.formatInstant(litebans.getDate()));

            stsm.addBatch();
        }

        stsm.executeBatch();
        stsm.close();
    }

//    private final ConcurrentLinkedQueue<PlayerRegister> playerRegisterQueue = new ConcurrentLinkedQueue<>(); //TODO

    private static void insertServerAddressBatch(PreparedStatement stsm,
                                                 ConcurrentLinkedQueue<ServerAddress> queue) throws SQLException {

        final int size = queue.size();

        for (int i = 0; i < size; i++) {

            final ServerAddress serverAddress = queue.poll();

            stsm.setString(1, serverAddress.getServerName());
            stsm.setString(2, serverAddress.getPlayerName());
            stsm.setString(3, serverAddress.getPlayerUUID());
            stsm.setString(4, serverAddress.getDomaineName());
            stsm.setString(5, DataBaseUtils.formatInstant(serverAddress.getDate()));

            stsm.addBatch();
        }

        stsm.executeBatch();
        stsm.close();
    }

    private static void insertPortalCreateBatch(PreparedStatement stsm,
                                                ConcurrentLinkedQueue<PortalCreation> queue) throws SQLException {

        final int size = queue.size();

        for (int i = 0; i < size; i++) {

            final PortalCreation portalCreation = queue.poll();

            stsm.setString(1, portalCreation.getServerName());
            stsm.setString(2, portalCreation.getWorld());
            stsm.setString(3, portalCreation.getCausedBy());
            stsm.setString(4, DataBaseUtils.formatInstant(portalCreation.getDate()));

            stsm.addBatch();
        }

        stsm.executeBatch();
        stsm.close();
    }

    private static void insertPrimedTntBatch(PreparedStatement stsm, ConcurrentLinkedQueue<PrimedTnt> queue) throws SQLException {

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
            stsm.setString(9, DataBaseUtils.formatInstant(primedTnt.getDate()));

            stsm.addBatch();
        }

        stsm.executeBatch();
        stsm.close();
    }

    private static void insertStandCrystalBatch(PreparedStatement stsm, ConcurrentLinkedQueue<StandCrystal> queue) throws SQLException {

        final int size = queue.size();

        for (int i = 0; i < size; i++) {

            final StandCrystal standCrystal = queue.poll();

            stsm.setString(1, standCrystal.getServerName());
            stsm.setString(2, standCrystal.getWorld());
            stsm.setString(3, standCrystal.getEntityPlayer().getPlayerName());
            stsm.setString(4, standCrystal.getEntityPlayer().getPlayerUniqueID());
            stsm.setInt(5, standCrystal.getX());
            stsm.setInt(6, standCrystal.getY());
            stsm.setInt(7, standCrystal.getZ());
            stsm.setBoolean(8, standCrystal.isStaff());
            stsm.setString(9, standCrystal.getArmorStandActionType().name());
            stsm.setString( 10, DataBaseUtils.formatInstant(standCrystal.getDate()));
            stsm.setString(11, standCrystal.getBlock());
            stsm.addBatch();
        }

        stsm.executeBatch();
        stsm.close();
    }

    private static void insertArmorStandBatch(PreparedStatement stsm, ConcurrentLinkedQueue<ArmorStandAction> queue) throws SQLException {
        final int size = queue.size();

        for (int i = 0; i < size; i++) {

            final ArmorStandAction armorStandAction = queue.poll();

            stsm.setString(1, armorStandAction.getServerName());
            stsm.setString(2, armorStandAction.getWorld());
            stsm.setString(3, armorStandAction.getEntityPlayer().getPlayerName());
            stsm.setString(4, armorStandAction.getEntityPlayer().getPlayerUniqueID());
            stsm.setInt(5, armorStandAction.getX());
            stsm.setInt(6, armorStandAction.getY());
            stsm.setInt(7, armorStandAction.getZ());
            stsm.setBoolean(8, armorStandAction.isStaff());
            stsm.setString( 9, DataBaseUtils.formatInstant(armorStandAction.getDate()));
            stsm.addBatch();
        }

        stsm.executeBatch();
        stsm.close();
    }

    private static void insertLeverInteractionBatch(PreparedStatement stsm,
                                                    ConcurrentLinkedQueue<LeverInteraction> queue) throws SQLException {

        final int size = queue.size();

        for (int i = 0; i < size; i++) {

            final LeverInteraction leverInteraction = queue.poll();

            stsm.setString( 1, DataBaseUtils.formatInstant(leverInteraction.getDate()));
            stsm.setString(2, leverInteraction.getServerName());
            stsm.setString(3, leverInteraction.getWorld());
            stsm.setString(4, leverInteraction.getEntityPlayer().getPlayerUniqueID());
            stsm.setString(5, leverInteraction.getEntityPlayer().getPlayerName());
            stsm.setInt(6, leverInteraction.getX());
            stsm.setInt(7, leverInteraction.getY());
            stsm.setInt(8, leverInteraction.getZ());
            stsm.setBoolean(9, leverInteraction.isStaff());
            stsm.addBatch();
        }

        stsm.executeBatch();
        stsm.close();
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

        this.addItemToQueue(p);
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

        this.addItemToQueue(p);
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

        this.addItemToQueue(p);
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

        this.addItemToQueue(p);
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

        this.addItemToQueue(p);
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

        this.addItemToQueue(p);
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

        this.addItemToQueue(p);
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

        this.addItemToQueue(p);
    }

    public void queueTps(String serverName, double tpss) {

        final Tps p = new Tps();

        p.setDate(Instant.now());
        p.setServerName(serverName);
        p.setTps((int) tpss);

        this.addItemToQueue(p);
    }

    public void queueRam(String serverName, long tm, long um, long fm) {

        final Ram p = new Ram();

        p.setDate(Instant.now());
        p.setServerName(serverName);
        p.setTotalMemory((int) tm);
        p.setUsedMemory((int) um);
        p.setFreeMemory((int) fm);

        this.addItemToQueue(p);
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

        this.addItemToQueue(p);
    }

    public void queuePortalCreate(String serverName, String worldName, String by) {

        final PortalCreation p = new PortalCreation();

        p.setDate(Instant.now());
        p.setServerName(serverName);
        p.setWorld(worldName);
        p.setCausedBy(by);

        this.addItemToQueue(p);
    }

    public void queueLevelChange(String serverName, String playerName, String playerUUID, boolean isStaff) {

        final PlayerLevel p = new PlayerLevel();

        p.setDate(Instant.now());
        p.setServerName(serverName);
        p.setEntityPlayer(new EntityPlayer(playerName, playerUUID));
        p.isStaff(isStaff);

        this.addItemToQueue(p);
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

        this.addItemToQueue(b);
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

        this.addItemToQueue(b);
    }

    public void queueAnvil(String serverName, String playerName, String playerUUID, String newName, boolean isStaff) {

        final Anvil b = new Anvil();

        b.setDate(Instant.now());
        b.setServerName(serverName);
        b.setEntityPlayer(new EntityPlayer(playerName, playerUUID));
        b.isStaff(isStaff);
        b.setNewName(newName);

        this.addItemToQueue(b);
    }

    public void queueServerStart(String serverName) {

        final ServerStart s = new ServerStart();

        s.setDate(Instant.now());
        s.setServerName(serverName);

        this.addItemToQueue(s);
    }

    public void queueStandCrystal(String serverName, String playerName, String playerUUID, String worldName, int x,
                                  int y, int z, boolean isStaff, ArmorStandActionType armorStandActionType, String block) {

        final StandCrystal standCrystal = new StandCrystal();

        standCrystal.setServerName(serverName);
        standCrystal.setEntityPlayer(new EntityPlayer(playerName, playerUUID));
        standCrystal.setWorld(worldName);
        standCrystal.setX(x);
        standCrystal.setY(y);
        standCrystal.setZ(z);
        standCrystal.isStaff(isStaff);
        standCrystal.setBlock(block);
        standCrystal.setDate(Instant.now());
        standCrystal.setArmorStandActionType(armorStandActionType);

        this.addItemToQueue(standCrystal);
    }

    public void queueArmorStandInteraction(String serverName, String playerName, String playerUUID, String worldName, int x,
                                int y, int z, boolean isStaff) {

        final ArmorStandAction armorStand = new ArmorStandAction();

        armorStand.setDate(Instant.now());
        armorStand.setServerName(serverName);
        armorStand.setEntityPlayer(new EntityPlayer(playerName, playerUUID));
        armorStand.setX(x);
        armorStand.setY(y);
        armorStand.setZ(z);
        armorStand.setWorld(worldName);
        armorStand.isStaff(isStaff);

        this.addItemToQueue(armorStand);
    }

    public void queueServerStop(String serverName) {

        final ServerStop s = new ServerStop();

        s.setDate(Instant.now());
        s.setServerName(serverName);

        this.addItemToQueue(s);
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

        this.addItemToQueue(e);
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

        this.addItemToQueue(b);
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

        this.addItemToQueue(b);
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

        this.addItemToQueue(b);
    }

    public void queueRCON(String serverName, String command) {

        final Rcon b = new Rcon();

        b.setDate(Instant.now());
        b.setServerName(serverName);

        this.addItemToQueue(b);
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

        this.addItemToQueue(b);
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

        this.addItemToQueue(b);
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

        this.addItemToQueue(b);
    }

    public void queueLiteBans(String serverName, String executor, String command, String onWho, String duration,
                              String reason, boolean isSilent) {

        final LiteBans litebans = new LiteBans();

        litebans.setServerName(serverName);
        litebans.setCommand(command);
        litebans.setSender(executor);
        litebans.setOnWho(onWho);
        litebans.setReason(reason);
        litebans.setDuration(duration);
        litebans.setSilent(isSilent);
        litebans.setDate(Instant.now());

        this.addItemToQueue(litebans);
    }

    public void queueAdvanceBanData(String serverName, String type, String executor, String executedOn,
                                    String reason, long expirationDate) {

        final AdvancedBan advancedBan = new AdvancedBan();

        advancedBan.setServerName(serverName);
        advancedBan.setType(type);
        advancedBan.setExecutor(executor);
        advancedBan.setExecutedOn(executedOn);
        advancedBan.setReason(reason);
        advancedBan.setExpirationDate(expirationDate);
        advancedBan.setDate(Instant.now());

        this.addItemToQueue(advancedBan);
    }

    public void queueLeverInteraction(String serverName, String playerName, String playerUUID, Coordinates coords,
                                      boolean isStaff) {

        final LeverInteraction leverInteraction = new LeverInteraction();

        leverInteraction.setDate(Instant.now());
        leverInteraction.setServerName(serverName);
        leverInteraction.setWorld(coords.getWorldName());
        leverInteraction.setX(coords.getX());
        leverInteraction.setY(coords.getY());
        leverInteraction.setZ(coords.getZ());
        leverInteraction.setEntityPlayer(new EntityPlayer(playerName, playerUUID));
        leverInteraction.isStaff(isStaff);

        this.addItemToQueue(leverInteraction);
    }

    public void queueCommandBlock(String serverName, String msg) {

        final CommandBlock b = new CommandBlock();

        b.setServerName(serverName);
        b.setCommand(msg);
        b.setDate(Instant.now());

        this.addItemToQueue(b);
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

        this.addItemToQueue(b);
    }

    public void queueEntityDeath(String serverName, String playerName, String playerUUID, String mob, Coordinates coords,
                                 boolean isStaff) {

        final EntityDeath b = new EntityDeath(serverName, coords, mob, new EntityPlayer(playerName, playerUUID), isStaff);

        b.setDate(Instant.now());

        this.addItemToQueue(b);
    }

    public void queueConsoleCommand(String serverName, String msg) {

        final ConsoleCommand p = new ConsoleCommand(serverName, msg);
        p.setDate(Instant.now());

        this.addItemToQueue(p);
    }

    public void queueServerReload(String serverName, String playerName, boolean isStaff) {

    }

    public void queuePlayerLogin(String serverName, String playerName, String toString, InetSocketAddress playerIP,
                                 boolean hasPermission) {

    }

    public void queueServerAddress(String serverName, String playerName, String playerUUID, String domainName) {

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


    private void addItemToQueue(Object item) {

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

            playerChatQueue.add((PlayerChat) item);

        } else if (item instanceof Anvil) {

            anvilQueue.add((Anvil) item);

        } else if (item instanceof BookEditing) {

            bookQueue.add((BookEditing) item);

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

        } else if (item instanceof PortalCreation) {

            portalCreateQueue.add((PortalCreation) item);

        } else if (item instanceof PrimedTnt) {

            primedTntQueue.add((PrimedTnt) item);

        } else if (item instanceof LiteBans) {

            liteBansQueue.add((LiteBans) item);

        } else if (item instanceof StandCrystal) {

            standCrystalQueue.add((StandCrystal) item);

        } else if (item instanceof ArmorStandAction) {

            armorStandQueue.add((ArmorStandAction) item);

        } else if (item instanceof LeverInteraction) {

            leverInteractionQueue.add((LeverInteraction) item);

        } else {
            throw new RuntimeException("Unidentified Object type! " + item.getClass());
        }
    }

    private void enableRepeater() {
        this.timer.scheduleAtFixedRate(this.timerTask, 10000, 10000);
    }

    private void disableRepeater() {

        this.timer.cancel();

        this.flushQueue();
    }

    public void flushQueue() {

        try (final Connection connection = this.database.getConnection()) {

            connection.setAutoCommit(false);
            if (!this.blockQueue.isEmpty())
                DatabaseQueue.insertBatchBlock(database.getBlockInteractionStsm(connection), this.blockQueue);
            if (!this.bucketQueue.isEmpty())
                DatabaseQueue.insertBucketBatch(database.getBucketActionStsm(connection), this.bucketQueue);
            if (!this.gameModeQueue.isEmpty())
                DatabaseQueue.insertGameModeBatch(database.getGamemodeStsm(connection), this.gameModeQueue);
            if (!this.itemActionQueue.isEmpty())
                DatabaseQueue.insertItemActionBatch(database.getItemActionStsm(connection), this.itemActionQueue);
            if (!this.consoleCommandQueue.isEmpty())
                DatabaseQueue.insertConsoleCommandBatch(database.getConsoleCommandStsm(connection), this.consoleCommandQueue);
            if (!this.playerConnectionQueue.isEmpty())
                DatabaseQueue.insertPlayerConnectionBatch(database.getPlayerConnectionStsm(connection), this.playerConnectionQueue);
            if (!this.playerChatQueue.isEmpty())
                DatabaseQueue.insertPlayerChatBatch(database.getPlayerChatStsm(connection), this.playerChatQueue);
            if (!this.anvilQueue.isEmpty())
                DatabaseQueue.insertAnvilBatch(database.getAnvilStsm(connection), this.anvilQueue);
            if (!this.bookQueue.isEmpty())
                DatabaseQueue.insertBookEditingBatch(database.getBookEditingStsm(connection), this.bookQueue);
            if (!this.enchantingQueue.isEmpty())
                DatabaseQueue.insertEnchantingBatch(database.getEnchantStsm(connection), this.enchantingQueue);
            if (!this.entityDeathQueue.isEmpty())
                DatabaseQueue.insertEntityDeathBatch(database.getEntityDeathStsm(connection), this.entityDeathQueue);
            if (!this.playerDeathQueue.isEmpty())
                DatabaseQueue.insertPlayerDeathBatch(database.getPlayerDeathStsm(connection), this.playerDeathQueue);
            if (!this.playerKickQueue.isEmpty())
                DatabaseQueue.insertPlayerKickBatch(database.getPlayerKickStsm(connection), this.playerKickQueue);
            if (!this.playerLevelQueue.isEmpty())
                DatabaseQueue.insertPlayerLevelBatch(database.getLevelChangeStsm(connection), this.playerLevelQueue);
            if (!this.playerTeleportQueue.isEmpty())
                DatabaseQueue.insertPlayerTeleportBatch(database.getPlayerTeleportStsm(connection), this.playerTeleportQueue);
            if (!this.playerSignTextQueue.isEmpty())
                DatabaseQueue.insertPlayerSignTextBatch(database.getPlayerSignTextStsm(connection), this.playerSignTextQueue);
            if (!this.playerCommandsQueue.isEmpty())
                DatabaseQueue.insertPlayerCommandBatch(database.getPlayerCommandStsm(connection), this.playerCommandsQueue);
            if (!this.chestInteractionQueue.isEmpty())
                DatabaseQueue.insertChestInteractionBatch(database.getChestInteractionStsm(connection), this.chestInteractionQueue);
            if (!this.craftingQueue.isEmpty())
                DatabaseQueue.insertCraftingBatch(database.getPlayerCraftStsm(connection), this.craftingQueue);
            if (!this.furnaceQueue.isEmpty())
                DatabaseQueue.insertFurnaceBatch(database.getFurnaceStsm(connection), this.furnaceQueue);
            if (!this.commandBlockQueue.isEmpty())
                DatabaseQueue.insertCommandBlockBatch(database.getCommandBlockStsm(connection), this.commandBlockQueue);
            if (!this.ramQueue.isEmpty())
                DatabaseQueue.insertRamBatch(database.getRAMStsm(connection), this.ramQueue);
//            Queue.insertRconBatch(database.getRcon(connection), this.rconQueue); //TODO FIX
            if (!this.tpsQueue.isEmpty())
                DatabaseQueue.insertTpsBatch(database.getTpsStsm(connection), this.tpsQueue);
            if (!this.afkQueue.isEmpty())
                DatabaseQueue.insertAfkBatch(database.getAfkStsm(connection), this.afkQueue);
            if (!this.playerCountQueue.isEmpty())
                DatabaseQueue.insertPlayerCountBatch(database.getPlayerCountStsm(connection), this.playerCountQueue);
            if (!this.advancedBanQueue.isEmpty())
                DatabaseQueue.insertAdvancedBanBatch(database.getAdvancedDataStsm(connection), this.advancedBanQueue);
            if (!this.serverAddresseQueue.isEmpty())
                DatabaseQueue.insertServerAddressBatch(database.getServerAddressStsm(connection), this.serverAddresseQueue);
            if (!this.portalCreateQueue.isEmpty())
                DatabaseQueue.insertPortalCreateBatch(database.getPortalCreateStsm(connection), this.portalCreateQueue);
            if (!this.primedTntQueue.isEmpty())
                DatabaseQueue.insertPrimedTntBatch(database.getPrimedTntStsm(connection), this.primedTntQueue);
            if (!this.liteBansQueue.isEmpty())
                DatabaseQueue.insertLiteBansBatch(database.getLiteBansStsm(connection), this.liteBansQueue);
            if(!this.standCrystalQueue.isEmpty())
                DatabaseQueue.insertStandCrystalBatch(database.getStandCrystalStsm(connection), this.standCrystalQueue);
            if(!this.armorStandQueue.isEmpty())
                DatabaseQueue.insertArmorStandBatch(database.getArmorStandStsm(connection), this.armorStandQueue);
            if(!this.leverInteractionQueue.isEmpty())
                DatabaseQueue.insertLeverInteractionBatch(database.getLeverInteractionStsm(connection), this.leverInteractionQueue);

            connection.commit();

        } catch (final Exception exception) { exception.printStackTrace(); }
    }
}
