package com.creativeartie.humming.document;

public enum StyleLines implements SpanStyle {
    QUOTE, AGENDA, NORMAL, BREAK,

    HEADING, OUTLINE,

    NUMBERED, BULLET,

    HEADER, NOTE, FIELD,

    FOOTNOTE, ENDNOTE,

    ROW;

    @Override
    public String getCssName() {
        return name().toLowerCase();
    }
}
