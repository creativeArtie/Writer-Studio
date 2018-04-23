package com.creativeartie.writerstudio.lang.markup;

import java.util.*;
import com.google.common.collect.*;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;

/**
 * Line that store research sources and how to do in-text citation. Represented
 * in design/ebnf.txt as {@code LinedCite}.
 */
public class LinedSpanCite extends LinedSpan {

    /** Check if the line start with {@link LINED_CITE}.
     *
     * @param text
     *      new text
     * @return answer
     */
    static boolean checkLine(String text){
        return text.startsWith(LINED_CITE);
    }

    private final CacheKeyMain<InfoFieldType> cacheFieldType;
    private final CacheKeyOptional<InfoDataSpan> cacheData;
    private final CacheKeyList<StyleInfo> cacheStyles;
    private final CacheKeyMain<Integer> cacheNote;

    public LinedSpanCite(List<Span> children){
        super(children);

        cacheFieldType = new CacheKeyMain<>(InfoFieldType.class);
        cacheData = new CacheKeyOptional<>(InfoDataSpan.class);
        cacheStyles = new CacheKeyList<>(StyleInfo.class);
        cacheNote = CacheKey.integerKey();
    }

    public InfoFieldType getFieldType(){
        return getLocalCache(cacheFieldType, () -> {
            Optional<InfoFieldSpan> field = spanFromFirst(InfoFieldSpan.class);
            if (field.isPresent()){
                return field.get().getFieldType();
            }
            return InfoFieldType.ERROR;
        });
    }

    public Optional<InfoDataSpan> getData(){
        return getLocalCache(cacheData, () -> spanFromLast(InfoDataSpan.class));
    }

    @Override
    public List<StyleInfo> getBranchStyles(){
        return getLocalCache(cacheStyles, () -> {
            ImmutableList.Builder<StyleInfo> builder = ImmutableList.builder();
            builder.addAll(super.getBranchStyles()).add(getFieldType());
            if (! getData().isPresent()){
                builder.add(AuxiliaryType.DATA_ERROR);
            }
            return builder.build();
        });
    }

    @Override
    public int getNoteTotal(){
        return getLocalCache(cacheNote, () -> {
            if (getFieldType() != InfoFieldType.ERROR){
                return getData().map(span -> getCount(span)).orElse(0);
            }
            return 0;
        });
    }

    private int getCount(InfoDataSpan span){
        if (span instanceof InfoDataSpanFormatted){
            FormattedSpan data = ((InfoDataSpanFormatted)span).getData();
            return data.getPublishTotal() + data.getNoteTotal();
        } else if (span instanceof InfoDataSpanText){
            return ((InfoDataSpanText)span).getData().wordCount();
        }
        return 0;
    }

    @Override
    protected SetupParser getParser(String text){
        return checkLine(text) && AuxiliaryChecker.checkLineEnd(text, isLast())?
            LinedParseCite.INSTANCE: null;
    }
}
