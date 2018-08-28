package com.creativeartie.writerstudio.javafx.utils;

import java.net.*;
import java.time.*;
import java.util.*;
import javafx.scene.layout.*;

import com.google.common.io.*;
import com.google.common.base.*;

import com.creativeartie.writerstudio.lang.markup.*;

public class LayoutConstants{

    private static ResourceBundle texts;
    static ResourceBundle getWindowText(){
        if (texts == null){
            texts = FileResource.DISPLAY_TEXT.getResourceBundle();
        }
        return texts;
    }

    static String getString(String text){
        return getWindowText().getString(text);
    }

    private static String getString(String base, String name){
        return getString(base + CaseFormat.UPPER_UNDERSCORE
            .to(CaseFormat.UPPER_CAMEL, name));
    }

    private static final String NOT_FOUND_STYLE = "no-span";

    private static final String EMPTY_NA = getString("CommonText.NotApplicable");
    private static final String EMPTY_TEXT = getString("CommonText.NoText");
    private static final String PROGRAM_NAME = getString("MainWindow.Title");

    private static final String CLOCK_FORMAT = "HH:mm:ss";

    static class UtilitiesConstants{
        public static final String NOT_FOUND_STYLE = LayoutConstants.
            NOT_FOUND_STYLE;
        public static final String NUMBER_COLUMN_STYLE = "id-numbered";
        public static final String NO_TEXT_STYLE       = "no-text";

        public static final String NO_ID = getString("CommonText.NoId");

        public static final String EMPTY_TEXT = LayoutConstants.EMPTY_TEXT;

        public static String getFieldTypeText(InfoFieldType area){
            return getString("NoteCards.", area.name() + "_COLUMN");
        }
    }

    public static class MainWindowConstants{
        public static final double[] VER_DIVIDER = new double[]{.2, .8};
        public static final double[] HOR_DIVIDER = new double[]{.0, 1.0};

        public static final String TAB_NOTE_CARD = getString("NoteTabs.TitleNoteCard");
        public static final String TAB_AGENDA    = getString("NoteTabs.TitleAgenda");
        public static final String TAB_LINK      = getString("NoteTabs.TitleLink");
        public static final String TAB_FOOTNOTE  = getString("NoteTabs.TitleFootnote");
        public static final String TAB_ENDNOTE   = getString("NoteTabs.TitleEndnote");
        public static final String TAB_REFERENCE = getString("NoteTabs.TitleReference");

        public static final String TAB_HEADINGS = getString("LeftTabs.TableOfContents");
        public static final String TAB_META     = getString("LeftTabs.MetaData");

        public static final String TAB_CONTENT = getString("RightTabs.TextArea");
        public static final String TAB_WEB     = getString("RightTabs.Research");
    }

    public static class MenuBarConstants{
        public static final String FILE        = getString("MainMenu.File");
        public static final String FILE_NEW    = getString("MainMenu.FileCreate");
        public static final String FILE_SAVE   = getString("MainMenu.FileSave");
        public static final String FILE_OPEN   = getString("MainMenu.FileOpen");
        public static final String FILE_EXPORT = getString("MainMenu.ExportPdf");
        public static final String FILE_EXIT   = getString("MainMenu.FileExit");
        public static final String STATS       = getString("MainMenu.Stats");
        public static final String STATS_GOALS = getString("MainMenu.StatsGoal");
        public static final String HELP       = getString("MainMenu.Help");
        public static final String HELP_ABOUT = getString("MainMenu.HelpAbout");

        public static final String OPEN_FILE = getString("MainMenu.ChooserTitle");
    }

    public static class WindowStatContants{
        public static final String WINDOW_TITLE = getString("StatsScreen.Title");
        public static final int WINDOW_WIDTH  = 490;
        public static final int WINDOW_HEIGHT = 490;

        public static final double GOAL_LABEL_COLUMN = 30.0;
        public static final double GOAL_DATA_COLUMN  = 70.0;

        public static final String GOAL_WORD_TEXT =
            getString("StatsScreen.WordGoal");
        public static final int WORD_GOAL_MIN = 0;
        public static final int WORD_GOAL_MAX = 10000;
        public static final int WORD_GOAL_DEFAULT = 100;

        public static final String GOALS_TIME_TEXT =
            getString("StatsScreen.TimeGoal");

