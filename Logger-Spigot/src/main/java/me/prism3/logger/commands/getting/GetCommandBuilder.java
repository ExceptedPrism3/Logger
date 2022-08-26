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

    private GetCommandBuilder() {}

    protected static final List<String> commandNames;

    static {

        final HashMap<String, GetCommandBuilderProperties> commandList = new HashMap<>();

        commandList.put("armor_stand_place", new GetCommandBuilderProperties("BlockInteraction", "AND e.block='ARMOR_STAND'"));
//        commands.put("item_frame_place", new Zabor2("BlockInteraction", "AND e.block='ARMOR_STAND'"));
//        commands.put("item_frame_break", new Zabor2("BlockInteraction", "AND e.block='ARMOR_STAND'"));
        commandList.put("lever_interaction", new GetCommandBuilderProperties("LeverInteraction"));
//        commands.put("armor_stand_break & crystal", ""); //TODO prism
        commandList.put("anvil", new GetCommandBuilderProperties("Anvil"));
        commandList.put("book_editing", new GetCommandBuilderProperties("BookEditing"));
        commandList.put("bucket_action", new GetCommandBuilderProperties("BucketAction"));
        commandList.put("chest_interaction", new GetCommandBuilderProperties("ChestInteraction"));
        commandList.put("command_block", new GetCommandBuilderProperties("CommandBlock"));
        commandList.put("console_command", new GetCommandBuilderProperties("ConsoleCommand"));
        commandList.put("crafting", new GetCommandBuilderProperties("Crafting"));
        commandList.put("enchanting", new GetCommandBuilderProperties("Enchanting"));
        commandList.put("entity_death", new GetCommandBuilderProperties("EntityDeath"));
        commandList.put("furnace", new GetCommandBuilderProperties("Furnace"));
        commandList.put("game_mode", new GetCommandBuilderProperties("GameMode"));
        commandList.put("item_action", new GetCommandBuilderProperties("ItemAction"));
        commandList.put("player_chat", new GetCommandBuilderProperties("PlayerChat"));
        commandList.put("player_command", new GetCommandBuilderProperties("PlayerCommand"));
        commandList.put("player_connection", new GetCommandBuilderProperties("PlayerConnection"));
        commandList.put("player_death", new GetCommandBuilderProperties("PlayerDeath"));
        commandList.put("player_kick", new GetCommandBuilderProperties("PlayerKick"));
        commandList.put("player_level", new GetCommandBuilderProperties("PlayerLevel"));
        commandList.put("player_sign_text", new GetCommandBuilderProperties("PlayerSignText"));
        commandList.put("player_teleport", new GetCommandBuilderProperties("PlayerTeleport"));
        commandList.put("portal_creation", new GetCommandBuilderProperties("PortalCreation"));
        commandList.put("server_start", new GetCommandBuilderProperties("ServerStart"));
        commandList.put("server_stop", new GetCommandBuilderProperties("ServerStop"));
        commandList.put("tps", new GetCommandBuilderProperties("Tps"));

        commandNames = new ArrayList<>(commandList.keySet());

    }
}
