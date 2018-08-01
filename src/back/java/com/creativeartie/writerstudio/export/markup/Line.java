package com.creativeartie.writerstudio.export.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.export.*;

abstract class Line<T extends LinedSpan> implements BridgeDivision{

    private T targetSpan;
    private PageInfo pageInfo;

    Line(T target, PageInfo info){
        targetSpan = target;
        pageInfo = info;
    }

    T getTargetSpan(){
        return targetSpan;
    }

    abstract Optional<FormattedSpan> getText();

    public Iterator<BridgeContent> iterator(){
        return null;
    }

}
