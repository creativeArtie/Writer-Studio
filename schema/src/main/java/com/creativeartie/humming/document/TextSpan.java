package com.creativeartie.humming.document;

import java.util.regex.*;

import com.creativeartie.humming.schema.*;
import com.google.common.base.*;

public class TextSpan extends SpanBranch {
    static class Builder {
        private TextSpan outputSpan;
        private TextSpanPatterns usePattern;

        private Builder(SpanBranch parent, StylesSpans... styles) {
            outputSpan = new TextSpan(parent, styles);
        }

        public TextSpan build(String text) {
            Preconditions.checkState(usePattern != null);
            return parseText(outputSpan, usePattern, text);
        }

        public Builder setPattern(TextSpanPatterns pattern) {
            usePattern = pattern;
            return this;
        }
    }

    static Builder builder(SpanBranch parent, StylesSpans... styles) {
        return new Builder(parent, styles);
    }

    static TextSpan newId(SpanBranch span, String text, StylesSpans... styles) {
        return parseText(new TextSpan(span, styles), TextSpanPatterns.ID, text);
    }

    static TextSpan newSpecial(SpanBranch span, String raw, StylesSpans... styles) {
        return parseText(new TextSpan(span, styles), TextSpanPatterns.SPECIAL, raw);
    }

    static TextSpan newSimple(SpanBranch span, String raw, StylesSpans... styles) {
        return parseText(new TextSpan(span, styles), TextSpanPatterns.SIMPLE, raw);
    }

    static TextSpan newHeading(SpanBranch span, String raw, StylesSpans... styles) {
        return parseText(new TextSpan(span, styles), TextSpanPatterns.HEADING, raw);
    }

    static TextSpan newFieldKey(SpanBranch span, String raw, StylesSpans... styles) {
        return parseText(new TextSpan(span, styles), TextSpanPatterns.KEY, raw);
    }

    private static TextSpan parseText(TextSpan span, TextSpanPatterns pattern, String text) {
        Matcher match = pattern.matcher(text);
        StringBuilder builder = new StringBuilder();
        while (match.find()) {
            String raw = TextSpanPatterns.TextSpanParts.TEXT.group(match);
            if (raw != null) {
                builder.append(raw);
                span.add(new SpanLeaf(span, raw, StylesSpans.TEXT));
                continue;
            }
            raw = TextSpanPatterns.TextSpanParts.ESCAPE.group(match);
            if (raw.length() == 2) builder.append(raw.charAt(1));
            span.add(new SpanLeaf(span, raw, StylesSpans.ESCAPE));
        }
        span.spanText = builder.toString();
        return span;
    }

    private String spanText;

    private TextSpan(SpanBranch parent, StylesSpans... styles) {
        super(parent, styles);
    }

    public String getText() {
        return CharMatcher.whitespace().trimAndCollapseFrom(spanText, ' ');
    }
}
