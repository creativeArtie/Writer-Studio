package com.creativeartie.humming.document;

import java.util.regex.*;

import com.creativeartie.humming.schema.*;
import com.google.common.base.*;

/**
 * Basic text span.
 */
public final class TextSpan extends SpanBranch {
    static class Builder {
        private TextSpan outputSpan;
        private TextSpanPatterns usePattern;

        private Builder(SpanBranch parent, CssSpanStyles... styles) {
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

    static Builder builder(SpanBranch parent, CssSpanStyles... styles) {
        return new Builder(parent, styles);
    }

    static TextSpan newFieldKey(SpanBranch span, String raw, CssSpanStyles... styles) {
        return parseText(new TextSpan(span, styles), TextSpanPatterns.KEY, raw);
    }

    static TextSpan newHeading(SpanBranch span, String raw, CssSpanStyles... styles) {
        return parseText(new TextSpan(span, styles), TextSpanPatterns.HEADING, raw);
    }

    static TextSpan newId(SpanBranch span, String text, CssSpanStyles... styles) {
        return parseText(new TextSpan(span, styles), TextSpanPatterns.ID, text);
    }

    static TextSpan newSimple(SpanBranch span, String raw, CssSpanStyles... styles) {
        return parseText(new TextSpan(span, styles), TextSpanPatterns.ERROR, raw);
    }

    static TextSpan newSpecial(SpanBranch span, String raw, CssSpanStyles... styles) {
        return parseText(new TextSpan(span, styles), TextSpanPatterns.SPECIAL, raw);
    }

    private static TextSpan parseText(TextSpan span, TextSpanPatterns pattern, String text) {
        Matcher match = pattern.matcher(text);
        StringBuilder builder = new StringBuilder();
        while (match.find()) {
            String raw = TextSpanPatterns.TextSpanParts.TEXT.group(match);
            if (raw != null) {
                builder.append(raw);
                span.add(new SpanLeaf(span, raw, CssSpanStyles.TEXT));
                continue;
            }
            raw = TextSpanPatterns.TextSpanParts.ESCAPE.group(match);
            if (raw.length() == 2) builder.append(raw.charAt(1));
            span.add(new SpanLeaf(span, raw, CssSpanStyles.ESCAPE));
        }
        span.spanText = builder.toString();
        return span;
    }

    private String spanText;

    private TextSpan(SpanBranch parent, CssSpanStyles... styles) {
        super(parent, styles);
    }

    /**
     * return the span text
     *
     * @return the span text
     */
    public String getText() {
        return CharMatcher.whitespace().collapseFrom(spanText, ' ');
    }
}
