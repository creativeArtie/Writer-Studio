package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;

/**
 * Line that stores a section heading.
 */
public class LinedSpanLevelSection extends LinedSpanLevel implements Catalogued{

    private Optional<Integer> cachePublish;
    private Optional<Integer> cacheNote;

    LinedSpanLevelSection(List<Span> children, LinedParseLevel reparser){
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

    @Override
    protected SetupParser getParser(String text){
        //TODO getParser(text)
        return null;
    }

    @Override
    protected void childEdited(){
        super.childEdited();
        cachePublish = Optional.empty();
        cacheNote = Optional.empty();
    }

    @Override
    protected void docEdited(){}
}
