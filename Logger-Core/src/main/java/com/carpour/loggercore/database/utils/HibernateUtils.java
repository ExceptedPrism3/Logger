package com.carpour.loggercore.database.utils;

import com.carpour.loggercore.database.data.DatabaseCredentials;
import com.carpour.loggercore.database.data.Options;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Properties;

public class HibernateUtils {

    private static final ThreadLocal<Session> threadLocal = new ThreadLocal<>();
    private static Properties databaseCredentials;
    private static SessionFactory sessionFactory = null;

    private HibernateUtils() {}

    public static void initializeHibernate(DatabaseCredentials databaseCredentials, Options options) {

        final Configuration a = new Configuration().setProperties(databaseCredentials.getPropertiesForHib());

        try {
            a.addPackage("com.carpour.loggercore.database.entity");

            if (options.getBooleanValue("Chat"))
                a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.PlayerChat"));

            if (options.getBooleanValue("Commands"))
                a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.PlayerCommand"));

            if (options.getBooleanValue("Sign-Text"))
                a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.PlayerSignText"));

            if (options.getBooleanValue("Join"))
                a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.PlayerJoin"));

            if (options.getBooleanValue("Leave"))
                a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.PlayerLeave"));

            if (options.getBooleanValue("Kick"))
                a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.PlayerKick"));

            if (options.getBooleanValue("Death"))
                a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.PlayerDeath"));

            if (options.getBooleanValue("Teleport"))
                a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.PlayerTeleport"));

            if (options.getBooleanValue("Level"))
                a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.PlayerLevel"));

            if (options.getBooleanValue("Block-Place")) //Break and place and woodstripping
                a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.PlayerLevel"));

            if (options.getBooleanValue("Bucket-Fill"))
                a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.BucketFill"));

            if (options.getBooleanValue("Bucket-Empty"))
                a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.BucketEmpty"));

            if (options.getBooleanValue("Anvil"))
                a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.Anvil"));

            if (options.getBooleanValue("Item-Pickup"))
                a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.ItemPickup"));

            if (options.getBooleanValue("Item-Drop"))
                a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.ItemDrop"));

            if (options.getBooleanValue("Enchanting"))
                a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.Enchanting"));

            if (options.getBooleanValue("Book-Editing"))
                a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.BookEditing"));

            if (options.getBooleanValue("Furnace"))
                a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.Furnace"));

            if (options.getBooleanValue("Game-Mode"))
                a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.GameMode"));

            if (options.getBooleanValue("Craft"))
                a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.Crafting"));
            //TODO registration

            if (options.getBooleanValue("Primed-TNT"))
                a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.PrimedTnt"));

            if (options.getBooleanValue("Chest-Interaction"))
                a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.ChestInteraction"));

            if (options.getBooleanValue("Entity-Death"))
                a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.EntityDeath"));

            if (options.getBooleanValue("Start"))
                a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.ServerStart"));

            if (options.getBooleanValue("Stop"))
                a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.ServerStop"));

            if (options.getBooleanValue("Console-Commands"))
                a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.ConsoleCommand"));

            if (options.getBooleanValue("RAM"))
                a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.Ram"));

            if (options.getBooleanValue("TPS"))
                a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.Tp"));

            if (options.getBooleanValue("Portal-Creation"))
                a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.PortalCreation"));

            if (options.getBooleanValue("RCON"))
                a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.Rcon"));

            if (options.getBooleanValue("Command-Block"))
                a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.CommandBlock"));

            a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.EntityPlayer"));
            a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.Registration"));

            sessionFactory = a.buildSessionFactory();

        } catch (ClassNotFoundException e) { throw new RuntimeException(e); }
    }

    public static Session getSession() {

        final Session session;

        if (threadLocal.get() == null) {
            // Create Session object
            session = sessionFactory.openSession();
            threadLocal.set(session);
        } else
            session = threadLocal.get();
        return session;
    }

    public static void closeSession() {

        final Session session;

        if (threadLocal.get() != null) {
            session = threadLocal.get();
            session.close();
            threadLocal.remove();
        }
    }
    public static void closeSessionFactory() { sessionFactory.close(); }
}
