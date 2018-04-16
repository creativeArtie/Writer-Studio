package com.creativeartie.writerstudio.lang.markup;

import java.util.*; // List

import com.google.common.collect.*;  // ImmutableList

import com.creativeartie.writerstudio.lang.*; // SetupParser, StyleInfoLeaf

import static com.creativeartie.writerstudio.main.Checker.*;

/**
 * All strings used in this package. Each field (private and public) has its own
 * prefix. See source code for how the strings are grouped.
 */
public final class AuxiliaryData{
    /// ========================================================================
    /// @Part-1: Token Constants------------------------------------------------

    /// @Part-1.1: Lines with Levels -------------------------------------------
    /// For LinedParseLevel

    /**
     * Maximum number of levels. Use for
     * {@link getLevelToken(LinedParseLevel, int)}
     */
    public static final int LEVEL_MAX = 6;

    /// Leveled Line begin part token for numbered and bullet
    private static final String LEVEL_BEGIN    = "\t";
    /// Leveled line begin part tokens
    private static final String LEVEL_HEADING  = "=";
    private static final String LEVEL_NUMBERED = "#";
    private static final String LEVEL_OUTLINE  = "#";
    private static final String LEVEL_BULLET   = "-";

    /** Create a list of tokens for different level of a
     * {@link LinedParseLevel}.
     */
    public static String[] getLevelTokens(LinedParseLevel parser){
        String[] levels = new String[LEVEL_MAX];
        for (int i = 0; i < LEVEL_MAX; i++){
            levels[i] = getLevelToken(parser, LEVEL_MAX - i);
        }
        return levels;
    }