        public static final String HOUR_UNIT =
            getString("StatsScreen.HourUnit");
        public static final int HOUR_GOAL_MIN = 0;
        public static final int HOUR_GOAL_MAX = 23;
        public static final int HOUR_GOAL_DEFAULT = 0;

        public static final String MINUTE_UNIT =
            getString("StatsScreen.MinuteUnit");
        public static final int MINS_GOAL_MIN = 0;
        public static final int MINS_GOAL_MAX = 59;
        public static final int MINS_GOAL_DEFAULT = 30;
    }

    public static class WindowStatChildContants{

        public static String MONTH_STYLE = "month";
        public static String WEEKDAY_STYLE = "weekday";
        public static String WEEKDAY_BOX_STYLE = "weekday-box";
        public static String DAY_BOX_STYLE = "day-box";

        public static int DAY_OF_WEEK = DayOfWeek.values().length;
        public static int SHOW_WEEKS = 6;
        public static int DAY_PANES = DAY_OF_WEEK * SHOW_WEEKS;
        public static double COLUMN_WIDTH = WindowStatContants.WINDOW_WIDTH /
            DAY_OF_WEEK;
        public static double CELL_HEIGHT = 60;

        public static double DAY_ANCHOR_TOP = 0.0;
        public static double DAY_ANCHOR_LEFT = 0.0;

        public static double STAT_ANCHOR_TOP = 5.0;
        public static double STAT_ANCHOR_RIGHT = 0.0;

        public static double ICON_SIZE = 50;

        public static String YEAR_MONTH_FORMAT = "MMMM yyyy";
        public static String TIP_FORMAT = "Written: %,d(%,d)\nTime: %s (%s)";
        public static String TIME_FORMAT = "%d:%02d:%02d";
        public static String NO_RECORD = getString("Calendar.NoRecord");
    }

    public static class WindowAboutConstants{
        public static final String TOP_STYLE = "top";
        public static final String TITLE_STYLE = "title";

        public static final int WINDOW_WIDTH = 650;
        public static final int WINDOW_HEIGHT = 500;
        public static final String WINDOW_TITLE = getString("AboutWindow.Title");

        public static final String PROGRAM_NAME = LayoutConstants.PROGRAM_NAME;
        public static final String LICENSE = getString("AboutWindow.License");
        public static final String PDF_BOX = getString("AboutWindow.PdfBox");
        public static final String RICH_TEXT = getString("AboutWindow.RichText");
        public static final String GUAVA = getString("AboutWindow.Guava");

        public static final String APACHE = getLicense(FileResource.APACHE);
        public static final String BSD = getLicense(FileResource.BSD);

        public static final String SOURCE_TEXT =
            getString("AboutWindow.SourceLabel");
        public static final String SOURCE_LINK =
            getString("AboutWindow.SourceLink");

        private static String getLicense(FileResource path){
            URL stream = path.getResourceFile();

            try {
                return Resources.asCharSource(stream, Charsets.UTF_8).read();
            } catch (Exception ex){
                throw new RuntimeException(ex);
            }
        }
    }

    public static class NoteCardConstants{
        public static final String LIST_ID_STYLE = "list-id";

        public static final String TAB_ID = getString("NoteCards.TabTitleId");
        public static final String EMPTY_LIST = getString("NoteCards.EmptyList");
        public static final String TAB_LOCATION = getString("NoteCards.TabTitleLocation");

        public static final String UNNAME_CARD = getString("NoteCards.UnnameCard");
        public static final String NAMED_CARD = getString("NoteCards.NamedCard");

        public static final String EMPTY_TEXT_LABEL = getString("NoteCards.EmptyTabText");
        public static final String TAB_EMPTY = getString("NoteCards.EmptyTabTitle");
        public static final String TAB_CONTENT = getString("NoteCards.ContentTabTitle");
        public static final String TAB_META = getString("NoteCards.MetaTabTitle");

        public static final String[] META_SHOW_ITEMS = new String[]{
            getString("NoteCards.SelectedUseMeta"),
            getString("NoteCards.SelectedUnusedMeta"),
            getString("NoteCards.SelectedAllMeta")
        };

