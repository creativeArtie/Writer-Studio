package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.Checker.*;

/**
 * Parser for {@link FormatSpanPointId}.
 */
final class FormatParsePointId extends FormatParsePoint {

    private final DirectoryType spanType;
    private final DirectoryParser idParser;

    public static FormatParsePointId[] getParsers(boolean[] formats){
        checkNotNull(formats, "formats");
        checkEqual(formats.length, "formats.length", FORMAT_TYPES);
        boolean[] setup = Arrays.copyOf(formats, formats.length);
        return new FormatParsePointId[]{
            new FormatParsePointId(DirectoryType.FOOTNOTE, CURLY_FOOTNOTE, setup),
            new FormatParsePointId(DirectoryType.ENDNOTE, CURLY_ENDNOTE, setup),
            new FormatParsePointId(DirectoryType.NOTE, CURLY_CITE, setup)
        };
    }

    private FormatParsePointId(DirectoryType type, String start,
            boolean[] formats){
        super(start, formats);
        assert type != null: "Null type.";
        spanType = type;
        idParser = DirectoryParser.getRefParser(type);
    }

    DirectoryType getDirectoryType(){
        return spanType;
    }

    @Override
    void parseContent(ArrayList<Span> children, SetupPointer pointer){
        idParser.parse(children, pointer);
    }

    @Override
    Optional<SpanBranch> parseFinish(ArrayList<Span> children,
            SetupPointer pointer){
        return Optional.of(new FormatSpanPointId(children, this));
    }

}