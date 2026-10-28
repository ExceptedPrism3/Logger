package me.prism3.loggervelocity;

import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import me.prism3.loggervelocity.events.OnSwitch;
import me.prism3.loggervelocity.managers.LogManager;
import me.prism3.loggervelocity.utils.Data;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.lang.reflect.Field;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoggerVelocityTest {

    @Test
    @SuppressWarnings("unchecked")
    public void testVelocityOnSwitchUsesPlayerSwitchKeyAndPopulatesFromToPlaceholders() throws Exception {
        Logger mockLogger = mock(Logger.class);
        LogManager mockLogManager = mock(LogManager.class);
        when(mockLogger.getLogManager()).thenReturn(mockLogManager);

        Field instanceField = Logger.class.getDeclaredField("instance");
        instanceField.setAccessible(true);
        instanceField.set(null, mockLogger);

        Data.isServerSwitch = true;
        Data.isStaffEnabled = true;
        Data.loggerStaffLog = "logger.staff.log";
        Data.loggerExempt = "logger.exempt";
        Data.dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        Player player = mock(Player.class);
        when(player.getUsername()).thenReturn("VelocityPlayer");
        when(player.hasPermission(anyString())).thenReturn(false);

        RegisteredServer toReg = mock(RegisteredServer.class);
        ServerInfo toInfo = new ServerInfo("minigames", new java.net.InetSocketAddress(25566));
        when(toReg.getServerInfo()).thenReturn(toInfo);

        RegisteredServer fromReg = mock(RegisteredServer.class);
        ServerInfo fromInfo = new ServerInfo("hub", new java.net.InetSocketAddress(25565));
        when(fromReg.getServerInfo()).thenReturn(fromInfo);

        OnSwitch onSwitch = new OnSwitch();

        // 1. Initial connection (previousServer empty) should be ignored
        ServerConnectedEvent initialEvent = mock(ServerConnectedEvent.class);
        when(initialEvent.getPlayer()).thenReturn(player);
        when(initialEvent.getServer()).thenReturn(toReg);
        when(initialEvent.getPreviousServer()).thenReturn(Optional.empty());
        onSwitch.onSwitch(initialEvent);
        verify(mockLogManager, never()).logPlayerEvent(anyString(), any(), any(), anyBoolean());

        // 2. Server switch (hub -> minigames)
        ServerConnectedEvent switchEvent = mock(ServerConnectedEvent.class);
        when(switchEvent.getPlayer()).thenReturn(player);
        when(switchEvent.getServer()).thenReturn(toReg);
        when(switchEvent.getPreviousServer()).thenReturn(Optional.of(fromReg));
        onSwitch.onSwitch(switchEvent);

        ArgumentCaptor<Map<String, String>> phCaptor = ArgumentCaptor.forClass(Map.class);
        verify(mockLogManager).logPlayerEvent(eq("Player-Switch"), eq(player), phCaptor.capture(), eq(false));
        Map<String, String> ph = phCaptor.getValue();
        assertEquals("hub", ph.get("from"));
        assertEquals("minigames", ph.get("to"));
        assertEquals("hub", ph.get("fromServer"));
        assertEquals("minigames", ph.get("toServer"));
    }
}
