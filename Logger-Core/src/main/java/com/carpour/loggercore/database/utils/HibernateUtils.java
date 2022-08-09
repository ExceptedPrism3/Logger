package com.carpour.loggercore.database.utils;

import com.carpour.loggercore.database.data.DatabaseCredentials;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.EntityManager;
import java.util.Properties;

public class HibernateUtils {
    private static ThreadLocal<Session> threadLocal = new ThreadLocal<Session>();
    private static Properties databaseCredentials;

    private static SessionFactory sessionFactory = null;

    private static EntityManager em;
    public static void initializeHibernate(DatabaseCredentials databaseCredentials) {

        Configuration a = new Configuration().setProperties(databaseCredentials.getPropertiesForHib());
        try {
            a.addPackage("com.carpour.loggercore.database.entity");
            a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.Anvil"));
            a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.BlockBreak"));
            a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.BlockPlace"));
            a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.BookEditing"));
            a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.BucketEmpty"));
            a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.BucketFill"));
            a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.ChestInteraction"));
            a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.CommandBlock"));
            a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.ConsoleCommand"));
            a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.Crafting"));
            a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.Enchanting"));
            a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.EntityPlayer"));
            a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.EntityDeath"));
            a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.Furnace"));
            a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.GameMode"));
            a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.ItemDrop"));
            a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.ItemPickup"));
            a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.PlayerChat"));
            a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.PlayerCommand"));
            a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.PlayerDeath"));
            a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.PlayerJoin"));
            a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.PlayerKick"));
            a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.PlayerLeave"));
            a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.PlayerLevel"));
            a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.PlayerSignText"));
            a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.PlayerTeleport"));
            a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.PortalCreation"));
            a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.PrimedTnt"));
            a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.Ram"));
            a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.Rcon"));
            a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.Registration"));
            a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.ServerStart"));
            a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.ServerStop"));
            a.addAnnotatedClass(Class.forName("com.carpour.loggercore.database.entity.Tp"));

            sessionFactory = a.buildSessionFactory();


        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Session getSession() {
        Session session = null;
        if (threadLocal.get() == null) {
            // Create Session object
            session = sessionFactory.openSession();
            threadLocal.set(session);
        } else {
            session = threadLocal.get();
        }
        return session;
    }

    public static void closeSession() {
        Session session = null;
        if (threadLocal.get() != null) {
            session = threadLocal.get();
            session.close();
            threadLocal.remove();
        }
    }

    public static void closeSessionFactory() {
        sessionFactory.close();
    }
}
