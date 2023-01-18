package com.creativeartie.humming.schema;

import java.util.regex.*;

/**
 * Patterns for simple lines (without endings). The list of lines included are:
 * <ul>
 * <li>Quote: quote blocks for printing
 * <li>Agenda: a line describing that something need to be do (line variant of
 * {@link TodoPattern}).
 * <li>Text: a normal text
 * <li>Break: a section break
 * </ul>
 * This should be call last
 *
 * @see HeadingLinePattern
 * @see NoteLinePatterns
 * @see ListLinePattern
 * @see ReferenceLinePatterns
 */
public enum LineSpanPatterns implements PatternEnum {
    QUOTE(LineSpanParts.QUOTER, LineSpanParts.FORMATTED), AGENDA(LineSpanParts.TODOER, LineSpanParts.TEXT),
    TEXT(LineSpanParts.FORMATTED), BREAK(LineSpanParts.BREAKER);

    public enum LineSpanParts implements PatternEnum {
        QUOTER("\\>"), TODOER("\\!"), FORMATTED("(" + LineTextPatterns.BASIC.getRawPattern() + ")?"),
        TEXT("(" + TextSpanPatterns.SIMPLE.getRawPattern() + ")?"), BREAKER("\\*+"), ENDER("\n?");

        private String rawPattern;

        LineSpanParts(String pattern) {
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

    private final LineSpanParts[] patternParts;
    private Pattern matchPattern;
    private String rawPattern;

    LineSpanPatterns(LineSpanParts... parts) {
        patternParts = parts;
    }

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

    @Override
    public String getRawPattern() {
        if (rawPattern == null) rawPattern = buildPattern(false);
        return rawPattern;
    }

    @Override
    public String getPatternName() {
        return name();
    }
}
