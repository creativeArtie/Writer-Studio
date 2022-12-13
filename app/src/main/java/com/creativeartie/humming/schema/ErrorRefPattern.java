package com.creativeartie.humming.schema;

import java.util.regex.*;

import com.google.common.base.*;

public enum ErrorRefPattern implements PatternEnum {
    START("\\{"), TEXT(BasicTextPatterns.SPECIAL.getRawPattern()), END("\\}");

    private static String fullPattern;
    private static Pattern matchPattern;

    private static String getFullPattern(boolean withName) {
        return START.getPattern(withName) + TEXT.getPattern(withName) + "?" +
            END.getPattern(withName) + "?";
    }

    public static String getFullPattern() {
        if (fullPattern == null) fullPattern = getFullPattern(false);
        return fullPattern;
    }

    public static Matcher matcher(String text) {
        if (matchPattern == null) {
            matchPattern = Pattern.compile("^" + getFullPattern(true) + "$");
        }
        Matcher answer = matchPattern.matcher(text);
        Preconditions.checkArgument(answer.find(), "Pattern not matched");
        return answer;
    }

    private final String pattern;

    private ErrorRefPattern(String pat) {
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
