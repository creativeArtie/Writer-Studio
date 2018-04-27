package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;

/**
 * Line that stores text of a footnote or a endnote. Represented in
 * design/ebnf.txt as {@code LinedFootnote}, {@code LinedEndnote}.
 */
public class LinedSpanPointNote extends LinedSpanPoint {
    private final CacheKeyMain<DirectoryType> cacheType;
    private final CacheKeyOptional<FormattedSpan> cacheFormatted;

    LinedSpanPointNote(List<Span> children){
        super(children);

        cacheType = new CacheKeyMain<>(DirectoryType.class);
        cacheFormatted = new CacheKeyOptional<>(FormattedSpan.class);
    }

    @Override
    public DirectoryType getDirectoryType(){
        return getLocalCache(cacheType, () -> getLinedType() ==
            LinedType.FOOTNOTE? DirectoryType.FOOTNOTE: DirectoryType.ENDNOTE);
    }

    public Optional<FormattedSpan> getFormattedSpan(){
        return getLocalCache(cacheFormatted, () -> spanFromLast(
            FormattedSpan.class));
    }

    @Override
    protected SetupParser getParser(String text){
        if (AuxiliaryChecker.checkLineEnd(text, isDocumentLast())){
            if( text.startsWith(LINED_ENDNOTE)){
                return LinedParsePointer.ENDNOTE;
            } else if( text.startsWith(LINED_FOOTNOTE)){
                return LinedParsePointer.FOOTNOTE;
            }
        }
        return null;
    }

    protected String getLookupStart(){
        return getLinedType() == LinedType.ENDNOTE? CURLY_ENDNOTE: CURLY_FOOTNOTE;
    }

    protected String getLookupEnd(){
        return CURLY_END;
    }
}
