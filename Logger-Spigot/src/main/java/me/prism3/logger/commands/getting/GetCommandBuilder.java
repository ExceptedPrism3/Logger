package me.prism3.logger.commands.getting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetCommandBuilder {

    /**
     * Plugin get Commands
     *
     * Key = Command name | Value = Table name
     */

    public static final HashMap<String, Zabor2> commands;

    static {

        final HashMap<String, Zabor2> commandList = new HashMap<>();

        commandList.put("armor_stand_place", new Zabor2("BlockInteraction", "AND e.block='ARMOR_STAND'"));
//        commands.put("item_frame_place", new Zabor2("BlockInteraction", "AND e.block='ARMOR_STAND'"));
//        commands.put("item_frame_break", new Zabor2("BlockInteraction", "AND e.block='ARMOR_STAND'"));
        commandList.put("lever_interaction", new Zabor2("LeverInteraction"));
//        commands.put("armor_stand_break & crystal", ""); //TODO prism
        commandList.put("anvil", new Zabor2("Anvil"));
        commandList.put("book_editing", new Zabor2("BookEditing"));
        commandList.put("bucket_action", new Zabor2("BucketAction"));
        commandList.put("chest_interaction", new Zabor2("ChestInteraction"));
        commandList.put("command_block", new Zabor2("CommandBlock"));
        commandList.put("console_command", new Zabor2("ConsoleCommand"));
        commandList.put("crafting", new Zabor2("Crafting"));
        commandList.put("enchanting", new Zabor2("Enchanting"));
        commandList.put("entity_death", new Zabor2("EntityDeath"));
        commandList.put("furnace", new Zabor2("Furnace"));
        commandList.put("game_mode", new Zabor2("GameMode"));
        commandList.put("item_action", new Zabor2("ItemAction"));
        commandList.put("player_chat", new Zabor2("PlayerChat"));
        commandList.put("player_command", new Zabor2("PlayerCommand"));
        commandList.put("player_connection", new Zabor2("PlayerConnection"));
        commandList.put("player_death", new Zabor2("PlayerDeath"));
        commandList.put("player_kick", new Zabor2("PlayerKick"));
        commandList.put("player_level", new Zabor2("PlayerLevel"));
        commandList.put("player_sign_text", new Zabor2("PlayerSignText"));
        commandList.put("player_teleport", new Zabor2("PlayerTeleport"));
        commandList.put("portal_creation", new Zabor2("PortalCreation"));
        commandList.put("server_start", new Zabor2("ServerStart"));
        commandList.put("server_stop", new Zabor2("ServerStop"));
        commandList.put("tps", new Zabor2("Tps"));

        final List<String> commandNames = new ArrayList<>(commandList.keySet());

        commands = commandList;

        System.out.println(commands);

    }
}
