package me.prism3.loggercore.database.queue;

import me.prism3.loggercore.database.DataSourceInterface;
import me.prism3.loggercore.database.entity.*;
import me.prism3.loggercore.database.utils.DateUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

class Queue {

    private final ConcurrentLinkedQueue<BlockInteraction> blockQueue = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<BucketAction> bucketQueue = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<GameMode> gamemodeQueue = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<ItemAction> itemActionQueue = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<ConsoleCommand> consoleComamndQueue = new ConcurrentLinkedQueue<>();

    private final DataSourceInterface database;
    private int batchSize;

    private int currentSize = 0;

    protected Queue(DataSourceInterface database) {
        this.database = database;
        this.enableRepeater();
    }

    protected static void insertBatchBlock(PreparedStatement stsm, ConcurrentLinkedQueue<BlockInteraction> queue) {
        try {
            int size = queue.size();
            for (int i = 0; i < size; i++) {

                BlockInteraction block = queue.poll();
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
        } catch (Exception exception) {
            exception.printStackTrace();
        }


    }

    //TODO add the rest of the insert Batches
    protected static void insertBucketBatch(PreparedStatement bucketStsm, ConcurrentLinkedQueue<BucketAction> bucketQueue) throws SQLException {
        int size = bucketQueue.size();
        for (int i = 0; i < size; i++) {
            BucketAction bucket = bucketQueue.poll();
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

    protected static void insertItemActionBatch(PreparedStatement itemActionStsm, ConcurrentLinkedQueue<ItemAction> itemActionQueue) throws SQLException {
        int size = itemActionQueue.size();
        for (int i = 0; i < size; i++) {
            ItemAction itemAction = itemActionQueue.poll();
            itemActionStsm.setString(1, itemAction.getServerName());
            itemActionStsm.setString(2, itemAction.getWorld());
            itemActionStsm.setString(3, itemAction.getEntityPlayer().getPlayerName());
            itemActionStsm.setString(4, itemAction.getItem());
            itemActionStsm.setInt(5, itemAction.getX());
            itemActionStsm.setInt(6, itemAction.getY());
            itemActionStsm.setInt(7, itemAction.getZ());
            itemActionStsm.setString(4, itemAction.getChangedName());

            itemActionStsm.setBoolean(9, itemAction.isStaff());
            itemActionStsm.setString(10, itemAction.getItemActionType().name());

            itemActionStsm.setString(11, DateUtils.formatInstant(itemAction.getDate()));
            itemActionStsm.setInt(12, itemAction.getAmount());
            itemActionStsm.addBatch();

        }
        itemActionStsm.executeBatch();
        itemActionStsm.close();
    }

    protected static void insertConsoleCommandBatch(PreparedStatement consoleComamndStsm, ConcurrentLinkedQueue<ConsoleCommand> consoleCommandQueue) throws SQLException {
        int size = consoleCommandQueue.size();
        for (int i = 0; i < size; i++) {
            ConsoleCommand consoleCommand = consoleCommandQueue.poll();
            consoleComamndStsm.setString(1, consoleCommand.getServerName());
            consoleComamndStsm.setString(2, consoleCommand.getCommand());
            consoleComamndStsm.setString(3, DateUtils.formatInstant(consoleCommand.getDate()));
            consoleComamndStsm.addBatch();

        }
        consoleComamndStsm.executeBatch();
        consoleComamndStsm.close();
    }

    protected static void insertGamemodeBatch(PreparedStatement stsm, ConcurrentLinkedQueue<GameMode> queue) throws SQLException {
        int size = queue.size();
        for (int i = 0; i < size; i++) {
            GameMode gamemode = queue.poll();
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
    protected void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    protected void addItemToQueue(Object item) {
        currentSize++;
        if (item instanceof BlockInteraction) {
            blockQueue.add((BlockInteraction) item);
        } else if (item instanceof BucketAction) {
            bucketQueue.add((BucketAction) item);
        } else if (item instanceof GameMode) {
            gamemodeQueue.add((GameMode) item);
        } else if (item instanceof ItemAction) {
            itemActionQueue.add((ItemAction) item);
        } else if (item instanceof ConsoleCommand) {
            consoleComamndQueue.add((ConsoleCommand) item);
        } else {
            throw new RuntimeException("Unidentified Object type!" + item.getClass());
        }
        if (currentSize == 100) {
            currentSize = 0;
            Thread periodicTask = new Thread() {
                @Override
                public void run() {
                    long start1 = System.nanoTime();
                    flushQueue();
                    long end1 = System.nanoTime();
                    System.out.println("Time in seconds: " + TimeUnit.SECONDS.convert((end1 - start1), TimeUnit.NANOSECONDS));
                }
            };
            periodicTask.start();
        }
        //TODO add the rest of the entities


    }

    protected void enableRepeater() {

    }

    public void flushQueue() {
        try (Connection connection = database.getConnection()) {
            connection.setAutoCommit(false);

            Queue.insertBatchBlock(database.getBlockInteractionStsm(connection), this.blockQueue);
            Queue.insertBucketBatch(database.getBucketActionStsm(connection), this.bucketQueue);
            Queue.insertGamemodeBatch(database.getGamemodeStsm(connection), this.gamemodeQueue);
            Queue.insertItemActionBatch(database.getItemActionStsm(connection), this.itemActionQueue);
            Queue.insertConsoleCommandBatch(database.getConsoleCommandStsm(connection), this.consoleComamndQueue);

            connection.commit();
            System.out.println("Commited!");

        } catch (SQLException exception) {
            exception.printStackTrace();
        }


    }
}

