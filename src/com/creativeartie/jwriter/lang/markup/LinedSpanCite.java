package com.creativeartie.jwriter.lang.markup;

import java.util.List;
import java.util.Optional;
import com.google.common.collect.ImmutableList;

import com.creativeartie.jwriter.lang.*;

public class LinedSpanCite extends LinedSpan {

    public LinedSpanCite(List<Span> children){
        super(children);
    }

    public InfoFieldType getFieldType(){
        Optional<InfoFieldSpan> field = spanFromFirst(InfoFieldSpan.class);
        if (field.isPresent()){
            return field.get().getFieldType();
        }
        return InfoFieldType.ERROR;
    }

    public Optional<InfoDataSpan<?>> getData(){
        return spanFromLast(InfoDataSpan.class)
            .map(value -> (InfoDataSpan<?>) value);
    }

    @Override
    public List<DetailStyle> getBranchStyles(){
        ImmutableList.Builder<DetailStyle> builder = ImmutableList.builder();
        builder.addAll(super.getBranchStyles()).add(getFieldType());
        if (! getData().isPresent()){
            builder.add(AuxiliaryStyle.DATA_ERROR);
        }
        return builder.build();
    }

    @Override
    public int getNoteCount(){
        if (getFieldType() != InfoFieldType.ERROR){
            return getData().map(span -> getCount(span)).orElse(0);
        }
        return 0;
    }

    private int getCount(InfoDataSpan<?> span){
        if (span instanceof InfoDataSpanFormatted){
            FormatSpanMain data = ((InfoDataSpanFormatted)span).getData();
            return data.getPublishCount() + data.getNoteCount();
        } else if (span instanceof InfoDataSpanText){
            return ((InfoDataSpanText)span).getData().wordCount();
        }
        return 0;
    }
}
