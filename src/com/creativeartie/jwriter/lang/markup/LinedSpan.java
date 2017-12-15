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

    private List<StyleInfo> cacheStyles;

    LinedSpan(List<Span> children){
        super(children);
        cacheStyles = ImmutableList.of(getLinedType());
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

    public boolean isLast(){
        Span child = this;
        SpanNode<?> parent = child.getParent();
        while (parent.get(size() - 1) == child){
            if (parent instanceof Document) {
                /// it is the last of the doucment
                return true;
            } else {
                /// still have parents
                child = parent;
                parent = child.getParent();
            }
        }

        /// it is in the middle of the children list
        return false;
    }
}
