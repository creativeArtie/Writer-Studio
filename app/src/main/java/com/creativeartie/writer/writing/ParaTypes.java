package com.creativeartie.writer.writing;

public enum ParaTypes {
    NORMAL(TextPhrase.TextEnders.NONE);

    private TextPhrase.TextEnders lineEnder;

    ParaTypes(TextPhrase.TextEnders ender) {
        lineEnder = ender;
    }

    public TypedStyles getStyle() {
        return TypedStyles.values()[ordinal() + TypedStyles.PARAGRAPH
            .ordinal()];
    }

    public TextPhrase.TextEnders getEnder() {
        return lineEnder;
    }
}
