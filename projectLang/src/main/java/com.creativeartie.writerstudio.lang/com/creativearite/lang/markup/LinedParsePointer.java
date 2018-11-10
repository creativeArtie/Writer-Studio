package com.creativeartie.writerstudio.lang.markup;

import java.util.*;
import java.util.function.*;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.util.ParameterChecker.*;

/** Implements {@code design/ebnf.txt LinedFootnote}, {@code LinedEndnote}, and
 * {@code LinedLink}.
 */
enum LinedParsePointer implements SetupParser {
    FOOTNOTE(LINED_FOOTNOTE, FORMATTED_TEXT, c -> new LinedSpanPointNote(c)),
    ENDNOTE(LINED_ENDNOTE, FORMATTED_TEXT, c -> new LinedSpanPointNote(c)),
    LINK(LINED_LINK, CONTENT_DIR_LINK, c -> new LinedSpanPointLink(c));

    private final String spanStart;
    private final SetupParser dataParser;
    private final Function<ArrayList<Span>, SpanBranch> spanBuilder;

    /** Creates a {@linkplain LinedParsePointer}.
     *
     * @param start
     *      start token
     * @param parser
     *      data span parser
     * @param builder
     *      span builder
     */
    private LinedParsePointer(String start, SetupParser parser,
            Function<ArrayList<Span>, SpanBranch> builder){
        spanStart = start;
        dataParser = parser;
        spanBuilder = builder;
    }

    @Override
    public Optional<SpanBranch> parse(SetupPointer pointer){
        argumentNotNull(pointer, "pointer");
        ArrayList<Span> children = new ArrayList<>();
        if (pointer.startsWith(children, spanStart)){

            /// Create directory type
            DirectoryType idType = DirectoryType.values()[ordinal() + 2];
            DirectoryParser.getIDParser(idType).parse(pointer, children);

            /// Create data span
            if (pointer.startsWith(children, LINED_DATA)){
                dataParser.parse(pointer, children);
            }

            pointer.startsWith(children, LINED_END);

            return Optional.of(spanBuilder.apply(children));
        }
        return Optional.empty();
    }
}
