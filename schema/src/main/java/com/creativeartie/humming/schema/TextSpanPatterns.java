package com.creativeartie.humming.schema;

import java.util.regex.*;

/**
 * Patterns for basic text with escape chars.
 */
public enum TextSpanPatterns implements PatternEnum {
    ID("\\p{IsIdeographic}\\p{IsAlphabetic}\\p{IsDigit} _\t", false), SPECIAL("\\}\\n", true), TEXT("\\n", true),
    SIMPLE("^\\\\\\n", false), HEADING("\\n\\#", true), KEY("\\p{IsAlphabetic}_", false), CELL("\\|\\n", true), NOTE;

    private enum BasicFormatParts {
        REF("\\{"), BOLD("\\*"), UNDERLINE("_"), ITALICS("`"), ESCAPE("\\\\");

        private static String listPatterns() {
            StringBuilder builder = new StringBuilder();
            for (BasicFormatParts part : values()) builder.append(part.rawPattern);
            return builder.toString();
        }

        private String rawPattern;

        BasicFormatParts(String pattern) {
            rawPattern = pattern;
        }
    }

    public enum TextSpanParts implements PatternEnum {
        ESCAPE("(\\\\.|\\\\$)"), TEXT("");

        private final String pattern;

        TextSpanParts(String pat) {
            pattern = pat;
        }

        @Override
        public String getRawPattern() {
            return pattern;
        }

        @Override
        public String getPatternName() {
            return name();
        }
    }

    private final String textPattern;
    private final String basePattern;
    private Pattern compiledPattern;

    TextSpanPatterns(String pat, boolean isNegate) {
        textPattern = isNegate ? ("[^" + pat + BasicFormatParts.listPatterns() + "]+") : ("[" + pat + "]+");
        // @formatter:off
        basePattern = "(" +
                TextSpanParts.ESCAPE.getRawPattern() + "|" +
                textPattern +
            ")+";
        // @formatter:on
    }

    TextSpanPatterns() { // For footnote and endnote
        textPattern = "[^\\n" + BasicFormatParts.listPatterns().substring(1) + "]+";
        // @formatter:off
        basePattern = "(" +
                TextSpanParts.ESCAPE.getRawPattern() + "|" +
                textPattern +
            ")+";
        // @formatter:on
    }

    public Matcher matcher(String text) {
        if (compiledPattern == null) compiledPattern = Pattern.compile(
        // @formatter:off
            "(" +
                TextSpanParts.ESCAPE.getNamedPattern() + "|" +
                PatternEnum.namePattern(TextSpanParts.TEXT.name(), textPattern) +
            ")"
        // @formatter:on
        );
        return compiledPattern.matcher(text);
    }

    @Override
    public String getRawPattern() {
        return basePattern;
    }

    @Override
    public String getPatternName() {
        return name();
    }
}
