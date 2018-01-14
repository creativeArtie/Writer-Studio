package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;
import static com.creativeartie.jwriter.main.Checker.*;

/**
 * Parser for {@link FormatSpanDirectory}.
 */
final class FormatParseDirectory implements SetupParser {

    private final String spanStart;
    private final DirectoryType spanType;
    private final boolean[] formatList;
    private final DirectoryParser idParser;

    public static FormatParseDirectory[] getParsers(boolean[] formats){
        checkNotNull(formats, "formats");
        checkEqual(formats.length, "formats.length", FORMAT_TYPES);
        boolean[] setup = Arrays.copyOf(formats, formats.length);
        return new FormatParseDirectory[]{
            new FormatParseDirectory(DirectoryType.FOOTNOTE, setup),
            new FormatParseDirectory(DirectoryType.ENDNOTE, setup),
            new FormatParseDirectory(DirectoryType.NOTE, setup)
        };
    }

    private FormatParseDirectory(DirectoryType type, boolean[] formats){
        assert type != null: "Null type.";
        assert formats != null && formats.length == FORMAT_TYPES:
            "Coruptted formats.";
        spanType = type;
        switch(type){
            case FOOTNOTE:
                spanStart = CURLY_FOOTNOTE;
                break;
            case ENDNOTE:
                spanStart = CURLY_ENDNOTE;
                break;
            case NOTE:
                spanStart = CURLY_CITE;
                break;
            default:
                assert false: "Incorrect DirectoryType.";
                spanStart = null;
        }
        formatList = formats;
        idParser = DirectoryParser.getRefParser(type);
    }

    DirectoryType getDirectoryType(){
        return spanType;
    }

    boolean[] getFormats(){
        return formatList;
    }

    @Override
    public Optional<SpanBranch> parse(SetupPointer pointer){
        checkNotNull(pointer, "pointer");
        ArrayList<Span> children = new ArrayList<>();
        if(pointer.startsWith(children, spanStart)){
            /// CatalogueIdentity for the other Parsers
            idParser.parse(children, pointer);

            /// Complete the last steps
            pointer.startsWith(children, CURLY_END);

            FormatSpanDirectory span = new FormatSpanDirectory(children, this);

            return Optional.of(span);
        }
        return Optional.empty();
    }

    boolean canParse(String text){
        checkNotNull(text, "text");
        return text.startsWith(spanStart) && AuxiliaryChecker.willEndWith(text,
            CURLY_END);
    }
}
