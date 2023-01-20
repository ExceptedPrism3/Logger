package me.prism3.logger.utils.enums;

import java.util.HashMap;
import java.util.Map;

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

    private static final Map<String, Languages> lookup = new HashMap<>();

    static {
        for (Languages lang : Languages.values())
            lookup.put(lang.name(), lang);
    }

    public static Languages get(String code) { return lookup.get(code.toUpperCase()); }
}
