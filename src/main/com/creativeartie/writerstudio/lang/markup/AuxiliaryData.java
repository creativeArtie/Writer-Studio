package com.creativeartie.writerstudio.lang.markup;

import java.util.*; // List;

import com.google.common.collect.*;  // ImmutableList;

import com.creativeartie.writerstudio.lang.*; // SetupParser, StyleInfoLeaf;

import static com.creativeartie.writerstudio.main.Checker.*;
import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** All constants used in this package. */
public final class AuxiliaryData{
    /// %Part 1: Token Constants ###############################################
    /// %Part 1.1: Lines =======================================================
    /// %Part 1.1.1: Lines with levels -----------------------------------------
    /// public static constants = LEVEL_MAX, LEVEL_STARTERS, LEVEL_HEADINGS

    /** Maximum number of levels:       {@value} */
    public static final int LEVEL_MAX = 6;

    /// Leveled Line begin part token for numbered and bullet
    private static final String LEVEL_BEGIN    = "\t";
    /// Leveled line begin part tokens
    private static final String LEVEL_HEADING  = "=";
    private static final String LEVEL_NUMBERED = "#";
    private static final String LEVEL_OUTLINE  = "#";
    private static final String LEVEL_BULLET   = "-";

    /** Headings and levels.
     *
     * <b> Be aware that the {@linkplain String#startsWith(String)} might return
     * true for several token starters. </b>
     */
    public static final Map<LinedParseLevel, List<String>> LEVEL_STARTERS =
        getLinedLevels();

    /** Heading and outlines only.
     *
     * See the warning in {@link LEVEL_STARTERS}
     */
    public static final List<String> LEVEL_HEADINGS = getLinedHeads();

    /** Fills the map of {@link LEVEL_STARTERS}.
     *
     * @return answer
     * @see #LEVEL_STARTERS
     */
    private static final Map<LinedParseLevel, List<String>> getLinedLevels(){
        ImmutableMap.Builder<LinedParseLevel, List<String>> ans = ImmutableMap
            .builder();
        for (LinedParseLevel parser: LinedParseLevel.values()){
            ImmutableList.Builder<String> set = ImmutableList.builder();
            for (int i = 0; i < LEVEL_MAX; i++){
                set.add(getLevelToken(parser, i + 1));
            }
            ans.put(parser, set.build());
        }
        return ans.build();
    }

    /** Creates a Leveled Line begin token.
     *
     * @param parser
     *      for parser
     * @param level
     *      line level
     * @return answer
     * @see #getLinedLevels()
     * @see #getLineHeads()
     */
    private static String getLevelToken(LinedParseLevel parser, int level){
        assert parser != null: "Null parser";
        assert level > 0 && level <= LEVEL_MAX: "Level not in range";
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

    /** repeats a character for {@code level} times
     *
     * @param repeat
     *      characters to repeat
     * @param level
     *      number of repeats
     * @see #getLevelToken(LinedParseLevel, int)
     */
    private static String repeat(String repeat, int level){
        assert repeat != null: "Null repeat";
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < level; i++){
            builder.append(repeat);
        }
        return builder.toString();
    }

    /** Fills the list of {@link LEVEL_HEADS}.
     *
     * @return answer
     * @see #LEVEL_HEADS.
     */
    private static final List<String> getLinedHeads(){
        ImmutableList.Builder<String> builder = ImmutableList.builder();
        return builder
            .addAll(LEVEL_STARTERS.get(LinedParseLevel.HEADING))
            .addAll(LEVEL_STARTERS.get(LinedParseLevel.OUTLINE))
            .build();
    }

    /// %Part 1.1.2: Other Lined Details ---------------------------------------
    /// public static constants: CHAR_NEWLINE, LINED_*

    /** new line character:                 {@value}. */
    public static final char CHAR_NEWLINE = '\n';
    /** new line token:                     {@value}. */
    public static final String LINED_END  = CHAR_NEWLINE + "";
    /** separator between field and data:   {@value}.*/
    public static final String LINED_DATA = ":";

