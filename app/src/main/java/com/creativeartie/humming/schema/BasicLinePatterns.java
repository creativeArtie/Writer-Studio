package com.creativeartie.humming.schema;

import java.util.regex.*;

public enum BasicLinePatterns implements PatternEnum {
    QUOTE(BasicLinePart.QUOTER, BasicLinePart.FORMATTED),
    AGENDA(BasicLinePart.TODOER, BasicLinePart.TEXT),
    TEXT(BasicLinePart.FORMATTED), BREAK(BasicLinePart.BREAKER);

    enum BasicLinePart implements PatternEnum {
        QUOTER("\\>"), TODOER("\\!"),
        FORMATTED(
            FormattedPattern.getFullPattern(BasicTextPatterns.TEXT) + "?"
        ), TEXT("(" + BasicTextPatterns.TEXT.getRawPattern() + ")?"),
        BREAKER("\\*+");

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

    private BasicLinePatterns(BasicLinePart... parts) {
        patternParts = parts;

    }

    public Matcher matcher(String text) {
        if (matchPattern == null) {
            matchPattern = Pattern.compile("^" + buildPattern(true) + "$");
        }
        Matcher match = matchPattern.matcher(text);
        if (match.find()) return match;
        return null;
    }

    private String buildPattern(boolean withName) {
        StringBuilder builder = new StringBuilder();
        for (BasicLinePart part : patternParts) {
            builder.append(part.getPattern(withName));
        }
        return builder.toString();
    }

    @Override
    public String getRawPattern() {
        if (rawPattern == null) {
            rawPattern = buildPattern(false);
        }
        return rawPattern;
    }

    @Override
    public String getPatternName() {
        return name();
    }
}
