package com.creativeartie.writer.writing;

public enum IdTypes {
    // @orderFor SpanStyles
    FOOTNOTE("\\{\\^"), ENDNOTE("\\{\\*"), SOURCE("\\{\\>");
    // @endOrder

    private final String textPattern;

    private IdTypes(String pattern) {
        textPattern = pattern;
    }

    public SpanStyles toTypedStyles() {
        return SpanStyles.valueOf(name());
    }

    static String listPatterns(boolean withName) {
        String patterns = "";
        for (IdTypes type : values()) {
            if (!patterns.isEmpty()) {
                patterns += "|";
            }
            patterns += withName ? Span.namePattern(type) : type.textPattern;
        }
        return "(" + patterns + ")";
    }

    @Override
    public String toString() {
        return textPattern;
    }
}
