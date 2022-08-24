package me.prism3.loggercore.database.utils;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Deque;

public class QueueThread extends Thread {

    private final Deque<Object> a;

    public QueueThread(Deque<Object> a) {
        this.a = a;
    }

    public void flushItems() {
        System.out.println("Running thread");
        long startTime = System.nanoTime();
        final Session session;
        Transaction tx = null;

        try {
            session = HibernateUtils.getSession();
            tx = session.beginTransaction();
            while (!this.a.isEmpty()) {
                session.merge(a.pollFirst());
            }

        } finally { if (tx != null) tx.commit(); }

        long stopTime = System.nanoTime();
        System.out.println(stopTime - startTime + " Thread");
    }

    @Override
    public void run() {
        this.flushItems();
    }
}
