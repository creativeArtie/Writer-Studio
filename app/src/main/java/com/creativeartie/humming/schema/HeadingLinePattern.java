package com.creativeartie.humming.schema;

import java.util.regex.*;

import com.google.common.base.*;

public enum HeadingLinePattern implements PatternEnum {
    OUTLINE("!"), LEVEL("\\={1,6}"),
    TEXT(FormattedPattern.getFullPattern(BasicTextPatterns.HEADING)),
    STATUS("\\#[a-zA-Z]*"), DETAILS(BasicTextPatterns.TEXT.getRawPattern());

    public enum StatusPattern {
        STUB, OUTLINE, DRAFT, FINAL, OTHERS;

        private static Pattern testPattern;

        public static StatusPattern getStatus(String text) {
            if (testPattern == null) {
                testPattern = Pattern.compile("\\#[^\\n]+");
            }
            Preconditions.checkArgument(
                testPattern.matcher(text).find(), "Pattern not found"
            );
            String name = text.toUpperCase();

            for (StatusPattern value : values()) {
                if (name.startsWith("#" + value.name())) {
                    return value;
                }

            }
            return OTHERS;
        }

    }

    private final String rawPattern;

    private static String getFullPattern(boolean withName) {
        return
        // @formatter:off
            OUTLINE.getPattern(withName) + "?" +
            LEVEL.getPattern(withName) +
            TEXT.getPattern(withName) + "?" +
            "(" + STATUS.getPattern(withName) + DETAILS.getPattern(withName) + "?)?";
        // @formatter:on
    }

    private static String fullPattern;
    private static Pattern matchPattern;

    public static String getFullPattern() {
        if (fullPattern == null) {
            fullPattern = getFullPattern(false);
        }
        return fullPattern;
    }

    private static Matcher createMatcher(String text) {
        if (matchPattern == null) {
            matchPattern = Pattern.compile("^" + getFullPattern(true) + "$");
        }
        return matchPattern.matcher(text);
    }

    public static boolean find(String text) {
        return createMatcher(text).find();
    }

    public static Matcher matcher(String text) {
        Matcher matcher = createMatcher(text);
        Preconditions.checkArgument(matcher.find(), "Pattern not found");
        return matcher;

    }

    private HeadingLinePattern(String pattern) {
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

    @Override
    public boolean runFind() {
        return false;
    }

}
