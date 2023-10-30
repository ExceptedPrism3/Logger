package me.prism3.logger.commands.subcommands;

import me.prism3.logger.Main;
import me.prism3.logger.commands.SubCommand;
import me.prism3.logger.utils.Data;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

import static me.prism3.logger.utils.Data.pluginPrefix;


public class Reload implements SubCommand {

    private final Main main = Main.getInstance();

    @Override
    public String getName() { return "reload"; }

    @Override
    public String getDescription() { return "Reloads the plugin files."; }

    @Override
    public String getSyntax() { return "/logger " + this.getName(); }

    @Override
    public void perform(final CommandSender commandSender, final String[] args) {

        this.main.reloadConfig();
        this.main.getMessagesFile().reload();
        this.main.getDiscordFile().reload();
        this.main.initializer(new Data());
        final String message = this.main.getMessagesFile().get().getString("General.Reload");
        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', message.replace("%prefix%", pluginPrefix)));
    }

    @Override
    public List<String> getSubCommandsArgs(final CommandSender commandSender, final String[] args) { return Collections.emptyList(); }
}
