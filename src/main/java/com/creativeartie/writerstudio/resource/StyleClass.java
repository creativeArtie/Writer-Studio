package com.creativeartie.writerstudio.resource;

import javafx.scene.*;

public enum StyleClass{
    NO_TEXT("no-text"), NOT_FOUND("no-span"), ID_NUMBERED("id-numbered"),

    HINT_SET("hint-set"), HINT_UNSET("hint-unset"),
    HINT_ALLOW("hint-allow"), HINT_DISALLOW("hint-disallow"),
    NUMBER_COLUMN("table-number");

    private final String styleClass;

    private StyleClass(String name){
        styleClass = name;
    }

    public void addClass(Node node){
        node.getStyleClass().add(styleClass);
    }

    public void removeClass(Node node){
        node.getStyleClass().remove(styleClass);
    }

    public static void setHintClass(Node node, boolean set, boolean allowed){
        node.getStyleClass().removeAll(HINT_SET.styleClass,
            HINT_UNSET.styleClass, HINT_ALLOW.styleClass,
            HINT_DISALLOW.styleClass);
        StyleClass setClass = set? HINT_SET: HINT_UNSET;
        StyleClass allowClass = allowed? HINT_ALLOW: HINT_DISALLOW;
        node.getStyleClass().addAll(setClass.styleClass, allowClass.styleClass);
    }
}