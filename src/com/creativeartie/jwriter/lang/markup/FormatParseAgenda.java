package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;
import com.creativeartie.jwriter.main.*;

/**
 * SetupParser for {@link FormatSpanCurlyDirectory} and {@link FormatSpanCurlyAgenda} that uses 
 * curly bracket. These are footnote, endnote, cite, and to do.
 */
enum FormatParseAgenda implements SetupParser {
    PARSER;
    
    @Override
    public Optional<SpanBranch> parse(SetupPointer pointer){
        Checker.checkNotNull(pointer, "pointer");
        ArrayList<Span> children = new ArrayList<>();
        if(pointer.startsWith(children, CURLY_AGENDA)){
            new ContentParser(CURLY_END).parse(children, pointer);
            
            /// Complete the last steps
            pointer.startsWith(children, CURLY_END);
            return Optional.of(new FormatSpanAgenda(children));
        }
        return Optional.empty();
    }
}
