package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;

/**
 * Line that stores a section heading.
 */
public class LinedSpanSection extends LinedSpanLevel implements Catalogued{

    LinedSpanSection(List<Span> children){
        super(children);
    }

    public Optional<EditionSpan> getEditionSpan(){
        return spanFromLast(EditionSpan.class);
    }

    @Override
    public Optional<CatalogueIdentity> getSpanIdentity(){
        return spanFromFirst(DirectorySpan.class).map(span -> span.buildId());
    }

    @Override
    public boolean isId(){
        return true;
    }

    public EditionType getEdition(){
        Optional<EditionSpan> status = getEditionSpan();
        return status.isPresent()? status.get().getEdition(): EditionType.NONE;
    }


    @Override
    public int getPublishTotal(){
        if (getLinedType() == LinedType.HEADING){
            return getFormattedSpan().map(span -> span.getPublishTotal())
                .orElse(0);
        }
        return 0;
    }

    @Override
    public int getNoteTotal(){
        if (getLinedType() == LinedType.HEADING){
            return getFormattedSpan().map(span -> span.getNoteTotal())
                .orElse(0);
        } else {
            assert getLinedType() == LinedType.OUTLINE: getLinedType();
            return getFormattedSpan().map(span -> span.getTotalCount())
                .orElse(0);
        }
    }
}
