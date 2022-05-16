package com.creativeartie.writer.writing;

public enum TypedStyles {
    ID("identifier"), OPERATOR("operator"), NAME("name"), TEXT("text"),
    ERROR("error"), FOOTNOTE("footnote"), ENDNOTE("endnote"), SOURCE("source"),
    TODO("todo");

    private String styleName;

    TypedStyles(String name) {
        styleName = name;
    }

    public String getStyle() {
        return styleName;
    }
}
