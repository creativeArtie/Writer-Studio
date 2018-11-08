package com.creativeartie.writerstudio.javafx.utils;

import java.io.*; // InputStrem
import java.net.*;
import java.util.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;

public enum FileResource{
    DISPLAY_TEXT("displayText", ".properties"),
    CODE_STYLE("codeStyles", ".properties"),
    HINT_TEXTS("syntaxHints", ".properties"),

    ICON_PATH("icons", ".png"),
    APACHE("apache", ".txt"), BSD("bsd", ".txt"),

    MAIN_CSS("main", ".css"), STAT_CSS("stats", ".css"),
    ABOUT_CSS("about", ".css"), META_CSS("meta", ".css");

    private static final String[] LOCATION = new String[]{
        "com", "creativeartie", "writerstudio", "javafx", "utils"
    };
    private final String baseString;
    private final String fileExt;

    private FileResource(String base, String ext){
        baseString = base;
        fileExt = ext;
    }

    ResourceBundle getResourceBundle(){
        stateCheck(ordinal() < ICON_PATH.ordinal(), "Not supported for " + this);
        return PropertyResourceBundle.getBundle(
            String.join(".", LOCATION) + "." + baseString, Locale.ENGLISH
        );
    }

    public String getCssPath(){
        stateCheck(ordinal() > BSD.ordinal(), "Nort supported for " + this);
        return String.join("/", LOCATION) + "/" + baseString + fileExt;
    }

    URL getResourceFile(){
        return FileResource.class.getResource(baseString + fileExt);
    }

    InputStream getResourceStream(){
        return FileResource.class.getResourceAsStream(baseString + fileExt);
    }
}
