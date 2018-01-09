package com.creativeartie.jwriter.lang.markup;

import java.util.*;
import com.google.common.collect.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;

/**
 * Line that store research sources and how to do in-text citation. Represented
 * in design/ebnf.txt as {@code LinedCite}.
 */
public class LinedSpanCite extends LinedSpan {

    private Optional<InfoFieldType> cacheFieldType;
    private Optional<Optional<InfoDataSpan>> cacheData;
    private Optional<List<StyleInfo>> cacheStyles;
    private Optional<Integer> cacheNote;

    public LinedSpanCite(List<Span> children){
        super(children);
    }

    public InfoFieldType getFieldType(){
        cacheFieldType = getCache(cacheFieldType, () -> {
            Optional<InfoFieldSpan> field = spanFromFirst(InfoFieldSpan.class);
            if (field.isPresent()){
                return field.get().getFieldType();
            }
            return InfoFieldType.ERROR;
        });
        return cacheFieldType.get();
    }

    public Optional<InfoDataSpan> getData(){
        cacheData = getCache(cacheData, () -> spanFromLast(InfoDataSpan.class));
        return cacheData.get();
    }

    @Override
    public List<StyleInfo> getBranchStyles(){
        cacheStyles = getCache(cacheStyles, () -> {
            ImmutableList.Builder<StyleInfo> builder = ImmutableList.builder();
            builder.addAll(super.getBranchStyles()).add(getFieldType());
            if (! getData().isPresent()){
                builder.add(AuxiliaryType.DATA_ERROR);
            }
            return builder.build();
        });
        return cacheStyles.get();
    }

    @Override
    public int getNoteTotal(){
        cacheNote = getCache(cacheNote, () -> {
            if (getFieldType() != InfoFieldType.ERROR){
                return getData().map(span -> getCount(span)).orElse(0);
            }
            return 0;
        });
        return cacheNote.get();
    }

    private int getCount(InfoDataSpan span){
        if (span instanceof InfoDataSpanFormatted){
            FormatSpanMain data = ((InfoDataSpanFormatted)span).getData();
            return data.getPublishTotal() + data.getNoteTotal();
        } else if (span instanceof InfoDataSpanText){
            return ((InfoDataSpanText)span).getData().wordCount();
        }
        return 0;
    }

    @Override
    protected SetupParser getParser(String text){
        return canParse(isLast(), text)?
            LinedParseCite.INSTANCE: null;
    }

    static boolean canParse(String text, boolean isLast){
        return text.startsWith(LINED_CITE) &&
            AuxiliaryChecker.checkLineEnd(isLast, text)?
    }

    @Override
    protected void childEdited(){
        cacheFieldType = Optional.empty();
        cacheData = Optional.empty();
        cacheStyles = Optional.empty();
        cacheNote = Optional.empty();
    }

    @Override
    protected void docEdited(){}
}
