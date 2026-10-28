package me.prism3.logger;

import me.prism3.logger.database.DatabaseLoggerFactory;
import me.prism3.logger.database.DatabasePlayerLogger;
import me.prism3.logger.database.DatabaseServerLogger;
import me.prism3.logger.events.player.*;
import me.prism3.logger.managers.LoggerManager;
import me.prism3.logger.managers.MessageManager;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.enums.LogType;
import me.prism3.logger_core.database.DatabaseConfig;
import me.prism3.logger_core.database.DatabaseManager;
import me.prism3.logger_core.platform.LoggerPlatform;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.ArgumentCaptor;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoggerSpigotFullSuiteTest {

    private static final String[] LANGUAGES = {
            "ar", "de_DE", "en_US", "es_ES", "fr_fr",
            "it_IT", "ja_JP", "ko_KR", "pt_BR", "ru_RU", "zh_cn"
    };

    private Map<String, String> buildCompletePlaceholders() {
        Map<String, String> ph = new HashMap<>();
        ph.put("world", "world");
        ph.put("player", "TestPlayer");
        ph.put("uuid", "12345678-1234-1234-1234-1234567890ab");
        ph.put("x", "100");
        ph.put("y", "64");
        ph.put("z", "-200");
        ph.put("date", "2026-10-28 12:00:00");
        ph.put("time", "2026-10-28 12:00:00");
        ph.put("message", "Hello World");
        ph.put("command", "/gamemode creative");
        ph.put("sign_x", "101");
        ph.put("sign_y", "65");
        ph.put("sign_z", "-199");
        ph.put("lines", "Line1 | Line2 | Line3 | Line4");
        ph.put("line", "Line1 | Line2 | Line3 | Line4");
        ph.put("line1", "Line1");
        ph.put("line2", "Line2");
        ph.put("line3", "Line3");
        ph.put("line4", "Line4");
        ph.put("ip", "127.0.0.1");
        ph.put("reason", "Kicked for testing");
        ph.put("level", "30");
        ph.put("death_cause", "FALL");
        ph.put("cause", "FALL");
        ph.put("killer", "Zombie");
        ph.put("killer_weapon", "IRON_SWORD");
        ph.put("inventory", "1x DIAMOND_SWORD");
        ph.put("armor", "1x DIAMOND_CHESTPLATE");
        ph.put("to_x", "150");
        ph.put("to_y", "70");
        ph.put("to_z", "-150");
        ph.put("oldX", "100");
        ph.put("oldY", "64");
        ph.put("oldZ", "-200");
        ph.put("newX", "150");
        ph.put("newY", "70");
        ph.put("newZ", "-150");
        ph.put("block", "DIAMOND_BLOCK");
        ph.put("block_x", "102");
        ph.put("block_y", "64");
        ph.put("block_z", "-198");
        ph.put("bucket", "LAVA_BUCKET");
        ph.put("bucket_x", "103");
        ph.put("bucket_y", "64");
        ph.put("bucket_z", "-197");
        ph.put("action", "RENAMED");
        ph.put("item_type", "DIAMOND_SWORD");
        ph.put("old_name", "Diamond Sword");
        ph.put("new_name", "Excalibur");
        ph.put("renamed", "Excalibur");
        ph.put("anvil_x", "104");
        ph.put("anvil_y", "64");
        ph.put("anvil_z", "-196");
        ph.put("amount", "64");
        ph.put("item", "NETHERITE_INGOT");
        ph.put("has_nbt", "true");
        ph.put("nbt_data", "{CustomModelData:1}");
        ph.put("enchants", "SHARPNESS 5");
        ph.put("enchanting_table_x", "105");
        ph.put("enchanting_table_y", "64");
        ph.put("enchanting_table_z", "-195");
        ph.put("page_count", "3");
        ph.put("first_page_snippet", "Once upon a time...");
        ph.put("book_title", "Chronicles");
        ph.put("author", "TestPlayer");
        ph.put("furnace_x", "106");
        ph.put("furnace_y", "64");
        ph.put("furnace_z", "-194");
        ph.put("game-mode", "CREATIVE");
        ph.put("item_amount", "4");
        ph.put("tnt_location_x", "107");
        ph.put("tnt_location_y", "64");
        ph.put("tnt_location_z", "-193");
        ph.put("container", "CHEST");
        ph.put("container_x", "108");
        ph.put("container_y", "64");
        ph.put("container_z", "-192");
        ph.put("original", "1x APPLE");
        ph.put("modified", "2x APPLE");
        ph.put("removed_items", "None");
        ph.put("added_items", "1x APPLE");
        ph.put("entity_type", "CREEPER");
        ph.put("entity_x", "109");
        ph.put("entity_y", "64");
        ph.put("entity_z", "-191");
        ph.put("mob_type", "VILLAGER");
        ph.put("mob_x", "110");
        ph.put("mob_y", "64");
        ph.put("mob_z", "-190");
        ph.put("advancement", "minecraft:story/mine_diamond");
        ph.put("logname", "OAK_LOG");
        ph.put("block_before", "OAK_LOG");
        ph.put("block_after", "STRIPPED_OAK_LOG");
        ph.put("used_tool", "DIAMOND_AXE");
        ph.put("lever_state", "ON");
        ph.put("lever_x", "111");
        ph.put("lever_y", "64");
        ph.put("lever_z", "-189");
        ph.put("villager_profession", "LIBRARIAN");
        ph.put("villager_level", "5");
        ph.put("cost_1", "16x EMERALD");
        ph.put("cost_2", "1x BOOK");
        ph.put("result", "1x ENCHANTED_BOOK");
        ph.put("input", "1x GOLD_INGOT");
        ph.put("output", "4x ENDER_PEARL");
        ph.put("charges", "4");
        ph.put("horse_type", "HORSE");
        ph.put("horse_uuid", "87654321-4321-4321-4321-ba0987654321");
        ph.put("horse_name", "Roach");
        ph.put("owner_uuid", "12345678-1234-1234-1234-1234567890ab");
        ph.put("owner", "TestPlayer");
        ph.put("tamed", "true");
        ph.put("saddle", "true");
        ph.put("details", "MOUNT HORSE");
        ph.put("victim", "Warden");
        ph.put("fall_distance", "12.5");
        ph.put("damage", "45.0");
        ph.put("vault_x", "112");
        ph.put("vault_y", "64");
        ph.put("vault_z", "-188");
        ph.put("items", "1x HEAVY_CORE");
        ph.put("total", "16384");
        ph.put("used", "8192");
        ph.put("free", "8192");
        ph.put("tps", "19.95");
        ph.put("log", "Manual audit entry");
        ph.put("sender", "RconAdmin");
        ph.put("executor", "AdminPlayer");
        ph.put("target", "NewPlayer");
        return ph;
    }

    @Test
    public void testAllLogTypesExistInSpigotConfigYml() {
        File configFile = new File("src/main/resources/config.yml");
        assertTrue(configFile.exists(), "Spigot config.yml must exist");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        for (LogType type : LogType.values()) {
            assertTrue(config.contains(type.getConfigPath()),
                    "config.yml is missing path '" + type.getConfigPath() + "' for LogType." + type.name());
        }
    }

    @Test
    public void testAllElevenLanguageFilesFormatEveryLogTypeWithoutUnreplacedPlaceholders(@TempDir Path tempDir) throws Exception {
        Map<String, String> placeholders = buildCompletePlaceholders();
        Pattern unreplacedPattern = Pattern.compile("%[a-zA-Z0-9_-]+%");

        File messagesDir = new File(tempDir.toFile(), "messages");
        assertTrue(messagesDir.mkdirs());
        for (String lang : LANGUAGES) {
            File src = new File("src/main/resources/messages/" + lang + ".yml");
            assertTrue(src.exists(), "Language file missing: " + src.getAbsolutePath());
            Files.copy(src.toPath(), new File(messagesDir, lang + ".yml").toPath());
        }

        for (String lang : LANGUAGES) {
            LoggerAPI plugin = mock(LoggerAPI.class);
            Data data = mock(Data.class);
            when(plugin.getDataFolder()).thenReturn(tempDir.toFile());
            when(plugin.getData()).thenReturn(data);
            when(data.getLanguage()).thenReturn(lang);
            when(data.getTimeFormatter()).thenReturn("2026-10-28 12:00:00");

            MessageManager mm = new MessageManager(plugin);

            for (LogType type : LogType.values()) {
                if (type == LogType.STAFF) continue;

                for (char channel : new char[]{'F', 'D'}) {
                    for (boolean isStaff : new boolean[]{false, true}) {
                        String formatted = mm.getMessage(type, isStaff, placeholders, channel, null);
                        assertNotNull(formatted);
                        assertFalse(formatted.startsWith("Message not found:"),
                                lang + " missing template for " + type + " (channel=" + channel + ", staff=" + isStaff + "): " + formatted);
                        assertFalse(formatted.startsWith("[File] Event:") || formatted.startsWith("[Discord] Event:"),
                                lang + " fell back to default template for " + type + " (channel=" + channel + ", staff=" + isStaff + "): " + formatted);

                        Matcher m = unreplacedPattern.matcher(formatted);
                        assertFalse(m.find(),
                                lang + " has unreplaced placeholder '" + (m.matches() ? m.group() : "") + "' in "
                                        + type + " (channel=" + channel + ", staff=" + isStaff + "): " + formatted);
                    }
                }
            }
        }
    }

    @Test
    public void testAllFiftyLogTypesInsertIntoSQLiteDatabaseCleanly(@TempDir Path tempDir) throws Exception {
        LoggerPlatform platform = mock(LoggerPlatform.class);
        when(platform.getDataFolder()).thenReturn(tempDir.toFile());
        when(platform.isDebug()).thenReturn(true);

        DatabaseConfig dbConfig = new DatabaseConfig(
                true, "sqlite", "localhost", 3306, "logger", "root", "", "", 30
        );
        DatabaseManager dbManager = new DatabaseManager(platform, dbConfig);
        dbManager.initialize();
        assertTrue(dbManager.isEnabled(), "SQLite DatabaseManager should be enabled");

        LoggerAPI plugin = mock(LoggerAPI.class);
        Data data = mock(Data.class);
        when(plugin.getDatabaseManager()).thenReturn(dbManager);
        when(plugin.getData()).thenReturn(data);
        when(data.getServerName()).thenReturn("survival-1");

        Player player = mock(Player.class);
        World world = mock(World.class);
        when(world.getName()).thenReturn("world");
        Location loc = new Location(world, 100.0, 64.0, -200.0);
        when(player.getUniqueId()).thenReturn(UUID.fromString("12345678-1234-1234-1234-1234567890ab"));
        when(player.getName()).thenReturn("TestPlayer");
        when(player.getWorld()).thenReturn(world);
        when(player.getLocation()).thenReturn(loc);
        when(player.hasPermission(anyString())).thenReturn(false);

        Map<String, String> placeholders = buildCompletePlaceholders();

        int executedLoggers = 0;
        for (LogType type : LogType.values()) {
            if (type == LogType.STAFF) continue;

            Object logger = DatabaseLoggerFactory.createLogger(type, plugin);
            assertNotNull(logger, "DatabaseLoggerFactory returned null for " + type);

            if (logger instanceof DatabasePlayerLogger) {
                ((DatabasePlayerLogger) logger).logEvent(player, placeholders);
                executedLoggers++;
            } else if (logger instanceof DatabaseServerLogger) {
                ((DatabaseServerLogger) logger).logEvent(placeholders);
                executedLoggers++;
            } else {
                fail("Unknown logger type for " + type);
            }
        }

        assertEquals(51, executedLoggers, "All 51 non-STAFF LogType values must have been executed");

        // Also test server_status update and proxy unified tables (player_events, server_events)
        dbManager.updateServerStatus("survival-1", "1.8.6", true);

        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("INSERT INTO player_events (server_name, player_name, event_type, message) VALUES ('proxy', 'TestPlayer', 'Player-Switch', 'Switched from hub to survival-1')");
            stmt.executeUpdate("INSERT INTO server_events (server_name, event_type, message) VALUES ('proxy', 'Server-Side.Start', 'Proxy started')");
        }

        // Shutdown flushes all queued tasks synchronously
        dbManager.shutdown();

        // Re-open SQLite file directly and verify every single table has rows
        try (Connection verifyConn = java.sql.DriverManager.getConnection("jdbc:sqlite:" + tempDir.resolve("logs.db").toAbsolutePath());
             Statement stmt = verifyConn.createStatement()) {

            for (String table : dbManager.getPlayerTableNames()) {
                if (table.equals("player_server_switch")) {
                    // Proxy switch uses player_events or player_server_switch
                    continue;
                }
                try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM " + table)) {
                    assertTrue(rs.next());
                    int count = rs.getInt(1);
                    assertTrue(count >= 1, "Expected at least 1 row in table '" + table + "' after running all loggers, found " + count);
                }
            }
        }
    }

    @Test
    @SuppressWarnings({"unchecked", "deprecation"})
    public void testSpigotEventListenersPopulateExpectedPlaceholders() {
        LoggerAPI plugin = mock(LoggerAPI.class);
        Data data = mock(Data.class);
        LoggerManager loggerManager = mock(LoggerManager.class);
        when(plugin.getData()).thenReturn(data);
        when(plugin.getLoggerManager()).thenReturn(loggerManager);
        when(data.isEnabled(any())).thenReturn(true);
        when(data.isShowIP()).thenReturn(true);
        when(data.getTimeFormatter()).thenReturn("2026-10-28 12:00:00");
        when(data.getEntitiesToLog()).thenReturn(Collections.singletonList("CREEPER"));

        Player player = mock(Player.class);
        World world = mock(World.class);
        when(world.getName()).thenReturn("world");
        Location fromLoc = new Location(world, 10, 64, 20);
        Location toLoc = new Location(world, 50, 70, 80);
        when(player.getUniqueId()).thenReturn(UUID.randomUUID());
        when(player.getName()).thenReturn("TestPlayer");
        when(player.getWorld()).thenReturn(world);
        when(player.getLocation()).thenReturn(fromLoc);
        when(player.hasPermission(anyString())).thenReturn(false);

        // 1. WoodStripListener
        PlayerInventory inv = mock(PlayerInventory.class);
        ItemStack diamondAxe = mock(ItemStack.class);
        when(diamondAxe.getType()).thenReturn(Material.DIAMOND_AXE);
        when(player.getInventory()).thenReturn(inv);
        when(inv.getItemInMainHand()).thenReturn(diamondAxe);
        Block oakLog = mock(Block.class);
        when(oakLog.getType()).thenReturn(Material.OAK_LOG);
        when(oakLog.getLocation()).thenReturn(fromLoc);
        when(oakLog.getX()).thenReturn(12);
        when(oakLog.getY()).thenReturn(65);
        when(oakLog.getZ()).thenReturn(22);

        WoodStripListener woodListener = new WoodStripListener(plugin);
        PlayerInteractEvent interactEvent = new PlayerInteractEvent(player, Action.RIGHT_CLICK_BLOCK, diamondAxe, oakLog, org.bukkit.block.BlockFace.UP);
        woodListener.onWoodStripped(interactEvent);

        ArgumentCaptor<Map<String, String>> mapCaptor = ArgumentCaptor.forClass(Map.class);
        verify(loggerManager).logEvent(eq(LogType.PLAYER_WOOD_STRIP), eq(player), mapCaptor.capture());
        Map<String, String> woodPh = mapCaptor.getValue();
        assertEquals("OAK_LOG", woodPh.get("logname"));
        assertEquals("OAK_LOG", woodPh.get("block_before"));
        assertEquals("STRIPPED_OAK_LOG", woodPh.get("block_after"));
        assertEquals("DIAMOND_AXE", woodPh.get("used_tool"));
        // Verify WoodStripListener did NOT manually mutate the block type
        verify(oakLog, never()).setType(any());

        // 2. TeleportListener
        TeleportListener tpListener = new TeleportListener(plugin);
        PlayerTeleportEvent tpEvent = new PlayerTeleportEvent(player, fromLoc, toLoc, PlayerTeleportEvent.TeleportCause.ENDER_PEARL);
        tpListener.onPlayerTeleport(tpEvent);

        verify(loggerManager).logEvent(eq(LogType.PLAYER_TELEPORT), eq(player), mapCaptor.capture());
        Map<String, String> tpPh = mapCaptor.getValue();
        assertEquals("50", tpPh.get("to_x"));
        assertEquals("50", tpPh.get("newX"));
        assertEquals("10", tpPh.get("oldX"));
        assertEquals("ENDER_PEARL", tpPh.get("cause"));

        // 3. SignInteractionListener
        SignInteractionListener signListener = new SignInteractionListener(plugin);
        SignChangeEvent signEvent = mock(SignChangeEvent.class);
        when(signEvent.isCancelled()).thenReturn(false);
        when(signEvent.getPlayer()).thenReturn(player);
        when(signEvent.getBlock()).thenReturn(oakLog);
        when(signEvent.getLines()).thenReturn(new String[]{"Hello", "World", "Line3", "Line4"});
        signListener.onSignChange(signEvent);

        verify(loggerManager).logEvent(eq(LogType.PLAYER_SIGN_INTERACTION), eq(player), mapCaptor.capture());
        Map<String, String> signPh = mapCaptor.getValue();
        assertEquals("Hello", signPh.get("line1"));
        assertEquals("World", signPh.get("line2"));
        assertTrue(signPh.get("lines").contains("Hello"));

        // 4. JoinListener (including first-join PLAYER_REGISTRATION)
        when(player.hasPlayedBefore()).thenReturn(false);
        JoinListener joinListener = new JoinListener(plugin);
        PlayerJoinEvent joinEvent = mock(PlayerJoinEvent.class);
        when(joinEvent.getPlayer()).thenReturn(player);
        joinListener.onPlayerJoin(joinEvent);

        verify(loggerManager).logEvent(eq(LogType.PLAYER_REGISTRATION), eq(player), mapCaptor.capture());
        assertNotNull(mapCaptor.getValue(), "PLAYER_REGISTRATION placeholders must not be null");
        assertNotNull(mapCaptor.getValue().get("date"), "PLAYER_REGISTRATION must populate 'date' placeholder");
        verify(loggerManager).logEvent(eq(LogType.PLAYER_JOIN), eq(player), any());

        // 5. EntityDeathListener
        LivingEntity creeper = mock(LivingEntity.class);
        when(creeper.getKiller()).thenReturn(player);
        when(creeper.getLocation()).thenReturn(toLoc);
        EntityDeathEvent edEvent = mock(EntityDeathEvent.class);
        when(edEvent.isCancelled()).thenReturn(false);
        when(edEvent.getEntity()).thenReturn(creeper);
        when(edEvent.getEntityType()).thenReturn(EntityType.CREEPER);
        EntityDeathListener edListener = new EntityDeathListener(plugin);
        edListener.onEntityDeath(edEvent);

        verify(loggerManager).logEvent(eq(LogType.PLAYER_ENTITY_DEATH), eq(player), mapCaptor.capture());
        assertEquals("CREEPER", mapCaptor.getValue().get("entity_type"));
    }

    @Test
    public void testWebPanelLogsPhpAndDataPhpMatchDatabaseManagerSchema() throws Exception {
        File logsPhpFile = new File("../logger-web-client/api/v1/logs.php");
        File dataPhpFile = new File("../logger-web-client/api/v1/data.php");
        org.junit.jupiter.api.Assumptions.assumeTrue(
                logsPhpFile.exists() && dataPhpFile.exists(),
                "Skipping private Web Panel schema check when logger-web-client is not present");

        String logsPhp = Files.readString(logsPhpFile.toPath());
        String dataPhp = Files.readString(dataPhpFile.toPath());

        LoggerPlatform platform = mock(LoggerPlatform.class);
        DatabaseConfig dbConfig = new DatabaseConfig(false, "sqlite", "", 3306, "", "", "", "", 30);
        DatabaseManager dbManager = new DatabaseManager(platform, dbConfig);

        for (String table : dbManager.getPlayerTableNames()) {
            assertTrue(dataPhp.contains("'" + table + "'"),
                    "data.php repair_schema is missing table definition for: " + table);
            if (!table.equals("server_status") && !table.equals("server_player_count")) {
                assertTrue(logsPhp.contains("'" + table + "'"),
                        "logs.php is missing query definition for table: " + table);
            }
        }
    }
}
