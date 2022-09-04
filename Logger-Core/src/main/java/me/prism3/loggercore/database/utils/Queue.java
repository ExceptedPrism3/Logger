package me.prism3.loggercore.database.utils;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayDeque;
import java.util.Deque;

public class Queue {

    private int batchSize;

    public Deque<Object> queuedItems = new ArrayDeque<>(50);

    public Queue() { }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public void addItemToQueue(Object item) {
        queuedItems.add(item);
        System.out.println("added " + queuedItems.size());
        if (queuedItems.size() >= batchSize) {
            Deque<Object> copiedItems = queuedItems;
            queuedItems = new ArrayDeque<>(50);
            QueueThread b = new QueueThread(copiedItems);
            Thread a = new Thread(b);
            a.start();
        }
    }

    public boolean flushCurrentQueue() {
        //TODO scheduler for queue
        long startTime = System.nanoTime();
        final Session session;
        Transaction tx = null;
        //TODO handle queue in case of an exception
        try {
            session = HibernateUtils.getSession();
            tx = session.beginTransaction();
            int count = 0;
            while (count <= batchSize && !queuedItems.isEmpty()) {

                session.merge(this.queuedItems.pollFirst());
                count++;
            }
        }
        finally {
            if (tx != null)
                tx.commit();
        }
        long stopTime = System.nanoTime();
        System.out.println(stopTime - startTime);
        return true;
    }
}
