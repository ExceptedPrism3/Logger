package me.prism3.logger.Commands.SubCommands;

import me.prism3.logger.Commands.SubCommand;
import me.prism3.logger.Discord.DiscordFile;
import me.prism3.logger.Main;
import me.prism3.logger.Utils.Messages;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;

public class ReloadCommand extends SubCommand {

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
        return null;
    }
}
