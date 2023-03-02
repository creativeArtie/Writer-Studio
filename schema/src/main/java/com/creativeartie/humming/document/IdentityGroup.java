package com.creativeartie.humming.document;

public enum IdentityGroup {
    FOOTNOTE, ENDNOTE, NOTE, META, IMAGE, TODO;

    StylesSpans getStyleClass() {
        switch (this) {
            case NOTE:
                return StylesSpans.INFO;
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
