package com.creativeartie.humming.document;

public enum IdentityGroup {
    FOOTNOTE, ENDNOTE, SOURCE, REF, IMAGE, TODO;

    StyleClasses getStyleClass() {
        return StyleClasses.valueOf(name());
    }
}
