package com.creativeartie.humming.schema;

import java.util.regex.*;

public enum FormattedPattern implements PatternEnum {
    BOLD("\\*"), UNDERLINE("_"), ITALICS("`");

    private static String fullPattern;
    private static Pattern checkPattern;
    private static Pattern matchPattern;

    public static String getFullPattern() {
        if (fullPattern == null) {
            fullPattern = "";
            for (PatternEnum pattern : values()) {
                fullPattern += pattern.getRawPattern() + "|";
            }
            fullPattern += LinkPattern.getFullPattern() + "|";
            fullPattern += TodoPattern.getFullPattern() + "|";
            fullPattern += ReferencePattern.getFullPattern();

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

    private FormattedPattern(String pat) {
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