    /// "Special" Line begin token  part 1
    private static final String LINED_BEGIN = "!";
    /// "Special" Line begin tokens part 2
    /** Agenda line starter:                    {@value}*/
    public static final String LINED_AGENDA   = LINED_BEGIN + "!"; /// aka: !!
    /** Note (heading & content) line starter:  {@value}*/
    public static final String LINED_NOTE     = LINED_BEGIN + "%"; /// aka: !%
    /** Citation line starter:                  {@value}*/
    public static final String LINED_CITE     = LINED_BEGIN + ">"; /// aka: !>
    /** Link line starter:                      {@value}*/
    public static final String LINED_LINK     = LINED_BEGIN + "@"; /// aka: !@
    /** Footnote line starter:                  {@value}*/
    public static final String LINED_FOOTNOTE = LINED_BEGIN + "^"; /// aka: !^
    /** Endnote line starter:                   {@value}*/
    public static final String LINED_ENDNOTE  = LINED_BEGIN + "*"; /// aka: !*
    /** Quote line starter:                     {@value}*/
    public static final String LINED_QUOTE    = ">";
    /** Line break token:                       {@value}*/
    public static final String LINED_BREAK    = "***\n";

    /** List of Line starter.
     *
     * See the warning in {@link LEVEL_STARTERS}.
     */
    public static final List<String> LINED_STARTERS = getLinedTokens();

    /** Fills {@link LINED_STARTERS}.
     *
     * @return answer
     */
    private static List<String> getLinedTokens(){
        ImmutableList.Builder<String> ans = ImmutableList.builder();
        for (List<String> list: LEVEL_STARTERS.values()){
            ans.addAll(list);
        }
        ans.add(LINED_AGENDA);
        ans.add(LINED_NOTE);
        ans.add(LINED_CITE);
        ans.add(LINED_LINK);
        ans.add(LINED_FOOTNOTE);
        ans.add(LINED_ENDNOTE);
        ans.add(LINED_QUOTE);
        ans.add(LINED_BREAK);
        return ans.build();
    }

    /// %Part 1.2: Directory ===================================================

    /** usual directory begin token:                {@value} */
    public static final String DIRECTORY_BEGIN    = "@";
    /** directory category separator token:         {@value} */
    public static final String DIRECTORY_CATEGORY = "-";
    /** usual directory end token  :                {@value} */
    public static final String DIRECTORY_END      = ":";

    /// %Part 1.3: Status ======================================================

    /** Edition start token:                   {@value} */
    public static final String EDITION_BEGIN = "#";
    /// (more constants are devise with {@link Edition#name()}

    /// %Part 1.4: Hyperlinks ==================================================

    /** Hyperlinks ref and direct starts:  {@value}*/
    public static final String LINK_BEGIN = "<";/// aka: <
    /** Hyperlinks ref starts:             {@value}*/
    public static final String LINK_REF   = LINK_BEGIN + "@";/// aka: <@
    /** Path - text separator token:        {@value}*/
    public static final String LINK_TEXT  = "|";
    /** Hyperlink end token:                {@value}*/
    public static final String LINK_END   = ">";

    /// %Part 1.5: Curly Formats ==============================================-

    /// Curly format begins token  part 1
    private static final String CURLY_BEGIN   = "{";
    /// Curly format begins tokens part 2
    /** Agenda    start token:                  {@value}.*/
    public static final String CURLY_AGENDA   = CURLY_BEGIN + "!"; /// aka: {!
    /** Ref key   start token:                  {@value}.*/
    public static final String CURLY_KEY      = CURLY_BEGIN + "%"; /// aka: {%
    /** Footnote  start token:                  {@value}.*/
    public static final String CURLY_FOOTNOTE = CURLY_BEGIN + "^"; /// aka: {^
    /** Endnote   start token:                  {@value}.*/
    public static final String CURLY_ENDNOTE  = CURLY_BEGIN + "*"; /// aka: {*
    /** Citation  start token:                  {@value}.*/
    public static final String CURLY_CITE     = CURLY_BEGIN + DIRECTORY_BEGIN;

