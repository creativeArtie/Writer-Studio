package com.creativeartie.humming.document;

import java.util.*;
import java.util.regex.*;

import com.creativeartie.humming.schema.*;
import com.creativeartie.humming.schema.TextFormattedPatterns.*;
import com.google.common.base.*;

/**
 * Formatted text
 */
public final class TextFormatted extends SpanBranch {
    static TextFormatted newBasicText(SpanBranch parent, String text, CssSpanStyles... classes) {
        Matcher match = TextFormattedPatterns.BASIC.matcher(text);
        if (match == null) return null;
        return parseText(new TextFormatted(parent, classes), match, TextSpanPatterns.TEXT, true);
    }

    static TextFormatted newCellText(SpanBranch parent, String text, CssSpanStyles... classes) {
        Matcher match = TextFormattedPatterns.CELL.matcher(text);
        if (match == null) return null;
        return parseText(new TextFormatted(parent, classes), match, TextSpanPatterns.CELL, true);
    }

    static TextFormatted newHeadingText(SpanBranch parent, String text, CssSpanStyles... classes) {
        Matcher match = TextFormattedPatterns.HEADING.matcher(text);
        if (match == null) return null;
        return parseText(new TextFormatted(parent, classes), match, TextSpanPatterns.HEADING, true);
    }

    static TextFormatted newNoteText(SpanBranch parent, String text, CssSpanStyles... classes) {
        Matcher match = TextFormattedPatterns.NOTE.matcher(text);
        if (match == null) return null;
        return parseText(new TextFormatted(parent, classes), match, TextSpanPatterns.NOTE, false);
    }

    private static void checkStyle(
            Matcher match, TextFormatted span, TreeSet<CssSpanStyles> formatting, TextFormattedParts finder,
            CssSpanStyles style
    ) {
        String raw;
        if ((raw = finder.group(match)) != null) {
            if (formatting.contains(style)) formatting.remove(style);
            else formatting.add(style);
            SpanLeaf.addLeaf(span, raw);
        }
    }

    private static TextFormatted
            parseText(TextFormatted span, Matcher match, TextSpanPatterns pattern, boolean hasRefers) {
        TreeSet<CssSpanStyles> formatting = new TreeSet<>();
        while (match.find()) {
            checkStyle(match, span, formatting, TextFormattedParts.BOLD, CssSpanStyles.BOLD);
            checkStyle(match, span, formatting, TextFormattedParts.ITALICS, CssSpanStyles.ITALICS);
            checkStyle(match, span, formatting, TextFormattedParts.UNDERLINE, CssSpanStyles.UNDERLINE);

            String raw;
            if (hasRefers && (raw = TextFormattedParts.REFER.group(match)) != null) {
                span.add(IdentityReference.newSpan(span, raw, formatting.toArray(new CssSpanStyles[0])));
            }
            if ((raw = TextFormattedParts.TODO.group(match)) != null) {
                span.add(IdentityTodo.newSpan(span, raw, formatting.toArray(new CssSpanStyles[0])));
            }
            if ((raw = TextFormattedParts.TEXT.group(match)) != null) {
                span.add(
                        TextSpan.builder(span, formatting.toArray(new CssSpanStyles[0])).setPattern(pattern).build(raw)
                );
            }
        }
        return span;
    }

    private TextFormatted(SpanBranch parent, CssSpanStyles... classes) {
        super(parent, classes);
    }

    int getWrittenCount() {
        String answer = new String();
        for (Span child : this) {
            if (child instanceof TextSpan) {
                answer += ((TextSpan) child).getText();
            }
        }
        return Splitter.on(' ').omitEmptyStrings().trimResults().splitToList(answer).size();
    }

    int getOutlineCount() {
        int answer = 0;
        for (Span child : this) {
            if (child instanceof IdentityTodo) {
                String text = ((IdentityTodo) child).getAgenda();
                answer += Splitter.on(' ').omitEmptyStrings().trimResults().splitToList(text).size();
            }
        }
        return answer;
    }
}
