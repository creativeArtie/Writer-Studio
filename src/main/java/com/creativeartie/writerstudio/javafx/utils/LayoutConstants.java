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
            texts = PropertyResourceBundle.getBundle(FileResource.DISPLAY_TEXT
                .getBundleString(),
                Locale.ENGLISH);
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

    private static final String EMPTY_TEXT = getString("CommonText.NoText");
    private static final String PROGRAM_NAME = getString("MainWindow.Title");
    private static final String CLOCK_FORMAT = "HH:mm:ss";

    static class UtilitiesConstants{
        public static String NOT_FOUND_STYLE = "no-span";
        public static String EMPTY_TEXT = LayoutConstants.EMPTY_TEXT;
    }

    public static class MainWindowConstants{
        public static final double[] VER_DIVIDER = new double[]{.2, .8};
        public static final double[] HOR_DIVIDER = new double[]{.0, 1.0};

        public static final String TAB_META = getString("LeftTabs.MetaData");
        public static final String TAB_CONTENT = getString("RightTabs.TextArea");
        public static final String TAB_WEB = getString("RightTabs.Research");
    }

    public static class MenuBarConstants{
        public static final String FILE = getString("MainMenu.File");
        public static final String FILE_NEW = getString("MainMenu.FileCreate");
        public static final String FILE_SAVE = getString("MainMenu.FileSave");
        public static final String FILE_OPEN = getString("MainMenu.FileOpen");
        public static final String FILE_EXPORT = getString("MainMenu.ExportPdf");
        public static final String FILE_EXIT = getString("MainMenu.FileExit");

        public static final String STATS = getString("MainMenu.Stats");
        public static final String STATS_GOALS = getString("MainMenu.StatsGoal");

        public static final String HELP = getString("MainMenu.Help");
        public static final String HELP_ABOUT = getString("MainMenu.HelpAbout");

        public static final String OPEN_FILE = getString("MainMenu.ChooserTitle");
    }

    public static class WindowStatContants{
        public static final String WINDOW_TITLE = getString("StatsScreen.Title");
        public static final int WINDOW_WIDTH = 490;
        public static final int WINDOW_HEIGHT = 490;

        public static final double GOAL_LABEL_COLUMN = 30.0;
        public static final double GOAL_DATA_COLUMN = 70.0;

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
            URL stream = LayoutConstants.class.getResource(path.getLocalPath());

            try {
                return Resources.asCharSource(stream, Charsets.UTF_8).read();
            } catch (Exception ex){
                throw new RuntimeException(ex);
            }
        }
    }

    public static class MetaDataConstants{
        public static final String BORDER_STYLE = "border";
        public static final int WINDOW_WIDTH = 650;
        public static final int WINDOW_HEIGHT = 500;
        public static final int AREA_HEIGHT = (500 / 2) - 10;

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
