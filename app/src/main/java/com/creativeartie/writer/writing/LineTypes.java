package com.creativeartie.writer.writing;

public enum LineTypes {
    // @orderFor SpanStyles
    NORMAL(LinePhrase.TextEnders.NONE);
    // @endOrder

    private LinePhrase.TextEnders lineEnder;

    LineTypes(LinePhrase.TextEnders ender) {
        lineEnder = ender;
    }

    public SpanStyles getStyle() {
        return SpanStyles.values()[ordinal() + SpanStyles.PARAGRAPH.ordinal()];
    }

    public LinePhrase.TextEnders getEnder() {
        return lineEnder;
    }
}
