package com.creativeartie.jwriter.lang.markup;

import java.util.ArrayList;
import java.util.Optional;



import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;
import com.creativeartie.jwriter.main.Checker;

/**
 * SetupParser for {{@link FormatSpanLinkDirect}.
 */
class FormatParseLinkDirect extends FormatParseLink {

    FormatParseLinkDirect(boolean[] formats){
        super(LINK_BEGIN, formats);
    }

    @Override
    public Optional<SpanBranch> parseFinish(ArrayList<Span> children,
        SetupPointer pointer
    ){
        Checker.checkNotNull(children, "children");
        Checker.checkNotNull(pointer, "pointer");

        /// Link path
        new ContentParser(StyleInfoLeaf.PATH, LINK_TEXT, LINK_END)
            .parse(children, pointer);

        /// Complete the last steps
        parseRest(children, pointer);
        return Optional.of(new FormatSpanLinkDirect(children, getFormats()));
    }
}
