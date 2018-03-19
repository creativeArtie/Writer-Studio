package com.creativeartie.writerstudio.lang.markup;

import java.util.ArrayList;
import java.util.Optional;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.Checker.*;

/**
 * SetupParser for {{@link FormatSpanLinkDirect}.
 */
final class FormatParseLinkDirect extends FormatParseLink {

    FormatParseLinkDirect(boolean[] formats){
        super(LINK_BEGIN, formats);
    }

    @Override
    Optional<SpanBranch> parseFinish(ArrayList<Span> children,
            SetupPointer pointer){
        checkNotNull(children, "children");
        checkNotNull(pointer, "pointer");

        /// Link path
        CONTENT_LINE_LINK.parse(children, pointer);

        /// Complete the last steps
        parseRest(children, pointer);
        return Optional.of(new FormatSpanLinkDirect(children, this));
    }
}
