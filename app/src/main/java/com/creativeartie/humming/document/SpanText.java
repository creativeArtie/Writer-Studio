package com.creativeartie.humming.document;

import java.util.regex.*;

import com.creativeartie.humming.schema.*;

public class SpanText extends SpanBranch {
    private final String spanText;

    public SpanText(SpanBranch parent, BasicTextPatterns pattern, String text) {
        super(parent);
        Matcher match = pattern.matcher(text);
        StringBuilder builder = new StringBuilder();
        while (match.find()) {
            String raw = BasicTextPatterns.BasicTextPart.TEXT.group(match);
            if (raw != null) {
                builder.append(raw);
                add(new SpanLeaf(this, raw.length(), StyleClasses.TEXT));
                continue;
            }
            raw = BasicTextPatterns.BasicTextPart.ESCAPE.group(match);
            builder.append(raw.charAt(1));
            add(new SpanLeaf(this, raw.length(), StyleClasses.ESCAPE));
        }
        spanText = builder.toString();
    }

    public String getText() {
        return spanText;
    }
}
