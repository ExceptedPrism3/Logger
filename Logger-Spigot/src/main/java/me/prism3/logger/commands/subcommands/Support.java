package me.prism3.logger.commands.subcommands;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.commands.SubCommand;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

import me.prism3.logger.utils.enums.GeneralSideMessages;

public class Support implements SubCommand {

    private final LoggerAPI plugin;

    public Support(final LoggerAPI plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "support";
    }

    @Override
    public String getDescription() {
        return "Get support or join our Discord server.";
    }

    @Override
    public String getSyntax() {
        return "/logger " + this.getName();
    }

    @Override
    public void perform(CommandSender sender, String[] args) {

        String header = plugin.getMessageManager().getGeneralMessage(GeneralSideMessages.SUPPORT_HEADER);
        String clickableText = ChatColor.translateAlternateColorCodes('&',
                plugin.getConfig().getString("Messages.General.Support-Clickable",
                        "&b&lClick here to join our Discord!"));
        String hoverText = ChatColor.translateAlternateColorCodes('&',
                plugin.getConfig().getString("Messages.General.Support-Hover", "&7Click to join!"));
        String url = "https://discord.gg/MfR5mcpVfX";

        sender.sendMessage("");
        sender.sendMessage(header);

        if (sender instanceof org.bukkit.entity.Player) {
            TextComponent message = new TextComponent(clickableText);
            message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
            message.setHoverEvent(
                    new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverText).create()));
            ((org.bukkit.entity.Player) sender).spigot().sendMessage(message);
        } else {
            // Console doesn't support clickable components well in all versions, send raw
            // URL
            sender.sendMessage(url);
        }

        sender.sendMessage("");
    }

    @Override
    public List<String> getSubCommandsArgs(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }

    @Override
    public String getPermission() {
        return null; // Available to everyone
    }
}
