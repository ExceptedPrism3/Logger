package com.carpour.loggercore.database;

import com.carpour.loggercore.database.data.DatabaseCredentials;
import com.carpour.loggercore.database.data.Options;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractDataSource implements DataSourceInterface {
    protected static final List<String> tablesNames = Stream.of("player_chat", "player_commands", "player_sign_text",
            "player_death", "player_teleport", "player_join", "player_leave", "block_place", "block_break",
            "player_kick", "player_level", "Bucket_fill", "bucket_empty", "anvil", "item_drop", "enchanting",
            "book_editing", "item_pickup", "furnace", "game_mode", "crafting", "registration", "server_start",
            "server_stop", "console_commands", "ram", "tps", "portal_creation", "rcon", "primed_tnt", "command_block",
            "chest_interaction", "entity_death", "logger_playertime").collect(Collectors.toCollection(ArrayList::new));


    protected final DatabaseCredentials databaseCredentials;
    private final String className;
    protected final Logger logger = Logger.getLogger(AbstractDataSource.class.getName());
    protected final Options options;

    protected AbstractDataSource(DatabaseCredentials databaseCredentials, Options options,
                                 String className) throws SQLException {
        this.className = className;
        this.databaseCredentials = databaseCredentials;
        this.options = options;



    }



    protected abstract String getJdbcUrl();



}
