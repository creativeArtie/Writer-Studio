package com.creativeartie.humming.schema;

import java.util.regex.*;

import com.google.common.base.*;

public enum BasicLinePatterns implements PatternEnum {
    QUOTE(BasicLinePart.QUOTER, BasicLinePart.FORMATTED),
    AGENDA(BasicLinePart.TODOER, BasicLinePart.TEXT),
    TEXT(BasicLinePart.FORMATTED), BREAK(BasicLinePart.BREAKER);

    enum BasicLinePart implements PatternEnum {
        QUOTER("\\>"), TODOER("\\!"),
        FORMATTED(
            FormattedPattern.getFullPattern(BasicTextPatterns.TEXT) + "?"
        ), TEXT(BasicTextPatterns.TEXT.getRawPattern() + "?"), BREAKER("\\*+");

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

    private Matcher matches(String text) {
        if (matchPattern == null) {
            matchPattern = Pattern.compile("^" + buildPattern(true) + "$");
        }
        return matchPattern.matcher(text);
    }

    public boolean find(String text) {
        return matches(text).find();
    }

    public Matcher matcher(String text) {
        Matcher match = matches(text);
        Preconditions.checkArgument(match.find(), "Pattern not found");
        return match;
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
