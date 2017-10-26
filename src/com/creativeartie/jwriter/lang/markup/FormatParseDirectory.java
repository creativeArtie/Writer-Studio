package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;
import com.creativeartie.jwriter.main.*;

/**
 * SetupParser for {@link FormatSpanCurlyDirectory} and {@link FormatSpanCurlyAgenda} that uses 
 * curly bracket. These are footnote, endnote, cite, and to do.
 */
class FormatParseDirectory implements SetupParser {
    
    private final String spanStart;
    private final DirectoryType spanType;
    private final boolean[] formatList;
    
    public static FormatParseDirectory[] getParsers(boolean[] formats){
        Checker.checkArraySize(formats, "spanFormats", FORMAT_TYPES);
        boolean[] setup = Arrays.copyOf(formats, formats.length);
        return new FormatParseDirectory[]{
            new FormatParseDirectory(DirectoryType.FOOTNOTE, setup),
            new FormatParseDirectory(DirectoryType.ENDNOTE, setup),
            new FormatParseDirectory(DirectoryType.NOTE, setup)
        };
    }
    
    private FormatParseDirectory(DirectoryType type, boolean[] formats){
        Checker.checkNotNull(type, "type");
        Checker.checkArraySize(formats, "formats", 4);
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
                throw new IllegalArgumentException("DirectoryType not allowed.");
        }
        formatList = formats;
    }
    
    @Override
    public Optional<SpanBranch> parse(SetupPointer pointer){
        Checker.checkNotNull(pointer, "pointer");
        ArrayList<Span> children = new ArrayList<>();
        if(pointer.startsWith(children, spanStart)){
            /// CatalogueIdentity for the other Parsers
            DirectoryParser id = new DirectoryParser(spanType, CURLY_END);
            id.parse(children, pointer);
            
            /// Complete the last steps
            pointer.startsWith(children, CURLY_END);
            
            FormatSpanDirectory span = new FormatSpanDirectory(children, 
                formatList, spanType);
            
            return Optional.of(span);
        }
        return Optional.empty();
    }
}
