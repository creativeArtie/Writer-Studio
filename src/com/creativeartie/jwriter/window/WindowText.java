package com.creativeartie.jwriter.window;

import java.util.*;

import static com.google.common.base.CaseFormat.*;
import static com.google.common.base.Preconditions.*;

import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.lang.Span;
import com.creativeartie.jwriter.main.*;
import com.creativeartie.jwriter.property.*;
public enum WindowText {
    EDITION_STUB("DisplayHeading"), EDITION_DRAFT("DisplayHeading"),
    EDITION_FINAL("DisplayHeading"), EDITION_OTHER("DisplayHeading"),
    EDITION_NONE("DisplayHeading"),

    HEADING_NO_TEXT("DisplayHeading"), HEADING_PLACEHOLDER("DisplayHeading"),

    MENU_FILE("MainMenu.File"), MENU_FILE_NEW("MainMenu.FileCreate"),
    MENU_FILE_SAVE("MainMenu.FileSave"), MENU_FILE_OPEN("MainMenu.FileOpen"),
    MENU_FILE_EXIT("MainMenu.FileExit"),
    MENU_STATS("MainMenu.Stats"), MENU_STATS_GOALS("MainMenu.StatsGoal")

    AGENDA_EMPTY("AgendaList.NoText"), AGENDA_TITLE("AgendaList.PaneTitle");

    public static String getText(EditionType type){
        return valueOf("EDITION_" + type.name()).getText();
    }

    private String categoryKey;
    private WindowText(String cat){
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