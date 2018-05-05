package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** Implements rules not in {@link LinedParseLevel} or {@link LinedParsePointer}.
 *
 * This includes rules (in {@code design/ebnf.txt})
 * <ul>
 * <li>{@code LinedCite} </li>
 * <li>{@code NoteLine} </li>
 * <li>{@code LinedAgenda} </li>
 * <li>{@code LinedBreak} </li>
 * <li>{@code LinedQuote} </li>
 * <li>{@code LinedParagraph} </li>
 * </ul>
 *
 * The value order is set by:
 * <ul>
 * <li>{@link #getSectionList()}</li>
 * </ul>
 */
enum LinedParseRest implements SetupParser {
    /** Implements {@code design/ebnf.txt LinedCite}. */
    CITE(pointer -> {
        assert pointer != null: "Null pointer";
        ArrayList<Span> children = new ArrayList<>();

        if (pointer.startsWith(children, LINED_CITE)){
            /// Create field span
            Optional<InfoDataParser> used = Optional.empty();
            for (InfoFieldParser parser: InfoFieldParser.values()){
                if (parser.parse(pointer, children)){
                    used = parser.getDataParser();
                    break;
                }
            }

            pointer.trimStartsWith(children, LINED_DATA);

            /// Create the data span
            if (! (used.isPresent() && used.get().parse(pointer, children))){
                pointer.getTo(children, StyleInfoLeaf.TEXT, LINED_END);
            }

            /// Create non dat text
            pointer.startsWith(children, LINED_END);

            return Optional.of(new LinedSpanCite(children));
        }
        return Optional.empty();
    }),
    /** Implements {@code design/ebnf.txt LinedNote}. */
    NOTE(pointer -> {
        assert pointer != null: "Null pointer.";
        ArrayList<Span> children = new ArrayList<>();

        if (pointer.startsWith(children, LINED_NOTE)){
            /// Create note id
            Optional<DirectorySpan> id = Optional.empty();
            if (pointer.trimStartsWith(children, DIRECTORY_BEGIN)){
                DirectoryParser.ID_NOTE.parse(pointer, children);
                pointer.startsWith(children, DIRECTORY_END);
            }

            /// Add note text
            NOTE_TEXT.parse(pointer, children);

            pointer.startsWith(children, LINED_END);

            return Optional.of(new LinedSpanNote(children));
        }
        return Optional.empty();
    }),
    /** Implements {@code design/ebnf.txt LinedAgenda}. */
    AGENDA(pointer ->{
        assert pointer != null: "Null pointer.";
        ArrayList<Span> children = new ArrayList<>();

        if (pointer.startsWith(children, LINED_AGENDA)){
            CONTENT_BASIC.parse(pointer, children);
            pointer.startsWith(children, LINED_END);
            return Optional.of(new LinedSpanAgenda(children));
        }

        return Optional.empty();
    }),
    /** Implements {@code design/ebnf.txt LinedQuote}. */
    QUOTE(pointer ->{
        assert pointer != null: "Null pointer.";
        ArrayList<Span> children = new ArrayList<>();

        if (pointer.startsWith(children, LINED_QUOTE)){
            FORMATTED_TEXT.parse(pointer, children);
            pointer.startsWith(children, LINED_END);
            return Optional.of(new LinedSpanQuote(children));
        }

        return Optional.empty();
    }),
    /** Implements {@code design/ebnf.txt LinedBreak}. */
    BREAK(pointer ->{
        assert pointer != null: "Null pointer.";
        ArrayList<Span> children = new ArrayList<>();

        if (pointer.startsWith(children, LINED_BREAK)){
            return Optional.of(new LinedSpanBreak(children));
        }

        return Optional.empty();
    }),
    /** Implements {@code design/ebnf.txt LinedParagraph}.
     *
     * This must be the list value in the enum.
     */
    PARAGRAPH(pointer ->{
        assert pointer != null: "Null pointer.";

        if (pointer.hasNext()){
            ArrayList<Span> children = new ArrayList<>();
            FORMATTED_TEXT.parse(pointer, children);
            pointer.startsWith(children, LINED_END);
            return Optional.of(new LinedSpanParagraph(children));
        }

        return Optional.empty();
    });

    /** Gets the list of section line parsers.
     *
     * @return answer
     */
    static SetupParser[] getSectionList(){
        return Arrays.copyOfRange(values(), 1, values().length);
    }

    private final SetupParser llambaParser;

    /** Create a {@linkplain LinedParseRest}.
     *
     * @param parser
     *      llamba parser
     */
    private LinedParseRest(SetupParser parser){
        llambaParser = parser;
    }

    @Override
    public Optional<SpanBranch> parse(SetupPointer pointer){
        argumentNotNull(pointer, "pointer");
        return llambaParser.parse(pointer);
    }
}
