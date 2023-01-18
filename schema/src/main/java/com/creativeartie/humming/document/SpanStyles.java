package com.creativeartie.humming.document;

public enum SpanStyles implements StyleClass {
    TEXT, ESCAPE, OPERATOR, ID, ERROR,

    FOOTNOTE, ENDNOTE, INFO, METADATA, IMAGE, TODO,

    BOLD, UNDERLINE, ITALICS,

    HEADCELL, TEXTCELL;

    @Override
    public String getCssName() {
        return name().toLowerCase();
    }
}
