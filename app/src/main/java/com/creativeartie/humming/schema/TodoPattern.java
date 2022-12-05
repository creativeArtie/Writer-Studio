package com.creativeartie.humming.schema;

import java.util.regex.*;

public enum TodoPattern implements PatternEnum {
    START("{!"), TEXT(BasicTextPatterns.SPECIAL.getRawPattern()), END("}");

    private static String fullPattern;
    private static Pattern checkPattern;
    private static Pattern matchPattern;

    public static String getFullPattern() {
        if (fullPattern == null) {
            // @formatter:off
            fullPattern =
                START.getRawPattern() +
                TEXT.getRawPattern() + "?" +
                END.getRawPattern() + "?"
                ;
             // @formatter:on
        }
        return fullPattern;
    }

    public static Matcher matcher(String text) {
        if (checkPattern == null) {
            checkPattern = Pattern.compile(getFullPattern());
            matchPattern = PatternEnum.compilePattern(values());
        }
        assert checkPattern.matcher(text).find();
        return matchPattern.matcher(text);
    }

    private final String pattern;

    private TodoPattern(String pat) {
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
