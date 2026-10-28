package me.prism3.logger_bungee;

import me.prism3.logger_bungee.listeners.player.PlayerSwitchListener;
import me.prism3.logger_bungee.managers.ConfigManager;
import me.prism3.logger_bungee.managers.LogManager;
import me.prism3.logger_bungee.utils.Constants;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoggerBungeeTest {

    @Test
    @SuppressWarnings("unchecked")
    public void testPlayerSwitchListenerSkipsInitialJoinAndPopulatesFromToPlaceholders() {
        LoggerBungee plugin = mock(LoggerBungee.class);
        ConfigManager configManager = mock(ConfigManager.class);
        LogManager logManager = mock(LogManager.class);
        Configuration config = mock(Configuration.class);

        me.prism3.logger_bungee.managers.PermissionManager permManager = mock(me.prism3.logger_bungee.managers.PermissionManager.class);
        when(plugin.getConfigManager()).thenReturn(configManager);
        when(plugin.getLogManager()).thenReturn(logManager);
        when(plugin.getPermissionManager()).thenReturn(permManager);
        when(configManager.getConfig()).thenReturn(config);
        when(config.getBoolean(eq("Log-Player.Player-Switch.Enable"), anyBoolean())).thenReturn(true);
        when(config.getBoolean(eq("Player-Login.Player-IP"), anyBoolean())).thenReturn(false);
        when(config.getBoolean(eq("Staff.Enabled"), anyBoolean())).thenReturn(true);

        ProxiedPlayer player = mock(ProxiedPlayer.class);
        Server currentServer = mock(Server.class);
        ServerInfo toInfo = mock(ServerInfo.class);
        ServerInfo fromInfo = mock(ServerInfo.class);

        when(toInfo.getName()).thenReturn("survival");
        when(fromInfo.getName()).thenReturn("lobby");
        when(currentServer.getInfo()).thenReturn(toInfo);
        when(player.getName()).thenReturn("ProxyPlayer");
        when(player.getUniqueId()).thenReturn(UUID.randomUUID());
        when(player.getServer()).thenReturn(currentServer);
        when(player.getAddress()).thenReturn(new InetSocketAddress("192.168.1.50", 25565));
        when(permManager.isExempt(player)).thenReturn(false);
        when(permManager.isStaff(player)).thenReturn(false);

        PlayerSwitchListener listener = new PlayerSwitchListener(plugin);

        // 1. Initial join (from == null) MUST be ignored
        ServerSwitchEvent initialJoin = new ServerSwitchEvent(player, null);
        listener.onServerSwitch(initialJoin);
        verify(logManager, never()).logPlayerEvent(any(), any(), any(), anyBoolean());

        // 2. Actual server switch (from == lobby -> to == survival)
        ServerSwitchEvent switchEvent = new ServerSwitchEvent(player, fromInfo);
        listener.onServerSwitch(switchEvent);

        ArgumentCaptor<Map<String, String>> phCaptor = ArgumentCaptor.forClass(Map.class);
        verify(logManager).logPlayerEvent(eq(Constants.Events.PLAYER_SWITCH), eq(player), phCaptor.capture(), eq(false));
        Map<String, String> ph = phCaptor.getValue();

        assertEquals("lobby", ph.get("from"));
        assertEquals("survival", ph.get("to"));
        assertEquals("lobby", ph.get("fromServer"));
        assertEquals("survival", ph.get("toServer"));
        assertEquals("", ph.get("IP"), "IP must be masked when Player-Login.Player-IP is false");
    }

    @Test
    public void testBungeeAllTenMessageFilesContainSwitchAndKickPlaceholders() throws Exception {
        String[] langs = {"ar_SA", "de_DE", "en_US", "es_ES", "fr_FR", "it_IT", "ja_JP", "ko_KR", "pt_BR", "ru_RU", "zh_CN"};
        for (String lang : langs) {
            File f = new File("src/main/resources/bungee-messages/" + lang + ".yml");
            assertTrue(f.exists(), "Missing Bungee messages file: " + f.getPath());
            Configuration cfg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(f);
            assertNotNull(cfg.getString("File.Player-Switch"), lang + " missing File.Player-Switch");
            assertNotNull(cfg.getString("Discord.Player-Switch"), lang + " missing Discord.Player-Switch");
            assertNotNull(cfg.getString("File.Player-Kick"), lang + " missing File.Player-Kick");
            assertNotNull(cfg.getString("Discord.Player-Kick"), lang + " missing Discord.Player-Kick");
        }
    }
}
