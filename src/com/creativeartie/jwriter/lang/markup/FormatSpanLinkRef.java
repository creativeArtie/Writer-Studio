package com.creativeartie.jwriter.lang.markup;

import com.google.common.collect.*;

import java.util.*;
import com.creativeartie.jwriter.lang.*;

/**
 * A {@link FormatSpanLink} with path located somewhere in the document.
 */
public final class FormatSpanLinkRef extends FormatSpanLink 
    implements Catalogued{
    
    FormatSpanLinkRef(List<Span> children, boolean[] formats){
        super(children, formats);
    }
    
    @Override
    public String getPath(){
        Optional<CatalogueIdentity> id = getSpanIdentity();
        if (id.isPresent()){
            CatalogueData data = getDocument().getCatalogue().get(id.get());
            if (data.isReady()){
                Span span = data.getTarget();
                return ((LinedSpanPointLink)span).getPath();
            }
        }
        return "";
    }
    
    @Override
    public String getText(){
        Optional<ContentSpan> text = spanFromFirst(ContentSpan.class);
        if (text.isPresent()){
            return text.get().getParsed();
        }
        return getPath();
    }
    
    @Override
    public Optional<CatalogueIdentity> getSpanIdentity(){
        return spanFromFirst(DirectorySpan.class).map(
            span -> span.buildId());
    }
    
    @Override
    public boolean isId(){
        return false;
    }
    
    @Override
    public List<DetailStyle> getBranchStyles(){
        ImmutableList.Builder<DetailStyle> builder = ImmutableList.builder();
        return builder.add(AuxiliaryStyle.REF_LINK).add(getIdStatus())
            .addAll(super.getBranchStyles()).build();
        
    }
}
