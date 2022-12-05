package com.creativeartie.humming.schema;

import java.util.regex.*;

public enum IdentityPattens implements PatternEnum {
    SEP("\\-"), NAME(BasicTextPatterns.ID.getRawPattern());

    private static String fullPattern;
    private static Pattern checkPattern;
    private static Pattern matchPattern;

    static String getFullPattern() {
        if (fullPattern == null) {
            fullPattern =
            // @formatter:off
                NAME.getRawPattern() +
                "(" +
                    SEP.getRawPattern() +
                    NAME.getRawPattern() +
                ")*";
             // @formatter:on
        }
        return fullPattern;
    }

    public static Matcher match(String text) {
        if (checkPattern == null) {
            checkPattern = Pattern.compile(getFullPattern());
            matchPattern = PatternEnum.compilePattern(IdentityPattens.values());
        }
        assert checkPattern.matcher(text).find();
        return matchPattern.matcher(text);
    }

    private String pattern;

    private IdentityPattens(String pat) {
        pattern = pat;
    }

    @Override
    public String getRawPattern() {
        return pattern;
    }

    @Override
    public String getPatternName() {
        return name();
    }
}
