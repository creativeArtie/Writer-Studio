package com.creativeartie.humming.schema;

import java.util.regex.*;

/**
 * List of references, excluding links. Links references are defined in
 * {@link ImageRefPattern}. Spans declared here are:
 * <ul>
 * <li>{@link ReferenceLinePatterns#FOOTNOTE foot note}
 * <li>{@link ReferenceLinePatterns#ENDNOTE end note}
 * <li>{@link NoteLinePatterns#HEADING sources and notes}
 * <li>References TODO add reference items
 * </ul>
 *
 * @see ImageRefPattern
 * @see TodoPattern
 * @see ImageDirectPattern
 * @see ErrorRefPattern
 */
public enum ReferencePattern implements PatternEnum {
    FOOTNOTE("\\^"), ENDNOTE("\\*"), SOURCE("\\>"), REF("\\%"), IMAGE("\\+"), START("\\{"),
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
                REF.getPattern(withName) + "|" +
                IMAGE.getPattern(withName) +
            ")" + ID.getPattern(withName) +
            END.getPattern(withName) + "?";
         // @formatter:on
    }

    public static Matcher matcher(String text) {
        if (matchPattern == null) {
            matchPattern = Pattern.compile("^" + getFullPattern(true) + "$");
        }
        final Matcher answer = matchPattern.matcher(text);
        if (answer.find()) return answer;
        return null;
    }

    private final String pattern;

    ReferencePattern(String pat) {
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
