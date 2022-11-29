package com.creativeartie.writer.writing;

import java.util.*;
import java.util.regex.*;

import org.fxmisc.richtext.*;
import org.fxmisc.richtext.model.*;

import com.google.common.base.*;
import com.google.common.collect.*;

/**
 * Stores information about the writing text files. This includes
 * <ul>
 * <li>A list of id and their references
 * <li>list of styles and the ability to rebuild them
 * </ul>
 *
 * @author wai
 */
public class DocBuilder {

    /**
     * Adds styles that is unknown until the syntax is
     *
     * @author wai
     */
    public static interface CheckedIdStyle {
        public void update(DocBuilder builder);
    }

    private class StyleData {
        private final int textLength;
        private final ImmutableList.Builder<String> styleClasses;

        private StyleData(int length, List<SpanStyles> styles) {
            textLength = length;
            styleClasses = ImmutableList.builder();
            addStyles(styles);
        }

        private void addStyles(List<SpanStyles> styles) {
            final ImmutableList.Builder<String> builder = ImmutableList
                .builder();
            for (final SpanStyles style : styles) {
                builder.add(style.getStyle());
            }
            styleClasses.addAll(builder.build());
        }
    }

    private final boolean isDebug;
    private String docText;

    /**
     * printing style for debugging purposes.
     *
     * @param matcher
     *                the matcher to use
     * @param group
     *                the matcher group to use
     * @param styles
     *                what style has been added
     */
    private void printMessage(
        Matcher matcher, String group, List<SpanStyles> styles
    ) {
        if (!isDebug) return;
        String groupName, groupText;
        int groupStart, groupEnd;
        if (group.isBlank()) {
            groupName = "";
            groupText = matcher.group();
            groupStart = matcher.start();
            groupEnd = matcher.end();
        } else {
            groupName = group + " ";
            groupText = matcher.group(group);
            groupStart = matcher.start(group);
            groupEnd = matcher.end(group);
        }
        docText += groupText;
        // System.out.printf(
        // "For %8s (%2d - %2d) is styled %s: %s (format: %s)\n", groupName,
        // groupStart, groupEnd, styles, "\"" + groupText + "\"", matcher
        // .pattern().pattern()
        // );
        System.out.printf(
            "For %8s (%2d - %2d) is styled %s: %s\n", groupName, groupStart,
            groupEnd, styles, "\"" + groupText + "\""
        );
    }

    private StyleSpans<Collection<String>> printStyle(
        StyleSpans<Collection<String>> styleSpans
    ) {
        if (!isDebug) return styleSpans;
        int start = 0;
        for (StyleSpan<Collection<String>> style : styleSpans) {
            System.out.printf(
                "%s: \"%s\"\n", Joiner.on(" ").join(style.getStyle()), docText
                    .substring(start, start + style.getLength())
            );
            start += style.getLength();
        }
        System.out.println("Input text (btw quotes): \"" + docText + "\"");

        return styleSpans;
    }

    private final ArrayList<StyleData> styleList;

    // List of ids
    private ArrayListMultimap<IdTypes, String> idList;

    // Methods add more styles for ids
    private final ArrayList<CheckedIdStyle> checkedIdStyle;

    public DocBuilder(boolean debug) {
        isDebug = debug;
        if (isDebug) {
            System.out.println();
        }
        styleList = new ArrayList<>();
        idList = ArrayListMultimap.create();
        checkedIdStyle = new ArrayList<>();
        docText = "";
    }

    /**
     * Add style with a group name
     *
     * @param  matcher
     *                 the matcher to use
     * @param  group
     *                 the name of the group
     * @param  styles
     *                 the style classes to add
     * @return         the index of this style
     * @see            #addStyle(Matcher, String, SpanStyles...)
     */
    protected int addStyle(
        Matcher matcher, String name, List<SpanStyles> styles
    ) {
        Preconditions.checkArgument(
            matcher.group(name) != null, "Match is null"
        );

        printMessage(matcher, name, styles);

        styleList.add(
            new StyleData(matcher.end(name) - matcher.start(name), styles)
        );
        return styleList.size() - 1;
    }

    /**
     * Add style
     *
     * @param  matcher
     *                 the matcher to use
     * @param  styles
     *                 the style classes to add
     * @return         the index of this style
     * @see            #addStyle(Matcher, SpanStyles...)
     */
    protected int addStyle(Matcher matcher, List<SpanStyles> styles) {
        Preconditions.checkArgument(matcher.group() != null, "Match is null");
        printMessage(matcher, "", styles);
        styleList.add(new StyleData(matcher.end() - matcher.start(), styles));
        return styleList.size() - 1;
    }

    /**
     * Add style without names
     *
     * @param  text
     *                the text to use
     * @param  styles
     *                the styles class to add
     * @return        the index of this style
     * @see           #addTextStyle(String, SpanStyles...)
     */
    protected int addTextStyle(String text, List<SpanStyles> styles) {
        Preconditions.checkArgument(!text.isEmpty(), "Text is empty");
        docText += text;

        if (isDebug) System.out.printf(
            "For text of length %3d is styled %s: %s\n", text.length(), styles,
            text
        );
        styleList.add(new StyleData(text.length(), styles));
        return styleList.size() - 1;
    }

    /**
     * Gets the list of styles for {@linkplain CodeArea}.
     *
     * @return the list of styles
     */
    StyleSpans<Collection<String>> getStyles() {
        final StyleSpansBuilder<Collection<String>> styleSpans =
            new StyleSpansBuilder<>();

        for (final StyleData data : styleList) {
            styleSpans.add(data.styleClasses.build(), data.textLength);
        }
        return printStyle(styleSpans.create());

    }

    /**
     * add styles into a {@link StyleSpan}.
     *
     * @param position
     *                 the location of {@linkplain StyleSpan}.
     * @param styles
     *                 the styles to add
     */
    void insertStyles(int position, SpanStyles... styles) {
        if (isDebug) System.out.printf(
            "For span with index %3d add new styles: %s\n", position, Arrays
                .asList(styles)
        );
        styleList.get(position).addStyles(Arrays.asList(styles));
    }

    /**
     * Adds a id
     *
     * @param  group
     *               the type of id
     * @param  name
     *               the name of id
     * @return       {@code true} if the id doesn't exist previously
     */
    boolean putId(IdTypes group, String name) {
        if (idList.containsEntry(group, name)) {
            return false;
        }
        return idList.put(group, name);
    }

    /**
     * Check if the id is found
     *
     * @param  group
     *               the type of id
     * @param  name
     *               the name of id
     * @return       {@code true} if the id exist
     */
    boolean containsId(IdTypes group, String name) {
        return idList.containsEntry(group, name);
    }

    /**
     * add method to add styles to a id checked.
     *
     * @param caller
     *               the method to call
     */
    void addCheckedIdStyle(CheckedIdStyle caller) {
        checkedIdStyle.add(caller);
    }

    /**
     * Style clean up
     */
    public void runCleanup() {
        for (CheckedIdStyle caller : checkedIdStyle) {
            caller.update(this);
        }
    }
}
