package com.creativeartie.humming.schema;

import java.util.regex.*;

import com.google.common.base.*;

public enum IdentityPattern implements PatternEnum {
    SEP(":"), NAME(BasicTextPatterns.ID.getRawPattern());

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

    public static Matcher matcher(String text) {
        if (checkPattern == null) {
            checkPattern = Pattern.compile("^" + getFullPattern() + "$");
            final StringBuilder pattern = new StringBuilder();
            for (final PatternEnum value : IdentityPattern.values()) {
                if (!pattern.isEmpty()) {
                    pattern.append("|");
                }
                pattern.append("(" + value.getNamedPattern() + ")");
            }
            matchPattern = Pattern.compile(pattern.toString());
        }
        Preconditions.checkArgument(checkPattern.matcher(text).find(), "Pattern does not match Id");
        return matchPattern.matcher(text);
    }

    private String pattern;

    IdentityPattern(String pat) {
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

    @Override
    public boolean runFind() {
        return true;
    }
}
