package com.creativeartie.humming.document;

public enum IdentityGroup {
    FOOTNOTE, ENDNOTE, CITATION, META, IMAGE, TODO;

    SpanStyles getStyleClass() {
        switch (this) {
            case CITATION:
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
