package com.creativeartie.humming.schema;

import java.util.regex.*;

/**
 * Patterns for simple lines. The list of lines included are:
 * <ul>
 * <li>Quote: quote blocks for printing
 * <li>Agenda: a line describing that something need to be do (line variant of
 * {@link IdentityTodoPattern}).
 * <li>Text: a normal text
 * <li>Break: a section break
 * </ul>
 *
 * @see ParaHeadingPattern
 * @see ParaListPattern
 * @see ParaNotePatterns
 * @see ParaReferencePatterns
 * @see ParaTableRowPattern
 * @see com.creativeartie.humming.document.Para#newLine where is used
 */
@SuppressWarnings("nls")
public enum ParaBasicPatterns implements PatternEnum {
    /** Quote paragraph pattern. */
    QUOTE(LineSpanParts.QUOTER, LineSpanParts.FORMATTED),
    /** Agenda paragraph pattern. */
    AGENDA(LineSpanParts.TODOER, LineSpanParts.TEXT),
    /** Normal paragraph pattern. */
    TEXT(LineSpanParts.FORMATTED),
    /** Section break pattern. */
    BREAK(LineSpanParts.BREAKER);

    /** Parts of a line. */
    public enum LineSpanParts implements PatternEnum {
        /** Quote paragraph start pattern. */
        QUOTER("\\>"),
        /** Agenda paragraph start pattern. */
        TODOER("\\!"),
        /** Section break paragraph start pattern. */
        BREAKER("\\*+"),

        /** Basic text pattern for agenda. */
        TEXT("(" + TextSpanPatterns.ERROR.getRawPattern() + ")?"),
        /** Formatted text pattern */
        FORMATTED("(" + TextFormattedPatterns.BASIC.getRawPattern() + ")?"),

        /** Line ending pattern */
        ENDER("\n?");

        private String rawPattern;

        LineSpanParts(String pattern) {
            rawPattern = pattern;
        }

        @Override
        public String getPatternName() {
            return name();
        }

        @Override
        public String getRawPattern() {
            return rawPattern;
        }
    }

    private final LineSpanParts[] patternParts;
    private Pattern matchPattern;
    private String rawPattern;

    ParaBasicPatterns(LineSpanParts... parts) {
        patternParts = parts;
    }

    @Override
    public String getPatternName() {
        return name();
    }

    @Override
    public String getRawPattern() {
        if (rawPattern == null) rawPattern = buildPattern(false);
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
        if (matchPattern == null) matchPattern = Pattern.compile("^" + buildPattern(true) + "$");
        final Matcher match = matchPattern.matcher(text);
        if (match.find()) return match;
        return null;
    }

    private String buildPattern(boolean withName) {
        final StringBuilder builder = new StringBuilder();
        for (final LineSpanParts part : patternParts) builder.append(part.getPattern(withName));
        builder.append(LineSpanParts.ENDER.getPattern(withName));
        return builder.toString();
    }
}
