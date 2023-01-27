///*
//package me.prism3.logger.hooks;
//
//import com.sk89q.worldguard.WorldGuard;
//import com.sk89q.worldguard.session.SessionManager;
//import me.prism3.logger.Main;
//import me.prism3.logger.events.plugindependent.OnWorldGuard;
//import me.prism3.logger.utils.Log;
//import org.bukkit.plugin.PluginUpdater;
//
//public class WorldGuardUtil {
//
//    private WorldGuardUtil() {}
//
//    public static boolean isAllowed = false;
//
//    public static void getWorldGuardHook() {
//
//        if (getWorldGuardAPI() != null && Main.getInstance().getConfig().getBoolean("Log-Extras.WorldGuard")) {
//
//            final SessionManager sessionManager = WorldGuard.getInstance().getPlatform().getSessionManager();
//            sessionManager.registerHandler(OnWorldGuard.FACTORY, null);
//
//            Log.info("WorldGuard PluginUpdater Detected!");
//            isAllowed = true;
//        }
//    }
//
//    private static PluginUpdater getWorldGuardAPI() { return Main.getInstance().getServer().getPluginManager().getPlugin("WorldGuard"); }
//}
//*/
