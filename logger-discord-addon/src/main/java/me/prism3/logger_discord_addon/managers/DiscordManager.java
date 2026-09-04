package me.prism3.logger_discord_addon.managers;

import me.prism3.logger_core.objects.LogPlayer;
import me.prism3.logger_discord_addon.utils.enums.DiscordChannels;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.yaml.snakeyaml.Yaml;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Pattern;

public class DiscordManager extends ListenerAdapter implements me.prism3.logger_core.discord.DiscordManager {

    private File configFile;
    private Map<String, Object> configData;
    private JDA jda;
    private boolean isEnabled = false;
    private boolean isBotMode = true;

    private final Map<String, TextChannel> channelMap = new ConcurrentHashMap<>();
    private final Map<String, String> webhookMap = new ConcurrentHashMap<>();
    private ScheduledExecutorService activityScheduler;

    private String messageType = "normal";
    private String embedTitle = "Server Notification";
    private Color embedColor = Color.decode("#FF5733");
    private String embedFooter = "Sent by Logger";
    private boolean embedTimestamp = true;

    private static final Pattern MINECRAFT_COLOR_PATTERN = Pattern.compile("(?i)[§&][0-9a-fk-or]");
    private static final Pattern HEX_COLOR_PATTERN = Pattern.compile("(?i)[§&]x([§&][0-9a-f]){6}");

    public DiscordManager(File configFile) {
        this.configFile = configFile;
        this.activityScheduler = Executors.newScheduledThreadPool(1);
    }

    public void init() {
        this.loadYamlConfig();
        if (this.configData == null) {
            this.isEnabled = false;
            return;
        }

        Map<String, Object> discordSection = getSection(this.configData, "Discord");
        if (discordSection == null) {
            this.isEnabled = false;
            return;
        }

        boolean enabled = getBoolean(discordSection, "Enabled", false);
        if (!enabled) {
            this.isEnabled = false;
            System.out.println("[LoggerDiscordAddon] Discord integration is disabled in discord.yml. Addon is idle.");
            return;
        }

        String mode = getString(discordSection, "Mode", "BOT");
        this.isBotMode = "BOT".equalsIgnoreCase(mode);

        this.messageType = getString(this.configData, "Message-Type", "normal");
        Map<String, Object> embedSection = getSection(this.configData, "Embed-Settings");
        if (embedSection != null) {
            this.embedTitle = getString(embedSection, "Title", "Server Notification");
            String colorHex = getString(embedSection, "Color", "#FF5733");
            try {
                this.embedColor = Color.decode(colorHex);
            } catch (Exception e) {
                this.embedColor = Color.decode("#FF5733");
            }
            this.embedFooter = getString(embedSection, "Footer", "Sent by Logger");
            this.embedTimestamp = getBoolean(embedSection, "Timestamp", true);
        }

        if (this.isBotMode) {
            String botToken = getString(discordSection, "Bot-Token", "");
            if (botToken == null || botToken.trim().isEmpty() || botToken.equals("your-bot-token-here") || botToken.equals("BOT_KEY")) {
                this.isEnabled = false;
                System.out.println("[LoggerDiscordAddon] Discord is enabled in discord.yml, but Bot-Token is missing or set to placeholder. Addon is idle until a valid Bot-Token is configured.");
                return;
            }

            try {
                JDABuilder builder = JDABuilder.createDefault(botToken.trim());
                builder.enableIntents(GatewayIntent.GUILD_MESSAGES);
                builder.addEventListeners(this);

                OnlineStatus status = parseStatus(getString(getSection(this.configData, "ActivityCycling"), "Status", "online"));
                builder.setStatus(status);

                List<Activity> activities = parseActivities(getSection(this.configData, "ActivityCycling"));
                if (!activities.isEmpty()) {
                    builder.setActivity(activities.get(0));
                }

                this.jda = builder.build();
                this.jda.awaitReady();
                this.cacheChannels(discordSection);
                this.startActivityCycling(activities);
                this.isEnabled = true;
                System.out.println("[LoggerDiscordAddon] JDA Bot successfully connected and " + this.channelMap.size() + " channel routes cached!");
            } catch (Exception e) {
                System.err.println("[LoggerDiscordAddon] Failed to initialize JDA: " + e.getMessage());
                this.isEnabled = false;
            }
        } else {
            this.initWebhooks(discordSection);
            this.isEnabled = !this.webhookMap.isEmpty();
            if (this.isEnabled) {
                System.out.println("[LoggerDiscordAddon] Discord Webhook mode active with " + this.webhookMap.size() + " route endpoints.");
            } else {
                System.out.println("[LoggerDiscordAddon] Discord is enabled in webhook mode, but no valid webhooks are configured.");
            }
        }
    }

