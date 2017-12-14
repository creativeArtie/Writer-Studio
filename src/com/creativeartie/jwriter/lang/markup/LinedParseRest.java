package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;
import com.creativeartie.jwriter.main.Checker;

/**
 * Parser for all other {@link LinedSpan}.
 */
enum LinedParseRest implements SetupParser {
    NOTE(pointer -> {
        Checker.checkNotNull(pointer, "pointer");
        ArrayList<Span> children = new ArrayList<>();
        if (pointer.startsWith(children, LINED_NOTE)){
            Optional<DirectorySpan> id = Optional.empty();
            if (pointer.trimStartsWith(children, DIRECTORY_BEGIN)){
                new DirectoryParser(DirectoryType.NOTE, DIRECTORY_END)
                    .parse(children, pointer);
                pointer.startsWith(children, DIRECTORY_END);
            }
            new FormatParser().parse(children, pointer);
            pointer.startsWith(children, LINED_END);
            LinedSpanNote ans = new LinedSpanNote(children);
            return Optional.of(ans);
        }
        return Optional.empty();
    }),
    AGENDA(pointer ->{
        Checker.checkNotNull(pointer, "pointer");
        ArrayList<Span> children = new ArrayList<>();
        if (pointer.startsWith(children, LINED_AGENDA)){
            new ContentParser().parse(children, pointer);
            pointer.startsWith(children, LINED_END);
            return Optional.of(new LinedSpanAgenda(children));
        }
        return Optional.empty();
    }),
    QUOTE(pointer ->{
        Checker.checkNotNull(pointer, "pointer");
        ArrayList<Span> children = new ArrayList<>();
        if (pointer.startsWith(children, LINED_QUOTE)){
            new FormatParser(false).parse(children, pointer);
            pointer.startsWith(children, LINED_END);
            return Optional.of(new LinedSpanQuote(children));
        }
        return Optional.empty();
    }),
    BREAK(pointer ->{
        Checker.checkNotNull(pointer, "pointer");
        ArrayList<Span> children = new ArrayList<>();
        if (pointer.startsWith(children, LINED_BREAK)){
            return Optional.of(new LinedSpanBreak(children));
        }
        return Optional.empty();
    }),
    PARAGRAPH(pointer ->{
        Checker.checkNotNull(pointer, "pointer");
        if (pointer.hasNext()){
            ArrayList<Span> children = new ArrayList<>();
            new FormatParser(false).parse(children, pointer);
            pointer.startsWith(children, LINED_END);
            return Optional.of(new LinedSpanParagraph(children));
        }
        return Optional.empty();
    });

    static SetupParser[] getSectionList(){
        return new LinedParseRest[]{AGENDA, BREAK, PARAGRAPH};
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
        return parser.parse(pointer);
    }
}
