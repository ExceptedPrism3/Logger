package me.prism3.logger.utils.enums;

public enum Languages {

    AR("ar"),
    DA("da"),
    DE("de"),
    EN("en_en"),
    EU("eu"),
    FR("fr_fr"),
    JA("ja"),
    NL("nl"),
    RU("ru"),
    TR("tr"),
    ZHT("zh_cht"),
    ZH("zh_cn");

    private final String messageFile;

    Languages(String status) { this.messageFile = status; }

    public String getMessageFile() { return this.messageFile; }
}