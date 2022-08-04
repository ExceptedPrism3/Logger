package com.carpour.loggercore.database.data;

import lombok.Data;

@Data
public class Options {
    private boolean isAuthMeEnabled = false;
    private boolean isEssentialsEnabled = false;
    private boolean isVaultEnabled = false;
    private boolean isLiteBansEnabled = false;
    private boolean isAdvancedBanEnabled = false;
    private boolean isViaVersion = false;
    private int dataDelete = 0;
    private boolean isPlayerIPEnabled = false;
}