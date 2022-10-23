package me.prism3.loggercore.database.utils;

import me.prism3.loggercore.database.DataSourceInterface;
import me.prism3.loggercore.database.entity.BlockInteraction;
import me.prism3.loggercore.database.entity.BucketAction;
import me.prism3.loggercore.database.entity.GameMode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Queue {

    private final ArrayBlockingQueue<BlockInteraction> blockQueue = new ArrayBlockingQueue<>(100);
    private final ArrayBlockingQueue<BucketAction> bucketQueue = new ArrayBlockingQueue<>(100);
    private final ArrayBlockingQueue<GameMode> gamemodeQueue = new ArrayBlockingQueue<>(100);
    private final DataSourceInterface database;
    private int batchSize;

    public Queue(DataSourceInterface database) {
        this.database = database;
        this.enableRepeater();
    }

    public static void insertBatchBlock(PreparedStatement stsm, ArrayBlockingQueue<BlockInteraction> queue) {
        try {
            int size = queue.size();
            for (int i = 0; i < size; i++) {

                BlockInteraction block = queue.take();
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
    public static void insertBucketBatch(PreparedStatement bucketStsm, ArrayBlockingQueue<BucketAction> bucketQueue) throws SQLException {
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

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public void addItemToQueue(Object item) {
        if (item instanceof BlockInteraction) {
            blockQueue.add((BlockInteraction) item);
        } else if (item instanceof BucketAction) {
            bucketQueue.add((BucketAction) item);
        } else if (item instanceof GameMode) {
            gamemodeQueue.add((GameMode) item);
        } else {
            throw new RuntimeException("Unidentified Object type!" + item.getClass());
        }
        //TODO add the rest of the entities


    }

    public void enableRepeater() {
        ScheduledExecutorService executor =
                Executors.newSingleThreadScheduledExecutor();

        Runnable periodicTask = new Runnable() {
            @Override
            public void run() {
                flushQueue();
            }
        };

        executor.scheduleAtFixedRate(periodicTask, 15, 30, TimeUnit.SECONDS);
    }

    public void flushQueue() {
        try (Connection connection = database.getConnection()) {
            connection.setAutoCommit(false);

            Queue.insertBatchBlock(database.getBlockInteractionStsm(connection), blockQueue);
            Queue.insertBucketBatch(database.getBucketActionStsm(connection), this.bucketQueue);

            connection.commit();


        } catch (SQLException exception) {
            exception.printStackTrace();
        }


        System.out.println("Repeater is happening");


    }
}

