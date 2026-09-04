package me.prism3.logger_bungee.listeners.server;

import me.prism3.logger_bungee.LoggerBungee;
import me.prism3.logger_bungee.utils.Constants;
import me.prism3.logger_bungee.utils.Log;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class ServerCommandInterceptor {

    private final LoggerBungee plugin;
    private Map<String, Command> originalCommandMap;
    private String lastLoggedConsoleCommand = null;
    private long lastLoggedTime = 0;

    public ServerCommandInterceptor(LoggerBungee plugin) {
        this.plugin = plugin;
        this.inject();
    }

    @SuppressWarnings("unchecked")
    public void inject() {
        try {
            PluginManager pm = plugin.getProxy().getPluginManager();
            Field mapField = PluginManager.class.getDeclaredField("commandMap");
            mapField.setAccessible(true);
            this.originalCommandMap = (Map<String, Command>) mapField.get(pm);

            if (originalCommandMap instanceof InterceptingCommandMap) {
                return; // already injected
            }

            InterceptingCommandMap customMap = new InterceptingCommandMap(originalCommandMap, this);
            mapField.set(pm, customMap);
        } catch (Exception e) {
            Log.warn("Could not hook Server Command interceptor: " + e.getMessage());
        }
    }

    public synchronized void logServerCommand(String fullCommand) {
        if (fullCommand == null || fullCommand.trim().isEmpty()) return;
        fullCommand = fullCommand.trim();

        // Avoid duplicate logging within 100ms for the same command
        long now = System.currentTimeMillis();
        if (fullCommand.equals(lastLoggedConsoleCommand) && (now - lastLoggedTime) < 100) {
            return;
        }
        lastLoggedConsoleCommand = fullCommand;
        lastLoggedTime = now;

        if (!plugin.getConfigManager().getConfig().getBoolean("Log-Server.Server-Commands", true)
            && !plugin.getConfigManager().getConfig().getBoolean("Log-Server.Console-Commands", true)) {
            return;
        }

        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("command", fullCommand);
        placeholders.put("log", fullCommand);
        placeholders.put("server", plugin.getConfigManager().getConfig().getString("Server-Name", "BungeeCord"));

        plugin.getLogManager().logServerEvent(Constants.Events.SERVER_COMMANDS, placeholders);
    }

    public void onCommandExecuted(CommandSender sender, Command cmd, String[] args) {
        // Only log if sender is not a player (i.e. Console / Server Command)
        if (!(sender instanceof ProxiedPlayer)) {
            String fullCommand = cmd.getName();
            if (args != null && args.length > 0) {
                fullCommand += " " + String.join(" ", args);
            }
            logServerCommand(fullCommand);
        }
    }

    public void onUnknownCommandLookup(String key) {
        // Try to get the exact line from BungeeCord's ConsoleReader
        String line = getConsoleReaderLastLine();
        if (line != null && !line.trim().isEmpty()) {
            logServerCommand(line);
        } else {
            logServerCommand(key);
        }
    }

    private String getConsoleReaderLastLine() {
        try {
            Object proxy = plugin.getProxy();
            Method getConsoleReader = proxy.getClass().getMethod("getConsoleReader");
            Object consoleReader = getConsoleReader.invoke(proxy);
            if (consoleReader != null) {
                Method getHistory = consoleReader.getClass().getMethod("getHistory");
                Object history = getHistory.invoke(consoleReader);
                if (history != null) {
                    Method size = history.getClass().getMethod("size");
                    int s = (int) size.invoke(history);
                    if (s > 0) {
                        Method get = history.getClass().getMethod("get", int.class);
                        CharSequence seq = (CharSequence) get.invoke(history, s - 1);
                        if (seq != null) {
                            return seq.toString();
                        }
                    }
                }
            }
        } catch (Throwable ignored) {}
        return null;
    }

    private static class InterceptingCommandMap extends HashMap<String, Command> {
        private final Map<String, Command> delegate;
        private final ServerCommandInterceptor interceptor;
        private final Map<Command, Command> wrappedCache = new WeakHashMap<>();

        public InterceptingCommandMap(Map<String, Command> delegate, ServerCommandInterceptor interceptor) {
            this.delegate = delegate;
            this.interceptor = interceptor;
            if (delegate != null) {
                super.putAll(delegate);
            }
        }

        @Override
        public Command get(Object key) {
            Command cmd = delegate != null ? delegate.get(key) : super.get(key);
            
            if (cmd == null && key != null) {
                // Check if called from dispatchCommand
                if (isCalledFromConsoleDispatch()) {
                    interceptor.onUnknownCommandLookup(key.toString());
                }
            }

            if (cmd == null) return null;
            return wrap(cmd);
        }

        private boolean isCalledFromConsoleDispatch() {
            StackTraceElement[] stack = Thread.currentThread().getStackTrace();
            for (StackTraceElement el : stack) {
                if (el.getMethodName().equals("dispatchCommand") || el.getClassName().contains("BungeeCordLauncher")) {
                    return true;
                }
            }
            return false;
        }

        private synchronized Command wrap(Command original) {
            if (original instanceof WrappedCommand) {
                return original;
            }
            return wrappedCache.computeIfAbsent(original, c -> {
                if (c instanceof TabExecutor) {
                    return new WrappedTabCommand((TabExecutor) c, c, interceptor);
                }
                return new WrappedCommand(c, interceptor);
            });
        }

        @Override
        public Command put(String key, Command value) {
            if (delegate != null) delegate.put(key, value);
            return super.put(key, value);
        }

        @Override
        public Command remove(Object key) {
            if (delegate != null) delegate.remove(key);
            return super.remove(key);
        }

        @Override
        public boolean containsKey(Object key) {
            return delegate != null ? delegate.containsKey(key) : super.containsKey(key);
        }

        @Override
        public Collection<Command> values() {
            return delegate != null ? delegate.values() : super.values();
        }

        @Override
        public Set<Map.Entry<String, Command>> entrySet() {
            return delegate != null ? delegate.entrySet() : super.entrySet();
        }

        @Override
        public int size() {
            return delegate != null ? delegate.size() : super.size();
        }

        @Override
        public boolean isEmpty() {
            return delegate != null ? delegate.isEmpty() : super.isEmpty();
        }
    }

    private static class WrappedCommand extends Command {
        protected final Command original;
        protected final ServerCommandInterceptor interceptor;

        public WrappedCommand(Command original, ServerCommandInterceptor interceptor) {
            super(original.getName(), original.getPermission(), original.getAliases());
            this.original = original;
            this.interceptor = interceptor;
        }

        @Override
        public void execute(CommandSender sender, String[] args) {
            try {
                interceptor.onCommandExecuted(sender, original, args);
            } catch (Throwable ignored) {}
            original.execute(sender, args);
        }

        @Override
        public boolean hasPermission(CommandSender sender) {
            return original.hasPermission(sender);
        }

        @Override
        public String getPermissionMessage() {
            return original.getPermissionMessage();
        }
    }

    private static class WrappedTabCommand extends WrappedCommand implements TabExecutor {
        private final TabExecutor tabOriginal;

        public WrappedTabCommand(TabExecutor tabOriginal, Command original, ServerCommandInterceptor interceptor) {
            super(original, interceptor);
            this.tabOriginal = tabOriginal;
        }

        @Override
        public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
            return tabOriginal.onTabComplete(sender, args);
        }
    }
}
