package com.creativeartie.writer.lang.markup;

import java.time.format.*;
import java.util.*;

import com.google.common.base.*;
import com.google.common.collect.*;

import com.creativeartie.writer.lang.*;

/** All constants used in this package. */
public final class AuxiliaryData{
    /// %Part 1: Token Constants ###############################################
    /// %Part 1.1: Lines =======================================================
    /// %Part 1.1.1: Lines with levels -----------------------------------------
    /// public static constants = LEVEL_MAX, LEVEL_STARTERS, LEVEL_HEADINGS

    /** Maximum number of levels:       {@value} */
    public static final int LEVEL_MAX = 6;

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
    static final Map<LinedParseLevel, List<String>> LEVEL_STARTERS =
        getLinedLevels();


    static final Map<LinedParseLevel, List<String>> LEVEL_REVERSE =
        getReverse(LEVEL_STARTERS);

    /** Heading and outlines only.
     *
     * See the warning in {@link #LEVEL_STARTERS}
     */
    static final List<String> LEVEL_HEADINGS = getLinedHeads();

    /** Fills the map of {@link #LEVEL_STARTERS}.
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

    private static Map<LinedParseLevel, List<String>> getReverse(
            Map<LinedParseLevel, List<String>> map){
        ImmutableMap.Builder<LinedParseLevel, List<String>> ans = ImmutableMap
            .builder();
        for (Map.Entry<LinedParseLevel, List<String>> entry: map.entrySet()){
            ans.put(entry.getKey(), Lists.reverse(entry.getValue()));
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
    public static final String LINED_BEGIN = "!";
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
     * See the warning in {@link #LEVEL_STARTERS}.
     */
    static final List<String> LINED_STARTERS = getLinedTokens();

    /** Fills {@link #LINED_STARTERS}.
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

    /** Number of format types: from
     * {@link FormatTypeStyle}{@code .values().length()}
     */
    public static final int FORMAT_TYPES = FormatTypeStyle.values().length;

    /// %Part 1.7: Escape Begin Token ==========================================

    /** Char escape character:          {@value}*/
    public static final char CHAR_ESCAPE    = '\\';
    /** Char escape token:  from string of "{@value CHAR_ESCAPE}".*/
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
    private static final String SOURCE          = "source";
    /** Citation in-text  field:                 {@value}*/
    public static final String SOURCE_IN_TEXT   = SOURCE + "|in-text";
    /** Citation footnote field:                  {@value}*/
    public static final String SOURCE_FOOTNOTE  = SOURCE + "|footnote";
    /** Citation last page field:                 {@value}*/
    public static final String SOURCE_MAIN      = SOURCE + "|work-cited";
    /** Citation error field    :                 {@value}*/
    public static final String SOURCE_REFERENCE = SOURCE + "|reference";

    /// %Part 2: Catalougue Categories #########################################

    /// %Part 2.1: DirectoryType Base Categories ===============================

    /** For {@link DirectoryType#FOOTNOTE}:     {@value} */
    public static final String TYPE_FOOTNOTE  = "foot";
    /** For {@link DirectoryType#ENDNOTE}:      {@value} */
    public static final String TYPE_ENDNOTE   = "end";
    /** For {@link DirectoryType#LINK}:         {@value} */
    public static final String TYPE_LINK      = "link";
    /** For {@link DirectoryType#RESEARCH}:     {@value} */
    public static final String TYPE_RESEARCH  = "note";
    /** For {@link DirectoryType#NOTE}:         {@value} */
    public static final String TYPE_NOTE      = "idless";


    /// %Part 2.2: Agenda Categories ===========================================

    /** Basic agenda category:               {@value}. */
    public static final String TYPE_AGENDA = "agenda";
    /** Same as {@link #TYPE_AGENDA} in a list for {@link FormatSpanAgenda}.*/
    static final List<String> TYPE_AGENDA_INLINE = ImmutableList.of(
        TYPE_AGENDA);
    /** Same as {@link #TYPE_AGENDA} in a list for {@link LinedSpanAgenda}.*/
    static final List<String> TYPE_AGENDA_LINED = ImmutableList.of(
        TYPE_AGENDA);

    /// %Part 3: Setup Parsers #################################################
    /// %Part 3.1: Other Content Span Parsers ==================================

    /** A {@link ContentParser} for simple text.
     *
     * This set leaf style as {@link SpanLeafStyle.TEXT}.
     *
     * @see EditionParser
     * @see LinedParseRest.AGENDA
     * @see TextParser
     */
    static final ContentParser CONTENT_BASIC = new ContentParser(
        SpanLeafStyle.TEXT);

