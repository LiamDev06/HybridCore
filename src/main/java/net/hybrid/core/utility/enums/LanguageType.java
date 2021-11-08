package net.hybrid.core.utility.enums;

public enum LanguageType {

    ENGLISH("eng"),
    SWEDISH("swe");

    private final String code;

    LanguageType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
