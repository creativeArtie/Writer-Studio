package com.creativeartie.humming.schema;

import java.util.regex.*;

import com.creativeartie.humming.document.*;

/**
 * Patterns for basic text with escape chars.
 */
public enum TextSpanPatterns implements PatternEnum {
    /**
     * Text pattern for ID
     *
     * @see IdentityPattern
     */
    ID("\\p{IsIdeographic}\\p{IsAlphabetic}\\p{IsDigit} _\t", false),
    /**
     * Text pattern for malformed address id error and Todo.
     *
     * @see IdentityReferencePattern
     * @see IdentityTodoPattern
     */
    SPECIAL("\\}\\n", true),
    /**
     * Text pattern for basic cases
     *
     * @see TextFormattedPatterns#BASIC
     * @see TextFormattedPatterns#NOTE
     */
    TEXT("\\n", true),
    /**
     * Text pattern for malformed address id text
     *
     * @see ParaHeadingPattern
     * @see ParaNotePatterns
     * @see ParaReferencePatterns
     * @see ParaBasicPatterns
     */
    SIMPLE("^\\\\\\n", false),
    /**
     * Text format for heading
     *
     * @see TextFormattedPatterns#HEADING
     */
    HEADING("\\n\\#", true),
    /**
     * Text for for note field's key
     *
     * @see ParaNotePatterns
     */
    KEY("\\p{IsAlphabetic}_", false),
    /**
     * Text format for table cell
     *
     * @see TextFormattedPatterns#CELL
     */
    CELL("\\|\\n", true),
    /**
     * Text format for note. Uses in {@link TextFormatted}.
     */
    NOTE;

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

    /** Parts of a text */
    public enum TextSpanParts implements PatternEnum {
        /** escape text pattern. This includes escaping no character. */
        ESCAPE("(\\\\.|\\\\$)"),
        /** Placeholder for text pattern. */
        TEXT("");

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

    /**
     * Match text to this pattern
     *
     * @param text
     *        the text to match
     *
     * @return Matcher of null if not matched
     */
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
