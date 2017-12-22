package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;

/**
 * Line that stores a section heading.
 */
public class LinedSpanLevelSection extends LinedSpanLevel implements Catalogued{

    private Optional<Optional<EditionSpan>> cacheEditionSpan;
    private Optional<Optional<CatalogueIdentity>> cacheId;
    private Optional<EditionType> cacheEdition;
    private Optional<Integer> cachePublish;
    private Optional<Integer> cacheNote;

    LinedSpanLevelSection(List<Span> children){
        super(children);
    }

    public Optional<EditionSpan> getEditionSpan(){
        return spanFromLast(EditionSpan.class);
    }

    @Override
    public Optional<CatalogueIdentity> getSpanIdentity(){
        cacheId = getCache(cacheId, () ->
            spanFromFirst(DirectorySpan.class).map(span -> span.buildId()));
        return cacheId.get();
    }

    @Override
    public boolean isId(){
        return true;
    }

    public EditionType getEdition(){
        cacheEdition = getCache(cacheEdition, () -> {
            Optional<EditionSpan> status = getEditionSpan();
            return status.isPresent()? status.get().getEdition():
                EditionType.NONE;
        });
        return cacheEdition.get();
    }


    @Override
    public int getPublishTotal(){
        cachePublish = getCache(cachePublish, () -> {
            if (getLinedType() == LinedType.HEADING){
                return getFormattedSpan().map(span -> span.getPublishTotal())
                    .orElse(0);
            }
            return 0;
        });
        return cachePublish.get();
    }

    @Override
    public int getNoteTotal(){
        cacheNote = getCache(cacheNote, () -> {
            if (getLinedType() == LinedType.HEADING){
                return getFormattedSpan().map(span -> span.getNoteTotal())
                    .orElse(0);
            } else {
                assert getLinedType() == LinedType.OUTLINE: getLinedType();
                return getFormattedSpan().map(span -> span.getTotalCount())
                    .orElse(0);
            }
        });
        return cacheNote.get();
    }

    @Override
    protected SetupParser getParser(String text){
        return text.startsWith(getLevelToken(LinedParseLevel.HEADING, 1))?
            LinedParseLevel.HEADING :
            (text.startsWith(getLevelToken(LinedParseLevel.OUTLINE, 1))?
                LinedParseLevel.OUTLINE: null);
    }
    @Override
    protected void childEdited(){
        super.childEdited();
        cacheEditionSpan = Optional.empty();
        cacheId = Optional.empty();
        cacheEdition = Optional.empty();
        cachePublish = Optional.empty();
        cacheNote = Optional.empty();
    }

    @Override
    protected void docEdited(){}
}
