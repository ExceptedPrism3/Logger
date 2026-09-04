package me.prism3.logger.utils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Universal Scheduler Adapter supporting traditional Bukkit/Paper and Folia multi-threaded regions.
 */
public final class SchedulerAdapter {

    private static final boolean IS_FOLIA;
    private static Object asyncScheduler;
    private static Object globalRegionScheduler;

    private static Method asyncRunNowMethod;
    private static Method asyncRunDelayedMethod;
    private static Method asyncRunAtFixedRateMethod;
    private static Method asyncCancelTasksMethod;

    private static Method globalRunMethod;
    private static Method globalRunDelayedMethod;
    private static Method globalRunAtFixedRateMethod;
    private static Method globalCancelTasksMethod;

    static {
        boolean folia = false;
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            folia = true;
        } catch (final ClassNotFoundException ignored) {}

        IS_FOLIA = folia;

        if (folia) {
            try {
                final Method getAsyncScheduler = Bukkit.class.getMethod("getAsyncScheduler");
                asyncScheduler = getAsyncScheduler.invoke(null);
                final Class<?> asyncClass = asyncScheduler.getClass();

                asyncRunNowMethod = asyncClass.getMethod("runNow", Plugin.class, Consumer.class);
                asyncRunDelayedMethod = asyncClass.getMethod("runDelayed", Plugin.class, Consumer.class, long.class, TimeUnit.class);
                asyncRunAtFixedRateMethod = asyncClass.getMethod("runAtFixedRate", Plugin.class, Consumer.class, long.class, long.class, TimeUnit.class);
                asyncCancelTasksMethod = asyncClass.getMethod("cancelTasks", Plugin.class);

                final Method getGlobalScheduler = Bukkit.class.getMethod("getGlobalRegionScheduler");
                globalRegionScheduler = getGlobalScheduler.invoke(null);
                final Class<?> globalClass = globalRegionScheduler.getClass();

                globalRunMethod = globalClass.getMethod("run", Plugin.class, Consumer.class);
                globalRunDelayedMethod = globalClass.getMethod("runDelayed", Plugin.class, Consumer.class, long.class);
                globalRunAtFixedRateMethod = globalClass.getMethod("runAtFixedRate", Plugin.class, Consumer.class, long.class, long.class);
                globalCancelTasksMethod = globalClass.getMethod("cancelTasks", Plugin.class);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }

    private SchedulerAdapter() {}

    public static boolean isFolia() {
        return IS_FOLIA;
    }

    public static void runAsync(final Plugin plugin, final Runnable runnable) {
        if (IS_FOLIA && asyncRunNowMethod != null) {
            try {
                final Consumer<Object> consumer = task -> runnable.run();
                asyncRunNowMethod.invoke(asyncScheduler, plugin, consumer);
                return;
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
        Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    public static void runSync(final Plugin plugin, final Runnable runnable) {
        if (IS_FOLIA && globalRunMethod != null) {
            try {
                final Consumer<Object> consumer = task -> runnable.run();
                globalRunMethod.invoke(globalRegionScheduler, plugin, consumer);
                return;
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
        Bukkit.getScheduler().runTask(plugin, runnable);
    }

    public static void runLater(final Plugin plugin, final Runnable runnable, final long delayTicks) {
        if (IS_FOLIA && globalRunDelayedMethod != null) {
            try {
                final Consumer<Object> consumer = task -> runnable.run();
                globalRunDelayedMethod.invoke(globalRegionScheduler, plugin, consumer, Math.max(1L, delayTicks));
                return;
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
        Bukkit.getScheduler().runTaskLater(plugin, runnable, delayTicks);
    }

    public static void runTimer(final Plugin plugin, final Runnable runnable, final long initialDelayTicks, final long periodTicks) {
        if (IS_FOLIA && globalRunAtFixedRateMethod != null) {
            try {
                final Consumer<Object> consumer = task -> runnable.run();
                globalRunAtFixedRateMethod.invoke(globalRegionScheduler, plugin, consumer, Math.max(1L, initialDelayTicks), Math.max(1L, periodTicks));
                return;
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
        Bukkit.getScheduler().runTaskTimer(plugin, runnable, initialDelayTicks, periodTicks);
    }

    public static void runAsyncTimer(final Plugin plugin, final Runnable runnable, final long initialDelayTicks, final long periodTicks) {
        if (IS_FOLIA && asyncRunAtFixedRateMethod != null) {
            try {
                final Consumer<Object> consumer = task -> runnable.run();
                final long initMs = initialDelayTicks * 50L;
                final long periodMs = periodTicks * 50L;
                asyncRunAtFixedRateMethod.invoke(asyncScheduler, plugin, consumer, Math.max(1L, initMs), Math.max(50L, periodMs), TimeUnit.MILLISECONDS);
                return;
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, runnable, initialDelayTicks, periodTicks);
    }

    public static void cancelAllTasks(final Plugin plugin) {
        if (IS_FOLIA) {
            try {
                if (asyncCancelTasksMethod != null) {
                    asyncCancelTasksMethod.invoke(asyncScheduler, plugin);
                }
                if (globalCancelTasksMethod != null) {
                    globalCancelTasksMethod.invoke(globalRegionScheduler, plugin);
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
        } else {
            Bukkit.getScheduler().cancelTasks(plugin);
        }
    }
}
