package com.creativeartie.humming.document;

public enum IdentityGroup {
    FOOTNOTE, ENDNOTE, SOURCE, REF, LINK, TODO;

    StyleClasses getStyleClass() {
        return StyleClasses.valueOf(name());
    }
}
