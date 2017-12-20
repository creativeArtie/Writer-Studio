package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;
import static com.creativeartie.jwriter.main.Checker.*;

/**
 * Parser for {@link LinedSpanLevel} and it's subclasse {@link LinedSpanLevelSection}.
 */
enum LinedParseLevel implements SetupParser {
    HEADING, OUTLINE,
    /// Split into 2 to separate between praseSec(...) and parseBasic(...)
    NUMBERED, BULLET;

    private FormatParser SECTION_PARSR = new FormatParser(EDITION_BEGIN);
    private FormatParser TEXT_PARSER = new FormatParser();

    static SetupParser[] getSubList(){
        return new SetupParser[]{NUMBERED, BULLET};
    }

    @Override
    public Optional<SpanBranch> parse(SetupPointer pointer){
        checkNotNull(pointer, "pointer");
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
            SetupPointer pointer){
        assert children != null: "Null children.";
        assert pointer != null: "Null pointer.";
        DirectoryParser id = new DirectoryParser(DirectoryType.LINK,
            DIRECTORY_END, EDITION_BEGIN);
        if (pointer.trimStartsWith(children, DIRECTORY_BEGIN)){
            id.parse(children, pointer);
            pointer.startsWith(children, DIRECTORY_END);
        }

        SECTION_PARSR.parse(children, pointer);

        EditionParser.INSTANCE.parse(children, pointer);

        pointer.startsWith(children, LINED_END);

        LinedSpanLevelSection ans = new LinedSpanLevelSection(children, this);
        return Optional.of(ans);
    }

    private Optional<SpanBranch> parseBasic(ArrayList<Span> children,
            SetupPointer pointer){
        assert children != null: "Null children.";
        assert pointer != null: "Null pointer.";;
        TEXT_PARSER.parse(children, pointer);

        pointer.startsWith(children, LINED_END);
        return Optional.of(new LinedSpanLevelList(children, this));

    }
}
