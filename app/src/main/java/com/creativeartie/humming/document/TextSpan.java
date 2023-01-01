package com.creativeartie.humming.document;

import java.util.regex.*;

import com.creativeartie.humming.schema.*;

public class TextSpan extends SpanBranch {
    private String spanText;

    public static TextSpan newSpecial(SpanBranch parent, String text) {
        return parseText(new TextSpan(parent), BasicTextPatterns.SPECIAL, text);
    }

    public static TextSpan newId(SpanBranch parent, String text) {
        return parseText(new TextSpan(parent), BasicTextPatterns.ID, text);
    }

    private static TextSpan parseText(TextSpan span, BasicTextPatterns pattern, String text) {
        Matcher match = pattern.matcher(text);
        StringBuilder builder = new StringBuilder();
        while (match.find()) {
            String raw = BasicTextPatterns.BasicTextPart.TEXT.group(match);
            if (raw != null) {
                builder.append(raw);
                span.add(new SpanLeaf(span, raw.length(), StyleClasses.TEXT));
                continue;
            }
            raw = BasicTextPatterns.BasicTextPart.ESCAPE.group(match);
            builder.append(raw.charAt(1));
            span.add(new SpanLeaf(span, raw.length(), StyleClasses.ESCAPE));
        }
        span.spanText = builder.toString();
        return span;
    }

    private TextSpan(SpanBranch parent) {
        super(parent);
    }

    public String getText() {
        return spanText;
    }
}