    /** A {@link ContentParser} for agenda text.
     *
     * This set leaf style as {@link SpanLeafStyle.TEXT} and adds a ender
     * "{@value CURLY_END}".
     *
     * @see FormatParseAgenda
     */
    static final ContentParser CONTENT_AGENDA = new ContentParser(
        SpanLeafStyle.TEXT, CURLY_END);

    /** A {@link ContentParser} for reference field text
     *
     * This set leaf style as {@link SpanLeafStyle.FIELD} and adds a ender
     * "{@value CURLY_END}".
     *
     * @see FormatParsePointKey
     */
    static final ContentParser CONTENT_KEY = new ContentParser(
        SpanLeafStyle.FIELD, CURLY_END);

    /** A {@link ContentParser} for hyperlink text.
     *
     * This set leaf style as {@link SpanLeafStyle.TEXT} and adds a ender
     * "{@value LINK_END}".
     *
     * @see FormatParseLink
     */
    static final ContentParser CONTENT_LINK = new ContentParser(
        SpanLeafStyle.TEXT, LINK_END);

    /** A {@link ContentParser} for contntData text.
     *
     * This set leaf style as {@link SpanLeafStyle.Data}.
     *
     * @see InfoDataParser.TEXT
     */
    static final ContentParser CONTENT_DATA = new ContentParser(
        SpanLeafStyle.DATA);

    /** A {@link ContentParser} for link line text.
     *
     * This set leaf style as {@link SpanLeafStyle.PATH}.
     *
     * @see LinedParsePointer#HYPERLINK
     */
    static final ContentParser CONTENT_DIR_LINK  = new ContentParser(
        SpanLeafStyle.PATH);

    /** A {@link ContentParser} for direct link text.
     *
     * This set leaf style as {@link SpanLeafStyle.PATH} and adds a ender
     * "{@value LINK_TEXT}" and "{@value LINK_END}".
     *
     * @see LinedParsePointer#HYPERLINK
     */
    static final ContentParser CONTENT_LINE_LINK = new ContentParser(
        SpanLeafStyle.PATH, LINK_TEXT, LINK_END);

    /// For ContentParser for DirectoryParser see the class itself

    /// %Part 3.2: Formatted Span Parsers ======================================

    /** A {@link ContentParser} for note text.
     *
     * This set leaf style as {@link SpanLeafStyle.TEXT}, and allows notes.
     *
     * @see LinedParsePointer#FOOTNOTE
     * @see LinedParsePointer#ENDNOTE
     * @see LinedParseRest#QUOTE
     * @see LinedParseRest#PARAGRAPH
     */
    static final FormattedParser FORMATTED_TEXT = new FormattedParser(
        SpanLeafStyle.TEXT, true);

    /** A {@link ContentParser} for note text and other document matters.
     *
     * This set leaf style as {@link SpanLeafStyle.TEXT}, and not allows notes.
     *
     * @see LinedParseRest#NOTE
     * @see TextParser
     */
    static final FormattedParser NOTE_TEXT = new FormattedParser(
        SpanLeafStyle.TEXT, false);

    /** A {@link ContentParser} for citations.
     *
     * This set leaf style as {@link SpanLeafStyle.DATA}, and not allows notes.
     *
     * @see InfoDataParser#FORMATTED
     */
    static final FormattedParser FORMATTED_DATA = new FormattedParser(
        SpanLeafStyle.DATA, false);

    /** A {@link ContentParser} for heading text.
     *
     * This set leaf style as {@link SpanLeafStyle.PATH}, allows notes, and
     * adds a ender "{@value EDITION_BEGIN}".
     *
     * @see LinedParseLevel
     */
    static final FormattedParser FORMATTED_HEADER = new FormattedParser(
        SpanLeafStyle.TEXT, true, EDITION_BEGIN);

    /// %Part 3.3: Main Section Line Parsers ===================================

    /** Create the list of {@link SectionParser#SECTION_PARSERS}.
     *
     * @return answer
     */
    public static List<SetupParser> getSectionParsers(){
        ImmutableList.Builder<SetupParser> builder = ImmutableList.builder();
        builder.add(LinedParseLevel.BULLET, LinedParseLevel.NUMBERED);
        builder.add(LinedParsePointer.values());
        builder.add(LinedParseRest.getSectionList());
        return builder.build();
    }

    /// %Part 4: Statistical Data ##############################################

    public static final String STAT_KEY_DATA = ":";
    public static final String STAT_SEPARATOR = "|";
    public static final String STAT_ROW_END = "\n";
    public static final CharMatcher STAT_KEY_TEXT = CharMatcher.inRange('A', 'Z')
        .or(CharMatcher.inRange('a', 'z')).or(CharMatcher.is('-'))
        .or(CharMatcher.whitespace())
        .precomputed();

