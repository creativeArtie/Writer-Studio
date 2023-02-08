package com.creativeartie.humming.main;

public enum InterfaceText implements TextBase {
    HEADING_TEXT, OUTLINE_TEXT;

    @Override
    public String getPrefix() {
        return "highlight";
    }
}
