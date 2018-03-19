package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import com.creativeartie.writerstudio.main.Checker;

/**
 * {@link BasicText} with {@link FormatSpan format} for {@link FormatSpanMain}.
 * Represented in design/ebnf.txt as {@code FormatContent}.
 */
public final class FormatSpanContent extends FormatSpan implements BasicText{

    /// Stuff for reparsing
    private final FormatParseContent spanReparser;

    private Optional<String> cacheText;
    private Optional<String> cacheTrimmed;
    private Optional<Boolean> cacheSpaceBegin;
    private Optional<Boolean> cacheSpaceEnd;

    FormatSpanContent(List<Span> spanChildren, boolean[] formats,
            FormatParseContent reparser){
        super(spanChildren, formats);
        spanReparser = reparser;
    }

    @Override
    public String getOutput(){
        return getText();
    }

    @Override
    public String getText(){
        cacheText = getCache(cacheText, () -> BasicText.super.getText());
        return cacheText.get();
    }

    @Override
    public String getTrimmed(){
        cacheTrimmed = getCache(cacheTrimmed, () -> BasicText.super
            .getTrimmed());
        return cacheTrimmed.get();
    }

    @Override
    public boolean isSpaceBegin(){
        cacheSpaceBegin = getCache(cacheSpaceBegin,
            () -> BasicText.super.isSpaceBegin());
        return cacheSpaceBegin.get();
    }

    @Override
    public boolean isSpaceEnd(){
        cacheSpaceEnd = getCache(cacheSpaceEnd,
            () -> BasicText.super.isSpaceEnd());
        return cacheSpaceEnd.get();
    }

    @Override
    protected SetupParser getParser(String text){
        return null;
    }

    @Override
    protected void childEdited(){
        super.childEdited();
        cacheText = Optional.empty();
        cacheTrimmed = Optional.empty();
        cacheSpaceBegin = Optional.empty();
        cacheSpaceEnd = Optional.empty();
    }

    @Override
    protected void docEdited(){}

    @Override
    protected String toChildString(){
        return SpanLeaf.escapeText(getText());
    }
}
