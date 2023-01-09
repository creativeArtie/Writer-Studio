package com.creativeartie.humming.document;

public enum IdentityGroup {
    FOOTNOTE, ENDNOTE, CITATION, META, IMAGE, TODO;

    SpanStyles getStyleClass() {
        switch (this) {
            case CITATION:
                return SpanStyles.CITEREF;
            case ENDNOTE:
                return SpanStyles.ENDREF;
            case FOOTNOTE:
                return SpanStyles.FOOTREF;
            case META:
                return SpanStyles.METAREF;
            default:
                return SpanStyles.valueOf(name());
        }
    }
}
