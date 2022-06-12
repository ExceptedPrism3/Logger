package me.prism3.logger.commands.subcommands;

import me.prism3.logger.commands.SubCommand;
import me.prism3.logger.discord.DiscordFile;
import me.prism3.logger.Main;
import me.prism3.logger.utils.Messages;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ReloadCommand implements SubCommand {

    private final Main main = Main.getInstance();

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "Reloads the plugin files.";
    }

    @Override
    public String getSyntax() {
        return "/logger reload";
    }

    @Override
    public void perform(Player player, String[] args) {


        this.main.reloadConfig();
        Messages.reload();
        DiscordFile.reload();
        player.sendMessage(Objects.requireNonNull(Messages.get().getString("General.Reload")).replaceAll("&", "§"));

    }

    @Override
    public List<String> getSubCommandsArgs(Player player, String[] args) {
        return Collections.emptyList();
    }
}
