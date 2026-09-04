package me.prism3.logger.commands.subcommands;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.commands.SubCommand;
import me.prism3.logger.utils.Log;
import me.prism3.logger.utils.enums.GeneralSideMessages;

import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

/**
 * Reloads all plugin config, messages, Discord & database.
 * <p>
 * This command is available to all players with the permission
 * `logger.reload` (default: OP).
 */
public class Reload implements SubCommand {

    private final LoggerAPI plugin;

    public Reload(final LoggerAPI plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "Reloads all plugin config, messages, Discord & database.";
    }

    @Override
    public String getSyntax() {
        return "/logger " + this.getName();
    }

    @Override
    public void perform(final CommandSender sender, final String[] args) {

        // Reload config, messages, etc.
        this.plugin.getData().reload();
        this.plugin.getMessageManager().reloadMessages();
        Log.info("Configuration & messages reloaded.");

        // Fire Reload Event for addons (Discord, etc.)
        this.plugin.getServer().getPluginManager().callEvent(new me.prism3.logger.events.LoggerReloadEvent());

        // Reload Database
        try {
            if (this.plugin.getDatabaseManager() != null) {
                this.plugin.getDatabaseManager().shutdown();
                this.plugin.setDatabaseManager(null);
            }

            if (this.plugin.getData().getDatabaseSettings().enabled) {
                me.prism3.logger.utils.Data.DatabaseSettings db = this.plugin.getData().getDatabaseSettings();
                me.prism3.logger_core.database.DatabaseConfig dbConfig = new me.prism3.logger_core.database.DatabaseConfig(
                        db.enabled, db.type, db.host, db.port, db.name, db.username, db.password, db.tablePrefix,
                        db.dataDeletion);
                this.plugin
                        .setDatabaseManager(new me.prism3.logger_core.database.DatabaseManager(this.plugin, dbConfig));
                this.plugin.getDatabaseManager().initialize();
            }
        } catch (final Exception e) {
            Log.severe("Database reload failed: " + e.getMessage(), e);
            return;
        }

        sender.sendMessage(this.plugin.getMessageManager().getGeneralMessage(GeneralSideMessages.RELOAD));
    }

    @Override
    public List<String> getSubCommandsArgs(final CommandSender sender, final String[] args) {
        return Collections.emptyList();
    }

    @Override
    public String getPermission() {
        return me.prism3.logger.managers.PermissionManager.LOGGER_RELOAD;
    }
}
