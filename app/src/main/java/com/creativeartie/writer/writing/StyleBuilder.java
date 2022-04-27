package com.creativeartie.writer.writing;

import java.util.*;
import java.util.regex.*;

import org.fxmisc.richtext.model.*;

import com.google.common.base.*;
import com.google.common.collect.*;

public class StyleBuilder {
    private static final boolean debug = true;

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

        String classes = "";
        for (final TypedStyles style : styles) {
            if (classes != "") {
                classes += ", ";
            }
            classes += style.name();
        }
        System.out.printf(
            "For %3d to %3d: %-8s is styled %s: %s\n", groupStart, groupEnd,
            groupName, classes, "\"" + groupText + "\""
        );
    }

    private final StyleSpansBuilder<Collection<String>> styleSpans;

    public StyleBuilder() {
        styleSpans = new StyleSpansBuilder<>();
        if (debug) {
            System.out.println();
        }
    }

    protected void
        addStyle(Matcher matcher, Enum<?> group, TypedStyles... styles) {
        final String groupName = group.name();
        Preconditions
            .checkArgument(matcher.group(groupName) != null, "Match is null");

        printMessage(matcher, groupName, styles);
        final ImmutableList.Builder<String> builder = ImmutableList.builder();

        for (final TypedStyles style : styles) {
            builder.add(style.getStyle());
        }

        styleSpans.add(
            builder.build(), matcher.end(groupName) - matcher.start(groupName)
        );

    }

    protected void addStyle(Matcher matcher, TypedStyles... styles) {

        Preconditions.checkArgument(matcher.group() != null, "Match is null");
        printMessage(matcher, "", styles);
        final ImmutableList.Builder<String> builder = ImmutableList.builder();

        for (final TypedStyles style : styles) {
            builder.add(style.getStyle());
        }
        styleSpans.add(builder.build(), matcher.end() - matcher.start());
    }

    StyleSpans<Collection<String>> getStyles() {
        return styleSpans.create();
    }
}
