package com.creativeartie.humming.document;

/**
 * List of line styles
 */
public enum CssLineStyles implements CssStyle {
    /** Quote line. */
    QUOTE,
    /** Agenda line. */
    AGENDA,
    /** Normal text line. */
    NORMAL,
    /** Section break line. */
    BREAK,

    /** Heading line */
    HEADING,
    /** Outline line */
    OUTLINE,

    /** Numbered line. */
    NUMBERED,
    /** Bullet line. */
    BULLET,

    /** Note heading line. */
    HEADER,
    /** Note detail line. */
    NOTE,
    /** Note field line. */
    FIELD,

    /** Foot note line. */
    FOOTNOTE,
    /** End note line. */
    ENDNOTE,
    /** Image line. */
    IMAGE,

    /** Table row line */
    ROW;

    @Override
    public String getCssName() {
        return name().toLowerCase();
    }
}
