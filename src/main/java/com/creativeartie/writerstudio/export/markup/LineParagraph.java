package com.creativeartie.writerstudio.export.markup;

import java.util.*;
import java.util.function.*;

import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.export.*;

class LineParagraph implements BridgeDivision{
    private LinedSpanParagraph targetSpan;

    public Iterable<BridgeContent> getContent(){
        return null;
    }

    public DataLineType getLineType(){
        return null;
    }
}
