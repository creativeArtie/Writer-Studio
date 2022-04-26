package com.creativeartie.writer.writing;

public enum TypedStyles {
    IDER("identifier"), OPER("operator"), NAME("name");

    private String styleName;

    TypedStyles(String name) {
        styleName = name;
    }

    public String getStyle() {
        return styleName;
    }
}
