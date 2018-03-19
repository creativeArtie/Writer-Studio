package com.creativeartie.writerstudio.lang.markup;

import java.util.ArrayList;
import java.util.Optional;


import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.Checker.*;

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

        DirectoryParser.REF_LINK.parse(children, pointer);

        /// Complete the last steps
        parseRest(children, pointer);
        FormatSpanLinkRef span = new FormatSpanLinkRef(children, this);
        return Optional.of(span);
    }
}
