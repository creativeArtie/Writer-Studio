package com.creativeartie.writer.writing;

public enum LineTypes {
    NORMAL(LinePhrase.LineEnders.NONE);

    private LinePhrase.LineEnders lineEnder;

    LineTypes(LinePhrase.LineEnders ender) {
        lineEnder = ender;
    }

    public SpanStyles getStyle() {
        return SpanStyles.values()[ordinal() + SpanStyles.PARAGRAPH
            .ordinal()];
    }

    public LinePhrase.LineEnders getEnder() {
        return lineEnder;
    }
}
