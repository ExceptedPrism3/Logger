package me.prism3.logger.commands.subcommands;

import com.google.common.io.Files;
import io.github.cdimascio.dotenv.Dotenv;
import me.prism3.logger.commands.SubCommand;
import me.prism3.logger.Main;
import me.prism3.logger.utils.Pastebin;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import static me.prism3.logger.utils.Data.pluginPrefix;
import static me.prism3.logger.utils.Data.selectedLang;

public class Dump implements SubCommand {

    private final File dataFolder = Main.getInstance().getDataFolder();

    private String pastebin_api;
    Properties properties = new Properties();
    String configFilePath = "./.env";
    String defaultValue = "8QZE95XsAEZwf8jhpd7MrL2c3zTSIY_S";
    @Override
    public String getName() {
        return "dump";
    }

    @Override
    public String getDescription() {
        return "Creates an online pastebin url of the plugin's config, discord, the used messages file and server's latest.log file";
    }

    @Override
    public String getSyntax() {
        return "/logger dump";
    }

    @Override
    public void perform(CommandSender commandSender, String[] args) throws IOException { this.pastebinExecution(commandSender); }

    @Override
    public List<String> getSubCommandsArgs(CommandSender commandSender, String[] args) { return Collections.emptyList(); }

    private void pastebinExecution(CommandSender commandSender) throws IOException {

        try {
            properties.load(java.nio.file.Files.newInputStream(Paths.get(configFilePath)));
            String value = properties.getProperty("PASTEBIN_API");
            if (value != null) {
                final Dotenv dotenv = Dotenv.load();

                pastebin_api = dotenv.get("PASTEBIN_API");
            }
        } catch (IOException e) {
            pastebin_api = defaultValue;
        }



        final String config = Files.asCharSource(new File(dataFolder + "/config.yml"), StandardCharsets.UTF_8).read();
        final String discord = Files.asCharSource(new File(dataFolder + "/discord.yml"), StandardCharsets.UTF_8).read();
        final String messages = Files.asCharSource(new File(dataFolder + "/messages/" + selectedLang + ".yml"), StandardCharsets.UTF_8).read();
        final String latest = Files.asCharSource(new File("logs/latest.log"), StandardCharsets.UTF_8).read();

        final  Pastebin.PasteRequest request = new Pastebin.PasteRequest(pastebin_api, config + " \n\n\n\nDISCORD CONFIG\n\n\n\n " + discord + " \n\n\n\nMESSAGES PART\n\n\n\n " + messages + " \n\n\n\nLATEST LOG PART\n\n\n\n " + latest);
        request.setPasteName("Logger MC Plugin");
        request.setPasteFormat("yaml");
        request.setPasteState(0);
        request.setPasteExpire("10M");
        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                pluginPrefix + request.postPaste() + "\n&cDo not share this link unless a developer asks you to do so!"));
    }
}