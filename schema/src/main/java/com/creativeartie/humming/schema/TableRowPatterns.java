package com.creativeartie.humming.schema;

import java.util.regex.*;

public enum TableRowPatterns implements PatternEnum {
    BASIC {
        @Override
        String getCheckPattern() {
            return //@formatter:off
                TableRowParts.TEXT.getPattern(false) + "+" +
                TableRowParts.END.getPattern(false)
            ;
            //@formatter:on
        }

        @Override
        String getMatchPattern() {
            return//@formatter:off
                    TableRowParts.TEXT.getPattern(true) + "|" +
                    TableRowParts.END.getPattern(true)
                ;
                //@formatter:on
        }
    },
    HEADER {
        @Override
        String getCheckPattern() {
            return //@formatter:off
                TableRowParts.HEADING.getPattern(false) +
                TableRowParts.SUBHEAD.getPattern(false) + TableRowParts.END.getPattern(false)
            ;
            //@formatter:on
        }

        @Override
        String getMatchPattern() {
            return//@formatter:off
                    TableRowParts.SUBHEAD.getPattern(true) + "|" +
                    TableRowParts.END.getPattern(true)
                ;
                //@formatter:on
        }
    };

    public enum TableRowParts implements PatternEnum {
        HEADING(TableCellPatterns.HEADING.getRawPattern()), SUBHEAD(TableCellPatterns.SUBHEAD.getRawPattern()),
        TEXT(TableCellPatterns.TEXT.getRawPattern()), END("\\|?\\n?");

        private final String rawPattern;

        TableRowParts(String pattern) {
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

    abstract String getCheckPattern();

    abstract String getMatchPattern();

    private String rawPattern;
    private Pattern checkPattern;
    private Pattern matchPattern;

    @Override
    public String getRawPattern() {
        if (rawPattern == null) rawPattern = getCheckPattern();

        return rawPattern;
    }

    public Matcher matcher(String text) {
        if (checkPattern == null) checkPattern = Pattern.compile("^" + getCheckPattern() + "$");
        Matcher match = checkPattern.matcher(text);
        if (match.find()) {
            if (matchPattern == null) matchPattern = Pattern.compile(getMatchPattern());
            return matchPattern.matcher(text);
        }
        return null;
    }

    @Override
    public String getPatternName() {
        return name();
    }
}
