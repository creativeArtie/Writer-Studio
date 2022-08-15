package com.creativeartie.writer.writing;

public enum ParaTypes {
    NORMAL(LinePhrase.LineEnders.NONE);

    private LinePhrase.LineEnders lineEnder;

    ParaTypes(LinePhrase.LineEnders ender) {
        lineEnder = ender;
    }

    public TypedStyles getStyle() {
        return TypedStyles.values()[ordinal() + TypedStyles.PARAGRAPH
            .ordinal()];
    }

    public LinePhrase.LineEnders getEnder() {
        return lineEnder;
    }
}
