package me.prism3.loggercore.database.data;

import java.util.Map;

public class Options {

    private boolean isAuthMeEnabled;
    private boolean isEssentialsEnabled;
    private boolean isVaultEnabled;
    private boolean isLiteBansEnabled;
    private boolean isAdvancedBanEnabled;
    private boolean isViaVersion;
    private int dataDelete;
    private boolean isPlayerIPEnabled;

    private String databasePassword;

    private String databaseUsername;

    private String databaseName;

    private String databaseHost;

    private int databasePort;

    public String getDatabasePassword() {
        return databasePassword;
    }

    public void setDatabasePassword(String databasePassword) {
        this.databasePassword = databasePassword;
    }

    public String getDatabaseUsername() {
        return databaseUsername;
    }

    public void setDatabaseUsername(String databaseUsername) {
        this.databaseUsername = databaseUsername;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getDatabaseHost() {
        return databaseHost;
    }

    public void setDatabaseHost(String databaseHost) {
        this.databaseHost = databaseHost;
    }

    public int getDatabasePort() {
        return databasePort;
    }

    public void setDatabasePort(int databasePort) {
        this.databasePort = databasePort;
    }

    private Map<String, Object> enabledLogs;

    public Map<String, Object> getEnabledLogs() {
        return this.enabledLogs;
    }

    public void setEnabledLogs(Map<String, Object> enabledLogs) {
        this.enabledLogs = enabledLogs;
    }

    public boolean isAuthMeEnabled() {
        return this.isAuthMeEnabled;
    }

    public void setAuthMeEnabled(boolean authMeEnabled) {
        this.isAuthMeEnabled = authMeEnabled;
    }

    public boolean isEssentialsEnabled() {
        return this.isEssentialsEnabled;
    }

    public void setEssentialsEnabled(boolean essentialsEnabled) {
        this.isEssentialsEnabled = essentialsEnabled;
    }

    public boolean isVaultEnabled() {
        return this.isVaultEnabled;
    }

    public void setVaultEnabled(boolean vaultEnabled) {
        this.isVaultEnabled = vaultEnabled;
    }

    public boolean isLiteBansEnabled() {
        return this.isLiteBansEnabled;
    }

    public void setLiteBansEnabled(boolean liteBansEnabled) {
        this.isLiteBansEnabled = liteBansEnabled;
    }

    public boolean isAdvancedBanEnabled() {
        return this.isAdvancedBanEnabled;
    }

    public void setAdvancedBanEnabled(boolean advancedBanEnabled) {
        this.isAdvancedBanEnabled = advancedBanEnabled;
    }

    public boolean isViaVersion() {
        return this.isViaVersion;
    }

    public void setViaVersion(boolean viaVersion) {
        this.isViaVersion = viaVersion;
    }

    public int getDataDelete() {
        return this.dataDelete;
    }

    public void setDataDelete(int dataDelete) {
        this.dataDelete = dataDelete;
    }

    public boolean isPlayerIPEnabled() {
        return this.isPlayerIPEnabled;
    }

    public void setPlayerIPEnabled(boolean playerIPEnabled) {
        this.isPlayerIPEnabled = playerIPEnabled;
    }

    public Boolean getBooleanValue(String key) {
        return ((Boolean) this.enabledLogs.get(key));
    }

    @Override
    public String toString() {
        return "Options{" +
                "isAuthMeEnabled=" + isAuthMeEnabled +
                ", isEssentialsEnabled=" + isEssentialsEnabled +
                ", isVaultEnabled=" + isVaultEnabled +
                ", isLiteBansEnabled=" + isLiteBansEnabled +
                ", isAdvancedBanEnabled=" + isAdvancedBanEnabled +
                ", isViaVersion=" + isViaVersion +
                ", dataDelete=" + dataDelete +
                ", isPlayerIPEnabled=" + isPlayerIPEnabled +
                ", databasePassword='" + databasePassword + '\'' +
                ", databaseUsername='" + databaseUsername + '\'' +
                ", databaseName='" + databaseName + '\'' +
                ", databaseHost='" + databaseHost + '\'' +
                ", databasePort=" + databasePort +
                ", enabledLogs=" + enabledLogs +
                '}';
    }
}