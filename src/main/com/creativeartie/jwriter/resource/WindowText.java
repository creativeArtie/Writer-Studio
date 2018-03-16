package com.creativeartie.jwriter.resource;

import java.util.*;
import java.time.*;

import static com.google.common.base.CaseFormat.*;
import static com.google.common.base.Preconditions.*;

import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.lang.Span;
import com.creativeartie.jwriter.main.*;

public enum WindowText {
    PROGRAM_NAME("MainWindow.Title"),
    EMPTY_TEXT("CommonText.NoText"),
    EMPTY_NA("CommonText.NA"),
    NO_ID("CommonText.NoId"),

    MENU_FILE("MainMenu.File"),            MENU_FILE_NEW("MainMenu.FileCreate"),
    MENU_FILE_SAVE("MainMenu.FileSave"),   MENU_FILE_OPEN("MainMenu.FileOpen"),
    MENU_FILE_EXIT("MainMenu.FileExit"),   MENU_HELP("MainMenu.Help"),
    MENU_HELP_ABOUT("MainMenu.HelpAbout"), MENU_CHOOSER_TITLE("MainMenu.ChooserTitle"),

    MENU_STATS("MainMenu.Stats"), MENU_STATS_GOALS("MainMenu.StatsGoal"),

    TAB_AGENDA("NoteTabs.TitleAgenda"),
    TAB_LINK("NoteTabs.TitleLink"),
    TAB_FOOTNOTE("NoteTabs.TitleFootnote"),
    TAB_ENDNOTE("NoteTabs.TitleEndnote"),
    TAB_NOTE_CARD("NoteTabs.TitleNoteCard"),

    AGENDA_LINE("AgendaTable.LineColumn"),
    AGENDA_TYPE("AgendaTable.TypeColumn"),
    AGENDA_SECTION("AgendaTable.SectionColumn"),
    AGENDA_TEXT("AgendaTable.TextColumn"),
    AGENDA_EMPTY("AgendaTable.EmptyTable"),

    LINKS_ID("LinksTable.IdentityColumn"),
    LINKS_LOOKUP("LinksTable.LookupColumn"),
    LINKS_LOCATION("LinksTable.LocationColumn"),
    LINKS_BOOKMARK("LinksTable.BookmarkColumn"),
    LINKS_DATA("LinksTable.ContentColumn"),
    LINKS_EMPTY("LinksTable.EmptyTable"),

    NOTES_ID("NotesTable.IdentityColumn"),
    NOTES_LOOKUP("NotesTable.LookupColumn"),
    NOTES_LOCATION("NotesTable.LocationColumn"),
    NOTES_BOOKMARK("NotesTable.BookmarkColumn"),
    NOTES_DATA("NotesTable.ContentColumn"),
    FOOTNOTE_EMPTY("NotesTable.EmptyFootnote"),
    ENDNOTE_EMPTY("NotesTable.EmptyEndnote"),

    NOTE_CARDS_ID("NoteCards.IdentityColumn"),
    NOTE_CARDS_SECTION("NoteCards.SectionColumn"),
    NOTE_CARDS_TITLE("NoteCards.TitleColumn"),
    NOTE_CARDS_EMPTY("NoteCards.EmptyTables"),
    NOTE_CARD_PLACEHOLDER_TITLE("NoteDetail.PlaceholderTitle"),
    NOTE_CARD_PLACHOLDER_DETAIL("NoteDetail.PlaceholderDetail"),
    NOTE_CARD_EMPTY_TITLE("NoteDetail.NoTitle"),
    NOTE_CARD_EMTPY_DETAIL("NoteDetail.NoDetail"),
    NOTE_CARD_FOOTNOTE("NoteDetail.FootnoteLabel"),
    NOTE_CARD_IN_TEXT("NoteDetail.InTextLabel"),
    NOTE_CARD_SOURCE("NoteDetail.SourceLabel"),
    NOTE_CARD_EDIT("NoteDetail.GotoEdit"),

    CALENDAR_NO_RECORD("Calendar.NoRecord"),

    HEADING_TITLE("HeadingTree.HeadingTitle"),
    OUTLINE_TITLE("HeadingTree.OutlineTitle"),
    PLACEHOLDER_HEADING("HeadingTree.HeadingPlaceholder"),

    GOALS_TITLE("StatsScreen.Title"),   GOAL_WORD_TEXT("StatsScreen.WordGoal"),
    GOALS_TIME_TEXT("StatsScreen.TimeGoal"), HOUR_UNIT("StatsScreen.HourUnit"),
    MINUTE_UNIT("StatsScreen.MinuteUnit"),


    SYNTAX_MODE("WriteScene.SyntaxMode"),
    PARSED_MODE("WriteScene.ParsedMode"),

    ABOUT_TITLE("AboutWindow.Title"), ABOUT_LICENSE("AboutWindow.License"),
    ABOUT_PDF_BOX("AboutWindow.PdfBox"), ABOUT_RICH_TEXT("AboutWindow.RichText"),
    ABOUT_GUAVA("AboutWindow.Guava"), ABOUT_LIBRARIES("AboutWindow.ThirdParty"),

    WORK_CITED("ExportText.WorkCited");

    static String getText(MetaData data){
        return getDisplay(data.getKey());
    }

    public static String getText(DirectoryType type){
        return getDisplay(getText("UserLists.", type.name()) + "ListName");
    }

    public static String getText(ButtonIcon icon){
        return getDisplay(getText("Icon.", icon.name()));
    }

    public static String getText(Month month){
        return getDisplay(getText("Calendar.", month.name()));
    }
    public static String getText(DayOfWeek day){
        return getDisplay(getText("Calendar.", day.name()));
    }

    public static String getText(EditionType type){
        return getDisplay(getText("DisplayHeading.Edition", type.name()));
    }

    private static String getText(String base, String name){
        return base + UPPER_UNDERSCORE.to(UPPER_CAMEL, name);
    }

    private static String getDisplay(String key){
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
        return getDisplay(bundleKey);
    }
}