    /** Curly format end token.                 {@value} **/
    public static final String CURLY_END      = "}";

    /// %Part 1.6: Format Modifiers ============================================

    /// list of possible formats
    private static final String FORMAT_ITALICS   = "*" ;
    private static final String FORMAT_BOLD      = "**";
    private static final String FORMAT_UNDERLINE = "_" ;
    private static final String FORMAT_CODED     = "`" ;

    /** Number of format types:            {@value} */
    public static final int FORMAT_TYPES = FormatType.values().length;

    /** List of Format keys in parse order*/
    public static final List<String> FORMAT_KEYS = ImmutableList.of(
        FORMAT_BOLD, FORMAT_ITALICS, FORMAT_UNDERLINE, FORMAT_CODED);

    /// %Part 1.7: Escape Begin Token ==========================================

    /** Char escape character:          {@value}*/
    public static char CHAR_ESCAPE    = '\\';
    /** Char escape token:              {@value}*/
    public static String TOKEN_ESCAPE = CHAR_ESCAPE + "";

    /// %Part 1.9: Format Part Separators ======================================
    /// For FormattedParsers

    /** Create the list of how children spans of format can end.
     * @return the list of term, without {@linkplain FORMAT_BOLD}
     */
    public static String[] listFormatEnderTokens(boolean note,
            String ... enders){
        ImmutableList.Builder<String> builder = ImmutableList.builder();

        builder.add(CURLY_AGENDA);
        if (note){
            builder.add(CURLY_FOOTNOTE, CURLY_ENDNOTE, CURLY_CITE);
        }
        ImmutableList<String> ans = builder.add(CURLY_KEY)
            /// Skipping FORMAT_BOLD
            .add(FORMAT_ITALICS, FORMAT_UNDERLINE, FORMAT_CODED)
            .add(LINK_BEGIN, LINK_REF)
            .add(enders).build();
        return ans.toArray(new String[ans.size()]);

    }

    /// ========================================================================
    /// @Part-2: Catalougue Categories ========================================-

    /// @Part-2-1: Document Cross-Refereneces ==================================
    /// For DirectoryType

    public static final String TYPE_FOOTNOTE = "foot";
    public static final String TYPE_ENDNOTE  = "end";
    public static final String TYPE_LINK     = "link";
    public static final String TYPE_NOTE     = "note";
    public static final String TYPE_COMMENT  = "comment";


    /// @Part-2-1: Document automatic bookmarks ================================
    /// For DirectoryType
    public static final List<String> TYPE_SECTION = ImmutableList.of("head");

    public static final String TYPE_AGENDA = "agenda";
    public static final List<String> TYPE_AGENDA_INLINE = ImmutableList.of(
        TYPE_AGENDA);
    public static final List<String> TYPE_AGENDA_LINED = ImmutableList.of(
        TYPE_AGENDA);

    /// ========================================================================
    /// @Part-3: Setup Parsers ================================================-

    /// Part-3-1: Content Span Parsers ========================================-
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

    /// Part-3-2: Formatted Span Parsers ======================================-
    static final SetupParser FORMATTED_TEXT = new FormattedParser(
        StyleInfoLeaf.TEXT, true);
    static final SetupParser NOTE_TEXT = new FormattedParser(
        StyleInfoLeaf.TEXT, false);
    static final SetupParser FORMATTED_DATA = new FormattedParser(
        StyleInfoLeaf.DATA, false);
    static final SetupParser FORMATTED_HEADER = new FormattedParser(
        StyleInfoLeaf.TEXT, true, EDITION_BEGIN);

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
    /// @Part-4: Private Constructor ==========================================-
    private AuxiliaryData(){}
}
