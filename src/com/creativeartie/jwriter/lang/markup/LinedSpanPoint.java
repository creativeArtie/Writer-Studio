package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.google.common.collect.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;

/**
 * Base class of {@code LinedSpanPoint*} classes.
 */
public abstract class LinedSpanPoint extends LinedSpan implements Catalogued{

    LinedSpanPoint(List<Span> children){
        super(children);
    }

    public abstract DirectoryType getDirectoryType();

    @Override
    public List<StyleInfo> getBranchStyles(){
        ImmutableList.Builder<StyleInfo> builder = ImmutableList.builder();
        return builder.addAll(super.getBranchStyles()).add(getIdStatus()).build();
    }

    @Override
    public Optional<CatalogueIdentity> getSpanIdentity(){
        return spanFromFirst(DirectorySpan.class).map(span -> span.buildId());
    }

    @Override
    public boolean isId(){
        return true;
    }
}
