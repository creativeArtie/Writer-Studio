package com.creativeartie.writerstudio.resource;

import java.util.*;
import java.time.*;

import static com.google.common.base.CaseFormat.*;
import static com.google.common.base.Preconditions.*;

import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.main.*;

public enum SyntaxHintText {
    LABEL("LabelText."), TOOLTIP("TooltipText.");

    private String keyPrefix;

    private SyntaxHintText(String prefix){
        keyPrefix = prefix;
    }

    private static ResourceBundle texts;
    private static ResourceBundle getBundle(){
        if (texts == null){
            texts = PropertyResourceBundle.getBundle("data.syntaxHints",
                Locale.ENGLISH);
        }
        return texts;
    }

    public String getText(LinedType type){
        return getText("Line", type);
    }

    public String getText(FormatTypeStyle type){
        return getText("Format", type);
    }

    public String getText(EditionType type){
        return getText("Edition", type);
    }

    public String getText(DirectoryType type){
        return getText("Point", type);
    }

    public String getText(InfoFieldType type){
        return getText("Field", type);
    }

    public String getText(AuxiliaryType type){
        return getText("Other", type);
    }

    public String getIdText(){
        return getBundle().getString(keyPrefix + "ID");
    }

    private String getText(String start, StyleInfo style){
        String value = UPPER_UNDERSCORE.to(UPPER_CAMEL, style.name());
        return getBundle().getString(keyPrefix + start + value);
    }
}