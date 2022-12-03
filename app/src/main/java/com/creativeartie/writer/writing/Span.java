package com.creativeartie.writer.writing;

import java.util.*;
import java.util.regex.*;

import com.google.common.collect.*;

/**
 * A span of text
 *
 * @author wai
 */
public abstract class Span {

    static String getPhraseName() {
        throw new IllegalStateException("Span missing a phrase name");
    }

    static String getPhrasePattern(boolean withName) {
        throw new IllegalStateException("Span missing a phrase pattern");
    }

    /**
     * Helper method for Matcher. Reduce the following code by 4 characters:
     * <code> matcher.group(group.name()) </code> to
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

    /**
     * create a name pattern with a {@linkplain Enum}.
     *
     * @param value
     *                the name extracted from {@linkplain Enum#name()}.
     * @param pattern
     *                the pattern
     * @param result
     *                regular expression
     * @see           #namePattern(String, String)
     * @see           #namePattern(Enum)
     */
    protected static String namePattern(Enum<?> value, String pattern) {
        return namePattern(value.name(), pattern);
    }

    /**
     * create a name pattern with a String
     *
     * @param value
     *                the token name
     * @param pattern
     *                the pattern
     * @param result
     *                regular expression
     * @see           #namePattern(Enum)
     * @see           #namePattern(Enum, String)
     */
    protected static String namePattern(String name, String pattern) {
        return "(?<" + name + ">" + pattern + ")";
    }

    /**
     * Create a name pattern with a {@linkplain Enum} using its
     * {@linkplain #toString()} method.
     *
     * @param value
     *               the name extracted from {@linkplain Enum#name()}.
     * @param result
     *               regular expression
     * @see          #namePattern(String, String)
     * @see          #namePattern(Enum, String)
     */
    protected static String namePattern(Enum<?> value) {
        return namePattern(value.name(), value.toString());
    }

    private DocBuilder docRoot;

    Span(DocBuilder root) {
        inheritedStyles = ImmutableList.of();
        docRoot = root;
    }

    private List<SpanStyles> inheritedStyles;

    protected int
        addStyle(Matcher matcher, Enum<?> group, List<SpanStyles> styles) {
        return addStyle(matcher, group.name(), styles);
    }

    protected int
        addStyle(Matcher matcher, Enum<?> group, SpanStyles... styles) {
        return addStyle(matcher, group.name(), Arrays.asList(styles));
    }

    protected int addStyle(
        Matcher matcher, Enum<?> group, SpanStyles[] others,
        SpanStyles... styles
    ) {
        return addStyle(matcher, group, Arrays.asList(others), styles);
    }

    protected int addStyle(
        Matcher matcher, Enum<?> group, Collection<SpanStyles> others,
        SpanStyles... styles
    ) {
        ArrayList<SpanStyles> combine = new ArrayList<>();
        combine.addAll(others);
        combine.addAll(Arrays.asList(styles));

        return addStyle(matcher, group, combine);
    }

    protected int
        addStyle(Matcher matcher, String name, Collection<SpanStyles> styles) {
        return docRoot.addStyle(matcher, name, combineStyles(styles));
    }

    protected int addStyle(Matcher matcher, String name, SpanStyles... styles) {
        return addStyle(matcher, name, Arrays.asList(styles));
    }

    protected int addStyle(
        Matcher matcher, String name, SpanStyles[] others, SpanStyles... styles
    ) {
        return addStyle(matcher, name, Arrays.asList(others), styles);
    }

    protected int addStyle(
        Matcher matcher, String name, Collection<SpanStyles> others,
        SpanStyles... styles
    ) {
        ArrayList<SpanStyles> combine = new ArrayList<>();
        combine.addAll(others);
        combine.addAll(Arrays.asList(styles));

        return addStyle(matcher, name, combine);
    }

    protected int addStyle(Matcher matcher, Collection<SpanStyles> styles) {
        return docRoot.addStyle(matcher, combineStyles(styles));
    }

    protected int addStyle(Matcher matcher, SpanStyles... styles) {
        return addStyle(matcher, Arrays.asList(styles));
    }

    protected int
        addStyle(Matcher matcher, SpanStyles[] others, SpanStyles... styles) {
        return addStyle(matcher, Arrays.asList(others), styles);
    }

    protected int addStyle(
        Matcher matcher, Collection<SpanStyles> others, SpanStyles... styles
    ) {
        ArrayList<SpanStyles> combine = new ArrayList<>();
        combine.addAll(others);
        combine.addAll(Arrays.asList(styles));

        return addStyle(matcher, combine);
    }

    protected int addTextStyle(String text, SpanStyles... styles) {
        return addTextStyle(text, Arrays.asList(styles));
    }

    protected int addTextStyle(String text, Collection<SpanStyles> styles) {
        return docRoot.addTextStyle(text, combineStyles(styles));
    }

    protected void setInheritedStyles(SpanStyles... styles) {
        inheritedStyles = ImmutableList.copyOf(styles);
    }

    protected ImmutableList<SpanStyles>
        combineStyles(Collection<SpanStyles> styles) {
        ImmutableList.Builder<SpanStyles> builder = ImmutableList.builder();
        return builder.addAll(styles).addAll(inheritedStyles).build();
    }

}
