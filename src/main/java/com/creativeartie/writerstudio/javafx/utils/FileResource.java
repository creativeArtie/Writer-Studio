package com.creativeartie.writerstudio.javafx.utils;

import java.io.*; // InputStrem
import java.net.*; // URL

public enum FileResource{
    MAIN_CSS("main", ".css"), STAT_CSS("stats", ".css"),
    ABOUT_CSS("about", ".css"),

    DISPLAY_TEXT("displayText", ".properties"),
    CODE_STYLE("codeStyles", ".properties"),
    ICON_PATH("icons", ".png"),

    APACHE("apache", ".txt"), BSD("bsd", ".txt");

    private static final String[] LOCATION = new String[]{
        "com", "creativeartie", "writerstudio", "javafx", "utils"
    };
    private final String baseString;
    private final String fileExt;

    private FileResource(String base, String ext){
        baseString = base;
        fileExt = ext;
    }

    public String getCssPath(){
        return String.join("/", LOCATION) + "/" + baseString + fileExt;
    }

    String getLocalPath(){
        return baseString + fileExt;
    }

    String getBundleString(){
        return String.join(".", LOCATION) + "." + baseString;
    }
}
