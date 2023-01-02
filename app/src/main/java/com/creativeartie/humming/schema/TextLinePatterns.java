package com.creativeartie.humming.schema;

import java.util.regex.*;

import com.google.common.base.*;

public enum TextLinePatterns implements PatternEnum {
    BASIC(BasicTextPatterns.TEXT, true), HEADING(BasicTextPatterns.HEADING, true), NOTE(BasicTextPatterns.TEXT, false),
    CELL(BasicTextPatterns.CELL, true);

    private final boolean withRefers;

    private String getFullPattern(boolean withName) {
        StringBuilder builder = new StringBuilder();
        builder.append(TextLinePart.BOLD.getPattern(withName) + "|");
        builder.append(TextLinePart.UNDERLINE.getPattern(withName) + "|");
        builder.append(TextLinePart.ITALICS.getPattern(withName) + "|");
        builder.append(TextLinePart.TODO.getPattern(withName) + "|");
        if (withRefers) {
            builder.append(TextLinePart.REFER.getPattern(withName) + "|");
        }
        builder.append(
                withName ? PatternEnum.namePattern(TextLinePart.TEXT.getPatternName(), textPattern.getRawPattern()) :
                        textPattern.getRawPattern()
        );
        if (withName) {
            return builder.toString();
        } else {
            return "(" + builder.toString() + ")*";
        }
    }

    private BasicTextPatterns textPattern;

    private TextLinePatterns(BasicTextPatterns pattern, boolean refers) {
        textPattern = pattern;
        withRefers = refers;
    }

    public enum TextLinePart implements PatternEnum {
        BOLD("\\*"), UNDERLINE("_"), ITALICS("`"), TODO(TodoPattern.getFullPattern()),
        REFER(ReferencePattern.getFullPattern()), TEXT("");

        private final String rawPattern;

        private TextLinePart(String pattern) {
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
        if (matchPattern == null) {
            matchPattern = Pattern.compile(getFullPattern(true));
        }
        Matcher match = matchPattern.matcher(text);
        return match;
    }

    @Override
    public String getRawPattern() {
        if (rawPattern == null) {
            rawPattern = getFullPattern(false);
        }
        return rawPattern;
    }

    @Override
    public String getPatternName() {
        return name();
    }
}