    public static final DateTimeFormatter STAT_DATE = DateTimeFormatter
        .ISO_LOCAL_DATE;

    public static final String STAT_PUBLISH_COUNT = "publish-count";
    public static final String STAT_NOTE_COUNT = "note-count";
    public static final String STAT_TIME_COUNT = "time-count";
    public static final String STAT_PUBLISH_GOAL = "publish-goal";
    public static final String STAT_TIME_GOAL = "time-goal";

    /// %Part 4: Meta and Matter Texts #########################################

    /// %Part 4.1: Common Constants ============================================

    public static final String TEXT_SEPARATOR = "|";
    public static final CharMatcher TEXT_KEY_TEXT = STAT_KEY_TEXT;

    /// %Part 4.2: Meta Text ===================================================

    private static final String META         = "meta-";
    /** PDF file property  for author:        {@value} */
    public static final String META_AUTHOR   = META   + "author";
    /** PDF file property  for keywords:      {@value} */
    /** PDF file property  for keywords:      {@value} */
    public static final String META_KEYWORDS = META   + "keywords";
    /** PDF file property  for subject:       {@value} */
    public static final String META_SUBJECT  = META   + "subject";
    /** PDF file property  for title:         {@value} */
    public static final String META_TITLE    = META   + "title";

    /// %Part 4.3: Matter Texts ================================================
    private static final String AREA         = "-";
    private static final String AREA_STARTER = AREA  + "starter";
    private static final String AREA_HEADER  = AREA  + "header";
    private static final String AREA_BREAK   = AREA  + "break";
    private static final String AREA_FOOTER  = AREA  + "footer";
    private static final String AREA_ENDER   = AREA  + "ender";

    /// %Part 4.3.1: Main Content Text -----------------------------------------

    private static final String MAIN        = "text";
    /** Text at the start of the main content. */
    public static final String MAIN_STARTER = MAIN + AREA_STARTER;
    /** Text for the top of each main content page. */
    public static final String MAIN_HEADER  = MAIN + AREA_HEADER;
    /** Text for section break in the main content */
    public static final String MAIN_BREAK   = MAIN + AREA_BREAK;
    /** Text for the bottom of each main content page. */
    public static final String MAIN_FOOTER  = MAIN + AREA_FOOTER;
    /** Text for the end of the main content. */
    public static final String MAIN_ENDER   = MAIN + AREA_ENDER;

    /// %Part 4.3.3: Endnote Page Text -----------------------------------------

    private static final String END         = "end";
    /** Text for start of the endnote content. */
    public static final String END_STARTER  = END + AREA_STARTER;
    /** Text for the top of each endnote content page. */
    public static final String END_HEADER   = END + AREA_HEADER;
    /** Text for the bottom of each endnote content page. */
    public static final String END_FOOTER   = END + AREA_FOOTER;
    /** Text for the end of the endnote content. */
    public static final String END_ENDER    = END + AREA_ENDER;

    /// %Part 4.3.4: Work Cited Text -------------------------------------------

    private static final String CITE        = "cite";
    /** Text for start of the work(s) cited content. */
    public static final String CITE_STARTER = CITE + AREA_STARTER;
    /** Text for the top of each work(s) cited content page. */
    public static final String CITE_HEADER  = CITE + AREA_HEADER;
    /** Text for the bottom of each work(s) cited content page. */
    public static final String CITE_FOOTER  = CITE + AREA_FOOTER;
    /** Text for the end of the work(s) cited content. */
    public static final String CITE_ENDER   = CITE + AREA_ENDER;

    /// %Part 4.3.5: Title Page Text -------------------------------------------

    private static final String TITLE         = "head-";
    /** At top    of a  title  page:          {@value} */
    public static final String TITLE_TOP     = TITLE  + "top";
    /** At centre of a  title  page:          {@value} */
    public static final String TITLE_CENTER  = TITLE  + "centre";
    /** At bottom of a  title  page:          {@value} */
    public static final String TITLE_BOTTOM  = TITLE  + "bottom";

    /// %Part 4.4 Data type and alignment ======================================

    /** Alignment right:                      {@value} */
    public static final String ALIGN_RIGHT  = "right";
    /** Alignment left:                       {@value} */
    public static final String ALIGN_LEFT   = "left";
    /** Alignment centre:                     {@value} */
    public static final String ALIGN_CENTER = "centre";
    /** Data type text:                       {@value} */
    public static final String ALIGN_TEXT   = "text";

    /// @Part.5: Private Constructor ###########################################

    private AuxiliaryData(){}
}
