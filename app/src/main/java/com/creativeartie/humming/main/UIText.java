package com.creativeartie.humming.main;

import java.util.*;

import com.google.common.base.*;

public enum UIText {
    NO_NOTES("note"), NO_FIELDS("note");

    private static ResourceBundle textBundle;
    private final String prefixNamespace;

    public static ResourceBundle getBundle() {
        if (textBundle == null) {
            textBundle = ResourceBundle.getBundle("data.uiText");
        }
        return textBundle;
    }

    private UIText(String prefix) {
        prefixNamespace = prefix;
    }

    public String getText() {
        return getBundle()
                .getString(prefixNamespace + "." + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name()));
    }
}
