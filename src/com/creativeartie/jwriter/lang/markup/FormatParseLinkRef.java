package com.creativeartie.jwriter.lang.markup;

import java.util.ArrayList;
import java.util.Optional;


import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;
import com.creativeartie.jwriter.main.Checker;

/**
 * SetupParser for {@link FormatSpanLink} that uses angler bracket. It therefore is a 
 * SetupParser for {@link FormatSpanLinkDirect} and {@link FormatRefSpan}.
 */
class FormatParseLinkRef extends FormatParseLink {
    
    FormatParseLinkRef(boolean[] formats){
        super(LINK_REF, formats);
    }

    @Override
    public Optional<SpanBranch> parseFinish(ArrayList<Span> children, 
        SetupPointer pointer
    ){
        Checker.checkNotNull(children, "children");
        Checker.checkNotNull(pointer, "pointer");
        
        DirectoryParser id = new DirectoryParser(DirectoryType.LINK, 
            LINK_TEXT, LINK_END);
        id.parse(children, pointer);
        
        /// Complete the last steps 
        parseRest(children, pointer);
        FormatSpanLinkRef span = new FormatSpanLinkRef(children, getFormats());
        return Optional.of(span);
    }
}
