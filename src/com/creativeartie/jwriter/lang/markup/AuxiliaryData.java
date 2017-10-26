package com.creativeartie.jwriter.lang.markup;

import java.util.List;
import java.util.Arrays;

import com.creativeartie.jwriter.main.Checker;

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
    /** Creates a Leveled Line begin token.
     * @param parser
     *      use to specific which token. This should be all of them.
     * @param level
     *      a number between 1 and {@link LEVEL_MAX}
     * @return
     *      the starter token created
     */
    public static String getLevelToken(LinedParseLevel parser, int level){
        Checker.checkNotNull(parser, "useParser");
        Checker.checkInt(level, "forLevel", 0, false, LEVEL_MAX + 1, true);
        switch (parser){
            case HEADING:
                /// ==
                return repeat(LEVEL_HEADING, level);
            case OUTLINE:
                /// !##
                return LINED_BEGIN + repeat(LEVEL_OUTLINE, level);
            case NUMBERED:
                /// \t#
                return repeat(LEVEL_BEGIN, level - 1) + LEVEL_NUMBERED;
            case BULLET:
                /// \t-
                return repeat(LEVEL_BEGIN, level - 1) + LEVEL_BULLET;
            default:
                throw Checker.typeNotUse(parser, "useParser");
        }
    }
    /// getLevelToken helper
    private static String repeat(String repeat, int level){
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < level; i++){
            builder.append(repeat);
        }
        return builder.toString();
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
    /// For FormatParseAgenda, FormatParseDirectory, listFormatEnderTokens()

    /// Curly format begins token  part 1
    private static final String CURLY_BEGIN   = "{";
    /// Curly format begins tokens part 2
    public static final String CURLY_AGENDA   = CURLY_BEGIN + "!"; /// aka: {!
    public static final String CURLY_FOOTNOTE = CURLY_BEGIN + "^"; /// aka: {^
    public static final String CURLY_ENDNOTE  = CURLY_BEGIN + "*"; /// aka: {*
    public static final String CURLY_CITE     = CURLY_BEGIN + DIRECTORY_BEGIN;
    ///                                                                aka: {@

    /** Curly format end token. **/
    public static final String CURLY_END      = "}";

    /// @Part-1-7: Format Modifiers --------------------------------------------
    /// For FormatParser, listFormatEnderTokens()

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
        // FORMAT_BOLD must before FORMAT_ITALICS
        return new String[]{
            FORMAT_BOLD, FORMAT_ITALICS, FORMAT_UNDERLINE, FORMAT_CODED
        };
    }

    /// @Part-1-8: Escape Begin Token ------------------------------------------
    /// For BasicParseText

    /** Char escape token. */
    public static final String CHAR_ESCAPE = "\\";

    /// @Part-1-9: Format Part Separators --------------------------------------
    /// For FormatParsers

    /** Create the list of how children spans of format can end.
     * @return the list of term, without {@linkplain FORMAT_BOLD}
     */
    public static String[] listFormatEnderTokens(){
        return new String[]{
            CURLY_AGENDA, CURLY_FOOTNOTE, CURLY_ENDNOTE, CURLY_CITE,
            LINK_BEGIN, LINK_REF,
            /* FORMAT_BOLD, */ FORMAT_ITALICS, FORMAT_UNDERLINE, FORMAT_CODED};
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
    public static final List<String> TYPE_SECTION = Arrays.asList("head");

    public static final String TYPE_AGENDA = "agenda";
    public static final List<String> TYPE_AGENDA_INLINE = Arrays.asList(
        TYPE_AGENDA);
    public static final List<String> TYPE_AGENDA_LINED = Arrays.asList(
        TYPE_AGENDA);


    /// @Part-2-2: Possible style catagory name. -------------------------------
    /// For AuxilliaryType, DirectoryType, EditionType, FormatType,
    ///     InfoFieldType, InfoDataType, LinedType

    public static final String STYLE_OTHER    = "OTHER"  ; /// AuxilliaryType
    public static final String STYLE_EDITION  = "EDITION"; /// EditionType
    public static final String STYLE_FORMAT   = "FORMAT" ; /// FormatType
    public static final String STYLE_FIELD    = "FIELD"  ; /// InfoFieldType
    public static final String STYLE_INLINE   = "INLINE" ; /// AuxilliaryType
    public static final String STYLE_CATEGORY = "INLINE" ; /// DirectoryType
    public static final String STYLE_LINED    = "LINED"  ; /// LinedType
    public static final String STYLE_MAIN     = "MAIN"   ; /// AuxilliaryType
    public static final String STYLE_DATA     = "DATA"   ; /// InfoDataType

    /// ========================================================================
    /// @Part-3: Private Constructor -------------------------------------------
    private AuxiliaryData(){}
}
