package com.creativeartie.humming.schema;

import java.util.*;
import java.util.regex.*;

interface PatternEnum {
    String getRawPattern();

    String getPatternName();

    default String getNamedPattern() {
        return namePattern(getPatternName(), getRawPattern());
    }

    default String match(Matcher matcher) {
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
            pattern.append("(" + value.getNamedPattern() + ")+");
        }
        return Pattern.compile(pattern.toString());
    }
}
