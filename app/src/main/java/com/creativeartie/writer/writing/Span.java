package com.creativeartie.writer.writing;

import java.util.regex.*;

/**
 * A span of text TODO stub class, not sure if this is needed
 *
 * @author wai
 */
public abstract class Span {

    /**
     * Helper method for Matcher. Reduce the following code 4 characters: <code>
     * matcher.group(group.name()) </code> to
     * <code> match(matcher, group)</code>.
     *
     * @param  matcher
     *                 the {@linkplain Matcher} to use
     * @param  group
     *                 the name
     * @return         match result
     */
    protected static String match(Matcher matcher, Enum<?> group) {
        return matcher.group(group.name());
    }

    protected static String namePattern(Enum<?> value, String pattern) {
        return namePattern(value.name(), pattern);
    }

    protected static String namePattern(String name, String pattern) {
        return "(?<" + name + ">" + pattern + ")";
    }

    protected static String namePattern(Enum<?> value) {
        return namePattern(value.name(), value.toString());
    }
}
