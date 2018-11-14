package com.creativeartie.writerstudio.resource;

import java.util.*;
import java.time.*;

import static com.google.common.base.CaseFormat.*;
import static com.google.common.base.Preconditions.*;

import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.lang.Span;
import com.creativeartie.writerstudio.main.*;
// import com.creativeartie.writerstudio.javafx.utils.*;

public enum WindowText {/*
    PROGRAM_NAME("MainWindow.Title"),
    EMPTY_TEXT("CommonText.NoText"),
    EMPTY_NA("CommonText.NA"),
    NO_ID("CommonText.NoId"),

    LINED_HEADING("LinedName.Heading"),   LINED_OUTLINE("LinedName.Outline"),
    LINED_NUMBERED("LinedName.Numbered"), LINED_BULLET("LinedName.Bullet"),

    LINED_FOOTNOTE("LinedName.Footnote"), LINED_ENDNOTE("LinedName.Endnote"),
    LINED_LINK("LinedName.Link"),         LINED_NOTE("LinedName.Note"),

    LINED_AGENDA("LinedName.Agenda"), LINED_QUOTE("LinedName.Quote"),
    LINED_BREAK("LinedName.Break"),   LINED_SOURCE("LinedName.Cite"),
    PARAGRAPH("LinedName.Paragraph"),

    MENU_FILE("MainMenu.File"),             MENU_FILE_NEW("MainMenu.FileCreate"),
    MENU_FILE_SAVE("MainMenu.FileSave"),    MENU_FILE_OPEN("MainMenu.FileOpen"),
    MENU_FILE_EXPORT("MainMenu.ExportPdf"), MENU_FILE_EXIT("MainMenu.FileExit"),
    MENU_HELP("MainMenu.Help"), MENU_HELP_ABOUT("MainMenu.HelpAbout"),
    MENU_CHOOSER_TITLE("MainMenu.ChooserTitle"),

    MENU_STATS("MainMenu.Stats"), MENU_STATS_GOALS("MainMenu.StatsGoal"),

    TAB_AGENDA("NoteTabs.TitleAgenda"),
    TAB_LINK("NoteTabs.TitleLink"),
    TAB_FOOTNOTE("NoteTabs.TitleFootnote"),
    TAB_ENDNOTE("NoteTabs.TitleEndnote"),
    TAB_NOTE_CARD("NoteTabs.TitleNoteCard"),
    TAB_REFERENCE("NoteTabs.TitleReference"),

    TAB_CONTENT("LeftTabs.TableOfContents"),
    TAB_META("LeftTabs.MetaData"),
    // TAB_FILE("LeftTabs.Files"),
    TAB_TEXT("RightTabs.TextArea"),
    TAB_WEB("RightTabs.Research"),

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

    RESEARCH_URL("ResearchPane.AddressPlaceholder"),
    RESEARCH_SEARCH("ResearchPane.SearchPlaceholder"),
    RESEARCH_BACK("ResearchPane.BackButton"),
    RESEARCH_FORWARD("ResearchPane.ForwardButton"),
    RESEARCH_TIMEOUT_MINS("ResearchPane.TimeoutMinutes"),
    RESEARCH_TIMEOUT_SECS("ResearchPane.TimeoutSeconds"),

    DATA_META("DocData.MetaDataTitle"),
    DATA_AREA("DocData.MattersTitle"),
    DATA_EDIT("DocData.EditButton"),

    CALENDAR_NO_RECORD("Calendar.NoRecord"),

    HEADING_TITLE("HeadingTree.HeadingTitle"),
    OUTLINE_TITLE("HeadingTree.OutlineTitle"),
    PLACEHOLDER_OUTLINE("HeadingTree.OutlinePlaceholder"),

    GOALS_TITLE("StatsScreen.Title"),   GOAL_WORD_TEXT("StatsScreen.WordGoal"),
    GOALS_TIME_TEXT("StatsScreen.TimeGoal"), HOUR_UNIT("StatsScreen.HourUnit"),
    MINUTE_UNIT("StatsScreen.MinuteUnit"),


    SYNTAX_MODE("WriteScene.SyntaxMode"),
    PARSED_MODE("WriteScene.ParsedMode"),

    ABOUT_TITLE("AboutWindow.Title"), ABOUT_LICENSE("AboutWindow.License"),
    ABOUT_PDF_BOX("AboutWindow.PdfBox"), ABOUT_RICH_TEXT("AboutWindow.RichText"),
    ABOUT_GUAVA("AboutWindow.Guava"), ABOUT_LIBRARIES("AboutWindow.ThirdParty"),

    REF_NAME("ReferenceData.ColumnName"),
    REF_ID("ReferenceData.ColumnId"),
    REF_LONG("ReferenceData.ColumnDescription"),
    REF_EXAMPLE("ReferenceData.ColumnExample"),

    WORK_CITED("ExportText.WorkCited");

    public static String getNameText(FormatTypeField area){
        return getDisplay(getText("ReferenceData.Name", area.name()));
    }

    public static String getDescriptionText(FormatTypeField area){
        return getDisplay(getText("ReferenceData.Description", area.name()));
    }

    public static String getExampleText(FormatTypeField area){
        return getDisplay(getText("ReferenceData.Example", area.name()));
    }

    public static String getString(TextTypeInfo meta){
        return getDisplay(getText("DocData.Field", meta.name()));
    }

    public static String getString(TextTypeMatter area){
        return getDisplay(getText("DocData.Matter", area.name()));
    }

    public static String getText(ImageIcon icon){
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

    public static String getText(LinedSpan span){
        if (span instanceof LinedSpanLevelSection){
            LinedSpanLevelSection line = (LinedSpanLevelSection) span;
            return (line.isHeading()? LINED_HEADING: LINED_OUTLINE).getText() +
                " " + line.getLevel();
        } else if( span instanceof  LinedSpanLevelList){
            LinedSpanLevelList line = (LinedSpanLevelList) span;
            return (line.isNumbered()? LINED_NUMBERED: LINED_BULLET).getText() +
                " " + line.getLevel();
        } else if (span instanceof LinedSpanPointNote){
            return ((LinedSpanPointNote) span).getDirectoryType() ==
                DirectoryType.FOOTNOTE?
                LINED_FOOTNOTE.getText(): LINED_ENDNOTE.getText();
        } else if (span instanceof LinedSpanPointLink){
            return LINED_LINK.getText();
        } else if (span instanceof LinedSpanNote){
            return LINED_NOTE.getText();
        } else if (span instanceof LinedSpanAgenda){
            return LINED_AGENDA.getText();
        } else if (span instanceof LinedSpanQuote){
            return LINED_QUOTE.getText();
        } else if (span instanceof LinedSpanBreak){
            return LINED_BREAK.getText();
        } else if (span instanceof LinedSpanCite){
            return LINED_SOURCE.getText();
        } else if (span instanceof LinedSpanParagraph){
            return PARAGRAPH.getText();
        }
        assert false: "Missing code for: " + span.getClass();
        return "";
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
    }*/
}
