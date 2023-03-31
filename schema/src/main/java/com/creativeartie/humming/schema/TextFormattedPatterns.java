package com.creativeartie.humming.schema;

import java.util.regex.*;

import com.google.common.base.*;

/** Different formatted patterns. */
public enum TextFormattedPatterns implements PatternEnum {
    /**
     * Basic formatted pattern.
     *
     * @see ParaBasicPatterns
     * @see ParaListPattern
     * @see ParaNotePatterns
     */
    BASIC(TextSpanPatterns.TEXT, true),
    /**
     * Heading formatted pattern.
     *
     * @see ParaHeadingPattern
     * @see ParaNotePatterns
     */
    HEADING(TextSpanPatterns.HEADING, true),
    /**
     * Note formatted pattern.
     *
     * @see ParaReferencePatterns
     */
    NOTE(TextSpanPatterns.TEXT, false),
    /**
     * Table cell formatted pattern.
     *
     * @see ParaTableRowPattern
     */
    CELL(TextSpanPatterns.CELL, true);

    /** Parts of a formatted text */
    public enum TextFormattedParts implements PatternEnum {
        /** Bold format pattern. */
        BOLD("\\*"),
        /** Underline format pattern. */
        UNDERLINE("_"),
        /** Italics format pattern. */
        ITALICS("`"),

        /** Agenda phrase pattern. */
        TODO(IdentityTodoPattern.getFullPattern()),
        /** Reference phrase pattern. */
        REFER(IdentityReferencePattern.getFullPattern()),

        /**
         * Empty text pattern. The pattern is determined by {@link TextFormattedPatterns
         * Constructor}
         */
        TEXT("");

        private final String rawPattern;

        TextFormattedParts(String pattern) {
            rawPattern = pattern;
        }

        @Override
        public String getPatternName() {
            return name();
        }

        @Override
        public String getRawPattern() {
            Preconditions.checkState(this != TEXT, "Text do not have a pattern");
            return rawPattern;
        }
    }

    private final boolean withRefers;
    private TextSpanPatterns textPattern;
    private Pattern matchPattern;
    private String rawPattern;

    TextFormattedPatterns(TextSpanPatterns pattern, boolean refers) {
        textPattern = pattern;
        withRefers = refers;
    }

    @Override
    public String getPatternName() {
        return name();
    }

    @Override
    public String getRawPattern() {
        if (rawPattern == null) rawPattern = getFullPattern(false);
        return rawPattern;
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
        if (matchPattern == null) matchPattern = Pattern.compile(getFullPattern(true));
        return matchPattern.matcher(text);
    }

    private String getFullPattern(boolean withName) {
        StringBuilder builder = new StringBuilder();
        builder.append(TextFormattedParts.BOLD.getPattern(withName) + "|");
        builder.append(TextFormattedParts.UNDERLINE.getPattern(withName) + "|");
        builder.append(TextFormattedParts.ITALICS.getPattern(withName) + "|");
        builder.append(TextFormattedParts.TODO.getPattern(withName) + "|");
        if (withRefers) builder.append(TextFormattedParts.REFER.getPattern(withName) + "|");
        String raw = textPattern.getRawPattern();
        builder.append(withName ? PatternEnum.namePattern(TextFormattedParts.TEXT.getPatternName(), raw) : raw);
        if (withName) return builder.toString();
        return "(" + builder.toString() + ")+";
    }
}