    /** Creates a Leveled Line begin token.
     *
     * <b> Be aware that the {@linkplain String#startsWith(String)} will return
     * true for several {@link LinedParseLevel#HEADING}'s or
     * {@link LinedParseLevel#OUTLINE}'s tokens
     *
     * @param parser
     *      use to specific which token. This should be all of them.
     * @param level
     *      a number between 1 and {@link LEVEL_MAX}
     * @return
     *      the starter token created
     */
    public static String getLevelToken(LinedParseLevel parser, int level){
        checkNotNull(parser, "parser");
        checkRange(level, "level", 0, false, LEVEL_MAX, true);
        switch (parser){
        case HEADING:
            /// =, ==, ===, ...
            return repeat(LEVEL_HEADING, level);
        case OUTLINE:
            /// !#, !##, !###, ...
            return LINED_BEGIN + repeat(LEVEL_OUTLINE, level);
        case NUMBERED:
            /// #, ##, ###, ...
            return repeat(LEVEL_NUMBERED, level);
        case BULLET:
            /// -, --, ---, ...
            return repeat(LEVEL_BULLET, level);
        default:
            throw new IllegalArgumentException("LinedParseLavel not use: " +
                parser);
        }
    }
    /// getLevelToken helper
    private static String repeat(String repeat, int level){
        assert repeat != null: "Null repeat";
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < level; i++){
            builder.append(repeat);
        }
        return builder.toString();
    }

    public static List<String> getLinedTokens(){
        ImmutableList.Builder<String> list = ImmutableList.builder();
        for (LinedParseLevel parser: LinedParseLevel.values()){
            for (String token: getLevelTokens(parser)){
                list.add(token);
            }
        }
        list.add(LINED_AGENDA);
        list.add(LINED_NOTE);
        list.add(LINED_CITE);
        list.add(LINED_LINK);
        list.add(LINED_FOOTNOTE);
        list.add(LINED_ENDNOTE);
        list.add(LINED_QUOTE);
        list.add(LINED_BREAK);
        return list.build();
    }

    /// @Part-1-2: Other Lined Details -----------------------------------------
    /// For BasicTextParse, LinedParseCite, LinedParseLevel, LinedParsePointer,
    ///     LinedParseRest, getLevelToken(LinedParseLevel, int)

    public static final String LINED_END = "\n";

    public static final String LINED_DATA = ":";

    /// "Special" Line begin token  part 1
    private static final String LINED_BEGIN = "!";
    /// "Special" Line begin tokens part 2
    public static final String LINED_AGENDA   = LINED_BEGIN + "!"; /// aka: !!
    public static final String LINED_NOTE     = LINED_BEGIN + "%"; /// aka: !%
    public static final String LINED_CITE     = LINED_BEGIN + ">"; /// aka: !>
    public static final String LINED_LINK     = LINED_BEGIN + "@"; /// aka: !@
    public static final String LINED_FOOTNOTE = LINED_BEGIN + "^"; /// aka: !^
    public static final String LINED_ENDNOTE  = LINED_BEGIN + "*"; /// aka: !*

    public static final String LINED_QUOTE = ">";
    public static final String LINED_BREAK = "***\n";

    /// @Part-1-3: Directory ---------------------------------------------------
    /// For DirectoryParser, LinedParseLevel, LinedParseRest, CURLY_CITE

    public static final String DIRECTORY_BEGIN    = "@"; /// Usual start
    public static final String DIRECTORY_CATEGORY = "-"; /// Category middle
    public static final String DIRECTORY_END      = ":"; /// Usual end

    /// @Part-1-4: Status ------------------------------------------------------
    /// For EditionParser, LinedParseLevel

    /** Edition start token */
    public static final String EDITION_BEGIN = "#";
    /// (more constants are devise with {@link Edition#name()}

    /// @Part-1-5: Hyperlinks --------------------------------------------------
    /// For FormatParseLinkDirect, FormatParseLinkRef, listFormatEnderTokens()

    public static final String LINK_BEGIN =              "<";/// aka: <
    public static final String LINK_REF   = LINK_BEGIN + "@";/// aka: <@

    /** Path - text separator token*/
    public static final String LINK_TEXT  = "|";
    public static final String LINK_END   = ">";

    /// @Part-1-6: Curly Formats -----------------------------------------------
    /// For FormatParseAgenda, FormatParsePointId, listFormatEnderTokens()

    /// Curly format begins token  part 1
    private static final String CURLY_BEGIN   = "{";
    /// Curly format begins tokens part 2
    public static final String CURLY_AGENDA   = CURLY_BEGIN + "!"; /// aka: {!
    public static final String CURLY_KEY      = CURLY_BEGIN + "%"; /// aka: {%
    public static final String CURLY_FOOTNOTE = CURLY_BEGIN + "^"; /// aka: {^
    public static final String CURLY_ENDNOTE  = CURLY_BEGIN + "*"; /// aka: {*
    public static final String CURLY_CITE     = CURLY_BEGIN + DIRECTORY_BEGIN;
    ///                                                                aka: {@

    /** Curly format end token. **/
    public static final String CURLY_END      = "}";

    /// @Part-1-7: Format Modifiers --------------------------------------------
    /// For FormattedParser, listFormatEnderTokens()

    /// list of possible formats
    private static final String FORMAT_ITALICS   = "*" ;
    private static final String FORMAT_BOLD      = "**";
    private static final String FORMAT_UNDERLINE = "_" ;
    private static final String FORMAT_CODED     = "`" ;

    /** Number of format types*/
    public static final int FORMAT_TYPES = FormatType.values().length;

    /** Create the list of possible format of text.
     * @return list of format with the correct parse order
     */
    public static final String[] listFormatTextTokens(){
        /// FORMAT_BOLD must before FORMAT_ITALICS
        return new String[]{
            FORMAT_BOLD, FORMAT_ITALICS, FORMAT_UNDERLINE, FORMAT_CODED
        };
    }

    /// @Part-1-8: Escape Begin Token ------------------------------------------
    /// For BasicParseText

    /** Char escape token. */
    public static char CHAR_ESCAPE = '\\';

    /// @Part-1-9: Format Part Separators --------------------------------------
    /// For FormattedParsers

    /** Create the list of how children spans of format can end.
     * @return the list of term, without {@linkplain FORMAT_BOLD}
     */
    public static String[] listFormatEnderTokens(boolean note){
        if (! note){
            return new String[]{
                CURLY_AGENDA, LINK_BEGIN, LINK_REF, CURLY_KEY,
                FORMAT_ITALICS, FORMAT_UNDERLINE, FORMAT_CODED
            };
        }
        return new String[]{
            CURLY_AGENDA, CURLY_FOOTNOTE, CURLY_ENDNOTE, CURLY_CITE, CURLY_KEY,
            LINK_BEGIN, LINK_REF,
            /* FORMAT_BOLD, */ FORMAT_ITALICS, FORMAT_UNDERLINE, FORMAT_CODED
        };
    }

    /// ========================================================================
    /// @Part-2: Catalougue Categories -----------------------------------------

    /// @Part-2-1: Document Cross-Refereneces ----------------------------------
    /// For DirectoryType

    public static final String TYPE_FOOTNOTE = "foot";
    public static final String TYPE_ENDNOTE  = "end";
    public static final String TYPE_LINK     = "link";
    public static final String TYPE_NOTE     = "note";
    public static final String TYPE_COMMENT  = "comment";


    /// @Part-2-1: Document automatic bookmarks --------------------------------
    /// For DirectoryType
    public static final List<String> TYPE_SECTION = ImmutableList.of("head");

    public static final String TYPE_AGENDA = "agenda";
    public static final List<String> TYPE_AGENDA_INLINE = ImmutableList.of(
        TYPE_AGENDA);
    public static final List<String> TYPE_AGENDA_LINED = ImmutableList.of(
        TYPE_AGENDA);

    /// ========================================================================
    /// @Part-3: Setup Parsers -------------------------------------------------

    /// Part-3-1: Content Span Parsers -----------------------------------------
    static final SetupParser CONTENT_BASIC = new ContentParser(
        StyleInfoLeaf.TEXT);
    static final SetupParser CONTENT_AGENDA = new ContentParser(
        StyleInfoLeaf.TEXT, CURLY_END);
    static final SetupParser CONTENT_KEY = new ContentParser(
        StyleInfoLeaf.FIELD, CURLY_END);
    static final SetupParser CONTENT_LINK = new ContentParser(
        StyleInfoLeaf.TEXT, LINK_END);
    static final SetupParser CONTENT_DATA = new ContentParser(
        StyleInfoLeaf.DATA);
    static final SetupParser CONTENT_DIR_LINK  = new ContentParser(
        StyleInfoLeaf.PATH);
    static final SetupParser CONTENT_LINE_LINK = new ContentParser(
        StyleInfoLeaf.PATH, LINK_TEXT, LINK_END);

    /// Part-3-2: Formatted Span Parsers ---------------------------------------
    static final SetupParser FORMATTED_TEXT = new FormattedParser(
        StyleInfoLeaf.TEXT, true);
    static final SetupParser NOTE_TEXT = new FormattedParser(
        StyleInfoLeaf.TEXT, false);
    static final SetupParser FORMATTED_DATA = new FormattedParser(
        StyleInfoLeaf.DATA, false);
    static final SetupParser FORMATTED_HEADER = new FormattedParser(
        StyleInfoLeaf.TEXT, true, EDITION_BEGIN);

    static final SetupParser ID_DATA = DirectoryParser
        .getRefParser(DirectoryType.NOTE);

    /// Part-3-3: Main Section Line Parsers
    public static final List<SetupParser> SECTION_PARSERS = getSectionParsers();

    private static List<SetupParser> getSectionParsers(){
        ImmutableList.Builder<SetupParser> builder = ImmutableList.builder();
        builder.add(LinedParseLevel.BULLET, LinedParseLevel.NUMBERED);
        builder.add(LinedParsePointer.values());
        builder.add(LinedParseRest.getSectionList());
        return builder.build();
    }

    /// ========================================================================
    /// @Part 4: Writing Data

    private static final String TITLE        = "head-";
    public static final String TITLE_TOP     = TITLE  + "top     |";
    public static final String TITLE_CENTER  = TITLE  + "centre  |";
    public static final String TITLE_BOTTOM  = TITLE  + "bottom  |";
    private static final String TEXT         = "text-";
    public static final String TEXT_HEADER   = TEXT   + "header  |";
    public static final String TEXT_AFTER    = TEXT   + "after   |";
    public static final String TEXT_BREAK    = TEXT   + "break   |";
    private static final String CITE         = "cite-";
    public static final String CITE_TITLE    = CITE   + "header  |";
    private static final String META         = "meta-";
    public static final String META_AUTHOR   = META   + "author  |";
    public static final String META_KEYWORDS = META   + "keywords|";
    public static final String META_SUBJECT  = META   + "subject |";
    public static final String META_TITLE    = META   + "title   |";

    public static final int ALIGN_START = TITLE_TOP.length();
    public static final String ALIGN_RIGHT  = "right |";
    public static final String ALIGN_LEFT   = "left  |";
    public static final String ALIGN_CENTER = "center|";
    public static final String ALIGN_TEXT   = "text  |";


    /// ========================================================================
    /// @Part-4: Private Constructor -------------------------------------------
    private AuxiliaryData(){}
}
