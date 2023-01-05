package com.creativeartie.humming.document;

public enum LineStyles implements StyleClass {
    QUOTE, AGENDA, NORMAL, BREAK,

    HEADING, OUTLINE,

    NUMBERED, BULLET,

    SUMMARY, NOTE, CITATION,

    FOOTNOTE, ENDNOTE,

    ROW, HEADER;

    @Override
    public String getCssName() {
        return name().toLowerCase();
    }
}
