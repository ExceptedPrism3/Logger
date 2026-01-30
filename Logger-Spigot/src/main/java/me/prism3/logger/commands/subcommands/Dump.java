package me.prism3.logger.commands.subcommands;

import io.github.cdimascio.dotenv.Dotenv;
import me.prism3.logger.Main;
import me.prism3.logger.commands.SubCommand;
import me.prism3.logger.utils.Pastebin;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import static me.prism3.logger.utils.Data.pluginPrefix;
import static me.prism3.logger.utils.Data.selectedLang;

public class Dump implements SubCommand {

    private final File dataFolder = Main.getInstance().getDataFolder();

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
        return "/logger " + this.getName();
    }

    @Override
    public void perform(CommandSender commandSender, String[] args) throws IOException {
        this.pastebinExecution(commandSender);
    }

    @Override
    public List<String> getSubCommandsArgs(CommandSender commandSender, String[] args) {
        return Collections.emptyList();
    }

    private void pastebinExecution(CommandSender commandSender) throws IOException {

        final Dotenv dotenv = Dotenv.load();

        final String config = readFile(new File(dataFolder + "/config.yml"));
        final String discord = readFile(new File(dataFolder + "/discord.yml"));
        final String messages = readFile(new File(dataFolder + "/messages/" + selectedLang + ".yml"));
        final String latest = readFile(new File("logs/latest.log"));
        final Pastebin.PasteRequest request = new Pastebin.PasteRequest(dotenv.get("PASTEBIN_API"),
                config + " \n\n\n\nDISCORD CONFIG\n\n\n\n " + discord + " \n\n\n\nMESSAGES PART\n\n\n\n " + messages
                        + " \n\n\n\nLATEST LOG PART\n\n\n\n " + latest);

        if (request.postPaste() == null)
            return;

        request.setPasteName("Logger MC Plugin");
        request.setPasteFormat("yaml");
        request.setPasteState(1);
        request.setPasteExpire("10M");
        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                pluginPrefix + request.postPaste() + "\n&cDo not share this link at all!"));
    }

    private String readFile(File file) {
        StringBuilder content = new StringBuilder();
        try {
            if (file.exists()) {
                java.util.Scanner scanner = new java.util.Scanner(file, StandardCharsets.UTF_8.name());
                while (scanner.hasNextLine()) {
                    content.append(scanner.nextLine()).append("\n");
                }
                scanner.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error reading file: " + file.getName();
        }
        return content.toString();
    }
}