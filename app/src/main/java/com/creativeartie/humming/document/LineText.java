package com.creativeartie.humming.document;

import java.util.*;
import java.util.regex.*;

import com.creativeartie.humming.schema.*;
import com.creativeartie.humming.schema.LineTextPatterns.*;

public class LineText extends SpanBranch {
    public static LineText newCellText(SpanBranch parent, String text, StyleClasses... classes) {
        Matcher match = LineTextPatterns.CELL.matcher(text);
        if (match == null) return null;
        return parseText(new LineText(parent, classes), match, BasicTextPatterns.CELL);
    }

    public static LineText newBasicText(SpanBranch parent, String text, StyleClasses... classes) {
        Matcher match = LineTextPatterns.BASIC.matcher(text);
        if (match == null) return null;
        return parseText(new LineText(parent, classes), match, BasicTextPatterns.TEXT);
    }

    private static LineText parseText(LineText span, Matcher match, BasicTextPatterns pattern) {
        TreeSet<StyleClasses> formatting = new TreeSet<>();
        while (match.find()) {
            checkStyle(match, span, formatting, LineTextPart.BOLD, StyleClasses.BOLD);
            checkStyle(match, span, formatting, LineTextPart.ITALICS, StyleClasses.ITALICS);
            checkStyle(match, span, formatting, LineTextPart.UNDERLINE, StyleClasses.UNDERLINE);

            String raw;
            if ((raw = LineTextPart.REFER.group(match)) != null) {
                span.add(ReferencePointerSpan.createSpan(span, raw, formatting.toArray(new StyleClasses[0])));
            }
            if ((raw = LineTextPart.TODO.group(match)) != null) {
                span.add(TodoSpan.newSpan(span, raw, formatting.toArray(new StyleClasses[0])));
            }
            if ((raw = LineTextPart.TEXT.group(match)) != null) {
                span.add(
                        TextSpan.builder(span, formatting.toArray(new StyleClasses[0])).setPattern(pattern).build(raw)
                );
            }
        }
        return span;
    }

    private static void checkStyle(
            Matcher match, LineText span, TreeSet<StyleClasses> formatting, LineTextPart finder, StyleClasses style
    ) {
        if ((finder.group(match)) != null) {
            if (formatting.contains(style)) formatting.remove(style);
            else formatting.add(style);
            span.add(new SpanLeaf(span, 1, StyleClasses.OPERATOR));
        }
    }

    private LineText(SpanBranch parent, StyleClasses... classes) {
        super(parent, classes);
        // TODO Auto-generated constructor stub
    }
}
