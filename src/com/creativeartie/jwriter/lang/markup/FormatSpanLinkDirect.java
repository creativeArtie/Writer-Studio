package com.creativeartie.jwriter.lang.markup;

import com.google.common.collect.*;

import java.util.*;
import java.util.Optional;
import com.creativeartie.jwriter.lang.*;

/**
 * {@link FormatSpanLink} with path indicated right in the text.
 */
public class FormatSpanLinkDirect extends FormatSpanLink {

    FormatSpanLinkDirect(List<Span> children, boolean[] formats){
        super(children, formats);
    }

    @Override
    public String getPath(){
        Optional<ContentSpan> path = spanFromFirst(ContentSpan.class);
        return path.isPresent()? path.get().getTrimmed(): "";
    }

    @Override
    public String getText(){
        Optional<ContentSpan> text = spanFromLast(ContentSpan.class);
        return text.isPresent()? text.get().getTrimmed(): "";
    }

    @Override
    public List<StyleInfo> getBranchStyles(){
        ImmutableList.Builder<StyleInfo> builder = ImmutableList.builder();
        return builder.add(AuxiliaryType.DIRECT_LINK)
            .addAll(super.getBranchStyles()).build();
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
