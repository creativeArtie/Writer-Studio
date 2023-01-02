package com.creativeartie.humming.schema;

import java.util.regex.*;

public enum TableCellPatterns implements PatternEnum {
    TEXT {
        @Override
        String getFullPattern(boolean withName) {
            return getExpand(withName) + CellParts.TEXT.getPattern(withName);
        }
    },
    HEADING

    {
        @Override
        String getFullPattern(boolean withName) {
            return getExpand(withName) + CellParts.HEADER.getPattern(withName) + CellParts.TEXT.getPattern(withName);
        }
    };

    String getExpand(boolean withName) {
        return //@formatter:off
            CellParts.START.getPattern(withName) + "(" +
                CellParts.COLS.getPattern(withName) + "*|" +
                CellParts.ROWS.getPattern(withName) +
            ")";
        //@formatter:on
    }

    public enum CellParts implements PatternEnum {
        START("\\|"), BORDER("\\|"), TEXT(TextLinePatterns.CELL.getRawPattern()), HEADER("="), ROWS("\\^"), COLS("\\|");

        private final String patternText;

        CellParts(String pattern) {
            patternText = pattern;
        }

        @Override
        public String getRawPattern() {
            return patternText;
        }

        @Override
        public String getPatternName() {
            return name();
        }
    }

    abstract String getFullPattern(boolean withName);

    private String rawPattern;
    private Pattern matchPattern;

    @Override
    public String getRawPattern() {
        if (rawPattern == null) rawPattern = getFullPattern(false);
        return rawPattern;
    }

    public Matcher matcher(String text) {
        if (matchPattern == null) matchPattern = Pattern.compile("^" + getFullPattern(true) + "$");
        Matcher match = matchPattern.matcher(text);
        return match.find() ? match : null;
    }

    @Override
    public String getPatternName() {
        return name();
    }
}
