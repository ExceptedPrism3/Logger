package me.prism3.logger_bungee.utils;

public final class Constants {
    private Constants() {
    } // Prevent instantiation

    public enum Events {
        PLAYER_LOGIN("Player Login", "Player-Login", "Log-Player.Login"),
        PLAYER_LEAVE("Player Leave", "Player-Leave", "Log-Player.Leave"),
        PLAYER_SWITCH("Player Switch", "Player-Switch", "Log-Player.Switch"),
        PLAYER_CHAT("Player Chat", "Player-Chat", "Log-Player.Chat"),
        PLAYER_COMMAND("Player Command", "Player-Command", "Log-Player.Commands"),
        PLAYER_KICK("Player Kick", "Player-Kick", "Log-Player.Kick"),
        SERVER_COMMANDS("Server Commands", "Server-Side.Server-Commands", "Log-Server.Server-Commands"),
        SERVER_START("Server Start", "Server-Side.Start", "Log-Server.Start"),
        SERVER_STOP("Server Stop", "Server-Side.Stop", "Log-Server.Stop"),
        SERVER_RELOAD("Server Reload", "Server-Side.Reload-Console", "Log-Server.Reload"),
        SERVER_MANUAL_LOG("Manual Log", "Server-Side.Manual-Log", "Log-Server.Manual-Log");

        private final String folderName;
        private final String configKey;
        private final String enablePath;

        Events(String folderName, String configKey, String enablePath) {
            this.folderName = folderName;
            this.configKey = configKey;
            this.enablePath = enablePath;
        }

        public String getFolderName() {
            return folderName;
        }

        public String getConfigKey() {
            return configKey;
        }

        public String getEnablePath() {
            return enablePath;
        }

        public String getValue() {
            return folderName;
        }
    }

    public enum Permissions {
        STAFF_LOG("loggerproxy.staff.log"),
        STAFF_ALERTS("loggerproxy.staff.alerts"),
        STAFF_NOTIFICATIONS("loggerproxy.staff.notifications"),
        EXEMPT("loggerproxy.exempt"),
        RELOAD("loggerproxy.reload"),
        ADMIN("loggerproxy.admin");

        private final String value;

        Permissions(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum Config {
        SERVER_NAME("Server-Name"),
        LOG_PLAYER_LOGIN("Log-Player.Login"),
        LOG_PLAYER_LEAVE("Log-Player.Leave"),
        LOG_PLAYER_SWITCH("Log-Player.Switch"),
        LOG_PLAYER_CHAT("Log-Player.Chat"),
        LOG_PLAYER_COMMANDS("Log-Player.Commands"),
        LOG_PLAYER_KICK("Log-Player.Kick"),
        LOG_SERVER_COMMANDS("Log-Server.Server-Commands"),
        LOG_SERVER_START("Log-Server.Start"),
        LOG_SERVER_STOP("Log-Server.Stop"),
        LOG_SERVER_RELOAD("Log-Server.Reload"),
        LOG_SERVER_MANUAL("Log-Server.Manual-Log");

        private final String value;

        Config(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
