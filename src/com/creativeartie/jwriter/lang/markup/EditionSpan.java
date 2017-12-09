package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.google.common.collect.ImmutableList;

import com.creativeartie.jwriter.lang.*;


/**
 * A {@link Span} for stating the current status of a section with a heading or
 * an outline.
 */
public class EditionSpan extends SpanBranch{

    EditionSpan(List<Span> children){
        super(children);
    }

    public EditionType getEdition(){
        Span first = get(0);
        if (first instanceof SpanLeaf){
            String text = first.getRaw();
            if (text.length() == 1){
                return EditionType.OTHER;
            }
            return EditionType.valueOf(text.substring(1));
        }
        return EditionType.NONE;
    }

    public String getDetail(){
        Optional<ContentSpan> span = spanAtLast(ContentSpan.class);
        return span.isPresent()? span.get().getParsed(): "";
    }

    public Optional<ContentSpan> getDetailSpan(){
        return spanAtLast(ContentSpan.class);
    }

    @Override
    public List<StyleInfo> getBranchStyles(){
        return ImmutableList.of(getEdition());
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
