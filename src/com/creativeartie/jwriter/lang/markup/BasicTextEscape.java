package com.creativeartie.jwriter.lang.markup;

import java.util.List;

import com.creativeartie.jwriter.lang.*;
import com.google.common.collect.*;


/**
 * Created from {@link ContentParser}. Used only in {@link ContentSpan}
 */
public class BasicTextEscape extends SpanBranch{

    BasicTextEscape(List<Span> children){
        super(children);
    }

    public String getEscape(){
        return size() == 2? get(1).getRaw(): "";
    }

    @Override
    public List<DetailStyle> getBranchStyles(){
        return ImmutableList.of(AuxiliaryType.ESCAPE);
    }
}
