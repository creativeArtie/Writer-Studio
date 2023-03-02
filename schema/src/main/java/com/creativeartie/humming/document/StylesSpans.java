package com.creativeartie.humming.document;

public enum StylesSpans implements SpanStyle {
    TEXT, ESCAPE, OPERATOR, ID, ERROR,

    FOOTNOTE, ENDNOTE, INFO, METADATA, IMAGE, TODO,

    BOLD, UNDERLINE, ITALICS,

    HEADCELL, TEXTCELL;

    @Override
    public String getCssName() {
        return name().toLowerCase();
    }
}
