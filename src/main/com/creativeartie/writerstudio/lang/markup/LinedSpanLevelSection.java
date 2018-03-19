package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;

/**
 * Line that stores a section heading. Represented in design/ebnf.txt as
 * {@code LinedHeading}, {@code LinedOutline}.
 */
public final class LinedSpanLevelSection extends LinedSpanLevel
        implements Catalogued{

    private Optional<Optional<EditionSpan>> cacheEditionSpan;
    private Optional<Optional<CatalogueIdentity>> cacheId;
    private Optional<String> cacheLookup;
    private Optional<EditionType> cacheEdition;
    private Optional<Integer> cachePublish;
    private Optional<Integer> cacheNote;
    private Optional<String> cacheTitle;

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

    public String getLookupText(){
        cacheLookup = getCache(cacheLookup, () ->
            spanFromFirst(DirectorySpan.class)
                .map(span -> LINK_REF + span.getLookupText() + LINK_END)
                .orElse("")
        );
        return cacheLookup.get();
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

    public String getTitle(){
        cacheTitle = getCache(cacheTitle, () ->{
            Optional<FormatSpanMain> main = spanFromLast(FormatSpanMain.class);
            return main.isPresent()? main.get().getParsedText(): "";
        });
        return cacheTitle.get();
    }

    @Override
    protected SetupParser getParser(String text){
        if (! AuxiliaryChecker.checkLineEnd(isLast(), text)){
            return null;
        }
        LinedParseLevel parser = getLinedType() == LinedType.HEADING?
            LinedParseLevel.HEADING: LinedParseLevel.OUTLINE;
        return text.startsWith(getLevelToken(parser, getLevel()))? parser: null;
    }

    @Override
    protected void childEdited(){
        super.childEdited();
        cacheEditionSpan = Optional.empty();
        cacheId = Optional.empty();
        cacheLookup = Optional.empty();
        cacheEdition = Optional.empty();
        cachePublish = Optional.empty();
        cacheNote = Optional.empty();
        cacheTitle = Optional.empty();
    }

    @Override
    protected void docEdited(){}
}
