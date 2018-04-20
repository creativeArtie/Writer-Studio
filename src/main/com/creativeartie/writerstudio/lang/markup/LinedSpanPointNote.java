package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;

/**
 * Line that stores text of a footnote or a endnote. Represented in
 * design/ebnf.txt as {@code LinedFootnote}, {@code LinedEndnote}.
 */
public class LinedSpanPointNote extends LinedSpanPoint {
    private Optional<DirectoryType> cacheType;
    private Optional<Optional<FormattedSpan>> cacheFormatted;

    LinedSpanPointNote(List<Span> children){
        super(children);
    }

    @Override
    public DirectoryType getDirectoryType(){
        cacheType = getCache(cacheType, () -> getLinedType() ==
            LinedType.FOOTNOTE? DirectoryType.FOOTNOTE: DirectoryType.ENDNOTE);
        return cacheType.get();
    }

    public Optional<FormattedSpan> getFormattedSpan(){
        cacheFormatted = getCache(cacheFormatted, () -> spanFromLast(
            FormattedSpan.class));
        return cacheFormatted.get();
    }

    @Override
    protected SetupParser getParser(String text){
        if (AuxiliaryChecker.checkLineEnd(text, isLast())){
            if( text.startsWith(LINED_ENDNOTE)){
                return LinedParsePointer.ENDNOTE;
            } else if( text.startsWith(LINED_FOOTNOTE)){
                return LinedParsePointer.FOOTNOTE;
            }
        }
        return null;
    }

    @Override
    protected void childEdited(){
        super.childEdited();
        cacheType = Optional.empty();
        cacheFormatted = Optional.empty();
    }

    @Override
    protected void docEdited(){}
       protected String getLookupStart(){
        return getLinedType() == LinedType.ENDNOTE? CURLY_ENDNOTE: CURLY_FOOTNOTE;
    }

    protected String getLookupEnd(){
        return CURLY_END;
    }
}
