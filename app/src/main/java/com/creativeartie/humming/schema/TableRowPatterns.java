package com.creativeartie.humming.schema;

import java.util.regex.*;

import com.google.common.base.*;

public enum TableRowPatterns implements PatternEnum {
    BASIC {
        @Override
        String getMatchPattern() {
            return //@formatter:off
                TableRowParts.TEXT.getPattern(false) +
                "(" +
                    TableRowParts.TEXT.getPattern(false) + "|" +
                    TableRowParts.HEADING.getPattern(false) +
                ")" + TableRowParts.END.getPattern(false)
            ;
            //@formatter:on
        }
    },
    HEADER {
        @Override
        String getMatchPattern() {
            return //@formatter:off
                TableRowParts.HEADING.getPattern(false) +
                "(" +
                    TableRowParts.TEXT.getPattern(false) + "|" +
                    TableRowParts.HEADING.getPattern(false) +
                ")" + TableRowParts.END.getPattern(false)
            ;
            //@formatter:on
        }
    },
    COLUMN {
        @Override
        String getMatchPattern() {
            return //@formatter:off
                TableRowParts.HEADING.getPattern(false) +
                "(" +
                    TableRowParts.TEXT.getPattern(false) + "|" +
                    TableRowParts.HEADING.getPattern(false) +
                ")" + TableRowParts.END.getPattern(false)
            ;
            //@formatter:on
        }
    };

    public enum TableRowParts implements PatternEnum {
        HEADING(TableCellPatterns.HEADING.getRawPattern()), TEXT(TableCellPatterns.TEXT.getRawPattern()),
        END("\\|?\\n?");

        private final String rawPattern;

        TableRowParts(String pattern) {
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

    abstract String getMatchPattern();

    private String rawPattern;
    private Pattern matchPattern;

    @Override
    public String getRawPattern() {
        if (rawPattern == null) {
            rawPattern = "";
            for (TableRowParts part : TableRowParts.values()) {
                if (rawPattern != "") rawPattern += "|";
                rawPattern += part.getPattern(false);
            }
        }
        return rawPattern;
    }

    public Matcher matcher(String text) {
        if (matchPattern == null) matchPattern = Pattern.compile("^" + getMatchPattern() + "$");
        Matcher match = matchPattern.matcher(text);
        return match.find() ? match : null;
    }

    @Override
    public String getPatternName() {
        return name();
    }
}
