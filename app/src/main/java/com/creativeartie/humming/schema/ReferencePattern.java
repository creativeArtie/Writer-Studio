package com.creativeartie.humming.schema;

import java.util.regex.*;

import com.google.common.base.*;

public enum ReferencePattern implements PatternEnum {
    FOOTNOTE("\\^"), ENDNOTE("\\*"), SOURCE("\\>"), REF("\\%"), START("\\{"),
    ID(IdentityPattern.getFullPattern()), END("\\}");

    private static String fullPattern;
    private static Pattern matchPattern;

    public static String getFullPattern() {
        if (fullPattern == null) {
            fullPattern = getFullPattern(false);
        }
        return fullPattern;
    }

    private static String getFullPattern(boolean withName) {
        return
        // @formatter:off
            START.getPattern(withName) + "(" +
                FOOTNOTE.getPattern(withName) + "|" +
                ENDNOTE.getPattern(withName) + "|" +
                SOURCE.getPattern(withName) + "|" +
                REF.getPattern(withName) +
            ")" + ID.getPattern(withName) +
            END.getPattern(withName) + "?";
         // @formatter:on

    }

    public static Matcher matcher(String text) {
        if (matchPattern == null) {
            matchPattern = Pattern.compile("^" + getFullPattern(true) + "$");
        }
        Matcher answer = matchPattern.matcher(text);
        Preconditions.checkArgument(answer.find(), "Pattern not found");
        return answer;
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
}
