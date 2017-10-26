package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;
import com.creativeartie.jwriter.main.Checker;

enum LinedParseLevel implements SetupParser {
    HEADING, OUTLINE,
    /// Split into 2 to separate between praseSec(...) and parseBasic(...)
    NUMBERED, BULLET;

    static SetupParser[] getSubList(){
        return new SetupParser[]{NUMBERED, BULLET};
    }

    @Override
    public Optional<SpanBranch> parse(SetupPointer pointer){
        Checker.checkNotNull(pointer, "pointer");
        ArrayList<Span> children = new ArrayList<>();
        boolean isFirst = true;
        for(int i = LEVEL_MAX; i >= 1; i--){
            if (pointer.startsWith(children, getLevelToken(this, i))){
                return ordinal() <= OUTLINE.ordinal()?
                    parseSec(children, pointer):
                    parseBasic(children, pointer);
            }
        }
        return Optional.empty();
    }

    private Optional<SpanBranch> parseSec(ArrayList<Span> children,
        SetupPointer pointer
    ){
        Checker.checkNotNull(pointer, "pointer");
        Checker.checkNotNull(children, "children");
        DirectoryParser id = new DirectoryParser(DirectoryType.LINK,
            DIRECTORY_END, EDITION_BEGIN);
        if (pointer.trimStartsWith(children, DIRECTORY_BEGIN)){
            id.parse(children, pointer);
            pointer.startsWith(children, DIRECTORY_END);
        }

        new FormatParser(EDITION_BEGIN).parse(children, pointer);

        EditionParser.parseAll(children, pointer);

        pointer.startsWith(children, LINED_END);

        LinedSpanSection ans = new LinedSpanSection(children);
        return Optional.of(ans);
    }

    private Optional<SpanBranch> parseBasic(
        ArrayList<Span> children, SetupPointer pointer
    ){
        Checker.checkNotNull(pointer, "pointer");
        Checker.checkNotNull(children, "children");
        new FormatParser().parse(children, pointer);

        pointer.startsWith(children, LINED_END);
        return Optional.of(new LinedSpanLevel(children));

    }
}
