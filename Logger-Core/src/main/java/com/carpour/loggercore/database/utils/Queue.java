package com.carpour.loggercore.database.utils;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayDeque;
import java.util.Deque;

public class Queue {
    private static final Deque<Object> queuedItems = new ArrayDeque<>(20);
    public static int batchSize = 15;

    public static void addItemToQueue(Object item) {
        queuedItems.add(item);
        System.out.println("added " + queuedItems.size());
        if(queuedItems.size() >= batchSize) { flushItems(); }
    }

    public static boolean flushItems() {
        long startTime = System.nanoTime();
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtils.getSession();
            tx = session.beginTransaction();
            int count = 0;
            while (count <= batchSize) {

                session.merge(queuedItems.pollFirst());
                count++;
            }
            session.flush();
        }
        finally {
            if(tx != null) {
                tx.commit();
            }
        }
        long stopTime = System.nanoTime();
        System.out.println(stopTime - startTime);
        return true;
    }


}
