package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;

/**
 * Line representing a basic paragraph.
 */
public class LinedSpanParagraph extends LinedSpan {

    private Optional<Optional<FormatSpanMain>> cacheFormatted;

    LinedSpanParagraph(List<Span> children){
        super(children);
    }

    public Optional<FormatSpanMain> getFormattedSpan(){
        return spanAtFirst(FormatSpanMain.class);
    }

    @Override
    public int getPublishTotal(){
        return getFormattedSpan().map(span -> span.getPublishTotal()).orElse(0);
    }

    @Override
    public int getNoteTotal(){
        return getFormattedSpan().map(span -> span.getNoteTotal()).orElse(0);
    }

    @Override
    protected SetupParser getParser(String text){
        for (String token: getLinedTokens()){
            if (text.startsWith(token)){
                return null;
            }
        }
        return BasicParseText.checkLineEnd(isLast(), text)?
            LinedParseRest.PARAGRAPH: null;
    }

    @Override
    protected void childEdited(){
        // TODO childEdit
    }

    @Override
    protected void docEdited(){
        // TODO docEdited
    }
}
