package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.util.ParameterChecker.*;

/** Footnote or a endnote line. */
public class LinedSpanPointNote extends LinedSpanPoint {
    private final CacheKeyMain<DirectoryType> cacheType;
    private final CacheKeyOptional<FormattedSpan> cacheFormatted;

    /** Creates a {@linkplain LinedSpanPointNote}.
     *
     * @param children
     *      span children
     * @see LinedParsePointer#FOOTNOTE
     * @see LinedParsePointer#ENDNOTE
     */
    LinedSpanPointNote(List<Span> children){
        super(children);

        cacheType = new CacheKeyMain<>(DirectoryType.class);
        cacheFormatted = new CacheKeyOptional<>(FormattedSpan.class);
    }

    /** Gets the formatted content.
     *
     * @return answer
     */
    public Optional<FormattedSpan> getFormattedSpan(){
        return getLocalCache(cacheFormatted, () -> spanFromLast(
            FormattedSpan.class));
    }

    @Override
    protected String getLookupStart(){
        return getDirectoryType() == DirectoryType.ENDNOTE? CURLY_ENDNOTE:
            CURLY_FOOTNOTE;
    }

    @Override
    protected String getLookupEnd(){
        return CURLY_END;
    }

    @Override
    public DirectoryType getDirectoryType(){
        return getLocalCache(cacheType, () -> leafFromFirst(SpanLeafStyle.KEYWORD)
            .map(s -> s.getRaw().startsWith(LINED_FOOTNOTE)).orElse(false)?
            DirectoryType.FOOTNOTE: DirectoryType.ENDNOTE);
    }

    @Override
    protected SetupParser getParser(String text){
        argumentNotNull(text, "text");
        if (AuxiliaryChecker.checkLineEnd(text, isDocumentLast())){
            if( text.startsWith(LINED_ENDNOTE)){
                return LinedParsePointer.ENDNOTE;
            } else if( text.startsWith(LINED_FOOTNOTE)){
                return LinedParsePointer.FOOTNOTE;
            }
        }
        return null;
    }
}
