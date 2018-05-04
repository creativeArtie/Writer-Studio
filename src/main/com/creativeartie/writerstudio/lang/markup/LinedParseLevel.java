package com.creativeartie.writerstudio.lang.markup;

import java.util.*;
import java.util.function.*;

import com.creativeartie.writerstudio.lang.*;

import com.google.common.collect.*;

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** Implements rules prefixed with {@code design/ebnf.txt LevelLined}.
 *
 * The value order is set by:
 * <ul>
 * <li>{@link #parse(SetupParser)}</li>
 * </ul>
 */
enum LinedParseLevel implements SetupParser {
    /** A section header that that will render on publishing pdf file. */
    HEADING(LinedParseLevel::buildHeading),
    /** A section header that that will not render on publishing pdf file. */
    OUTLINE(LinedParseLevel::buildHeading),
    /// Split into 2 to separate between praseSec(...) and parseBasic(...)
    /** A numbered list line. */
    NUMBERED(LinedParseLevel::buildBasic),
    /** A bullet list line. */
    BULLET(LinedParseLevel::buildBasic);

    private final BiFunction<SetupPointer, ArrayList<Span>, SpanBranch>
        spanParser;

    /** Creates a {@linkplain LinedParseLevel}.
     *
     * @param parser
     *      found span parser
     */
    private LinedParseLevel(
            BiFunction<SetupPointer, ArrayList<Span>, SpanBranch> parser){
        spanParser = parser;
    }

    @Override
    public Optional<SpanBranch> parse(SetupPointer pointer){
        argumentNotNull(pointer, "pointer");
        ArrayList<Span> children = new ArrayList<>();

        boolean isFirst = true;
        for(String token: Lists.reverse(LEVEL_STARTERS.get(this))){
            /// reverse is needed b/c !# will match all outline headings
            if (pointer.startsWith(children, token)){
                /// Finds a match
                return Optional.of(spanParser.apply(pointer, children));
            }
        }
        return Optional.empty();
    }

    /** Parse a section heading
     *
     * @param pointer
     *      setup pointer
     * @param children
     *      span children
     */
    private static SpanBranch buildHeading(SetupPointer pointer,
            ArrayList<Span> children){
        assert children != null: "Null children.";
        assert pointer != null: "Null pointer.";

        /// Parse id
        if (pointer.trimStartsWith(children, DIRECTORY_BEGIN)){
            DirectoryParser.ID_BOOKMARK.parse(pointer, children);
            pointer.startsWith(children, DIRECTORY_END);
        }

        FORMATTED_HEADER.parse(pointer, children);

        EditionParser.PARSER.parse(pointer, children);

        pointer.startsWith(children, LINED_END);

        return new LinedSpanLevelSection(children);
    }

    /** Parse a list line.
     *
     * @param pointer
     *      setup pointer
     * @param children
     *      span children
     */
    private static SpanBranch buildBasic(SetupPointer pointer,
            ArrayList<Span> children){
        assert children != null: "Null children.";
        assert pointer != null: "Null pointer.";

        FORMATTED_TEXT.parse(pointer, children);

        pointer.startsWith(children, LINED_END);
        return new LinedSpanLevelList(children);

    }
}
