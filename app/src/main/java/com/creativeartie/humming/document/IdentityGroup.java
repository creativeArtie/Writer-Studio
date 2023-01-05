package com.creativeartie.humming.document;

public enum IdentityGroup {
    FOOTNOTE, ENDNOTE, SOURCE, REF, IMAGE, TODO;

    SpanStyles getStyleClass() {
        return SpanStyles.valueOf(name());
    }
}
