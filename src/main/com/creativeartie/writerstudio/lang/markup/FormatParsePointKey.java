package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.Checker.*;

/**
 * Parser for {@link FormatSpanCurlyAgenda}.
 */
class FormatParsePointKey extends FormatParsePoint {
    FormatParsePointKey(boolean[] formats){
        super(CURLY_KEY, formats);
    }

    @Override
    void parseContent(ArrayList<Span> children, SetupPointer pointer){
        CONTENT_KEY.parse(children, pointer);
    }

    @Override
    Optional<SpanBranch> parseFinish(ArrayList<Span> children,
            SetupPointer pointer){
        return Optional.of(new FormatSpanPointKey(children, this));
    }
}