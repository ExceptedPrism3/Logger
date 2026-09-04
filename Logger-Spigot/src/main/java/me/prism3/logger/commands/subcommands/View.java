package me.prism3.logger.commands.subcommands;

import me.prism3.logger.utils.SchedulerAdapter;
import me.prism3.logger.LoggerAPI;
import me.prism3.logger.commands.SubCommand;
import me.prism3.logger.managers.PermissionManager;
import me.prism3.logger.utils.enums.GeneralSideMessages;
import me.prism3.logger.utils.enums.LogType;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class View implements SubCommand {

    private static final int PER_PAGE = 20;
    private final LoggerAPI plugin;

    public View(LoggerAPI plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "view";
    }

    @Override
    public String getDescription() {
        return "View paginated logs from the database.";
    }

    @Override
    public String getSyntax() {
        return "/logger view <type|player> [<player|page>] [<page>]";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        // 1) db enabled + connection + perms
        if (!plugin.getData().getDatabaseSettings().enabled) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getData().getPluginPrefix())
                    + ChatColor.RED + "Database logging is disabled.");
            return;
        }
        if (plugin.getDatabaseManager() == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getData().getPluginPrefix())
                    + ChatColor.RED + "Database connection not established.");
            return;
        }
        if (!PermissionManager.canView(sender)) {
            sender.sendMessage(plugin.getMessageManager()
                    .getGeneralMessage(GeneralSideMessages.NO_PERMISSION));
            return;
        }

        // 2) parse args
        if (args.length < 2) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getData().getPluginPrefix())
                    + ChatColor.YELLOW + "Usage: " + getSyntax());
            return;
        }
        String arg1 = args[1];
        LogType type = null;
        boolean isTypeView = true;
        try {
            type = LogType.valueOf(arg1.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            isTypeView = false;
        }

        String playerFilter = null;
        int page = 1;

        if (isTypeView) {
            // args[2] may be player name or page
            if (args.length >= 3 && !isInteger(args[2]))
                playerFilter = args[2];
            else if (args.length >= 3)
                page = Integer.parseInt(args[2]);
            if (args.length >= 4 && isInteger(args[3]))
                page = Integer.parseInt(args[3]);
        } else {
            // pure player view: arg1 is player
            playerFilter = arg1;
            if (args.length >= 3 && isInteger(args[2]))
                page = Integer.parseInt(args[2]);
        }
        page = Math.max(1, page);
        final boolean playerOnly = !isTypeView;
        final String finalPlayer = playerFilter;
        final LogType finalType = type;
        final int requestedPage = page;

        // Use Bukkit async scheduler to avoid blocking the DB write queue
        SchedulerAdapter.runAsync(plugin, () -> {
            try (Connection conn = plugin.getDatabaseManager().getConnection()) {

                List<Row> rows = new ArrayList<>();
                int totalCount = 0;

                if (playerOnly || finalType == LogType.STAFF) {
                    int limitPerTable = 100; // Safety limit per table
                    Set<String> existingTables = new HashSet<>();
                    try (ResultSet rs = conn.getMetaData().getTables(null, null, "%", null)) {
                        while (rs.next()) {
                            existingTables.add(rs.getString("TABLE_NAME").toLowerCase());
                        }
                    } catch (Exception ignored) {}

                    for (String rawTbl : plugin.getDatabaseManager().getPlayerTableNames()) {
                        String fullTbl = plugin.getDatabaseManager().getTableName(rawTbl);
                        if (!existingTables.isEmpty() && !existingTables.contains(fullTbl.toLowerCase())) {
                            continue;
                        }

                        StringBuilder sql = new StringBuilder("SELECT * FROM " + fullTbl + " WHERE 1=1");
                        if (finalPlayer != null)
                            sql.append(" AND player_name = ?");
                        if (finalType == LogType.STAFF)
                            sql.append(" AND is_staff = 1");
                        sql.append(" ORDER BY date DESC LIMIT ?");

                        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
                            int paramIdx = 1;
                            if (finalPlayer != null)
                                ps.setString(paramIdx++, finalPlayer);
                            ps.setInt(paramIdx, limitPerTable);

                            try (ResultSet rs = ps.executeQuery()) {
                                ResultSetMetaData md = rs.getMetaData();
                                while (rs.next()) {
                                    Row r = new Row();
                                    r.table = rawTbl;
                                    r.ts = rs.getTimestamp("date");
                                    r.data = new LinkedHashMap<>();
                                    for (int i = 1; i <= md.getColumnCount(); i++) {
                                        String col = md.getColumnLabel(i);
                                        String val = rs.getString(i);
                                        if (val != null && !"".equals(val) && !"date".equals(col)
                                                && !"server_name".equals(col))
                                            r.data.put(col, val);
                                    }
                                    rows.add(r);
                                }
                            }
                        } catch (SQLException sq) {
                            // skip missing or un-migrated tables gracefully
                        }
                    }
                    // Sort in memory (since we fetched from multiple tables)
                    rows.sort((a, b) -> b.ts != null && a.ts != null ? b.ts.compareTo(a.ts) : 0);
                    totalCount = rows.size();

                    // In-memory pagination for multi-table
                    int maxPage = Math.max(1, (totalCount + PER_PAGE - 1) / PER_PAGE);
                    int pageUsed = Math.min(requestedPage, maxPage);
                    int from = (pageUsed - 1) * PER_PAGE;
                    int to = Math.min(totalCount, from + PER_PAGE);
                    rows = (from < totalCount) ? rows.subList(from, to) : Collections.emptyList();

                    final int finalPage = pageUsed;
                    final int finalMaxPage = maxPage;
                    final int finalTotal = totalCount;
                    final List<Row> finalRows = rows;

                    // Display on main thread
                    SchedulerAdapter.runSync(plugin, () -> displayLogs(sender, finalRows, finalPage,
                            finalMaxPage, finalTotal, finalType, finalPlayer, true));

                } else {
                    // Single-table: Optimized SQL Pagination
                    String rawTbl = getRawTableForType(finalType);
                    String fullTbl = plugin.getDatabaseManager().getTableName(rawTbl);

                    // 1. Get Total Count
                    String countSql = "SELECT COUNT(*) FROM " + fullTbl
                            + (finalPlayer != null ? " WHERE player_name = ?" : "");
                    try (PreparedStatement ps = conn.prepareStatement(countSql)) {
                        if (finalPlayer != null)
                            ps.setString(1, finalPlayer);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next())
                                totalCount = rs.getInt(1);
                        }
                    } catch (SQLException sq) {
                        sender.sendMessage(ChatColor.RED + "Log table not found: " + fullTbl);
                        return;
                    }

                    // 2. Calculate Offset
                    int maxPage = Math.max(1, (totalCount + PER_PAGE - 1) / PER_PAGE);
                    int pageUsed = Math.min(requestedPage, maxPage);
                    int offset = (pageUsed - 1) * PER_PAGE;

                    // 3. Fetch Page
                    String sql = "SELECT * FROM " + fullTbl +
                            (finalPlayer != null ? " WHERE player_name = ?" : "") +
                            " ORDER BY date DESC LIMIT ? OFFSET ?";

                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        int paramIdx = 1;
                        if (finalPlayer != null)
                            ps.setString(paramIdx++, finalPlayer);
                        ps.setInt(paramIdx++, PER_PAGE);
                        ps.setInt(paramIdx++, offset);

                        try (ResultSet rs = ps.executeQuery()) {
                            ResultSetMetaData md = rs.getMetaData();
                            while (rs.next()) {
                                Row r = new Row();
                                r.table = rawTbl;
                                r.ts = rs.getTimestamp("date");
                                r.data = new LinkedHashMap<>();
                                for (int i = 1; i <= md.getColumnCount(); i++) {
                                    String col = md.getColumnLabel(i);
                                    String val = rs.getString(i);
                                    if (val != null && !"".equals(val) && !"date".equals(col)
                                            && !"server_name".equals(col))
                                        r.data.put(col, val);
                                }
                                rows.add(r);
                            }
                        }
                    } catch (SQLException sq) {
                        // Safe fallback
                    }

                    final int finalPage = pageUsed;
                    final int finalMaxPage = maxPage;
                    final int finalTotal = totalCount;
                    final List<Row> finalRows = rows;

                    // Display on main thread
                    SchedulerAdapter.runSync(plugin, () -> displayLogs(sender, finalRows, finalPage,
                            finalMaxPage, finalTotal, finalType, finalPlayer, false));
                }

            } catch (RuntimeException rte) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getData().getPluginPrefix())
                        + ChatColor.RED + rte.getMessage());
            } catch (Exception ex) {
                plugin.getLogger().severe("View cmd SQL error: " + ex.getMessage());
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getData().getPluginPrefix())
                        + ChatColor.RED + "Database error: " + ex.getMessage());
            }
        });
    }

    // Helper class for rows
    private static class Row {
        String table;
        Timestamp ts;
        Map<String, String> data;
    }

    private void displayLogs(CommandSender sender, List<Row> sub, int pageUsed, int maxPage, int total,
            LogType finalType, String finalPlayer, boolean playerOnly) {
        String dbType = plugin.getData().getDatabaseSettings().type.toUpperCase();

        // Header
        sender.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "----------------" +
                ChatColor.AQUA + " [ Logger View ] " +
                ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "----------------");

        String context = (finalPlayer != null ? ChatColor.YELLOW + finalPlayer : ChatColor.GRAY + "Global") +
                ChatColor.DARK_GRAY + " | " +
                (finalType != null ? ChatColor.AQUA + finalType.name() : ChatColor.AQUA + "ALL TYPES");

        sender.sendMessage(ChatColor.GRAY + "Target: " + context);
        sender.sendMessage(ChatColor.GRAY + "Source: " + ChatColor.WHITE + dbType +
                ChatColor.GRAY + " | Page: " + ChatColor.WHITE + pageUsed + "/" + maxPage +
                ChatColor.GRAY + " | Total: " + ChatColor.WHITE + total);
        sender.sendMessage(""); // Spacer

        if (sub.isEmpty()) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getData().getPluginPrefix())
                    + ChatColor.RED + "No logs found matching your criteria.");
        } else {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MM-dd HH:mm:ss");
            for (Row r : sub) {
                TextComponent line = new TextComponent();

                // Timestamp
                TextComponent time = new TextComponent(
                        ChatColor.DARK_GRAY + "[" + r.ts.toLocalDateTime().format(fmt) + "] ");
                line.addExtra(time);

                // Summary
                String summary = getSummary(r);
                TextComponent msg = new TextComponent(summary);
                line.addExtra(msg);

                // Hover details
                ComponentBuilder hover = new ComponentBuilder("Log Details\n").color(ChatColor.AQUA).bold(true);
                hover.append("Table: ").color(ChatColor.GRAY).bold(false).append(r.table).color(ChatColor.WHITE)
                        .append("\n");

                for (Map.Entry<String, String> e : r.data.entrySet()) {
                    hover.append(e.getKey() + ": ").color(ChatColor.GRAY)
                            .append(e.getValue()).color(ChatColor.YELLOW).append("\n");
                }

                // Teleport hint
                if (r.data.containsKey("location_x")) {
                    hover.append("\n[Click to Teleport]").color(ChatColor.GREEN).bold(true);

                    double x = Double.parseDouble(r.data.get("location_x"));
                    double y = Double.parseDouble(r.data.get("location_y"));
                    double z = Double.parseDouble(r.data.get("location_z"));
                    String tpCmd = "/tp " + (sender instanceof Player ? sender.getName() : "player") + " " + x + " " + y
                            + " " + z;
                    line.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, tpCmd));
                }

                line.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hover.create()));

                if (sender instanceof Player) {
                    ((Player) sender).spigot().sendMessage(line);
                } else {
                    sender.sendMessage(line.toLegacyText());
                }
            }
        }

        // Footer
        sender.sendMessage("");
        if (sender instanceof Player) {
            TextComponent foot = new TextComponent(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "-------"
                    + ChatColor.RESET + " ");
            if (pageUsed > 1) {
                foot.addExtra(makeBtn("« Prev", pageUsed - 1, finalType, finalPlayer));
            } else {
                foot.addExtra(new TextComponent(ChatColor.DARK_GRAY + "« Prev"));
            }

            foot.addExtra(new TextComponent(ChatColor.DARK_GRAY + " | "));

            if (pageUsed < maxPage) {
                foot.addExtra(makeBtn("Next »", pageUsed + 1, finalType, finalPlayer));
            } else {
                foot.addExtra(new TextComponent(ChatColor.DARK_GRAY + "Next »"));
            }

            foot.addExtra(new TextComponent(" " + ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "-------"));
            ((Player) sender).spigot().sendMessage(foot);
        }
    }

    private String getSummary(Row r) {
        // Try to find the most relevant field for a summary
        if (r.data.containsKey("message"))
            return ChatColor.WHITE + r.data.get("message");
        if (r.data.containsKey("command"))
            return ChatColor.WHITE + r.data.get("command");
        if (r.data.containsKey("action"))
            return ChatColor.YELLOW + r.data.get("action");
        if (r.data.containsKey("item_type"))
            return ChatColor.AQUA + r.data.get("item_type");

        // Fallback to table name formatted
        String type = r.table.replace("player_", "").replace("server_", "").replace("_", " ").toUpperCase();
        return ChatColor.GRAY + type;
    }

    private TextComponent makeBtn(String text, int pg, LogType type, String pf) {
        String cmd = pf != null && type != null
                ? String.format("/logger view %s %s %d", type.name(), pf, pg)
                : pf != null
                        ? String.format("/logger view %s %d", pf, pg)
                        : String.format("/logger view %s %d", type.name(), pg);

        TextComponent btn = new TextComponent(ChatColor.AQUA + text);
        btn.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, cmd));
        btn.setHoverEvent(new HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder("Go to page " + pg)
                        .color(ChatColor.YELLOW)
                        .create()));
        return btn;
    }

    private static String getRawTableForType(LogType type) {
        if (type == null) return "player_chat";
        switch (type) {
            case PLAYER_CHAT: return "player_chat";
            case PLAYER_COMMAND: return "player_command";
            case PLAYER_BLOCK_BREAK: return "player_block_break";
            case PLAYER_BLOCK_PLACE: return "player_block_place";
            case PLAYER_DEATH: return "player_death";
            case PLAYER_JOIN: return "player_join";
            case PLAYER_LEAVE: return "player_leave";
            case PLAYER_KICK: return "player_kick";
            case PLAYER_TELEPORT: return "player_teleport";
            case PLAYER_LEVEL: return "player_level";
            case PLAYER_BUCKET_FILL: return "player_bucket_fill";
            case PLAYER_BUCKET_EMPTY: return "player_bucket_empty";
            case PLAYER_PRIME_TNT: return "player_prime_tnt";
            case PLAYER_ANVIL_INTERACTION: return "player_anvil";
            case PLAYER_ITEM_PICKUP: return "player_item_pickup";
            case PLAYER_ITEM_DROP: return "player_item_drop";
            case PLAYER_ITEM_ENCHANTING: return "player_item_enchant";
            case PLAYER_BOOK_INTERACTION: return "player_book_interaction";
            case PLAYER_FURNACE_INTERACTION: return "player_furnace_interaction";
            case PLAYER_GAME_MODE: return "player_gamemode";
            case PLAYER_ITEM_CRAFT: return "player_item_craft";
            case PLAYER_ENTITY_DEATH: return "player_entity_death";
            case PLAYER_PLAYER_SPAWN_EGG: return "player_spawn_egg";
            case PLAYER_PORTAL_CREATION: return "player_portal_creation";
            case PLAYER_ADVANCEMENTS: return "player_advancement_unlock";
            case PLAYER_CONTAINER_INTERACTION: return "player_container_interaction";
            case PLAYER_REGISTRATION: return "player_registration";
            case PLAYER_TOTEM_OF_UNDYING: return "player_totem_of_undying";
            case PLAYER_WOOD_STRIP: return "player_wood_strip";
            case PLAYER_RESPAWN_ANCHOR: return "player_respawn_anchor";
            case PLAYER_LEVER_INTERACTION: return "player_lever_interaction";
            case PLAYER_SIGN_INTERACTION: return "player_sign_interaction";
            case PLAYER_VILLAGER_TRADE: return "player_villager_trade";
            case PLAYER_PIGLIN_BARTER: return "player_piglin_barter";
            case PLAYER_SCULK_SHRIEKER: return "player_sculk_shrieker";
            case PLAYER_CRAFTER_CRAFT: return "player_crafter_craft";
            case SERVER_CONSOLE_COMMAND: return "server_console_command";
            case SERVER_RAM: return "server_ram";
            case SERVER_START: return "server_start";
            case SERVER_STOP: return "server_stop";
            case SERVER_TPS: return "server_tps";
            case SERVER_COMMAND_BLOCK: return "server_command_block";
            case SERVER_MANUAL_LOG: return "server_manual_log";
            case SERVER_RCON_COMMAND: return "server_rcon_command";
            default: return type.name().toLowerCase();
        }
    }

    private static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<String> getSubCommandsArgs(CommandSender sender, String[] args) {
        if (args.length == 2) {
            List<String> out = new ArrayList<>();
            // suggest types
            out.addAll(Arrays.stream(LogType.values())
                    .map(LogType::name)
                    .filter(n -> n.startsWith(args[1].toUpperCase()))
                    .collect(Collectors.toList()));
            // suggest players
            out.addAll(plugin.getServer().getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(n -> n.toLowerCase().startsWith(args[1].toLowerCase()))
                    .collect(Collectors.toList()));
            return out;
        }
        if (args.length >= 3 && args[1].matches("(?i)player_.*")) {
            // after a player_* type, next could be page
            return Arrays.asList("1", "2", "3", "4", "5");
        }
        if (args.length == 3 && !args[1].matches("(?i)player_.*")) {
            // after type or player name, could be page or player
            List<String> out = new ArrayList<>(Arrays.asList("1", "2", "3", "4", "5"));
            if (plugin.getData().getDatabaseSettings().enabled) {
                out = new ArrayList<>(plugin.getServer().getOnlinePlayers().stream()
                        .map(Player::getName).collect(Collectors.toList()));
                out.addAll(Arrays.asList("1", "2", "3", "4", "5"));
            }
            return out;
        }
        return Collections.emptyList();
    }

    @Override
    public String getPermission() {
        return me.prism3.logger.managers.PermissionManager.LOGGER_VIEW;
    }
}
