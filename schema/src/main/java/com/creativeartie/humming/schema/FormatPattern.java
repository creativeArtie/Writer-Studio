package com.creativeartie.humming.schema;

class FormatPattern {
    private static String escape = "\\\\";
    private static String formats = "\\*`\\_";
    private static String withRef = "\\{";

    private FormatPattern() {}

    static String getEscapePattern() {
        return escape;
    }

    static String getFormatPattern() {
        return formats + escape;
    }

    static String getWithRefPattern() {
        return getFormatPattern() + withRef;
    }
}
