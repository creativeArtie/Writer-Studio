package com.creativeartie.humming.document;

import java.util.*;
import java.util.regex.*;

import com.creativeartie.humming.schema.*;
import com.creativeartie.humming.schema.LineTextPatterns.*;

public class LineText extends SpanBranch {
    static LineText newNoteText(SpanBranch parent, String text, SpanStyles... classes) {
        Matcher match = LineTextPatterns.NOTE.matcher(text);
        if (match == null) return null;
        return parseText(new LineText(parent, classes), match, TextSpanPatterns.NOTE, false);
    }

    static LineText newHeadingText(SpanBranch parent, String text, SpanStyles... classes) {
        Matcher match = LineTextPatterns.HEADING.matcher(text);
        if (match == null) return null;
        return parseText(new LineText(parent, classes), match, TextSpanPatterns.HEADING, true);
    }

    static LineText newCellText(SpanBranch parent, String text, SpanStyles... classes) {
        Matcher match = LineTextPatterns.CELL.matcher(text);
        if (match == null) return null;
        return parseText(new LineText(parent, classes), match, TextSpanPatterns.CELL, true);
    }

    static LineText newBasicText(SpanBranch parent, String text, SpanStyles... classes) {
        Matcher match = LineTextPatterns.BASIC.matcher(text);
        if (match == null) return null;
        return parseText(new LineText(parent, classes), match, TextSpanPatterns.TEXT, true);
    }

    private static LineText parseText(LineText span, Matcher match, TextSpanPatterns pattern, boolean hasRefers) {
        TreeSet<SpanStyles> formatting = new TreeSet<>();
        while (match.find()) {
            checkStyle(match, span, formatting, LineTextPart.BOLD, SpanStyles.BOLD);
            checkStyle(match, span, formatting, LineTextPart.ITALICS, SpanStyles.ITALICS);
            checkStyle(match, span, formatting, LineTextPart.UNDERLINE, SpanStyles.UNDERLINE);

            String raw;
            if (hasRefers && (raw = LineTextPart.REFER.group(match)) != null) {
                span.add(ReferenceSpan.newSpan(span, raw, formatting.toArray(new SpanStyles[0])));
            }
            if ((raw = LineTextPart.TODO.group(match)) != null) {
                span.add(TodoSpan.newSpan(span, raw, formatting.toArray(new SpanStyles[0])));
            }
            if ((raw = LineTextPart.TEXT.group(match)) != null) {
                span.add(TextSpan.builder(span, formatting.toArray(new SpanStyles[0])).setPattern(pattern).build(raw));
            }
        }
        return span;
    }

    private static void checkStyle(
            Matcher match, LineText span, TreeSet<SpanStyles> formatting, LineTextPart finder, SpanStyles style
    ) {
        String raw;
        if ((raw = finder.group(match)) != null) {
            if (formatting.contains(style)) formatting.remove(style);
            else formatting.add(style);
            span.add(new SpanLeaf(span, raw, SpanStyles.OPERATOR));
        }
    }

    private LineText(SpanBranch parent, SpanStyles... classes) {
        super(parent, classes);
    }
}
