package com.creativeartie.jwriter.lang.markup;

import java.util.ArrayList;
import java.util.Optional;


import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;
import static com.creativeartie.jwriter.main.Checker.*;

/**
 * SetupParser for {{@link FormatRefSpan}.
 */
final class FormatParseLinkRef extends FormatParseLink {

    FormatParseLinkRef(boolean[] formats){
        super(LINK_REF, formats);
    }

    @Override
    Optional<SpanBranch> parseFinish(ArrayList<Span> children,
            SetupPointer pointer){
        checkNotNull(children, "children");
        checkNotNull(pointer, "pointer");

        DirectoryParser id = new DirectoryParser(DirectoryType.LINK,
            LINK_TEXT, LINK_END);
        id.parse(children, pointer);

        /// Complete the last steps
        parseRest(children, pointer);
        FormatSpanLinkRef span = new FormatSpanLinkRef(children, this);
        return Optional.of(span);
    }
}
