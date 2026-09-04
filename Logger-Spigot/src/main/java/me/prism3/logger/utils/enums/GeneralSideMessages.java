package me.prism3.logger.utils.enums;

public enum GeneralSideMessages {

    NO_PERMISSION("General.No-Permission"),
    RELOAD("General.Reload"),
    INVALID_SYNTAX("General.Invalid-Syntax"),
    SUPPORT_HEADER("General.Support-Header"),
    SUPPORT_CLICKABLE("General.Support-Clickable"),
    SUPPORT_HOVER("General.Support-Hover");

    private final String path;

    GeneralSideMessages(final String path) {
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }
}
