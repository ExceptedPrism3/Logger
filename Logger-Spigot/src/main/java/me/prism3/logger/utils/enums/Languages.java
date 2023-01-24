package me.prism3.logger.utils.enums;

import java.util.EnumMap;

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

    private static final EnumMap<Languages, String> lookup = new EnumMap<>(Languages.class);

    static {
        for (final Languages lang : Languages.values())
            lookup.put(lang, lang.name());
    }

    public static Languages get(String code) { return lookup.entrySet().stream()
            .filter(e -> e.getValue().equalsIgnoreCase(code))
            .map(EnumMap.Entry::getKey)
            .findFirst()
            .orElse(null);
    }
}
