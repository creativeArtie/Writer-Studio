package com.creativeartie.writer.writing;

public enum IdGroups {
    // @orderFor RefPhrase.Types
    FOOTNOTE, ENDNOTE, SOURCE;
    // @endOrder RefPhrase.Types

    public TypedStyles toTypedStyles() {
        return TypedStyles.values()[TypedStyles.FOOTNOTE.ordinal() + ordinal()];
    }
}
