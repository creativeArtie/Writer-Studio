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

    private final CacheKeyOptional<EditionSpan> cacheEditionSpan;
    private final CacheKeyOptional<CatalogueIdentity> cacheId;
    private final CacheKeyMain<String> cacheLookup;
    private final CacheKeyMain<EditionType> cacheEdition;
    private final CacheKeyMain<Integer> cachePublish;
    private final CacheKeyMain<Integer> cacheNote;
    private final CacheKeyMain<String> cacheTitle;

    LinedSpanLevelSection(List<Span> children){
        super(children);

        cacheEditionSpan = new CacheKeyOptional<>(EditionSpan.class);
        cacheId = new CacheKeyOptional<>(CatalogueIdentity.class);
        cacheEdition = new CacheKeyMain<>(EditionType.class);
        cachePublish = CacheKey.integerKey();
        cacheNote = CacheKey.integerKey();
        cacheTitle = CacheKey.stringKey();
        cacheLookup = CacheKey.stringKey();
    }

    public Optional<EditionSpan> getEditionSpan(){
        return spanFromLast(EditionSpan.class);
    }

    @Override
    public Optional<CatalogueIdentity> getSpanIdentity(){
        return getLocalCache(cacheId, () ->
            spanFromFirst(DirectorySpan.class).map(span -> span.buildId())
        );
    }

    public String getLookupText(){
        return getLocalCache(cacheLookup, () ->
            spanFromFirst(DirectorySpan.class)
                .map(span -> LINK_REF + span.getLookupText() + LINK_END)
                .orElse("")
        );
    }

    @Override
    public boolean isId(){
        return true;
    }

    public EditionType getEdition(){
        return getLocalCache(cacheEdition, () -> {
            Optional<EditionSpan> status = getEditionSpan();
            return status.isPresent()? status.get().getEdition():
                EditionType.NONE;
        });
    }


    @Override
    public int getPublishTotal(){
        return getLocalCache(cachePublish, () -> {
            if (getLinedType() == LinedType.HEADING){
                return getFormattedSpan().map(span -> span.getPublishTotal())
                    .orElse(0);
            }
            return 0;
        });
    }

    @Override
    public int getNoteTotal(){
        return getLocalCache(cacheNote, () -> {
            if (getLinedType() == LinedType.HEADING){
                return getFormattedSpan().map(span -> span.getNoteTotal())
                    .orElse(0);
            } else {
                assert getLinedType() == LinedType.OUTLINE: getLinedType();
                return getFormattedSpan().map(span -> span.getTotalCount())
                    .orElse(0);
            }
        });
    }

    public String getTitle(){
        return getLocalCache(cacheTitle, () ->{
            Optional<FormattedSpan> main = spanFromLast(FormattedSpan.class);
            return main.isPresent()? main.get().getParsedText(): "";
        });
    }

    @Override
    protected SetupParser getParser(String text){
        if (! AuxiliaryChecker.checkLineEnd(text, isLast())){
            return null;
        }

        /// Gets the starting token and check it
        LinedParseLevel parser = getLinedType() == LinedType.HEADING?
            LinedParseLevel.HEADING: LinedParseLevel.OUTLINE;
        return text.startsWith(getLevelToken(parser, getLevel()))? parser: null;
    }

}
