package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.google.common.collect.*;

import com.creativeartie.writerstudio.lang.*;

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
    private static final String LEVEL_BULLET   = "-";
    private static final String LEVEL_HEADING  = "=";
    private static final String LEVEL_NUMBERED = "#";
    private static final String LEVEL_OUTLINE  = "#";

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
     * @see #LINED_STARTERS
     */
    private static List<String> getLinedTokens(){
        ImmutableList.Builder<String> ans = ImmutableList.builder();
        for (List<String> list: LEVEL_STARTERS.values()){
            ans.addAll(list);
        }
        ans.add(LINED_AGENDA, LINED_NOTE, LINED_CITE, LINED_LINK)
            .add(LINED_FOOTNOTE, LINED_ENDNOTE, LINED_QUOTE, LINED_BREAK);
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
    public static final String FORMAT_ITALICS   = "*" ;
    public static final String FORMAT_BOLD      = "**";
    public static final String FORMAT_UNDERLINE = "_" ;
    public static final String FORMAT_CODED     = "`" ;

    /** Number of format types:            {@value} */
    public static final int FORMAT_TYPES = FormatType.values().length;

    /// %Part 1.7: Escape Begin Token ==========================================

    /** Char escape character:          {@value}*/
    public static char CHAR_ESCAPE    = '\\';
    /** Char escape token:              {@value}*/
    public static String TOKEN_ESCAPE = CHAR_ESCAPE + "";

    /// %Part 1.8: Format Part Separators ======================================

    /** Create the list of how children spans of format can end.
     *
     * @return answer
     */
    public static String[] listFormatEnderTokens(boolean note,
            String ... enders){
        ImmutableList.Builder<String> builder = ImmutableList.builder();

        builder.add(CURLY_AGENDA);
        if (note){
            /// excludes notes:
            builder.add(CURLY_FOOTNOTE, CURLY_ENDNOTE, CURLY_CITE);
        }
        ImmutableList<String> ans = builder.add(CURLY_KEY)
            /// Skipping FORMAT_BOLD
            .add(FORMAT_ITALICS, FORMAT_UNDERLINE, FORMAT_CODED)
            .add(LINK_BEGIN, LINK_REF)
            .add(enders).build();
        return ans.toArray(new String[ans.size()]);

    }

    /// %Part 1.9 Citation Field Names #########################################

    /** Citation in-text  field:                 {@value}*/
    public static final String SOURCE_IN_TEXT   = "in-text";
    /** Citation footnote field:                  {@value}*/
    public static final String SOURCE_FOOTNOTE  = "footnote";
    /** Citation last page field:                 {@value}*/
    public static final String SOURCE_MAIN      = "source";
    /** Citation error field    :                 {@value}*/
    public static final String SOURCE_REFERENCE = "ref";

    /// %Part 2: Catalougue Categories #########################################

    /// %Part 2.1: DirectoryType Base Categories ===============================

    /** For {@link DirectoryType.FOOTNOTE}:     {@value} */
    public static final String TYPE_FOOTNOTE  = "foot";
    /** For {@link DirectoryType.ENDNOTE}:      {@value} */
    public static final String TYPE_ENDNOTE   = "end";
    /** For {@link DirectoryType.LINK}:         {@value} */
    public static final String TYPE_LINK      = "link";
    /** For {@link DirectoryType.RESEARCH}:     {@value} */
    public static final String TYPE_RESEARCH  = "note";
    /** For {@link DirectoryType.NOTE}:         {@value} */
    public static final String TYPE_NOTE      = "idless";


    /// %Part 2.2: Agenda Categories ===========================================

    /** Basic agenda category:               {@value}. */
    public static final String TYPE_AGENDA = "agenda";
    /** Same as {@link #TYPE_AGENDA} in a list for {@link FormatSpanAgenda}.*/
    public static final List<String> TYPE_AGENDA_INLINE = ImmutableList.of(
        TYPE_AGENDA);
    /** Same as {@link #TYPE_AGENDA} in a list for {@link LinedSpanAgenda}.*/
    public static final List<String> TYPE_AGENDA_LINED = ImmutableList.of(
        TYPE_AGENDA);

    /// %Part 3: Setup Parsers #################################################
    /// %Part 3.1: Other Content Span Parsers ==================================

    /** A {@link ContentParser} for simple text.
     *
     * This set leaf style as {@link StyleInfoLeaf.TEXT}.
     *
     * @see EditionParser
     * @see LinedParseRest.AGENDA
     * @see TextDataParser
     */
    static final ContentParser CONTENT_BASIC = new ContentParser(
        StyleInfoLeaf.TEXT);

    /** A {@link ContentParser} for agenda text.
     *
     * This set leaf style as {@link StyleInfoLeaf.TEXT} and adds a ender
     * "{@value CURLY_END}".
     *
     * @see FormatParseAgenda
     */
    static final ContentParser CONTENT_AGENDA = new ContentParser(
        StyleInfoLeaf.TEXT, CURLY_END);

    /** A {@link ContentParser} for reference field text
     *
     * This set leaf style as {@link StyleInfoLeaf.FIELD} and adds a ender
     * "{@value CURLY_END}".
     *
     * @see FormatParsePointKey
     */
    static final ContentParser CONTENT_KEY = new ContentParser(
        StyleInfoLeaf.FIELD, CURLY_END);

    /** A {@link ContentParser} for hyperlink text.
     *
     * This set leaf style as {@link StyleInfoLeaf.TEXT} and adds a ender
     * "{@value LINK_END}".
     *
     * @see FormatParseLink
     */
    static final ContentParser CONTENT_LINK = new ContentParser(
        StyleInfoLeaf.TEXT, LINK_END);

    /** A {@link ContentParser} for contntData text.
     *
     * This set leaf style as {@link StyleInfoLeaf.Data}.
     *
     * @see InfoDataParser.TEXT
     */
    static final ContentParser CONTENT_DATA = new ContentParser(
        StyleInfoLeaf.DATA);

    /** A {@link ContentParser} for link line text.
     *
     * This set leaf style as {@link StyleInfoLeaf.PATH}.
     *
     * @see LinedParsePointer#HYPERLINK
     */
    static final ContentParser CONTENT_DIR_LINK  = new ContentParser(
        StyleInfoLeaf.PATH);

    /** A {@link ContentParser} for direct link text.
     *
     * This set leaf style as {@link StyleInfoLeaf.PATH} and adds a ender
     * "{@value LINK_TEXT}" and "{@value LINK_END}".
     *
     * @see LinedParsePointer#HYPERLINK
     */
    static final ContentParser CONTENT_LINE_LINK = new ContentParser(
        StyleInfoLeaf.PATH, LINK_TEXT, LINK_END);

    /// For ContentParser for DirectoryParser see the class itself

    /// %Part 3.2: Formatted Span Parsers =======================================

    /** A {@link ContentParser} for note text.
     *
     * This set leaf style as {@link StyleInfoLeaf.TEXT}, and allows notes.
     *
     * @see LinedParsePointer#FOOTNOTE
     * @see LinedParsePointer#ENDNOTE
     * @see LinedParseRest#QUOTE
     * @see LinedParseRest#PARAGRAPH
     */
    static final FormattedParser FORMATTED_TEXT = new FormattedParser(
        StyleInfoLeaf.TEXT, true);

    /** A {@link ContentParser} for note text and other document matters.
     *
     * This set leaf style as {@link StyleInfoLeaf.TEXT}, and not allows notes.
     *
     * @see LinedParseRest#NOTE
     * @see TextDataParser
     */
    static final FormattedParser NOTE_TEXT = new FormattedParser(
        StyleInfoLeaf.TEXT, false);

    /** A {@link ContentParser} for citations.
     *
     * This set leaf style as {@link StyleInfoLeaf.DATA}, and not allows notes.
     *
     * @see InfoDataParser#FORMATTED
     */
    static final FormattedParser FORMATTED_DATA = new FormattedParser(
        StyleInfoLeaf.DATA, false);

    /** A {@link ContentParser} for heading text.
     *
     * This set leaf style as {@link StyleInfoLeaf.PATH}, allows notes, and
     * adds a ender "{@value EDITION_BEGIN}".
     *
     * @see LinedParseLevel
     */
    static final FormattedParser FORMATTED_HEADER = new FormattedParser(
        StyleInfoLeaf.TEXT, true, EDITION_BEGIN);

    /// %Part 3.3: Main Section Line Parsers ===================================
    public static final List<SetupParser> SECTION_PARSERS = getSectionParsers();

    /** Create the list of {@link SECTION_PARSERS}.
     *
     * @return answer
     */
    private static List<SetupParser> getSectionParsers(){
        ImmutableList.Builder<SetupParser> builder = ImmutableList.builder();
        builder.add(LinedParseLevel.BULLET, LinedParseLevel.NUMBERED);
        builder.add(LinedParsePointer.values());
        builder.add(LinedParseRest.getSectionList());
        return builder.build();
    }

    /// ========================================================================
    /// @Part 4: Writing Data

    private static final String TITLE         = "head-";
    /** At top    of a  title  page:          {@value} */
    public static final String TITLE_TOP     = TITLE  + "top     |";
    /** At center of a  title  page:          {@value} */
    public static final String TITLE_CENTER  = TITLE  + "centre  |";
    /** At bottom of a  title  page:          {@value} */
    public static final String TITLE_BOTTOM  = TITLE  + "bottom  |";
    private static final String TEXT         = "text-";
    /** For the header of a  content page:    {@value} */
    public static final String TEXT_HEADER   = TEXT   + "header  |";
    /** For the ending of a  content section:  {@value} */
    public static final String TEXT_AFTER    = TEXT   + "after   |";
    /** For a   break of a content page:      {@value} */
    public static final String TEXT_BREAK    = TEXT   + "break   |";
    private static final String CITE         = "cite-";
    /** For title   of a citation section:     {@value} */
    public static final String CITE_TITLE    = CITE   + "header  |";
    private static final String META         = "meta-";
    /** PDF file property  for author:        {@value} */
    public static final String META_AUTHOR   = META   + "author  |";
    /** PDF file property  for keywords:      {@value} */
    public static final String META_KEYWORDS = META   + "keywords|";
    /** PDF file property  for subject:       {@value} */
    public static final String META_SUBJECT  = META   + "subject |";
    /** PDF file property  for title:         {@value} */
    public static final String META_TITLE    = META   + "title   |";

    /** Data type or line alignment length:  {@value} */
    public static final int ALIGN_START     = TITLE_TOP.length();
    /** Alignment right:                      {@value} */
    public static final String ALIGN_RIGHT  = "right |";
    /** Alignment left:                       {@value} */
    public static final String ALIGN_LEFT   = "left  |";
    /** Alignment center:                     {@value} */
    public static final String ALIGN_CENTER = "center|";
    /** Data type text:                       {@value} */
    public static final String ALIGN_TEXT   = "text  |";


    /// ========================================================================
    /// @Part-4: Private Constructor ==========================================-
    private AuxiliaryData(){}
}
