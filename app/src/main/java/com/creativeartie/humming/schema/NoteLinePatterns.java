package com.creativeartie.humming.schema;

import java.util.regex.*;

import com.google.common.base.*;

public enum NoteLinePatterns implements PatternEnum {
    HEADING() {
        @Override
        protected String getValuePattern(boolean withName) {
            return // @formatter:off
                NoteLineParts.STARTER.getPattern(withName) +
                NoteLineParts.HEADING.getPattern(withName) +
                NoteLineParts.TITLE.getPattern(withName) +
                "(" +
                    NoteLineParts.IDER.getPattern(withName) +
                    NoteLineParts.ID.getPattern(withName) +
                ")?";
            //@formatter:on
        }
    },
    NOTE() {

        @Override
        protected String getValuePattern(boolean withName) {
            return // @formatter:off
                NoteLineParts.STARTER.getPattern(withName) +
                NoteLineParts.NOTE.getPattern(withName) +
                NoteLineParts.TEXT.getPattern(withName) + "?";
            //@formatter:on
        }

    },
    SOURCE {

        @Override
        protected String getValuePattern(boolean withName) {
            return // @formatter:off
                NoteLineParts.STARTER.getPattern(withName) +
                NoteLineParts.SOURCE.getPattern(withName) +
                NoteLineParts.FIELD.getPattern(withName) + "?" +
                NoteLineParts.SOURCE.getPattern(withName) + "?" +
                NoteLineParts.VALUE.getPattern(withName);
            //@formatter:on
        }

    };

    enum NoteLineParts implements PatternEnum {
        STARTER("!"), NOTE("%"), HEADING("="), SOURCE(">"), SOURCER(":"),
        TEXT(FormattedPattern.getFullPattern(BasicTextPatterns.TEXT)),
        TITLE(FormattedPattern.getFullPattern(BasicTextPatterns.HEADING)),
        IDER("#"), ID(IdentityPattern.getFullPattern()),
        FIELD(BasicTextPatterns.CITE.getRawPattern()),
        VALUE(BasicTextPatterns.TEXT.getRawPattern());

        private String rawPattern;

        private NoteLineParts(String pattern) {
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

    private String rawPattern;
    private Pattern matchPattern;

    @Override
    public String getRawPattern() {
        if (rawPattern == null) {
            rawPattern = getValuePattern(false);
        }
        return null;
    }

    private Matcher getMatcher(String text) {
        if (matchPattern == null) {
            matchPattern = Pattern.compile("^" + getValuePattern(true) + "$");
        }
        return matchPattern.matcher(text);
    }

    public boolean find(String text) {
        return getMatcher(text).find();
    }

    public Matcher matcher(String text) {
        Matcher match = getMatcher(text);
        Preconditions.checkArgument(match.find(), "Pattern not found.");
        return match;
    }

    protected abstract String getValuePattern(boolean withName);

    @Override
    public String getPatternName() {
        return name();
    }

}
