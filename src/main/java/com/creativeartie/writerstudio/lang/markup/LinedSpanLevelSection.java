package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** A section heading line. */
public final class LinedSpanLevelSection extends LinedSpanLevel
        implements Catalogued{

    private final CacheKeyMain<String> cacheLookup;

    private final CacheKeyMain<String> cacheEditionDetail;
    private final CacheKeyMain<EditionType> cacheEdition;

    private final CacheKeyMain<Integer> cachePublish;
    private final CacheKeyMain<Integer> cacheNote;

    private final CacheKeyOptional<CatalogueIdentity> cacheId;

    /** Creates a {@linkplain LinedSpanLevelSection}.
     *
     * @param children
     *      span children
     * @see LinedParseLevel#buildSection(SetupPointer, List)
     */
    LinedSpanLevelSection(List<Span> children){
        super(children);
        cacheLookup = CacheKeyMain.stringKey();

        cacheEditionDetail = CacheKeyMain.stringKey();
        cacheEdition = new CacheKeyMain<>(EditionType.class);

        cachePublish = CacheKeyMain.integerKey();
        cacheNote = CacheKeyMain.integerKey();

        cacheId = new CacheKeyOptional<>(CatalogueIdentity.class);
    }

    /** Gets the user reference help text.
     *
     * @return answer
     */
    public String getLookupText(){
        return getLocalCache(cacheLookup, () ->
            spanFromFirst(DirectorySpan.class)
                .map(s -> LINK_REF + s.getLookupText() + LINK_END)
                .orElse("")
        );
    }

    /** Gets the edition span.
     *
     * @return answer
     */
    public String getEditionDetail(){
        return getLocalCache(cacheEditionDetail, () ->
            spanFromLast(EditionSpan.class).map(s -> s.getDetail()).orElse(""));
    }

    /** Gets the edition status.
     *
     * @return answer
     */
    public EditionType getEditionType(){
        return getLocalCache(cacheEdition, () ->
            spanFromLast(EditionSpan.class).map(s -> s.getEditionType()).
            orElse(EditionType.NONE));
    }

    @Override
    public int getPublishTotal(){
        return getLocalCache(cachePublish, () -> {
            if (getLinedType() == LinedType.HEADING){
                return getFormattedSpan().map(s -> s.getPublishTotal())
                    .orElse(0);
            }
            return 0;
        });
    }

    @Override
    public int getNoteTotal(){
        return getLocalCache(cacheNote, () -> {
            if (getLinedType() == LinedType.HEADING){
                return getFormattedSpan().map(s -> s.getNoteTotal())
                    .orElse(0);
            } else {
                assert getLinedType() == LinedType.OUTLINE: getLinedType();
                return getFormattedSpan().map(s -> s.getGrandTotal())
                    .orElse(0);
            }
        });
    }

    @Override
    public Optional<CatalogueIdentity> getSpanIdentity(){
        return getLocalCache(cacheId, () ->
            spanFromFirst(DirectorySpan.class).map(s -> s.buildId())
        );
    }

    @Override
    public boolean isId(){
        return true;
    }

    @Override
    protected SetupParser getParser(String text){
        argumentNotNull(text, "text");

        if (! AuxiliaryChecker.checkLineEnd(text, isDocumentLast())){
            return null;
        }

        /// Gets the starting token and check it
        LinedParseLevel parser = getLinedType() == LinedType.HEADING?
            LinedParseLevel.HEADING: LinedParseLevel.OUTLINE;
        return text.startsWith(LEVEL_STARTERS.get(parser).get(getLevel() - 1))?
            parser: null;
    }

}