        public static final String IN_USE_COLUMN = getString("NoteCards.InUseColumn");
        public static final String FIELD_COLUMN  = getString("NoteCards.FieldColumn");
        public static final String VALUE_COLUMN  = getString("NoteCards.ValueColumn");
    }

    public static class AgendaConstants{
        public static final String EMPTY_TEXT =
            getString("AgendaTable.EmptyTable");

        public static final String LINE_NAME = getString("AgendaTable.LineColumn");
        public static final String TYPE_NAME = getString("AgendaTable.TypeColumn");
        public static final String SECTION_NAME =
            getString("AgendaTable.SectionColumn");
        public static final String TEXT_NAME = getString("AgendaTable.TextColumn");

        public static final double LINE_WIDTH = 10.0;
        public static final double TYPE_WIDTH = 10.0;
        public static final double SECTION_WIDTH = 40.0;
        public static final double TEXT_WIDTH = 40.0;
    }

    public static class LinkConstants{
        public static final String EMPTY_TEXT =
            getString("LinksTable.EmptyTable");
        public static final String EMPTY_NA = LayoutConstants.EMPTY_NA;

        public static final String LOCATION_NAME =
            getString("LinksTable.LocationColumn");
        public static final String ID_NAME =
            getString("LinksTable.IdentityColumn");
        public static final String BOOKMARK_NAME =
            getString("LinksTable.BookmarkColumn");
        public static final String LOOKUP_NAME =
            getString("LinksTable.LookupColumn");
        public static final String CONTENT_NAME =
            getString("LinksTable.ContentColumn");

        public static final double LOCATION_WIDTH = 10.0;
        public static final double ID_WIDTH = 20.0;
        public static final double BOOKMARK_WIDTH = 10.0;
        public static final double LOOKUP_WIDTH = 20.0;
        public static final double CONTENT_WIDTH = 40.0;
    }

    public static class NoteConstants{
        public static final String EMPTY_FOOTNOTE =
            getString("NotesTable.EmptyFootnote");
        public static final String EMPTY_ENDNOTE =
            getString("NotesTable.EmptyEndnote");
        public static final String EMPTY_NA = LayoutConstants.EMPTY_NA;

        public static final String LOCATION_NAME =
            getString("NotesTable.LocationColumn");
        public static final String ID_NAME =
            getString("NotesTable.IdentityColumn");
        public static final String LOOKUP_NAME =
            getString("NotesTable.LookupColumn");
        public static final String CONTENT_NAME =
            getString("NotesTable.ContentColumn");

        public static final double LOCATION_WIDTH = 10.0;
        public static final double ID_WIDTH = 20.0;
        public static final double LOOKUP_WIDTH = 20.0;
        public static final double CONTENT_WIDTH = 50.0;
    }

    public static class ReferenceConstants{
        public static final String REF_NAME =
            getString("ReferenceData.ColumnName");
        public static final String REF_ID =
            getString("ReferenceData.ColumnId");
        public static final String REF_LONG =
            getString("ReferenceData.ColumnDescription");
        public static final String REF_EXAMPLE =
            getString("ReferenceData.ColumnExample");
        public static final String EMPTY_NA = LayoutConstants.EMPTY_NA;

        public static final double NAME_COLUMN = 20.0;
        public static final double ID_COLUMN = 20.0;
        public static final double DESCRIBE_COLUMN = 50.0;
        public static final double EXAMPLE_COLUMN = 10.0;

        public static String getNameText(FormatTypeField area){
            return getString("ReferenceData.Name", area.name());
        }

        public static String getDescriptionText(FormatTypeField area){
            return getString("ReferenceData.Description", area.name());
        }

        public static String getExampleText(FormatTypeField area){
            return getString("ReferenceData.Example", area.name());
        }
    }

    public static class HintConstants{
        public static final String HINT_SET_STYLE = "hint-set";
        public static final String HINT_UNSET_STYLE = "hint-unset";
        public static final String HINT_ALLOW_STYLE = "hint-allow";
        public static final String HINT_DISALLOW_STYLE = "hint-disallow";

        public static final double COLUMN1  = 7;
        public static final double COLUMN2  = 8;  /// 15
        public static final double COLUMN3  = 11; /// 26
        public static final double COLUMN4  = 6;  /// 32
        public static final double COLUMN5  = 6;  /// 38
        public static final double COLUMN6  = 12; /// 50
        public static final double COLUMN7  = 12; /// 62
        public static final double COLUMN8  = 12; /// 74
        public static final double COLUMN9  = 12; /// 86
        public static final double COLUMN10 = 14; /// 100
    }

