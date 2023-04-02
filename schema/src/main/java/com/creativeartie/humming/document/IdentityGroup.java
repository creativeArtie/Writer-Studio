package com.creativeartie.humming.document;

public enum IdentityGroup {
    FOOTNOTE, ENDNOTE, NOTE, META, IMAGE, TODO, HEADING;

    StylesSpans getStyleClass() {
        switch (this) {
            case NOTE:
                return StylesSpans.NOTE;
            case ENDNOTE:
                return StylesSpans.ENDNOTE;
            case FOOTNOTE:
                return StylesSpans.FOOTNOTE;
            case META:
                return StylesSpans.METADATA;
            default:
                return StylesSpans.valueOf(name());
        }
    }
}
