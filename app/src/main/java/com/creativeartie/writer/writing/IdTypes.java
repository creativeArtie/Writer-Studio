package com.creativeartie.writer.writing;

public enum IdTypes {
    // @orderFor IdRefPhrase.Types
    FOOTNOTE, ENDNOTE, SOURCE;
    // @endOrder

    public SpanStyles toTypedStyles() {
        return SpanStyles.values()[SpanStyles.FOOTNOTE.ordinal() + ordinal()];
    }
}
