package com.creativeartie.humming.schema;

import java.util.regex.*;

import com.google.common.base.*;

/**
 * Headings for print and to describe scenes in an outline
 */
public enum HeadingLinePattern implements PatternEnum {
    OUTLINE("!"), LEVEL("\\={1,6}"), TEXT(TextLinePatterns.HEADING.getRawPattern()), STATUS("\\#[a-zA-Z]*"),
    DETAILS(BasicTextPatterns.TEXT.getRawPattern()), ENDER("\n?");

    public enum StatusPattern {
        STUB, OUTLINE, DRAFT, FINAL, OTHERS;

        private static Pattern testPattern;

        public static StatusPattern getStatus(String text) {
            if (testPattern == null) testPattern = Pattern.compile("\\#[^\\n]+");
            Preconditions.checkArgument(testPattern.matcher(text).find(), "Pattern not found");
            final String name = text.toUpperCase();
            for (final StatusPattern value : values()) if (name.startsWith("#" + value.name())) return value;
            return OTHERS;
        }
    }

    private final String rawPattern;

    private static String getFullPattern(boolean withName) {
        return
        // @formatter:off
            OUTLINE.getPattern(withName) + "?" +
            LEVEL.getPattern(withName) +
            TEXT.getPattern(withName) + "?" +
            "(" + STATUS.getPattern(withName) + DETAILS.getPattern(withName) + "?)?" +
            ENDER.getPattern(withName);
        // @formatter:on
    }

    private static String fullPattern;
    private static Pattern matchPattern;

    public static String getFullPattern() {
        if (fullPattern == null) fullPattern = getFullPattern(false);
        return fullPattern;
    }

    public static Matcher matcher(String text) {
        if (matchPattern == null) matchPattern = Pattern.compile("^" + getFullPattern(true) + "$");
        final Matcher matcher = matchPattern.matcher(text);
        if (matcher.find()) return matcher;
        return null;
    }

    HeadingLinePattern(String pattern) {
        rawPattern = pattern;
    }

    @Override
    public String getRawPattern() {
        return rawPattern;
    }

    @Override
    public String getPatternName() {
        return name();
    }
}
