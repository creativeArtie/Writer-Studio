package com.creativeartie.humming.document;

public enum SpanStyles implements StyleClass {
    TEXT, ESCAPE, OPERATOR, ID, ERROR,

    FOOTREF, ENDREF, CITEREF, METAREF, LINK, TODO,

    BOLD, UNDERLINE, ITALICS,

    HEADCELL, TEXTCELL;

    @Override
    public String getCssName() {
        return name().toLowerCase();
    }
}
