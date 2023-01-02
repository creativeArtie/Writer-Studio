package com.creativeartie.humming.schema;

import java.util.regex.*;

/**
 * Base class of all Pattern
 */
public interface PatternEnum {
    /**
     * Get the raw pattern. Does not contain any name
     *
     * @return the pattern
     *
     * @see #getPattern(boolean)
     */
    String getRawPattern();

    /**
     * Get the <b>name</b> of the pattern. Usually {@linkplain Enum#name()}.
     *
     * @return pattern name
     */
    String getPatternName();

    /**
     * Get the name along with the pattern
     *
     * @return the named pattern
     *
     * @see #getPattern(boolean) the pattern to use
     * @see #getPatternName() the name of the pattern
     * @see #namePattern(String, String)
     */
    default String getNamedPattern() {
        return namePattern(getPatternName(), getRawPattern());
    }

    /**
     * Get the patterns with or without the name. It will also add the brackets
     * around the pattern.
     *
     * @param withName
     *
     * @return the pattern
     *
     * @see #getRawPattern()
     * @see #getNamedPattern()
     */
    default String getPattern(boolean withName) {
        return withName ? getNamedPattern() : "(" + getRawPattern() + ")";
    }

    /**
     * Use the matcher. Almost the same as
     * {@code matcher.group(PatternEnum.VALUE.getPatternName()} (aka.
     * {@code PatternEnum.VALUE.group(matcher)} )
     *
     * @param matcher
     *        matcher to use
     *
     * @return result of matcher or null (if none found)
     */
    default String group(Matcher matcher) {
        return matcher.group(getPatternName());
    }

    /**
     * Names a pattern
     *
     * @param name
     * @param pattern
     *
     * @return pattern
     *
     * @see #getNamedPattern()
     */
    static String namePattern(String name, String pattern) {
        return "(?<" + name + ">" + pattern + ")";
    }
}
