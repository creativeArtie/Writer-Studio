package com.creativeartie.humming.document;

import java.util.*;
import java.util.regex.*;

import com.creativeartie.humming.schema.*;
import com.creativeartie.humming.schema.TextFormattedPatterns.*;

public class TextFormatted extends SpanBranch {
    static TextFormatted newNoteText(SpanBranch parent, String text, StylesSpans... classes) {
        Matcher match = TextFormattedPatterns.NOTE.matcher(text);
        if (match == null) return null;
        return parseText(new TextFormatted(parent, classes), match, TextSpanPatterns.NOTE, false);
    }

    static TextFormatted newHeadingText(SpanBranch parent, String text, StylesSpans... classes) {
        Matcher match = TextFormattedPatterns.HEADING.matcher(text);
        if (match == null) return null;
        return parseText(new TextFormatted(parent, classes), match, TextSpanPatterns.HEADING, true);
    }

    static TextFormatted newCellText(SpanBranch parent, String text, StylesSpans... classes) {
        Matcher match = TextFormattedPatterns.CELL.matcher(text);
        if (match == null) return null;
        return parseText(new TextFormatted(parent, classes), match, TextSpanPatterns.CELL, true);
    }

    static TextFormatted newBasicText(SpanBranch parent, String text, StylesSpans... classes) {
        Matcher match = TextFormattedPatterns.BASIC.matcher(text);
        if (match == null) return null;
        return parseText(new TextFormatted(parent, classes), match, TextSpanPatterns.TEXT, true);
    }

    private static TextFormatted parseText(TextFormatted span, Matcher match, TextSpanPatterns pattern, boolean hasRefers) {
        TreeSet<StylesSpans> formatting = new TreeSet<>();
        while (match.find()) {
            checkStyle(match, span, formatting, TextFormattedParts.BOLD, StylesSpans.BOLD);
            checkStyle(match, span, formatting, TextFormattedParts.ITALICS, StylesSpans.ITALICS);
            checkStyle(match, span, formatting, TextFormattedParts.UNDERLINE, StylesSpans.UNDERLINE);

            String raw;
            if (hasRefers && (raw = TextFormattedParts.REFER.group(match)) != null) {
                span.add(IdentityReference.newSpan(span, raw, formatting.toArray(new StylesSpans[0])));
            }
            if ((raw = TextFormattedParts.TODO.group(match)) != null) {
                span.add(IdentityTodo.newSpan(span, raw, formatting.toArray(new StylesSpans[0])));
            }
            if ((raw = TextFormattedParts.TEXT.group(match)) != null) {
                span.add(TextSpan.builder(span, formatting.toArray(new StylesSpans[0])).setPattern(pattern).build(raw));
            }
        }
        return span;
    }

    private static void checkStyle(
            Matcher match, TextFormatted span, TreeSet<StylesSpans> formatting, TextFormattedParts finder, StylesSpans style
    ) {
        String raw;
        if ((raw = finder.group(match)) != null) {
            if (formatting.contains(style)) formatting.remove(style);
            else formatting.add(style);
            span.add(new SpanLeaf(span, raw));
        }
    }

    private TextFormatted(SpanBranch parent, StylesSpans... classes) {
        super(parent, classes);
    }
}
