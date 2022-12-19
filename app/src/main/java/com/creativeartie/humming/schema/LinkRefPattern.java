package com.creativeartie.humming.schema;

import java.util.regex.*;

import com.google.common.base.*;

/**
 * Reference link span to a {@link ReferenceLinePatterns#LINK}
 *
 * @see LinkRefSpan
 * @see ReferenceLinePatterns
 */
public enum LinkRefPattern implements PatternEnum {
    START("\\{@"), ID(IdentityPattern.getFullPattern()), SEP("\\|"), TEXT(BasicTextPatterns.SPECIAL.getRawPattern()),
    END("\\}"), ERROR(BasicTextPatterns.SPECIAL.getRawPattern());

    private static String fullPattern;
    private static Pattern basePattern;

    private static String getFullPattern(boolean withName) {
        return
        // @formatter:off
            START.getPattern(withName) +
            "(" +
                ID.getPattern(withName) +
                "(" +
                    SEP.getPattern(withName) + TEXT.getPattern(withName) + "?" +
                ")?|" + ERROR.getPattern(withName) +
            ")" + END.getPattern(withName) + "?"
            ;
         // @formatter:on
    }

    public static String getFullPattern() {
        if (fullPattern == null) {
            fullPattern = getFullPattern(false);
        }
        return fullPattern;
    }

    public static Matcher matcher(String text) {
        if (basePattern == null) {
            basePattern = Pattern.compile("^" + getFullPattern(true) + "$");
        }
        final Matcher match = basePattern.matcher(text);
        Preconditions.checkArgument(match.find(), "Pattern does not match");
        return match;
    }

    private final String pattern;

    LinkRefPattern(String pat) {
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
