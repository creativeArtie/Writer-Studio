package com.creativeartie.humming.document;

public enum StylesSpans implements SpanStyle {
    TEXT, ESCAPE, OPERATOR, ID, ERROR,

    FOOTNOTE, ENDNOTE, NOTE, METADATA, IMAGE, AGENDA,

    BOLD, UNDERLINE, ITALICS,

    HEADCELL, TEXTCELL;

    @Override
    public String getCssName() {
        return name().toLowerCase();
    }
}
