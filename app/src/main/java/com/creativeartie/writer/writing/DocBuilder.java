package com.creativeartie.writer.writing;

import java.util.*;
import java.util.regex.*;

import org.fxmisc.richtext.model.*;

import com.google.common.base.*;
import com.google.common.collect.*;

public class DocBuilder {

    public static interface PostCall {
        public void update(DocBuilder builder);
    }

    private class StyleData {
        private final int textLength;
        private final ImmutableList.Builder<String> styleClasses;

        private StyleData(int length, TypedStyles... styles) {
            textLength = length;
            styleClasses = ImmutableList.builder();
            styleClasses.addAll(inheritedStyles);
            addStyles(styles);
        }

        private void addStyles(TypedStyles... styles) {
            final ImmutableList.Builder<String> builder =
                ImmutableList.builder();
            for (final TypedStyles style : styles) {
                builder.add(style.getStyle());
            }
            styleClasses.addAll(builder.build());
        }
    }

    private static final boolean debug = false;

    private static void
        printMessage(Matcher matcher, String group, TypedStyles... styles) {
        if (!debug) return;
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
        System.out.printf(
            "For %8s (%2d - %2d) is styled %s: %s\n", groupName, groupStart,
            groupEnd, Arrays.toString(styles), "\"" + groupText + "\""
        );
    }

    private List<String> inheritedStyles;

    private final ArrayList<StyleData> styleList;

    private ArrayListMultimap<IdGroups, String> idList;

    private final ArrayList<PostCall> postCall;

    public DocBuilder() {
        styleList = new ArrayList<>();
        if (debug) {
            System.out.println();
        }
        inheritedStyles = ImmutableList.of();
        idList = ArrayListMultimap.create();
        postCall = new ArrayList<>();
    }

    protected int
        addStyle(Matcher matcher, Enum<?> group, TypedStyles... styles) {
        final String name = group.name();
        return addStyle(matcher, name, styles);
    }

    protected int
        addStyle(Matcher matcher, String name, TypedStyles... styles) {
        Preconditions
            .checkArgument(matcher.group(name) != null, "Match is null");

        printMessage(matcher, name, styles);

        styleList.add(
            new StyleData(matcher.end(name) - matcher.start(name), styles)
        );
        return styleList.size() - 1;
    }

    protected int addStyle(Matcher matcher, TypedStyles... styles) {
        Preconditions.checkArgument(matcher.group() != null, "Match is null");
        printMessage(matcher, "", styles);
        styleList.add(new StyleData(matcher.end() - matcher.start(), styles));
        return styleList.size() - 1;
    }

    protected int addTextStyle(String text, TypedStyles... styles) {
        Preconditions.checkArgument(!text.isEmpty(), "Text is empty");

        if (debug) System.out.printf(
            "For text of length %3d is styled %s: %s\n", text.length(),
            Arrays.toString(styles), text
        );
        styleList.add(new StyleData(text.length(), styles));
        return styleList.size() - 1;
    }

    StyleSpans<Collection<String>> getStyles() {
        final StyleSpansBuilder<Collection<String>> styleSpans =
            new StyleSpansBuilder<>();

        for (final StyleData data : styleList) {
            styleSpans.add(data.styleClasses.build(), data.textLength);
        }
        return styleSpans.create();
    }

    void insertStyles(int position, TypedStyles... styles) {
        if (debug) System.out.printf(
            "For span with index %3d add styles: \"%s\"\n", position,
            Arrays.toString(styles)
        );
        styleList.get(position).addStyles(styles);
    }

    void setInheritedStyles(TypedStyles... styles) {
        final ImmutableList.Builder<String> builder = ImmutableList.builder();
        for (final TypedStyles style : styles) {
            builder.add(style.getStyle());
        }
        inheritedStyles = builder.build();
    }

    boolean putId(IdGroups group, String name) {
        if (idList.containsEntry(group, name)) {
            return false;
        }
        return idList.put(group, name);
    }

    boolean containsId(IdGroups group, String name) {
        return idList.containsEntry(group, name);
    }

    void addCleanup(PostCall caller) {
        postCall.add(caller);
    }

    public void runCleanup() {
        for (PostCall caller : postCall) {
            caller.update(this);
        }
    }
}
