package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.Checker.*;

/**
 * Parser for all other {@link LinedSpan}.
 */
enum LinedParseRest implements SetupParser {
    NOTE(pointer -> {
        assert pointer != null: "Null pointer.";
        ArrayList<Span> children = new ArrayList<>();
        if (pointer.startsWith(children, LINED_NOTE)){
            Optional<DirectorySpan> id = Optional.empty();
            if (pointer.trimStartsWith(children, DIRECTORY_BEGIN)){
                DirectoryParser.ID_NOTE.parse(pointer, children);
                pointer.startsWith(children, DIRECTORY_END);
            }
            NOTE_TEXT.parse(pointer, children);
            pointer.startsWith(children, LINED_END);
            LinedSpanNote ans = new LinedSpanNote(children);
            return Optional.of(ans);
        }
        return Optional.empty();
    }),
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
    BREAK(pointer ->{
        assert pointer != null: "Null pointer.";
        ArrayList<Span> children = new ArrayList<>();
        if (pointer.startsWith(children, LINED_BREAK)){
            return Optional.of(new LinedSpanBreak(children));
        }
        return Optional.empty();
    }),
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

    static SetupParser[] getSectionList(){
        return Arrays.copyOfRange(values(), 1, values().length);
    }

    static SetupParser[] getNoteList(){
        return new LinedParseRest[]{NOTE};
    }

    private final SetupParser parser;

    private LinedParseRest(SetupParser lineParser){
        parser = lineParser;
    }

    @Override
    public Optional<SpanBranch> parse(SetupPointer pointer){
        checkNotNull(pointer, "pointer");
        return parser.parse(pointer);
    }
}
