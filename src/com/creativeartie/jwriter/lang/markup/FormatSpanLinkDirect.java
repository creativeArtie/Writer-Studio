package com.creativeartie.jwriter.lang.markup;

import com.google.common.collect.*;

import java.util.*;
import java.util.Optional;
import com.creativeartie.jwriter.lang.*;

/**
 * A {@link FormatSpanLink} with path indicated right in the text.
 */
public class FormatSpanLinkDirect extends FormatSpanLink {

    FormatSpanLinkDirect(List<Span> children, boolean[] formats){
        super(children, formats);
    }

    @Override
    public String getPath(){
        Optional<ContentSpan> path = spanFromFirst(ContentSpan.class);
        return path.isPresent()? path.get().getParsed(): "";
    }

    @Override
    public String getText(){
        Optional<ContentSpan> text = spanFromLast(ContentSpan.class);
        return text.isPresent()? text.get().getParsed(): "";
    }

    @Override
    public List<DetailStyle> getBranchStyles(){
        ImmutableList.Builder<DetailStyle> builder = ImmutableList.builder();
        return builder.add(AuxiliaryType.DIRECT_LINK)
            .addAll(super.getBranchStyles()).build();
    }

}
