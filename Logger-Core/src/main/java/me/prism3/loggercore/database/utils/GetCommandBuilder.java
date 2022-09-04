package me.prism3.loggercore.database.utils;

import java.util.*;

public class GetCommandBuilder {

    /**
     * Plugin get Commands
     *
     * Key = Action Name | Value = Table name
     */

    private GetCommandBuilder() {}

    private static final List<String> commandNames;

    private static HashMap<String, String> commandList;

    public static List<String> actionParameters;

    public static HashMap<String, String> getCommandList() {
        return commandList;
    }

    static {

        final HashMap<String, String> commandList = new HashMap<>();

        commandList.put("armor_stand_place", "BlockInteraction");
        commandList.put("block_interaction", "BlockInteraction");
        commandList.put("lever_interaction", "LeverInteraction");
        commandList.put("anvil", "Anvil");
        commandList.put("book_editing", "BookEditing");
        commandList.put("bucket_action", "BucketAction");
        commandList.put("chest_interaction", "ChestInteraction");
        commandList.put("command_block", "CommandBlock");
        commandList.put("console_command", "ConsoleCommand");
        commandList.put("crafting", "Crafting");
        commandList.put("enchanting", "Enchanting");
        commandList.put("entity_death", "EntityDeath");
        commandList.put("furnace", "Furnace");
        commandList.put("game_mode", "GameMode");
        commandList.put("item_action", "ItemAction");
        commandList.put("player_chat", "PlayerChat");
        commandList.put("player_command", "PlayerCommand");
        commandList.put("player_connection", "PlayerConnection");
        commandList.put("player_death", "PlayerDeath");
        commandList.put("player_kick", "PlayerKick");
        commandList.put("player_level", "PlayerLevel");
        commandList.put("player_sign_text", "PlayerSignText");
        commandList.put("player_teleport", "PlayerTeleport");
        commandList.put("portal_creation", "PortalCreation");
        commandList.put("server_start", "ServerStart");
        commandList.put("server_stop", "ServerStop");
        commandList.put("tps", "Tps");
        GetCommandBuilder.commandList = commandList;
        commandNames = new ArrayList<>(commandList.keySet());
        GetCommandBuilder.actionParameters = new ArrayList<>();
        for(String a : commandNames)
        {
            GetCommandBuilder.actionParameters.add("action:"+a);
        }
    }
}
