package me.prism3.logger.commands.subcommands;

import com.google.common.io.Files;
import io.github.cdimascio.dotenv.Dotenv;
import me.prism3.logger.Main;
import me.prism3.logger.commands.SubCommand;
import me.prism3.logger.utils.Pastebin;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.io.File;
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
        return "Creates an online pastebin url of the plugin's config and discord";
    }

    @Override
    public String getSyntax() {
        return "/logger dump";
    }

    @Override
    public void perform(CommandSender commandSender, String[] args) { this.pastebinExecution(commandSender); }

    @Override
    public List<String> getSubCommandsArgs(CommandSender commandSender, String[] args) { return Collections.emptyList(); }

    private void pastebinExecution(CommandSender commandSender) {

        try {

            final Dotenv dotenv = Dotenv.load();

            final String[] loggerFiles = { "config.yml", "discord.yml", "messages" + File.separator + selectedLang + ".yml" };
            final StringBuilder sb = new StringBuilder();

            for (final String file : loggerFiles)
                sb.append(Files.asCharSource(new File(dataFolder + File.separator + file), StandardCharsets.UTF_8).read()).append("\n\n\n");

            sb.append(Files.asCharSource(new File("logs" + File.separator + "latest.log"), StandardCharsets.UTF_8).read());

            final Pastebin.PasteRequest request = new Pastebin.PasteRequest(dotenv.get("PASTEBIN_API"), sb.toString());
            request.setPasteName("Logger MC Plugin");
            request.setPasteFormat("yaml");
            request.setPasteState(1);
            request.setPasteExpire("10M");
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    pluginPrefix + request.postPaste() + "\n&cDo not share this link at all!"));

        } catch (final Exception e) {

            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    pluginPrefix + "&cAn error occurred while executing this command." +
                            " This can be caused by the following:\n" +
                            "- No internet connection\n" +
                            "- FireWall preventing the connection to the PasteBin site\n" +
                            "If the issue persists, contact the authors!"));

            e.printStackTrace();
        }
    }
}