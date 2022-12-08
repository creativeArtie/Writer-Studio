package com.creativeartie.humming.schema;

import java.util.*;
import java.util.regex.*;

import com.creativeartie.humming.document.*;

interface PatternEnum {
    String getRawPattern();

    String getPatternName();

    boolean runFind();

    default String getNamedPattern() {
        return namePattern(getPatternName(), getRawPattern());
    }

    default String getPattern(boolean withName) {
        return withName ? getNamedPattern() : getRawPattern();
    }

    /**
     * Use the matcher. Almost the same as
     * {@code matcher.group(PatternEnum.VALUE.getPatternName()} (aka.
     * {@code PatternEnum.VALUE.match(matcher)} ) but it will also do a find
     * call as needed.
     *
     * @param  matcher
     *                 matcher to use
     * @return         result of matcher or null (if none found)
     */
    default String group(Matcher matcher) {
        if (runFind()) {
            if (!matcher.find()) {
                return null;
            }
        }
        return matcher.group(getPatternName());
    }

    default void addStyles(Span span, StyleClasses... styles) {
        ArrayList<StyleClasses> all = new ArrayList<>();
        all.addAll(span.getInheritedStyles());
    }

    static String namePattern(String name, String pattern) {
        return "(?<" + name + ">" + pattern + ")";
    }

    static Pattern compilePattern(PatternEnum[] values) {
        StringBuilder pattern = new StringBuilder();
        for (PatternEnum value : values) {
            if (!pattern.isEmpty()) {
                pattern.append("|");
            }
            pattern.append("(" + value.getNamedPattern() + ")");
        }
        return Pattern.compile(pattern.toString());
    }
}
