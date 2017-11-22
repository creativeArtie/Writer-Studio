package com.creativeartie.jwriter.main;

import javafx.fxml.*;
import java.net.*;
import java.time.*;
import java.util.*;

import com.google.common.base.*;

import com.creativeartie.jwriter.property.*;
import com.creativeartie.jwriter.lang.markup.*;
import java.io.*;

public class Utilities{

    private static PropertyManager styles;
    private static ResourceBundle texts;
    private static PropertyManager options;

    public static String enumToKey(String name){
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, name);
    }

    @Deprecated
    public static PropertyManager getStyles(){
        if (styles == null){
            try {
                styles = new PropertyManager("data/base-styles",
                    "data/user-styles");
            } catch (IOException ex){
                throw new RuntimeException(ex);
            }
        }
        return styles;
    }

    public static PropertyManager getOptions(){
        if (options == null){
            try {
                options = new PropertyManager("data/base-options",
                    "data/user-options");
        }  catch (IOException ex){
                throw new RuntimeException(ex);
            }
        }
        return options;
    }

    @Deprecated
    public static StyleProperty getStyleProperty(String key){
        return getStyles().getStyleProperty(key);
    }

    @Deprecated
    public static String getCss(String key){
        return getStyles().getStyleProperty(key).toCss();
    }

    @Deprecated
    public static ResourceBundle getWindowText(){
        if (texts == null){
            texts = PropertyResourceBundle.getBundle("data.windowText", Locale.ENGLISH);
        }
        return texts;
    }

    @Deprecated
    public static String getString(String key){
        return getWindowText().getString(key);
    }

    public static String getString(DirectoryType type){
        String name = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL,
            type.name());
        return getWindowText().getString("ListView." + name);
    }

    public static String formatDuration(Duration duration){
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;
        return String.format("%d:%02d:%02d", hours, minutes, seconds);
    }

    private Utilities(){}
}
