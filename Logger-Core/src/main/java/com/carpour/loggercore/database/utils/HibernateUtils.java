package me.prism3.loggercore.database.utils;

import me.prism3.loggercore.database.data.DatabaseCredentials;
import me.prism3.loggercore.database.data.Options;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HibernateUtils {

    private static final ThreadLocal<Session> threadLocal = new ThreadLocal<Session>();
    private static Properties hibernateProperties;
    private static SessionFactory sessionFactory = null;

    private HibernateUtils() { }

    public static void initializeHibernate(DatabaseCredentials databaseCredentials,
                                           Options options) {

        Logger.getLogger("org.hibernate").setLevel(Level.ALL);

        final Configuration a = new Configuration().setProperties(
                databaseCredentials.getPropertiesForHib());

        try {

            a.addPackage("me.prism3.loggercore.database.entity");

            if (options.getBooleanValue("Chat")) {
                a.addAnnotatedClass(
                        Class.forName("me.prism3.loggercore.database.entity.PlayerChat"));
            }

            if (options.getBooleanValue("Commands")) {
                a.addAnnotatedClass(
                        Class.forName("me.prism3.loggercore.database.entity.PlayerCommand"));
            }

            if (options.getBooleanValue("Sign-Text")) {
                a.addAnnotatedClass(
                        Class.forName("me.prism3.loggercore.database.entity.PlayerSignText"));
            }

            if (options.getBooleanValue("Join") || options.getBooleanValue("Leave")) {
                a.addAnnotatedClass(
                        Class.forName("me.prism3.loggercore.database.entity.PlayerConnection"));
            }

            if (options.getBooleanValue("Kick")) {
                a.addAnnotatedClass(
                        Class.forName("me.prism3.loggercore.database.entity.PlayerKick"));
            }

            if (options.getBooleanValue("Death")) {
                a.addAnnotatedClass(
                        Class.forName("me.prism3.loggercore.database.entity.PlayerDeath"));
            }

            if (options.getBooleanValue("Teleport")) {
                a.addAnnotatedClass(
                        Class.forName("me.prism3.loggercore.database.entity.PlayerTeleport"));
            }

            if (options.getBooleanValue("Level")) {
                a.addAnnotatedClass(
                        Class.forName("me.prism3.loggercore.database.entity.PlayerLevel"));
            }

            if (options.getBooleanValue("Block-Place") || options.getBooleanValue(
                    "Block-Break")) //Break and place and woodstripping
            {
                a.addAnnotatedClass(
                        Class.forName("me.prism3.loggercore.database.entity.BlockInteraction"));
            }

            if (options.getBooleanValue("Bucket-Fill") || options.getBooleanValue("cket-Empty")) {
                a.addAnnotatedClass(
                        Class.forName("me.prism3.loggercore.database.entity.BucketAction"));
            }

            if (options.getBooleanValue("Anvil")) {
                a.addAnnotatedClass(Class.forName("me.prism3.loggercore.database.entity.Anvil"));
            }

            if (options.getBooleanValue("Item-Pickup") || options.getBooleanValue("Item-Drop")) {
                a.addAnnotatedClass(
                        Class.forName("me.prism3.loggercore.database.entity.ItemAction"));
            }

            if (options.getBooleanValue("Enchanting")) {
                a.addAnnotatedClass(
                        Class.forName("me.prism3.loggercore.database.entity.Enchanting"));
            }

            if (options.getBooleanValue("Book-Editing")) {
                a.addAnnotatedClass(
                        Class.forName("me.prism3.loggercore.database.entity.BookEditing"));
            }

            if (options.getBooleanValue("Furnace")) {
                a.addAnnotatedClass(
                        Class.forName("me.prism3.loggercore.database.entity.Furnace"));
            }

            if (options.getBooleanValue("Game-Mode")) {
                a.addAnnotatedClass(
                        Class.forName("me.prism3.loggercore.database.entity.GameMode"));
            }

            if (options.getBooleanValue("Craft")) {
                a.addAnnotatedClass(
                        Class.forName("me.prism3.loggercore.database.entity.Crafting"));
            }

            if (options.getBooleanValue("Primed-TNT")) {
                a.addAnnotatedClass(
                        Class.forName("me.prism3.loggercore.database.entity.PrimedTnt"));
            }

            if (options.getBooleanValue("Chest-Interaction")) {
                a.addAnnotatedClass(
                        Class.forName("me.prism3.loggercore.database.entity.ChestInteraction"));
            }

            if (options.getBooleanValue("Entity-Death")) {
                a.addAnnotatedClass(
                        Class.forName("me.prism3.loggercore.database.entity.EntityDeath"));
            }

            if (options.getBooleanValue("Start")) {
                a.addAnnotatedClass(
                        Class.forName("me.prism3.loggercore.database.entity.ServerStart"));
            }

            if (options.getBooleanValue("Stop")) {
                a.addAnnotatedClass(
                        Class.forName("me.prism3.loggercore.database.entity.ServerStop"));
            }

            if (options.getBooleanValue("Console-Commands")) {
                a.addAnnotatedClass(
                        Class.forName("me.prism3.loggercore.database.entity.ConsoleCommand"));
            }

            if (options.getBooleanValue("RAM")) {
                a.addAnnotatedClass(Class.forName("me.prism3.loggercore.database.entity.Ram"));
            }

            if (options.getBooleanValue("TPS")) {
                a.addAnnotatedClass(Class.forName("me.prism3.loggercore.database.entity.Tps"));
            }

            if (options.getBooleanValue("Portal-Creation")) {
                a.addAnnotatedClass(
                        Class.forName("me.prism3.loggercore.database.entity.PortalCreation"));
            }

            if (options.getBooleanValue("RCON")) {
                a.addAnnotatedClass(Class.forName("me.prism3.loggercore.database.entity.Rcon"));
            }

            if (options.getBooleanValue("Command-Block")) {
                a.addAnnotatedClass(
                        Class.forName("me.prism3.loggercore.database.entity.CommandBlock"));
            }

            if (options.getBooleanValue("Registration")) {
                a.addAnnotatedClass(
                        Class.forName("me.prism3.loggercore.database.entity.Registration"));
            }

            a.addAnnotatedClass(
                    Class.forName("me.prism3.loggercore.database.entity.EntityPlayer"));

            sessionFactory = a.buildSessionFactory();

        }
        catch (ClassNotFoundException e) { throw new RuntimeException(e); }
    }

    public static Session getSession() {

        final Session session;

        if (threadLocal.get() == null) {
            // Create Session object
            session = sessionFactory.openSession();
            threadLocal.set(session);
        } else { session = threadLocal.get(); }
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

    public static SessionFactory getSessionFactory() { return sessionFactory; }


    public static void closeSessionFactory() { sessionFactory.close(); }

}
