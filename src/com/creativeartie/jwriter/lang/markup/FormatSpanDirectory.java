package com.creativeartie.jwriter.lang.markup;

import com.google.common.collect.*;

import java.util.*;
import com.creativeartie.jwriter.lang.*;

/**
 * A {@linkplain FormatSpanCurly} for id reference. This has a span where it is
 * reference to
 */
public final class FormatSpanDirectory extends FormatSpan implements Catalogued{
    private final DirectoryType spanType;

    FormatSpanDirectory(List<Span> children, boolean[] formats,
        DirectoryType type
    ){
        super(children, formats);
        spanType = type;
    }

    public DirectoryType getIdType(){
        return spanType;
    }

    @Override
    public Optional<CatalogueIdentity> getSpanIdentity(){
        Optional<DirectorySpan> found = spanFromFirst(DirectorySpan.class);
        return found.map(span -> span.buildId());
    }

    @Override
    public boolean isId(){
        return false;
    }

    @Override
    public List<DetailStyle> getBranchStyles(){
        ImmutableList.Builder<DetailStyle> builder = ImmutableList.builder();
        return builder.add(spanType).add(getIdStatus())
            .addAll(super.getBranchStyles()).build();
    }

    @Override
    public String getOutput(){
        Optional<DirectorySpan> id = spanFromFirst(DirectorySpan.class);
        if (id.isPresent()){
            return id.get().getIdRaw();
        }
        return "";
    }

    @Override
    protected SetupParser getParser(String text){
        // TODO editRaw
        return null;
    }

    @Override
    protected void childEdited(){
        // TODO childEdit
    }

    @Override
    protected void docEdited(){
        // TODO docEdited
    }
}
