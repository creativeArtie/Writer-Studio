package com.creativeartie.jwriter.property.window;

import java.util.*;
import java.time.*;

import static com.google.common.base.CaseFormat.*;
import static com.google.common.base.Preconditions.*;

import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.lang.Span;
import com.creativeartie.jwriter.main.*;
import com.creativeartie.jwriter.property.*;

public enum WindowText {

    HEADING_NO_TEXT("DisplayHeading.HeadingNoText"),
    HEADING_PLACEHOLDER("DisplayHeading.HeadingPlaceholder"),

    MENU_FILE("MainMenu.File"),
    MENU_FILE_NEW("MainMenu.FileCreate"),
    MENU_FILE_SAVE("MainMenu.FileSave"),
    MENU_FILE_OPEN("MainMenu.FileOpen"),
    MENU_FILE_EXIT("MainMenu.FileExit"),

    MENU_STATS("MainMenu.Stats"),
    MENU_STATS_GOALS("MainMenu.StatsGoal"),

    AGENDA_EMPTY("AgendaList.NoText"),
    AGENDA_TITLE("AgendaList.PaneTitle"),

    CALENDAR_NO_RECORD("Calendar.NoRecord"),

    HEADING_TITLE("TreeList.HeadingTitle"),
    OUTLINE_TITLE("TreeList.OutlineTitle");

    public static String getText(ButtonIcon icon){
        return getText("Icon.", icon.name());
    }

    public static String getText(Month month){
        return getText("Calendar.", month.name());
    }
    public static String getText(DayOfWeek day){
        return getText("Calendar.", day.name());
    }

    public static String getText(EditionType type){
        return getText("DisplayHeaing.Edition", type.name());
    }

    private static String getText(String base, String name){
        String key = base + UPPER_UNDERSCORE.to(UPPER_CAMEL, name);
        return getWindowText().getString(key);
    }

    private String bundleKey;
    private WindowText(String key){
        bundleKey = key;
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
        return getWindowText().getString(bundleKey);
    }
}
