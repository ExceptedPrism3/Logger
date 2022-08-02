package com.carpour.loggercore.database;

import lombok.Data;

@Data
public class Options {
    private boolean isAuthMeEnabled;
    private boolean isEssentialsEnabled;
    private boolean isVaultEnabled;
    private boolean isLiteBansEnabled;
    private boolean isAdvancedBanEnabled;
    private boolean isAtleastVersion;
    private int dataDelete;
    private boolean isPlayerIPEnabled;
}