package me.prism3.logger.utils.enums;

public enum Languages {

    AR("ar"),
    FR("fr_fr"),
    EN("en_en"),
    ZH("zh_cn"),
    ZHT("zh_cht"),
    NL("nl");

    private final String messageFile;

    Languages(String status) { this.messageFile = status; }

    public String getMessageFile() { return this.messageFile; }
}
