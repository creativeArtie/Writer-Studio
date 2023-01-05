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
            default:
                return SpanStyles.valueOf(name());
        }
    }
}
