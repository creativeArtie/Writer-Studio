package com.creativeartie.writerstudio.lang.markup;

import java.util.*; // ArrayList, Optional

import com.creativeartie.writerstudio.lang.*; // Span, SpanBranch

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
    void parseContent(SetupPointer pointer, ArrayList<Span> children){
        CONTENT_KEY.parse(pointer, children);
    }

    @Override
    SpanBranch parseFinish(SetupPointer pointer, ArrayList<Span> children){
        return new FormatSpanPointKey(children, this);
    }
}