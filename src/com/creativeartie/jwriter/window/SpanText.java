package com.creativeartie.jwriter.window;

import java.util.*;

import static com.google.common.base.CaseFormat.*;
import static com.google.common.base.Preconditions.*;

import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.lang.Span;
import com.creativeartie.jwriter.main.*;
import com.creativeartie.jwriter.property.*;
public enum SpanText {
    EDITION_STUB("DisplayHeading"), EDITION_DRAFT("DisplayHeading"),
    EDITION_FINAL("DisplayHeading"), EDITION_OTHER("DisplayHeading"),
    EDITION_NONE("DisplayHeading"), HEADING_NO_TEXT("DisplayHeading"),
    HEADING_PLACEHOLDER("DisplayHeading");

    public static String getText(EditionType type){
        return valueOf("EDITION_" + type.name()).getText();
    }

    private String categoryKey;
    private SpanText(String cat){
        categoryKey = cat;
    }

    private static ResourceBundle texts;
    private static ResourceBundle getWindowText(){
        if (texts == null){
            texts = PropertyResourceBundle.getBundle("data.displayText",
                Locale.ENGLISH);
        }
        return texts;
    }

    public String getText(){
        String name = UPPER_UNDERSCORE.to(UPPER_CAMEL, name());
        return getWindowText().getString(categoryKey + "." + name);
    }
}