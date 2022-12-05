package com.creativeartie.humming.schema;

import java.util.regex.*;

public enum ReferencePattern implements PatternEnum {
    FOOTNOTE("\\^"), ENDNOTE("\\*"), SOURCE("\\>"), REF("\\%"), START("\\{"),
    ID(IdentityPattern.getFullPattern()), END("\\}");

    private static String fullPattern;
    private static Pattern checkPattern;
    private static Pattern matchPattern;

    public static String getFullPattern() {
        if (fullPattern == null) {
            fullPattern =
         // @formatter:off
                START.getRawPattern() + "(" +
                    FOOTNOTE.getRawPattern() + "|" +
                    ENDNOTE.getRawPattern() + "|" +
                    SOURCE.getRawPattern() + "|" +
                    REF.getRawPattern() +
                ")" + ID.getRawPattern() + "?" +
                END.getRawPattern() + "?";
         // @formatter:on
        }
        return fullPattern;
    }

    public static Matcher matcher(String text) {
        if (checkPattern == null) {
            checkPattern = Pattern.compile(getFullPattern());
            matchPattern =
                PatternEnum.compilePattern(ReferencePattern.values());
        }
        assert checkPattern.matcher(text).find();
        return matchPattern.matcher(text);
    }

    private final String pattern;

    private ReferencePattern(String pat) {
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
