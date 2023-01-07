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
public enum BasicLinePatterns implements PatternEnum {
    QUOTE(BasicLinePart.QUOTER, BasicLinePart.FORMATTED), AGENDA(BasicLinePart.TODOER, BasicLinePart.TEXT),
    TEXT(BasicLinePart.FORMATTED), BREAK(BasicLinePart.BREAKER);

    public enum BasicLinePart implements PatternEnum {
        QUOTER("\\>"), TODOER("\\!"), FORMATTED("(" + LineTextPatterns.BASIC.getRawPattern() + ")?"),
        TEXT("(" + BasicTextPatterns.SIMPLE.getRawPattern() + ")?"), BREAKER("\\*+"), ENDER("\n?");

        private String rawPattern;

        BasicLinePart(String pattern) {
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

    private final BasicLinePart[] patternParts;
    private Pattern matchPattern;
    private String rawPattern;

    BasicLinePatterns(BasicLinePart... parts) {
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
        for (final BasicLinePart part : patternParts) builder.append(part.getPattern(withName));
        builder.append(BasicLinePart.ENDER.getPattern(withName));
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
