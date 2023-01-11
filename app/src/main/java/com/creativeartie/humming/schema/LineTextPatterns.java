package com.creativeartie.humming.schema;

import java.util.regex.*;

import com.google.common.base.*;

public enum LineTextPatterns implements PatternEnum {
    BASIC(BasicTextPatterns.TEXT, true), HEADING(BasicTextPatterns.HEADING, true), NOTE(BasicTextPatterns.TEXT, false),
    CELL(BasicTextPatterns.CELL, true);

    private final boolean withRefers;

    private String getFullPattern(boolean withName) {
        StringBuilder builder = new StringBuilder();
        builder.append(LineTextPart.BOLD.getPattern(withName) + "|");
        builder.append(LineTextPart.UNDERLINE.getPattern(withName) + "|");
        builder.append(LineTextPart.ITALICS.getPattern(withName) + "|");
        builder.append(LineTextPart.TODO.getPattern(withName) + "|");
        if (withRefers) builder.append(LineTextPart.REFER.getPattern(withName) + "|");
        builder.append(
                withName ? PatternEnum.namePattern(LineTextPart.TEXT.getPatternName(), textPattern.getRawPattern()) :
                        textPattern.getRawPattern()
        );
        if (withName) return builder.toString();
        return "(" + builder.toString() + ")+";
    }

    private BasicTextPatterns textPattern;

    LineTextPatterns(BasicTextPatterns pattern, boolean refers) {
        textPattern = pattern;
        withRefers = refers;
    }

    public enum LineTextPart implements PatternEnum {
        BOLD("\\*"), UNDERLINE("_"), ITALICS("`"), TODO(TodoPattern.getFullPattern()),
        REFER(ReferencePattern.getFullPattern()), TEXT("");

        private final String rawPattern;

        LineTextPart(String pattern) {
            rawPattern = pattern;
        }

        @Override
        public String getRawPattern() {
            Preconditions.checkState(this != TEXT, "Text do not have a pattern");
            return rawPattern;
        }

        @Override
        public String getPatternName() {
            return name();
        }
    }

    private Pattern matchPattern;
    private String rawPattern;

    public Matcher matcher(String text) {
        if (matchPattern == null) matchPattern = Pattern.compile(getFullPattern(true));
        return matchPattern.matcher(text);
    }

    @Override
    public String getRawPattern() {
        if (rawPattern == null) rawPattern = getFullPattern(false);
        return rawPattern;
    }

    @Override
    public String getPatternName() {
        return name();
    }

    public boolean hasRefer() {
        return withRefers;
    }
}
