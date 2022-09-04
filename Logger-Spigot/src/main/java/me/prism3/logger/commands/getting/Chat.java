package me.prism3.logger.commands.getting;

import me.prism3.logger.Main;
import me.prism3.loggercore.database.DataInterface;
import me.prism3.loggercore.database.data.Arguments;
import me.prism3.loggercore.database.entity.AbstractAction;
import me.prism3.loggercore.database.entity.ActionInterface;
import me.prism3.loggercore.database.entity.PlayerChat;
import me.prism3.loggercore.database.utils.GetCommandBuilder;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
@SuppressWarnings("unchecked")
public class Chat implements TabExecutor {
private DataInterface database = Main.getInstance().getDatabase();
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        try {
            Pattern regex = Pattern.compile("[^a-zA-Z0-9-:-_]");
            Arguments arguments = new Arguments(args);
            arguments.setInputCommand(String.join(" ", args));
            if(Arrays.stream(args).anyMatch(regex.asPredicate()))
            {
                sender.sendMessage("Please provide valid values!");
                return false;
            }
            if(args.length == 0)
            {
                sender.sendMessage(command.getUsage());
            }
            if(args.length >= 1)
            {


                if(arguments.getAction() == null || arguments.getAction().isEmpty())
                {
                    sender.sendMessage("You need to specify an action!");
                    return false;
                }
                if(!arguments.isValidTime())
                {
                    sender.sendMessage("Specify a valid time");
                    return false;
                }
                if(arguments.getAction().equalsIgnoreCase("player_chat"))
                {
                    Chat.sendResults(sender, arguments, command, database.getPlayerChat(arguments));

                } else if (arguments.getAction().equalsIgnoreCase("console_command")) {
                    Chat.sendResults(sender, arguments, command, database.getConsoleCommands(arguments));

                }
                else if (arguments.getAction().equalsIgnoreCase("block_interaction")) {
                    Chat.sendResults(sender, arguments, command, database.getBlockInteraction(arguments));

                }
                else if (arguments.getAction().equalsIgnoreCase("player_connection")) {
                    Chat.sendResults(sender, arguments, command, database.getPlayerConnection(arguments));

                }
                //TODO checkers and rest of actions



            }


        } catch (final Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (args.length != 0 && args[args.length - 1].startsWith("a"))
            return GetCommandBuilder.actionParameters;

        if (args.length != 0 && args[args.length - 1].startsWith("u"))
            return sender.getServer().getOnlinePlayers().stream().map(value -> "user:"+value.getName())
                         .collect(Collectors.toList());

        //TODO All subcommands are shown in the 3rd param of the player, you can change that from the if checker

        return null;
    }
    private static void sendResults(@NotNull CommandSender sender, @NotNull Arguments arguments,
                               @NotNull Command command, List<ActionInterface> results)
    {
        if(results.isEmpty())
        {
            sender.sendMessage("No results found!");

        }
        for(ActionInterface element : results)
        {
            sender.sendMessage(element.getAction());
        }
        Pager pager = new Pager(arguments.getTotalCount(), arguments.getHeight(), 0,
                arguments.getPage());
        sender.spigot().sendMessage(pager.preparePaging(command.getLabel(), arguments));

    }
}