    public static class HeadingConstants{
        public static final String HEADING_TITLE =
            getString("HeadingTree.HeadingTitle");
        public static final String OUTLINE_TITLE =
            getString("HeadingTree.OutlineTitle");
    }

    public static class MetaDataConstants{
        public static final String BORDER_STYLE = "border";
        public static final int WINDOW_WIDTH = 650;
        public static final int WINDOW_HEIGHT = 500;
        public static final int AREA_HEIGHT = (500 / 2) - 10;
        public static final double MATTER_EDIT_HEIGHT = 43;
        public static final double MATTER_VIEW_HEIGHT = 43;
        public static final double HINT_WIDTH = 100.0 / 3;

        public static final String META_DATA_TITLE = LayoutConstants.getString(
            "DocData.MetaDataTitle");
        public static final String MATTER_AREA_TITLE = LayoutConstants.getString(
            "DocData.MattersTitle");
        public static final String EDIT_BUTTON = LayoutConstants.getString(
            "DocData.EditButton");

        public static String getString(TextTypeMatter area){
            return LayoutConstants.getString("DocData.Matter", area.name());
        }

        public static String getString(TextTypeInfo meta){
            return LayoutConstants.getString("DocData.Field", meta.name());
        }
    }

    public static class TextPaneConstants{
        public static final String LINE_TYPE_STYLE = "line-type";
        public static final String TEXT_STAT_STYLE = "text-stat";
        public static final String CLOCK_STYLE = "program-clock";

        public static final String CLOCK_FORMAT = LayoutConstants.CLOCK_FORMAT;
        public static final String STAT_TEXT =
            "Publish: %d (%#.2f%%); Time: %d:%02d:%02d (%#.2f%%)";

        public static final String HEADING = getString("LinedName.Heading");
        public static final String OUTLINE = getString("LinedName.Outline");
        public static final String NUMBERED = getString("LinedName.Numbered");
        public static final String BULLET = getString("LinedName.Bullet");

        public static final String FOOTNOTE = getString("LinedName.Footnote");
        public static final String ENDNOTE = getString("LinedName.Endnote");
        public static final String LINK = getString("LinedName.Link");
        public static final String NOTE = getString("LinedName.Note");
        public static final String AGENDA = getString("LinedName.Agenda");
        public static final String QUOTE = getString("LinedName.Quote");
        public static final String BREAK = getString("LinedName.Break");
        public static final String SOURCE = getString("LinedName.Cite");
        public static final String PARAGRAPH = getString("LinedName.Paragraph");

        public static final double LABEL_WIDTH = 100.0 / 3;

        public static final long STOP = -2;
        public static final long START = -1;
        public static final long LENGTH = 60 * 1000000l;
    }

    public static class ResearchConstants{
        public static final double BACK_MENU_WIDTH = 10;
        public static final double FORWARD_MENU_WIDTH = 10;
        public static final double ADDRESS_BAR_WIDTH = 60;
        public static final double SEARCH_BAR_WIDTH = 20;

        public static final String BACK_MENU_TEXT = getString(
            "ResearchPane.BackButton");
        public static final String FORWARD_MENU_TEXT = getString(
            "ResearchPane.ForwardButton");
        public static final String ADDRESS_BAR_TEXT = getString(
            "ResearchPane.AddressPlaceholder");
        public static final String SEARCH_BAR_TEXT = getString(
            "ResearchPane.SearchPlaceholder");

        public static final String HTTP_START = "https://";
        public static final Duration TIME_LIMITS = Duration.ofMinutes(5);
        public static final String HOME_PAGE = HTTP_START + "duckduckgo.com";
        public static final String HTTP_TEST = "http";
        public static final String CLOCK_FORMAT = LayoutConstants.CLOCK_FORMAT;
        public static final String SEARCH_START = "http://duckduckgo.com?q=";
        public static final char SEARCH_REPLACE = '+';


        public static final String TIMEOUT_MINS = getString(
            "ResearchPane.TimeoutMinutes");
        public static final String TIMEOUT_SECS = getString(
            "ResearchPane.TimeoutSeconds");

    }
}
