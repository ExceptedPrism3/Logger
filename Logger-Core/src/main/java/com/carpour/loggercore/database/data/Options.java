package com.carpour.loggercore.database.data;

public class Options {
    private boolean isAuthMeEnabled;
    private boolean isEssentialsEnabled;
    private boolean isVaultEnabled;
    private boolean isLiteBansEnabled;
    private boolean isAdvancedBanEnabled;
    private boolean isViaVersion;
    private int dataDelete;
    private boolean isPlayerIPEnabled;

    public boolean isAuthMeEnabled() {
        return isAuthMeEnabled;
    }

    public void setAuthMeEnabled(boolean authMeEnabled) {
        isAuthMeEnabled = authMeEnabled;
    }

    public boolean isEssentialsEnabled() {
        return isEssentialsEnabled;
    }

    public void setEssentialsEnabled(boolean essentialsEnabled) {
        isEssentialsEnabled = essentialsEnabled;
    }

    public boolean isVaultEnabled() {
        return isVaultEnabled;
    }

    public void setVaultEnabled(boolean vaultEnabled) {
        isVaultEnabled = vaultEnabled;
    }

    public boolean isLiteBansEnabled() {
        return isLiteBansEnabled;
    }

    public void setLiteBansEnabled(boolean liteBansEnabled) {
        isLiteBansEnabled = liteBansEnabled;
    }

    public boolean isAdvancedBanEnabled() {
        return isAdvancedBanEnabled;
    }

    public void setAdvancedBanEnabled(boolean advancedBanEnabled) {
        isAdvancedBanEnabled = advancedBanEnabled;
    }

    public boolean isViaVersion() {
        return isViaVersion;
    }

    public void setViaVersion(boolean viaVersion) {
        isViaVersion = viaVersion;
    }

    public int getDataDelete() {
        return dataDelete;
    }

    public void setDataDelete(int dataDelete) {
        this.dataDelete = dataDelete;
    }

    public boolean isPlayerIPEnabled() {
        return isPlayerIPEnabled;
    }

    public void setPlayerIPEnabled(boolean playerIPEnabled) {
        isPlayerIPEnabled = playerIPEnabled;
    }
}