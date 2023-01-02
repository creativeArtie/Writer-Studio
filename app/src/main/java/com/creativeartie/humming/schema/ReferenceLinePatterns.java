package com.creativeartie.humming.schema;

import java.util.regex.*;

/**
 * Lines that describes footnotes, end notes, and links.
 *
 * @see ImageRefPattern
 * @see ReferencePattern
 */
public enum ReferenceLinePatterns implements PatternEnum {
    FOOTNOTE {
        @Override
        protected String getValuePattern(boolean withName) {
            return // @formatter:off
                RefLineParts.START.getPattern(withName) +
                RefLineParts.FOOTNOTE.getPattern(withName) +
                "(" +
                    "(" +
                        RefLineParts.ID.getPattern(withName) +
                        RefLineParts.SEP.getPattern(withName) +
                        RefLineParts.TEXT.getPattern(withName) +
                    ")|" + RefLineParts.ERROR.getPattern(withName) +
                ")";
         // @formatter:on
        }
    },
    ENDNOTE {
        @Override
        protected String getValuePattern(boolean withName) {
            return // @formatter:off
                RefLineParts.START.getPattern(withName) +
                RefLineParts.ENDNOTE.getPattern(withName) +
                "(" +
                    "(" +
                        RefLineParts.ID.getPattern(withName) +
                        RefLineParts.SEP.getPattern(withName) +
                        RefLineParts.TEXT.getPattern(withName) +
                    ")|" + RefLineParts.ERROR.getPattern(withName) +
                ")";
         // @formatter:on
        }
    };

    public enum RefLineParts implements PatternEnum {
        START("!"), FOOTNOTE("\\^"), ENDNOTE("\\*"), SEP("="), ID(IdentityPattern.getFullPattern()),
        TEXT(TextLinePatterns.NOTE.getRawPattern()), ERROR(BasicTextPatterns.TEXT.getRawPattern()), ENDER("\n?");

        private final String rawPattern;

        RefLineParts(String pattern) {
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

    protected abstract String getValuePattern(boolean withName);

    private String fullPattern;
    private Pattern matchPattern;

    @Override
    public String getRawPattern() {
        if (fullPattern == null) fullPattern = getValuePattern(false) + RefLineParts.ENDER.getPattern(false);
        return fullPattern;
    }

    public Matcher matcher(String text) {
        if (matchPattern == null)
            matchPattern = Pattern.compile("^" + getValuePattern(true) + RefLineParts.ENDER.getPattern(true) + "$");
        final Matcher match = matchPattern.matcher(text);

        if (match.find()) return match;
        return null;
    }

    @Override
    public String getPatternName() {
        return name();
    }
}