    private void loadYamlConfig() {
        if (this.configFile == null || !this.configFile.exists()) return;
        try (InputStream in = new FileInputStream(this.configFile)) {
            Yaml yaml = new Yaml();
            this.configData = yaml.load(in);
        } catch (Exception e) {
            System.err.println("[LoggerDiscordAddon] Failed to load config " + this.configFile.getName() + ": " + e.getMessage());
        }
    }

    @Override
    public void onReady(ReadyEvent event) {
        if (this.configData != null) {
            cacheChannels(getSection(this.configData, "Discord"));
        }
    }

    private void cacheChannels(Map<String, Object> discordSection) {
        if (this.jda == null || discordSection == null) return;
        this.channelMap.clear();

        collectChannelsRecursively(discordSection, "");
    }

    @SuppressWarnings("unchecked")
    private void collectChannelsRecursively(Map<String, Object> section, String prefix) {
        for (Map.Entry<String, Object> entry : section.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof Map) {
                Map<String, Object> sub = (Map<String, Object>) value;
                String fullKey = prefix.isEmpty() ? key : prefix + "." + key;

                if (sub.containsKey("Channel-ID")) {
                    String channelIdStr = String.valueOf(sub.get("Channel-ID")).trim();
                    if (isValidChannelId(channelIdStr)) {
                        try {
                            TextChannel tc = this.jda.getTextChannelById(channelIdStr);
                            if (tc != null) {
                                registerChannel(fullKey, key, tc);
                            } else {
                                System.err.println("[LoggerDiscordAddon] Channel ID " + channelIdStr + " for '" + fullKey + "' was not found by the bot (check bot permissions and channel existence).");
                            }
                        } catch (Exception e) {
                            System.err.println("[LoggerDiscordAddon] Invalid Channel ID " + channelIdStr + " for '" + fullKey + "': " + e.getMessage());
                        }
                    }
                }

                collectChannelsRecursively(sub, fullKey);
            }
        }
    }

    private boolean isValidChannelId(String id) {
        if (id == null) return false;
        String trimmed = id.trim();
        if (trimmed.isEmpty() || trimmed.equalsIgnoreCase("CHANNEL_ID") || trimmed.equals("0")) {
            return false;
        }
        return trimmed.matches("\\d{16,22}");
    }

    @SuppressWarnings("unchecked")
    private void initWebhooks(Map<String, Object> discordSection) {
        if (discordSection == null) return;
        this.webhookMap.clear();

        collectWebhooksRecursively(discordSection, "");
    }

    @SuppressWarnings("unchecked")
    private void collectWebhooksRecursively(Map<String, Object> section, String prefix) {
        for (Map.Entry<String, Object> entry : section.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof Map) {
                Map<String, Object> sub = (Map<String, Object>) value;
                String fullKey = prefix.isEmpty() ? key : prefix + "." + key;

                if (sub.containsKey("Webhook")) {
                    String webhookUrl = String.valueOf(sub.get("Webhook")).trim();
                    if (isValidWebhookUrl(webhookUrl)) {
                        registerWebhook(fullKey, key, webhookUrl);
                    }
                }

                collectWebhooksRecursively(sub, fullKey);
            }
        }
    }

    private boolean isValidWebhookUrl(String url) {
        if (url == null) return false;
        String trimmed = url.trim();
        if (trimmed.isEmpty() || trimmed.contains("XXXX") || trimmed.contains("YOUR_WEBHOOK")) {
            return false;
        }
        return trimmed.startsWith("https://discord.com/api/webhooks/") || trimmed.startsWith("https://canary.discord.com/api/webhooks/");
    }

    public static String normalizeKey(String key) {
        if (key == null) return "";
        String s = key.toLowerCase(Locale.ROOT).trim();
        if (s.startsWith("discord.")) {
            s = s.substring(8);
        }
        if (s.endsWith(".channel-id")) {
            s = s.substring(0, s.length() - 11);
        } else if (s.endsWith(".webhook")) {
            s = s.substring(0, s.length() - 8);
        }
        if (s.startsWith("server-side.") || s.startsWith("serverside.")) {
            s = s.substring(s.indexOf('.') + 1);
        } else if (s.startsWith("version-exceptions.") || s.startsWith("versionexceptions.")) {
            s = s.substring(s.indexOf('.') + 1);
        } else if (s.startsWith("custom.")) {
            s = s.substring(s.indexOf('.') + 1);
        }
        return s.replaceAll("[^a-z0-9]", "");
    }

    private void registerChannel(String fullKey, String key, TextChannel tc) {
        registerKeyVariants(fullKey, key, norm -> this.channelMap.put(norm, tc));
    }

    private void registerWebhook(String fullKey, String key, String webhookUrl) {
        registerKeyVariants(fullKey, key, norm -> this.webhookMap.put(norm, webhookUrl));
    }

    private void registerKeyVariants(String fullKey, String key, java.util.function.Consumer<String> consumer) {
        String normFull = normalizeKey(fullKey);
        String normShort = normalizeKey(key);

        if (!normFull.isEmpty()) consumer.accept(normFull);
        if (!normShort.isEmpty()) consumer.accept(normShort);

        for (String base : Arrays.asList(normFull, normShort)) {
            if (base.isEmpty()) continue;
            if (base.startsWith("player")) {
                String sub = base.substring(6);
                if (!sub.isEmpty()) consumer.accept(sub);
            } else {
                consumer.accept("player" + base);
            }

            if (base.startsWith("server")) {
                String sub = base.substring(6);
                if (!sub.isEmpty()) consumer.accept(sub);
            } else {
                consumer.accept("server" + base);
            }
        }
    }

    private List<String> getAliases(String norm) {
        List<String> list = new ArrayList<>();
        if (norm == null || norm.isEmpty()) return list;

        if (norm.contains("commandblock")) {
            list.add("commandblock");
            list.add("servercommandblock");
        }
        if (norm.contains("container") || norm.contains("chest")) {
            list.add("chestinteraction");
            list.add("playercontainerinteraction");
            list.add("containerinteraction");
        }
        if (norm.contains("sign")) {
            list.add("playersigntext");
            list.add("signtext");
            list.add("playersigninteraction");
            list.add("signinteraction");
        }
        if (norm.contains("tnt")) {
            list.add("primedtnt");
            list.add("primetnt");
            list.add("playerprimetnt");
        }
        if (norm.contains("craft")) {
            list.add("craft");
            list.add("playercraft");
            list.add("itemcraft");
            list.add("playeritemcraft");
            list.add("craftercraft");
            list.add("playercraftercraft");
        }
        if (norm.contains("wood")) {
            list.add("woodstripping");
            list.add("woodstrip");
            list.add("playerwoodstrip");
        }
        if (norm.contains("totem")) {
            list.add("totemofundying");
            list.add("playertotemofundying");
        }
        if (norm.contains("book")) {
            list.add("bookediting");
            list.add("bookinteraction");
            list.add("playerbookinteraction");
        }
        if (norm.contains("enchant")) {
            list.add("enchanting");
            list.add("itemenchant");
            list.add("playeritemenchanting");
        }
        if (norm.contains("console") || norm.contains("servercommand")) {
            list.add("consolecommand");
            list.add("consolecommands");
            list.add("servercommands");
            list.add("serverconsolecommand");
        }
        if (norm.contains("manual")) {
            list.add("manual");
            list.add("manuallog");
            list.add("servermanuallog");
        }
        if (norm.contains("login") || norm.contains("join")) {
            list.add("playerjoin");
            list.add("join");
            list.add("playerlogin");
            list.add("login");
        }
        if (norm.contains("leave") || norm.contains("quit")) {
            list.add("playerleave");
            list.add("leave");
            list.add("playerquit");
            list.add("quit");
        }
        if (norm.contains("switch")) {
            list.add("playerswitch");
            list.add("switch");
        }
        if (norm.contains("advancement")) {
            list.add("advancement");
            list.add("advancements");
            list.add("playeradvancements");
            list.add("playeradvancement");
        }
        if (norm.contains("anvil")) {
            list.add("anvil");
            list.add("playeranvilinteraction");
        }
        if (norm.contains("furnace")) {
            list.add("furnace");
            list.add("playerfurnaceinteraction");
        }
        if (norm.contains("gamemode")) {
            list.add("gamemode");
            list.add("playergamemode");
        }
        if (norm.contains("itempickup") || norm.contains("pickup")) {
            list.add("itempickup");
            list.add("playeritempickup");
        }
        if (norm.contains("itemdrop") || norm.contains("drop")) {
            list.add("itemdrop");
            list.add("playeritemdrop");
        }
        if (norm.contains("entitydeath")) {
            list.add("entitydeath");
            list.add("playerentitydeath");
        }
        if (norm.contains("spawnegg")) {
            list.add("spawnegg");
            list.add("playerplayerspawnegg");
        }
        if (norm.contains("portal")) {
            list.add("portalcreation");
            list.add("playerportalcreation");
        }
        if (norm.contains("lever")) {
            list.add("leverinteraction");
            list.add("playerleverinteraction");
        }
        if (norm.contains("villager")) {
            list.add("villagertrade");
            list.add("playervillagertrade");
        }
        if (norm.contains("piglin")) {
            list.add("piglinbarter");
            list.add("playerpiglinbarter");
        }
        if (norm.contains("respawnanchor") || norm.contains("anchor")) {
            list.add("respawnanchor");
            list.add("playerrespawnanchor");
        }
        if (norm.contains("sculk")) {
            list.add("sculkshrieker");
            list.add("playersculkshrieker");
        }
        if (norm.contains("commandwhitelisted")) {
            list.add("playercommandwhitelisted");
            list.add("playercommand");
        }

        return list;
    }

    private OnlineStatus parseStatus(String status) {
        if (status == null) return OnlineStatus.ONLINE;
        switch (status.toLowerCase()) {
            case "idle": return OnlineStatus.IDLE;
            case "dnd": return OnlineStatus.DO_NOT_DISTURB;
            case "invisible": return OnlineStatus.INVISIBLE;
            default: return OnlineStatus.ONLINE;
        }
    }

    @SuppressWarnings("unchecked")
    private List<Activity> parseActivities(Map<String, Object> cyclingSection) {
        List<Activity> list = new ArrayList<>();
        if (cyclingSection == null) return list;

        Object activitiesObj = cyclingSection.get("Activities");
        if (activitiesObj instanceof List) {
            for (Object item : (List<?>) activitiesObj) {
                if (item instanceof List) {
                    List<?> pair = (List<?>) item;
                    if (pair.size() >= 2) {
                        String type = String.valueOf(pair.get(0)).toLowerCase();
                        String name = String.valueOf(pair.get(1));
                        switch (type) {
                            case "listening": list.add(Activity.listening(name)); break;
                            case "watching": list.add(Activity.watching(name)); break;
                            case "streaming": list.add(Activity.streaming(name, "https://twitch.tv")); break;
                            default: list.add(Activity.playing(name)); break;
                        }
                    }
                }
            }
        }
        if (list.isEmpty()) {
            list.add(Activity.playing("Minecraft"));
        }
        return list;
    }

    private void startActivityCycling(final List<Activity> activities) {
        Map<String, Object> section = getSection(this.configData, "ActivityCycling");
        if (section == null || !getBoolean(section, "Enabled", false) || activities == null || activities.isEmpty()) {
            return;
        }

        boolean random = getBoolean(section, "Random", false);
        int interval = getInt(section, "Time", 30);
        if (interval < 5) interval = 30;

        if (random) {
            Collections.shuffle(activities);
        }

        this.activityScheduler.scheduleWithFixedDelay(new Runnable() {
            private int index = 1;
            @Override
            public void run() {
                if (jda != null && jda.getStatus() == JDA.Status.CONNECTED && !activities.isEmpty()) {
                    Activity act = activities.get(index % activities.size());
                    jda.getPresence().setActivity(act);
                    index++;
                }
            }
        }, interval, interval, TimeUnit.SECONDS);
    }

    public void sendMessage(DiscordChannels channel, String message) {
        if (channel != null) {
            sendMessage(channel.name(), message);
        }
    }

    @Override
    public void sendMessage(String type, String message) {
        sendMessage(type, message, null, null);
    }

    @Override
    public void sendMessage(String type, String message, LogPlayer player, String logType) {
        if (!isEnabled()) return;
        if (message == null || message.trim().isEmpty()) return;

        boolean isStopEvent = (type != null && (type.contains("Stop") || type.contains("STOP") || type.contains("stop")))
                || (logType != null && (logType.contains("Stop") || logType.contains("STOP") || logType.contains("stop")));

        String cleanMessage = stripMinecraftColors(message);

        if (this.isBotMode) {
            TextChannel channel = resolveChannel(type);
            if (channel == null && logType != null && !logType.equalsIgnoreCase(type)) {
                channel = resolveChannel(logType);
            }
            if (channel == null) {
                // Channel is not configured or left as default placeholder. Do not send!
                return;
            }

            if ("embed".equalsIgnoreCase(this.messageType)) {
                EmbedBuilder eb = new EmbedBuilder();
                String title = getEventTitle(type, logType);
                eb.setTitle(truncate(title, 256));
                eb.setDescription(truncate(cleanMessage, 4096));
                eb.setColor(this.embedColor);
                if (player != null && player.getUniqueId() != null) {
                    String avatar = "https://minotar.net/avatar/" + player.getUniqueId() + "/100.png";
                    eb.setThumbnail(avatar);
                    eb.setAuthor(player.getName(), null, avatar);
                }
                if (this.embedFooter != null && !this.embedFooter.isEmpty()) {
                    eb.setFooter(truncate(this.embedFooter, 2048));
                }
                if (this.embedTimestamp) {
                    eb.setTimestamp(Instant.now());
                }
                if (isStopEvent) {
                    try {
                        channel.sendMessageEmbeds(eb.build()).complete();
                    } catch (Exception ignored) {}
                } else {
                    channel.sendMessageEmbeds(eb.build()).queue(null, e -> {});
                }
            } else {
                String payload = truncate(cleanMessage, 2000);
                if (isStopEvent) {
                    try {
                        channel.sendMessage(payload).complete();
                    } catch (Exception ignored) {}
                } else {
                    channel.sendMessage(payload).queue(null, e -> {});
                }
            }
        } else {
            String webhookUrl = resolveWebhook(type);
            if (webhookUrl == null && logType != null && !logType.equalsIgnoreCase(type)) {
                webhookUrl = resolveWebhook(logType);
            }
            if (webhookUrl == null) {
                // Webhook is not configured or left as placeholder. Do not send!
                return;
            }

            String payloadJson = buildWebhookPayload(type, logType, cleanMessage, player);
            if (isStopEvent) {
                sendWebhookSync(webhookUrl, payloadJson);
            } else {
                sendWebhookAsync(webhookUrl, payloadJson);
            }
        }
    }

    private TextChannel resolveChannel(String type) {
        if (type == null || type.trim().isEmpty()) return null;

        String norm = normalizeKey(type);
        TextChannel tc = this.channelMap.get(norm);
        if (tc != null) return tc;

        for (String alias : getAliases(norm)) {
            tc = this.channelMap.get(alias);
            if (tc != null) return tc;
        }

        tc = this.channelMap.get(type);
        if (tc != null) return tc;

        return null;
    }

    private String resolveWebhook(String type) {
        if (type == null || type.trim().isEmpty()) return null;

        String norm = normalizeKey(type);
        String url = this.webhookMap.get(norm);
        if (url != null) return url;

        for (String alias : getAliases(norm)) {
            url = this.webhookMap.get(alias);
            if (url != null) return url;
        }

        url = this.webhookMap.get(type);
        if (url != null) return url;

        return null;
    }

    private String getEventTitle(String type, String logType) {
        if (this.embedTitle != null && !this.embedTitle.isEmpty() && !this.embedTitle.equals("Server Notification")) {
            return this.embedTitle;
        }

        String target = (type != null && !type.isEmpty()) ? type : logType;
        if (target == null) return "Server Notification";

        String norm = normalizeKey(target);
        if ("staff".equals(norm)) return "Staff Notification";
        if (norm.contains("commandblock")) return "Command Block";
        if (norm.contains("playerchat") || "chat".equals(norm)) return "Player Chat";
        if (norm.contains("playercommand") || "command".equals(norm)) return "Player Command";
        if (norm.contains("playerjoin") || "join".equals(norm) || "login".equals(norm)) return "Player Join";
        if (norm.contains("playerleave") || "leave".equals(norm) || "quit".equals(norm)) return "Player Leave";
        if (norm.contains("playerkick") || "kick".equals(norm)) return "Player Kick";
        if (norm.contains("playerdeath") || "death".equals(norm)) return "Player Death";
        if (norm.contains("teleport")) return "Player Teleport";
        if (norm.contains("blockplace")) return "Block Place";
        if (norm.contains("blockbreak")) return "Block Break";
        if (norm.contains("bucketfill")) return "Bucket Fill";
        if (norm.contains("bucketempty")) return "Bucket Empty";
        if (norm.contains("tnt")) return "Primed TNT";
        if (norm.contains("anvil")) return "Anvil Interaction";
        if (norm.contains("chest") || norm.contains("container")) return "Chest Interaction";
        if (norm.contains("sign")) return "Sign Interaction";
        if (norm.contains("craft")) return "Crafting";
        if (norm.contains("enchant")) return "Enchanting";
        if (norm.contains("furnace")) return "Furnace";
        if (norm.contains("gamemode")) return "GameMode Change";
        if (norm.contains("start")) return "Server Start";
        if (norm.contains("stop")) return "Server Stop";
        if (norm.contains("console") || norm.contains("servercommand")) return "Console Command";
        if (norm.contains("ram")) return "Server RAM";
        if (norm.contains("tps")) return "Server TPS";
        if (norm.contains("rcon")) return "RCON Command";
        if (norm.contains("playercount")) return "Player Count";
        if (norm.contains("manual")) return "Manual Log";

        return this.embedTitle != null && !this.embedTitle.isEmpty() ? this.embedTitle : "Server Notification";
    }

    private String buildWebhookPayload(String type, String logType, String message, LogPlayer player) {
        if ("embed".equalsIgnoreCase(this.messageType)) {
            String title = escapeJson(truncate(getEventTitle(type, logType), 256));
            String desc = escapeJson(truncate(message, 4096));
            int colorDec = this.embedColor.getRGB() & 0xFFFFFF;

            StringBuilder sb = new StringBuilder();
            sb.append("{\"embeds\":[{");
            sb.append("\"title\":\"").append(title).append("\",");
            sb.append("\"description\":\"").append(desc).append("\",");
            sb.append("\"color\":").append(colorDec);

            if (player != null && player.getName() != null) {
                String authorName = escapeJson(player.getName());
                sb.append(",\"author\":{\"name\":\"").append(authorName).append("\"");
                if (player.getUniqueId() != null) {
                    sb.append(",\"icon_url\":\"https://minotar.net/avatar/").append(player.getUniqueId()).append("/100.png\"");
                }
                sb.append("}");
            }

            if (this.embedFooter != null && !this.embedFooter.isEmpty()) {
                sb.append(",\"footer\":{\"text\":\"").append(escapeJson(truncate(this.embedFooter, 2048))).append("\"}");
            }

            if (this.embedTimestamp) {
                sb.append(",\"timestamp\":\"").append(Instant.now().toString()).append("\"");
            }

            sb.append("}]}");
            return sb.toString();
        } else {
            return "{\"content\":\"" + escapeJson(truncate(message, 2000)) + "\"}";
        }
    }

    private static String escapeJson(String s) {
        if (s == null) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '\\': sb.append("\\\\"); break;
                case '"': sb.append("\\\""); break;
                case '\b': sb.append("\\b"); break;
                case '\f': sb.append("\\f"); break;
                case '\n': sb.append("\\n"); break;
                case '\r': sb.append("\\r"); break;
                case '\t': sb.append("\\t"); break;
                default:
                    if (c <= 0x1F) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
                    break;
            }
        }
        return sb.toString();
    }

    public static String stripMinecraftColors(String input) {
        if (input == null) return "";
        String stripped = HEX_COLOR_PATTERN.matcher(input).replaceAll("");
        stripped = MINECRAFT_COLOR_PATTERN.matcher(stripped).replaceAll("");
        return stripped.trim();
    }

    private static String truncate(String input, int maxLen) {
        if (input == null) return "";
        if (input.length() <= maxLen) return input;
        return input.substring(0, Math.max(0, maxLen - 3)) + "...";
    }

    private void sendWebhookSync(String webhookUrl, String jsonPayload) {
        try {
            URL url = new URL(webhookUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            conn.setRequestProperty("User-Agent", "LoggerDiscordAddon/1.8.3");
            conn.setConnectTimeout(4000);
            conn.setReadTimeout(4000);
            conn.setDoOutput(true);

            byte[] bytes = jsonPayload.getBytes(StandardCharsets.UTF_8);
            conn.setFixedLengthStreamingMode(bytes.length);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(bytes);
                os.flush();
            }

            int responseCode = conn.getResponseCode();
            if (responseCode >= 400) {
                try (InputStream err = conn.getErrorStream()) {
                    if (err != null) {
                        byte[] buf = new byte[256];
                        int read = err.read(buf);
                        if (read > 0) {
                            String errResp = new String(buf, 0, read, StandardCharsets.UTF_8);
                            System.err.println("[LoggerDiscordAddon] Webhook returned HTTP " + responseCode + ": " + errResp);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[LoggerDiscordAddon] Failed to deliver webhook: " + e.getMessage());
        }
    }

    private void sendWebhookAsync(String webhookUrl, String content) {
        CompletableFuture.runAsync(() -> sendWebhookSync(webhookUrl, content));
    }

    public boolean isConfigEnabled() {
        if (this.configData == null) return false;
        Map<String, Object> discordSection = getSection(this.configData, "Discord");
        if (discordSection == null) return false;
        return getBoolean(discordSection, "Enabled", false);
    }

    @Override
    public boolean isEnabled() {
        if (!this.isEnabled) return false;
        if (this.isBotMode) {
            return this.jda != null && this.jda.getStatus() == JDA.Status.CONNECTED;
        }
        return !this.webhookMap.isEmpty();
    }

    @Override
    public void shutdown() {
        if (this.activityScheduler != null) {
            this.activityScheduler.shutdownNow();
        }
        if (this.jda != null) {
            try {
                this.jda.shutdown();
                if (!this.jda.awaitShutdown(3, TimeUnit.SECONDS)) {
                    this.jda.shutdownNow();
                }
            } catch (Exception ignored) {
                this.jda.shutdownNow();
            }
            this.jda = null;
        }
        this.channelMap.clear();
        this.webhookMap.clear();
        this.isEnabled = false;
    }

    public void reload() {
        System.out.println("[LoggerDiscordAddon] Reloading Discord configuration...");
        shutdown();
        this.activityScheduler = Executors.newScheduledThreadPool(1);
        init();
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getSection(Map<String, Object> map, String path) {
        if (map == null || path == null) return null;
        String[] parts = path.split("\\.");
        Map<String, Object> cur = map;
        for (String part : parts) {
            Object val = cur.get(part);
            if (val instanceof Map) {
                cur = (Map<String, Object>) val;
            } else {
                return null;
            }
        }
        return cur;
    }

    private String getString(Map<String, Object> map, String key, String def) {
        if (map == null || key == null) return def;
        Object val = map.get(key);
        return val != null ? String.valueOf(val) : def;
    }

    private boolean getBoolean(Map<String, Object> map, String key, boolean def) {
        if (map == null || key == null) return def;
        Object val = map.get(key);
        if (val instanceof Boolean) return (Boolean) val;
        if (val != null) return Boolean.parseBoolean(String.valueOf(val));
        return def;
    }

    private int getInt(Map<String, Object> map, String key, int def) {
        if (map == null || key == null) return def;
        Object val = map.get(key);
        if (val instanceof Number) return ((Number) val).intValue();
        if (val != null) {
            try {
                return Integer.parseInt(String.valueOf(val));
            } catch (Exception ignored) {}
        }
        return def;
    }
}
