package com.creativeartie.writer.writing;

public enum IdGroups {
    FOOTNOTE, ENDNOTE, SOURCE;

    public TypedStyles toTypedStyles() {
        return TypedStyles.values()[TypedStyles.FOOT.ordinal() + ordinal()];
    }
}
