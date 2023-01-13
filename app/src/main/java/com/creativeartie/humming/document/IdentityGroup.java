package com.creativeartie.humming.document;

public enum IdentityGroup {
    FOOTNOTE, ENDNOTE, NOTE, META, IMAGE, TODO;

    SpanStyles getStyleClass() {
        switch (this) {
            case NOTE:
                return SpanStyles.INFO;
            case ENDNOTE:
                return SpanStyles.ENDNOTE;
            case FOOTNOTE:
                return SpanStyles.FOOTNOTE;
            case META:
                return SpanStyles.METADATA;
            default:
                return SpanStyles.valueOf(name());
        }
    }
}
