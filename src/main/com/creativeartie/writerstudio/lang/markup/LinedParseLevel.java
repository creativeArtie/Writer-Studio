package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.Checker.*;

import com.google.common.collect.*;

/**
 * Parser for {@link LinedSpanLevel} and it's subclasse {@link LinedSpanLevelSection}.
 */
enum LinedParseLevel implements SetupParser {
    HEADING, OUTLINE,
    /// Split into 2 to separate between praseSec(...) and parseBasic(...)
    NUMBERED, BULLET;

    static SetupParser[] getSubList(){
        return new SetupParser[]{NUMBERED, BULLET};
    }

    @Override
    public Optional<SpanBranch> parse(SetupPointer pointer){
        checkNotNull(pointer, "pointer");
        ArrayList<Span> children = new ArrayList<>();
        boolean isFirst = true;
        for(String token: Lists.reverse(LEVEL_STARTERS.get(this))){
            if (pointer.startsWith(children, token)){
                return ordinal() <= OUTLINE.ordinal()?
                    parseSec(children, pointer):
                    parseBasic(children, pointer);
            }
        }
        return Optional.empty();
    }

    private Optional<SpanBranch> parseSec(ArrayList<Span> children,
            SetupPointer pointer){
        assert children != null: "Null children.";
        assert pointer != null: "Null pointer.";
        if (pointer.trimStartsWith(children, DIRECTORY_BEGIN)){
            DirectoryParser.ID_BOOKMARK.parse(pointer, children);
            pointer.startsWith(children, DIRECTORY_END);
        }

        FORMATTED_HEADER.parse(pointer, children);

        EditionParser.PARSER.parse(pointer, children);

        pointer.startsWith(children, LINED_END);

        LinedSpanLevelSection ans = new LinedSpanLevelSection(children);
        return Optional.of(ans);
    }

    private Optional<SpanBranch> parseBasic(ArrayList<Span> children,
            SetupPointer pointer){
        assert children != null: "Null children.";
        assert pointer != null: "Null pointer.";;
        FORMATTED_TEXT.parse(pointer, children);

        pointer.startsWith(children, LINED_END);
        return Optional.of(new LinedSpanLevelList(children));

    }
}
