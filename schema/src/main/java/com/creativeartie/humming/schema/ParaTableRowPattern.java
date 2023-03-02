package com.creativeartie.humming.schema;

import java.util.regex.*;

public enum ParaTableRowPattern implements PatternEnum {
    SEP("\\|"), TEXT(TextSpanPatterns.CELL.getRawPattern()), END("\\|?\\n?"), STARTER("\\|\\n?");

    private static String fullPattern;
    private static Pattern matchPattern;
    private static Pattern checkPattern;

    private static String getFullPattern(boolean withName) {
        return
        // @formatter:off
            "(" +
                "(" +
                    SEP.getPattern(withName) +
                    TEXT.getPattern(withName) +
                ")+" + END.getPattern(withName) +
            ")|" + STARTER.getPattern(withName);
         // @formatter:on
    }

    public static String getFullPattern() {
        if (fullPattern == null) fullPattern = getFullPattern(false);
        return fullPattern;
    }

    public static Matcher matcher(String text) {

        if (matchPattern == null) {
            checkPattern = Pattern.compile("^" + getFullPattern(true) + "$");
            matchPattern = Pattern.compile(
            // @formatter:off
                "(" +
                    SEP.getPattern(true) +
                    TEXT.getPattern(true) +
                ")|" + END.getPattern(true));
            // @formatter:on
        }
        final Matcher match = checkPattern.matcher(text);
        if (!match.find()) return null;
        return matchPattern.matcher(text);
    }

    private String rawPattern;

    private ParaTableRowPattern(String pattern) {
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
