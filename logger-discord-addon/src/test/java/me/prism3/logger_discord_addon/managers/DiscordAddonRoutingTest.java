package me.prism3.logger_discord_addon.managers;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DiscordAddonRoutingTest {

    @Test
    @SuppressWarnings("unchecked")
    public void testDiscordYmlResourceContainsAllModernEvents() throws Exception {
        File discordYml = new File("src/main/resources/discord.yml");
        assertTrue(discordYml.exists(), "discord.yml must exist");

        try (FileInputStream in = new FileInputStream(discordYml)) {
            Map<String, Object> root = new Yaml().load(in);
            assertNotNull(root);
            Map<String, Object> discord = (Map<String, Object>) root.get("Discord");
            assertNotNull(discord);

            String[] requiredSections = {
                    "Staff", "Player-Chat", "Player-Command", "Player-Join", "Player-Leave",
                    "Player-Kick", "Player-Death", "Player-Teleport", "Player-Level",
                    "Block-Place", "Block-Break", "Bucket-Fill", "Bucket-Empty", "Anvil",
                    "Creative-Item", "Item-Pickup", "Item-Drop", "Enchanting", "Book-Editing",
                    "Furnace", "Game-Mode", "Craft", "Registration", "Primed-TNT",
                    "Chest-Interaction", "Entity-Death", "Spawn-Egg", "Portal-Creation",
                    "Advancement", "Lever-Interaction", "Villager-Trade", "Piglin-Barter",
                    "Respawn-Anchor", "Horse-Interaction", "Crafter-Craft", "Sculk-Shrieker",
                    "Mace-Smash", "Vault-Unlock"
            };
            for (String sec : requiredSections) {
                assertTrue(discord.containsKey(sec), "discord.yml is missing section: Discord." + sec);
            }
        }
    }

    @Test
    public void testChannelAndWebhookRoutingIsolationAndStaffPriority(@TempDir Path tempDir) throws Exception {
        String yamlContent =
                "Discord:\n" +
                "  Enabled: true\n" +
                "  Mode: WEBHOOK\n" +
                "  Staff:\n" +
                "    Enable: true\n" +
                "    Channel-ID: \"999999999999999999\"\n" +
                "    Webhook: \"https://discord.com/api/webhooks/999999999999999999/stafftoken\"\n" +
                "  Player-Chat:\n" +
                "    Enable: true\n" +
                "    Channel-ID: \"111111111111111111\"\n" +
                "    Webhook: \"https://discord.com/api/webhooks/111111111111111111/chattoken\"\n" +
                "  Player-Join:\n" +
                "    Enable: true\n" +
                "    Channel-ID: \"CHANNEL_ID\"\n" +
                "    Webhook: \"WEBHOOK_URL\"\n" +
                "  Block-Place:\n" +
                "    Enable: false\n" +
                "    Channel-ID: \"222222222222222222\"\n" +
                "    Webhook: \"https://discord.com/api/webhooks/222222222222222222/blocktoken\"\n" +
                "  Mace-Smash:\n" +
                "    Enable: true\n" +
                "    Channel-ID: \"333333333333333333\"\n" +
                "    Webhook: \"https://discord.com/api/webhooks/333333333333333333/macetoken\"\n";

        File cfgFile = tempDir.resolve("discord.yml").toFile();
        Files.writeString(cfgFile.toPath(), yamlContent);

        DiscordManager manager = new DiscordManager(cfgFile);
        manager.init();

        Method resolveWebhookForEvent = DiscordManager.class.getDeclaredMethod("resolveWebhookForEvent", String.class, String.class);
        resolveWebhookForEvent.setAccessible(true);

        // 1. Configured event routes to its own webhook
        assertEquals("https://discord.com/api/webhooks/111111111111111111/chattoken",
                resolveWebhookForEvent.invoke(manager, "PLAYER_CHAT", null));
        assertEquals("https://discord.com/api/webhooks/333333333333333333/macetoken",
                resolveWebhookForEvent.invoke(manager, "PLAYER_MACE_SMASH", null));

        // 2. Unconfigured event (CHANNEL_ID / WEBHOOK_URL) MUST return null, NOT fall back to Player-Chat!
        assertNull(resolveWebhookForEvent.invoke(manager, "PLAYER_JOIN", null),
                "Unconfigured event must NOT fall back to a single configured channel/webhook");

        // 3. Disabled event (Enable: false) MUST return null even if a valid URL is present
        assertNull(resolveWebhookForEvent.invoke(manager, "PLAYER_BLOCK_PLACE", null),
                "Event with Enable: false must not route to Discord");

        // 4. Staff context ("STAFF") prioritizes Staff webhook over specific event webhook
        assertEquals("https://discord.com/api/webhooks/999999999999999999/stafftoken",
                resolveWebhookForEvent.invoke(manager, "PLAYER_CHAT", "STAFF"),
                "Staff events must prioritize the Staff webhook when configured");

        // 5. Test JDA Bot channel caching & resolution as well
        JDA mockJda = mock(JDA.class);
        TextChannel chatChannel = mock(TextChannel.class);
        TextChannel staffChannel = mock(TextChannel.class);
        TextChannel blockChannel = mock(TextChannel.class);
        when(mockJda.getTextChannelById("111111111111111111")).thenReturn(chatChannel);
        when(mockJda.getTextChannelById("999999999999999999")).thenReturn(staffChannel);
        when(mockJda.getTextChannelById("222222222222222222")).thenReturn(blockChannel);

        Field jdaField = DiscordManager.class.getDeclaredField("jda");
        jdaField.setAccessible(true);
        jdaField.set(manager, mockJda);

        Field configDataField = DiscordManager.class.getDeclaredField("configData");
        configDataField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> root = (Map<String, Object>) configDataField.get(manager);
        @SuppressWarnings("unchecked")
        Map<String, Object> discordSec = (Map<String, Object>) root.get("Discord");

        Method cacheChannels = DiscordManager.class.getDeclaredMethod("cacheChannels", Map.class);
        cacheChannels.setAccessible(true);
        cacheChannels.invoke(manager, discordSec);

        Method resolveChannelForEvent = DiscordManager.class.getDeclaredMethod("resolveChannelForEvent", String.class, String.class);
        resolveChannelForEvent.setAccessible(true);

        assertSame(chatChannel, resolveChannelForEvent.invoke(manager, "PLAYER_CHAT", null));
        assertNull(resolveChannelForEvent.invoke(manager, "PLAYER_JOIN", null),
                "Unconfigured event must NOT fall back to single configured bot channel");
        assertNull(resolveChannelForEvent.invoke(manager, "PLAYER_BLOCK_PLACE", null),
                "Disabled event (Enable: false) must NOT be cached or resolved in bot mode");
        assertSame(staffChannel, resolveChannelForEvent.invoke(manager, "PLAYER_CHAT", "STAFF"),
                "Staff event must prioritize Staff channel in bot mode");

        manager.shutdown();
    }
}
