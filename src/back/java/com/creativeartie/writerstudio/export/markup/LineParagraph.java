package com.creativeartie.writerstudio.export.markup;

import java.util.*;
import java.util.function.*;

import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.export.*;

class LineParagraph extends Line<LinedSpanParagraph>{

    LineParagraph(LinedSpanParagraph span, PageInfo info){
        super(span, info);
    }

    public Optional<FormattedSpan> getText(){
        return getTargetSpan().getFormattedSpan();
    }

    public DataLineType getLineType(){
        return DataLineType.LEFT;
    }
}
