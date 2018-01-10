package com.creativeartie.jwriter.lang.markup;

import java.util.*;
import com.google.common.collect.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;
import static com.creativeartie.jwriter.main.Checker.*;

/**
 * Base class for all {@link LinedSpan}.
 */
public abstract class LinedSpan extends SpanBranch {

    LinedSpan(List<Span> children){
        super(children);
    }

    private LinedType linedType;

    public LinedType getLinedType(){
        if (linedType == null){
            linedType = LinedType.findType(get(0).getRaw());
        }
        return linedType;
    }

    @Override
    public String toString(){
        return getLinedType() + super.toString();
    }

    @Override
    public List<StyleInfo> getBranchStyles(){
        return ImmutableList.of(getLinedType());
    }

    public int getPublishTotal(){
        return 0;
    }

    public int getNoteTotal(){
        return 0;
    }
}
