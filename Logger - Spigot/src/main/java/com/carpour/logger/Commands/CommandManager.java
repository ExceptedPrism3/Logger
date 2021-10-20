package com.carpour.logger.Commands;

import com.carpour.logger.Commands.onCommands.Credits;
import com.carpour.logger.Commands.onCommands.Reload;
import com.carpour.logger.Discord.DiscordFile;
import com.carpour.logger.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CommandManager implements TabExecutor {

    private final Main main = Main.getInstance();

    private final ArrayList<SubCommands> subCommands = new ArrayList<>();

    public CommandManager(){

        subCommands.add(new Reload());
        //subCommands.add(new get());
        subCommands.add(new Credits());
        //subCommands.add(new CommandSpy());

    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player){

            Player p = (Player) sender;

            if (args.length > 0){

                for (int i = 0; i < getSubCommands().size(); i++){

                    if (args[0].equalsIgnoreCase(getSubCommands().get(i).getName())){

                        getSubCommands().get(i).perform(p, args);

                    }
                }

            }else{

                p.sendMessage(ChatColor.GREEN + "Running" + ChatColor.AQUA + " Logger " + main.getDescription().getVersion() + ChatColor.RED + " - SNAP");

                for (int i = 0; i < getSubCommands().size(); i++){

                    p.sendMessage(ChatColor.GREEN + "» " + ChatColor.AQUA + getSubCommands().get(i).getSyntax() + ChatColor.GOLD + " - " + ChatColor.AQUA + getSubCommands().get(i).getDescription());

                }
            }

        } else if (args.length == 0){

            Bukkit.getServer().getConsoleSender().sendMessage("\n\n" + ChatColor.DARK_PURPLE + "Thank you for using the Logger Plugin!\n");

        } else if (args.length == 1 && args[0].equalsIgnoreCase("reload")){

            main.reloadConfig();
            DiscordFile.reload();
            sender.sendMessage(Objects.requireNonNull(main.getConfig().getString("Messages.Reload-Message")).replaceAll("&", "§"));

        }

        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        if (args.length == 1){

            ArrayList<String> cmds = new ArrayList<>();

            for (int i = 0; i < getSubCommands().size(); i++){

                cmds.add(getSubCommands().get(i).getName());

            }return cmds;
        }return null;
    }

    public ArrayList<SubCommands> getSubCommands() { return subCommands; }

}
