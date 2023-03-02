package com.creativeartie.humming.schema;

import java.util.regex.*;

/**
 * List of references, excluding links. Links references are defined in
 * {@link ImageRefPattern}. Spans declared here are:
 * <ul>
 * <li>{@link ParaReferencePatterns#FOOTNOTE foot note}
 * <li>{@link ParaReferencePatterns#ENDNOTE end note}
 * <li>{@link ParaNotePatterns#SUMMARY sources and notes}
 * <li>References TODO add reference items
 * </ul>
 *
 * @see ImageRefPattern
 * @see IdentityTodoPattern
 * @see ImageDirectPattern
 * @see ErrorRefPattern
 */
public enum IdentityReferencePattern implements PatternEnum {
    FOOTREF("\\^"), ENDREF("\\*"), CITEREF("\\>"), METAREF("\\%"), IMAGE("\\+"), START("\\{"),
    ID(IdentityPattern.getFullPattern()), ERROR(TextSpanPatterns.SPECIAL.getRawPattern()), END("\\}");

    private static String fullPattern;
    private static Pattern matchPattern;

    public static String getFullPattern() {
        if (fullPattern == null) fullPattern = getFullPattern(false);
        return fullPattern;
    }

    private static String getFullPattern(boolean withName) {
        return
        // @formatter:off
            START.getPattern(withName) + "((" +
                "(" +
                    FOOTREF.getPattern(withName) + "|" +
                    ENDREF.getPattern(withName) + "|" +
                    CITEREF.getPattern(withName) + "|" +
                    METAREF.getPattern(withName) + "|" +
                    IMAGE.getPattern(withName) +
                ")" + ID.getPattern(withName) +
                ")|(" + ERROR.getPattern(withName) + ")?" +
            ")" + END.getPattern(withName) + "?";
         // @formatter:on
    }

    public static Matcher matcher(String text) {
        if (matchPattern == null) matchPattern = Pattern.compile("^" + getFullPattern(true) + "$");
        final Matcher answer = matchPattern.matcher(text);
        if (answer.find()) return answer;
        return null;
    }

    private final String pattern;

    IdentityReferencePattern(String pat) {
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
