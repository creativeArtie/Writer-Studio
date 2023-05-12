package com.creativeartie.humming.document;

/**
 * Type of Identity.
 */
public enum IdentityGroup {
    /** a footnote identity. */
    FOOTNOTE,
    /** a research note identity. */
    NOTE,
    /** a meta data identity. */
    META,
    /** an image identity. */
    IMAGE,
    /** an agenda identity. */
    TODO,
    /** a heading identity. */
    HEADING;

    CssSpanStyles getStyleClass() {
        switch (this) {
            case NOTE:
                return CssSpanStyles.NOTE;
            case FOOTNOTE:
                return CssSpanStyles.FOOTNOTE;
            case META:
                return CssSpanStyles.METADATA;
            default:
                return CssSpanStyles.valueOf(name());
        }
    }
}
