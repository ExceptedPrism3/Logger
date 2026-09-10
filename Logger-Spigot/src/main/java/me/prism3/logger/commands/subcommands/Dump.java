package me.prism3.logger.commands.subcommands;

import com.google.common.io.Files;
import io.github.cdimascio.dotenv.Dotenv;
import me.prism3.logger.LoggerAPI;
import me.prism3.logger.commands.SubCommand;
import me.prism3.logger.utils.PasteBin;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * Dump class is a subcommand for the LoggerAPI plugin that creates an online
 * pastebin URL
 * containing the plugin's configuration files and the server's latest log file.
 * It implements the SubCommand interface and provides functionality to generate
 * a pastebin link.
 */
public class Dump implements SubCommand {

    private static final String PASTE_EXPIRATION = "10M";
    private static final String DEFAULT_PASTEBIN_API = "r_Cgj_xsPsCUrQi13xyxfGo6uiBDIjB9";
    private final LoggerAPI plugin;

    public Dump(final LoggerAPI plugin) {
        this.plugin = plugin;
    }

    /**
     * Returns the name of the subcommand.
     *
     * @return The name of the subcommand.
     */
    @Override
    public String getName() {
        return "dump";
    }

    /**
     * Returns the description of the subcommand.
     *
     * @return The description of the subcommand.
     */
    @Override
    public String getDescription() {
        return "Creates an online pastebin URL of the plugin's config, discord, messages file, and server's latest.log file.";
    }

    /**
     * Returns the syntax of the subcommand.
     *
     * @return The syntax of the subcommand.
     */
    @Override
    public String getSyntax() {
        return "/logger " + this.getName();
    }

    /**
     * Executes the subcommand.
     *
     * @param sender The command sender.
     * @param args   The command arguments.
     */
    @Override
    public void perform(final CommandSender sender, final String[] args) {

        try {
            this.pastebinExecution(sender);
        } catch (final IOException e) {
            sender.sendMessage(ChatColor.RED + "Failed to create Pastebin dump: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void pastebinExecution(final CommandSender sender) throws IOException {

        final File dataFolder = plugin.getDataFolder();
        final String apiKey = resolvePastebinApiKey(dataFolder);

        if (apiKey == null || apiKey.trim().isEmpty()) {
            sender.sendMessage(ChatColor.RED + "Pastebin API key not configured. Set PASTEBIN_API in environment variables or .env file.");
            return;
        }

        File discordFile = new File(dataFolder, "discord.yml");
        if (!discordFile.exists()) {
            // Check for separate addon folder
            File addonFolder = new File(dataFolder.getParentFile(), "LoggerDiscordAddon");
            File addonConfig = new File(addonFolder, "discord.yml");
            if (addonConfig.exists()) {
                discordFile = addonConfig;
            }
        }

        final String combinedContent = String.join("\n\n",
                readFile(new File(dataFolder, "config.yml")),
                readFile(discordFile),
                readFile(new File(dataFolder + File.separator + "messages" + File.separator
                        + plugin.getData().getLanguage() + ".yml")),
                readFile(new File("logs" + File.separator + "latest.log")));

        final PasteBin.PasteRequest request = new PasteBin.PasteRequest(apiKey, combinedContent);
        request.setPasteName("Logger MC Plugin Dump");
        request.setPasteFormat("yaml");
        request.setPasteState(1);
        request.setPasteExpire(PASTE_EXPIRATION);

        final String pasteUrl = request.postPaste();
        sender.sendMessage(pasteUrl != null
                ? ChatColor.translateAlternateColorCodes('&',
                        plugin.getData().getPluginPrefix() + pasteUrl + "\n&cDo not share this link at all!")
                : ChatColor.RED + "Failed to post to Pastebin.");
    }

    /**
     * Resolves the Pastebin API key across environment variables, local .env files,
     * bundled jar resources, and a default fallback.
     *
     * @param dataFolder The plugin data folder.
     * @return The resolved API key string.
     */
    private String resolvePastebinApiKey(final File dataFolder) {
        // 1. System environment variable
        String envKey = System.getenv("PASTEBIN_API");
        if (envKey != null && !envKey.trim().isEmpty()) {
            return envKey.trim();
        }

        // 2. Plugin data folder .env (extract bundled .env if absent)
        if (dataFolder != null) {
            File pluginEnv = new File(dataFolder, ".env");
            if (!pluginEnv.exists()) {
                try {
                    plugin.saveResource(".env", false);
                } catch (final Exception ignored) {}
            }
            if (pluginEnv.exists()) {
                try {
                    Dotenv pluginDotenv = Dotenv.configure().directory(dataFolder.getAbsolutePath()).ignoreIfMissing().load();
                    String key = pluginDotenv.get("PASTEBIN_API");
                    if (key != null && !key.trim().isEmpty()) {
                        return key.trim();
                    }
                } catch (final Exception ignored) {}
            }
        }

        // 3. Server root .env
        try {
            Dotenv rootDotenv = Dotenv.configure().ignoreIfMissing().load();
            String rootKey = rootDotenv != null ? rootDotenv.get("PASTEBIN_API") : null;
            if (rootKey != null && !rootKey.trim().isEmpty()) {
                return rootKey.trim();
            }
        } catch (final Exception ignored) {}

        // 4. Bundled resource stream /.env
        try (InputStream in = plugin.getResource(".env")) {
            if (in != null) {
                Properties props = new Properties();
                props.load(in);
                String resKey = props.getProperty("PASTEBIN_API");
                if (resKey != null && !resKey.trim().isEmpty()) {
                    return resKey.trim();
                }
            }
        } catch (final Exception ignored) {}

        // 5. Default fallback key
        return DEFAULT_PASTEBIN_API;
    }

    /**
     * Reads the content of a file and returns it as a string.
     *
     * @param file The file to read.
     * @return The content of the file as a string.
     */
    private String readFile(File file) {
        if (!file.exists()) {
            return "File not found: " + file.getPath();
        }
        try {
            return Files.asCharSource(file, StandardCharsets.UTF_8).read();
        } catch (IOException e) {
            return "Error reading file " + file.getPath() + ": " + e.getMessage();
        }
    }

    /**
     * Returns a list of subcommand arguments for tab completion.
     *
     * @param sender The command sender.
     * @param args   The command arguments.
     * @return A list of subcommand arguments.
     */
    @Override
    public List<String> getSubCommandsArgs(final CommandSender sender, final String[] args) {
        return Collections.emptyList();
    }

    @Override
    public String getPermission() {
        return me.prism3.logger.managers.PermissionManager.LOGGER_RELOAD;
    }
}
