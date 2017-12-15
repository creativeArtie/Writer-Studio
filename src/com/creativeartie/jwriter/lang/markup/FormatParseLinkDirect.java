package com.creativeartie.jwriter.lang.markup;

import java.util.ArrayList;
import java.util.Optional;



import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;
import static com.creativeartie.jwriter.main.Checker.*;

/**
 * SetupParser for {{@link FormatSpanLinkDirect}.
 */
final class FormatParseLinkDirect extends FormatParseLink {
    private static final ContentParser PATH_PARSER = new ContentParser(
        StyleInfoLeaf.PATH, LINK_TEXT, LINK_END);

    FormatParseLinkDirect(boolean[] formats){
        super(LINK_BEGIN, formats);
    }

    @Override
    Optional<SpanBranch> parseFinish(ArrayList<Span> children,
            SetupPointer pointer){
        checkNotNull(children, "children");
        checkNotNull(pointer, "pointer");

        /// Link path
        PATH_PARSER.parse(children, pointer);

        /// Complete the last steps
        parseRest(children, pointer);
        return Optional.of(new FormatSpanLinkDirect(children, this));
    }
}
