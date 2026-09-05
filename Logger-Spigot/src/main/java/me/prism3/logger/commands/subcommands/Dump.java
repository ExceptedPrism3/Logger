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
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

/**
 * Dump class is a subcommand for the LoggerAPI plugin that creates an online
 * pastebin URL
 * containing the plugin's configuration files and the server's latest log file.
 * It implements the SubCommand interface and provides functionality to generate
 * a pastebin link.
 */
public class Dump implements SubCommand {

    private static final String PASTE_EXPIRATION = "10M";
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
        Dotenv dotenv = null;
        try {
            dotenv = Dotenv.configure().ignoreIfMissing().load();
        } catch (final Exception ignored) {}

        String apiKey = dotenv != null ? dotenv.get("PASTEBIN_API") : null;
        if (apiKey == null || apiKey.trim().isEmpty()) {
            apiKey = System.getenv("PASTEBIN_API");
        }

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
