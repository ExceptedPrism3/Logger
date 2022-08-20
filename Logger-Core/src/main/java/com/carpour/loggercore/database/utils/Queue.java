package com.carpour.loggercore.database.utils;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayDeque;
import java.util.Deque;

public class Queue {

    public Queue() { }

    public Deque<Object> queuedItems = new ArrayDeque<>(20);
    public int batchSize = 15;

    public void addItemToQueue(Object item) {
        queuedItems.add(item);
        System.out.println("added " + queuedItems.size());
        if (queuedItems.size() >= batchSize) {
            Deque<Object> copiedItems = queuedItems;
            queuedItems = new ArrayDeque<>(20);
            new QueueThread(copiedItems).run();
        }
    }

    public boolean flushCurrentQueue() {

        long startTime = System.nanoTime();
        final Session session;
        Transaction tx = null;

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
