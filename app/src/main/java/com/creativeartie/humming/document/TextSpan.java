package com.creativeartie.humming.document;

import java.util.regex.*;

import com.creativeartie.humming.schema.*;
import com.google.common.base.*;

public class TextSpan extends SpanBranch {
    public static class Builder {
        private TextSpan outputSpan;
        private BasicTextPatterns usePattern;

        private Builder(SpanBranch parent, StyleClasses... styles) {
            outputSpan = new TextSpan(parent, styles);
        }

        public TextSpan build(String text) {
            Preconditions.checkState(usePattern != null);
            return parseText(outputSpan, usePattern, text);
        }

        public Builder setPattern(BasicTextPatterns pattern) {
            usePattern = pattern;
            return this;
        }
    }

    public static Builder builder(SpanBranch parent, StyleClasses... styles) {
        return new Builder(parent, styles);
    }

    public static TextSpan newId(SpanBranch span, String text, StyleClasses... styles) {
        return parseText(new TextSpan(span, styles), BasicTextPatterns.ID, text);
    }

    public static TextSpan newSpecial(SpanBranch span, String raw, StyleClasses... styles) {
        return parseText(new TextSpan(span, styles), BasicTextPatterns.SPECIAL, raw);
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

    private String spanText;

    private TextSpan(SpanBranch parent, StyleClasses... styles) {
        super(parent, styles);
    }

    public String getText() {
        return spanText;
    }
}
