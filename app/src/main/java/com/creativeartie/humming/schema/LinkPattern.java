package com.creativeartie.humming.schema;

import java.util.regex.*;

import com.google.common.base.*;

public enum LinkPattern implements PatternEnum {
    START("{@"), LINK(BasicTextPatterns.LINK.getRawPattern()), SEP("\\|"),
    TEXT(BasicTextPatterns.SPECIAL.getRawPattern()), END("}");

    private static String fullPattern;
    private static Pattern checkPattern;
    private static Pattern matchPattern;

    public static String getFullPattern() {
        if (fullPattern == null) {
            // @formatter:off
            fullPattern =
                START.getRawPattern() + "(" +
                    LINK.getRawPattern() + "(" +
                        SEP.getRawPattern() + TEXT.getRawPattern() + "?" +
                    ")?" +
                ")?" + END.getRawPattern() + "?"
                ;
             // @formatter:on
        }
        return fullPattern;
    }

    public static Matcher match(String text) {
        if (checkPattern == null) {
            checkPattern = Pattern.compile(getFullPattern());
            matchPattern = PatternEnum.compilePattern(values());
        }
        Preconditions.checkArgument(
            checkPattern.matcher(text).find(), "Pattern does not match"
        );
        return matchPattern.matcher(text);
    }

    private final String pattern;

    private LinkPattern(String pat) {
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
