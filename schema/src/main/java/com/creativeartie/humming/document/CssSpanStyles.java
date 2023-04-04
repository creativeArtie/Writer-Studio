package com.creativeartie.humming.document;

/**
 * CSS styles for spans
 */
public enum CssSpanStyles implements CssStyle {
    /** Basic text. */
    TEXT,
    /** escaped text. */
    ESCAPE,
    /** Operator text. */
    OPERATOR,
    /** Id text. */
    ID,
    /** Error text */
    ERROR,

    /** foot note pointer span. */
    FOOTNOTE,
    /** end note pointer span. */
    ENDNOTE,
    /** research note pointer span. */
    NOTE,
    /** meta data pointer span. */
    METADATA,
    /** Agenda span */
    AGENDA,

    /** bold format */
    BOLD,
    /** underline format */
    UNDERLINE,
    /** italics format */
    ITALICS,

    /** table heading cell. */
    HEADCELL,
    /** table text cell. */
    TEXTCELL;

    @Override
    public String getCssName() {
        return name().toLowerCase();
    }
}
