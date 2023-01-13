package com.creativeartie.humming.document;

import java.util.regex.*;

import com.creativeartie.humming.schema.*;
import com.google.common.base.*;

public class TextSpan extends SpanBranch {
    static class Builder {
        private TextSpan outputSpan;
        private TextSpanPatterns usePattern;

        private Builder(SpanBranch parent, SpanStyles... styles) {
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

    static Builder builder(SpanBranch parent, SpanStyles... styles) {
        return new Builder(parent, styles);
    }

    static TextSpan newId(SpanBranch span, String text, SpanStyles... styles) {
        return parseText(new TextSpan(span, styles), TextSpanPatterns.ID, text);
    }

    static TextSpan newSpecial(SpanBranch span, String raw, SpanStyles... styles) {
        return parseText(new TextSpan(span, styles), TextSpanPatterns.SPECIAL, raw);
    }

    static TextSpan newSimple(SpanBranch span, String raw, SpanStyles... styles) {
        return parseText(new TextSpan(span, styles), TextSpanPatterns.SIMPLE, raw);
    }

    static TextSpan newHeading(SpanBranch span, String raw, SpanStyles... styles) {
        return parseText(new TextSpan(span, styles), TextSpanPatterns.HEADING, raw);
    }

    static TextSpan newFieldKey(SpanBranch span, String raw, SpanStyles... styles) {
        return parseText(new TextSpan(span, styles), TextSpanPatterns.KEY, raw);
    }

    private static TextSpan parseText(TextSpan span, TextSpanPatterns pattern, String text) {
        Matcher match = pattern.matcher(text);
        StringBuilder builder = new StringBuilder();
        while (match.find()) {
            String raw = TextSpanPatterns.TextSpanParts.TEXT.group(match);
            if (raw != null) {
                builder.append(raw);
                span.add(new SpanLeaf(span, raw, SpanStyles.TEXT));
                continue;
            }
            raw = TextSpanPatterns.TextSpanParts.ESCAPE.group(match);
            builder.append(raw.charAt(1));
            span.add(new SpanLeaf(span, raw, SpanStyles.ESCAPE));
        }
        span.spanText = builder.toString();
        return span;
    }

    private String spanText;

    private TextSpan(SpanBranch parent, SpanStyles... styles) {
        super(parent, styles);
    }

    public String getText() {
        return CharMatcher.whitespace().trimAndCollapseFrom(spanText, ' ');
    }
}
