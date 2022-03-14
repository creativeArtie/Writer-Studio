package com.creativeartie.writer.lang.markup;

import java.util.*;

import com.creativeartie.writer.lang.*;

import static com.creativeartie.writer.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writer.main.ParameterChecker.*;

/**A reusable hyperlink. */
public class LinedSpanPointLink extends LinedSpanPoint {

    private final CacheKeyMain<String> cachePath;

    /** Creates a {@linkplain LinedSpanPointLink}.
     *
     * @param children
     *      span children
     * @see LinedParsePointer#LINK
     */
    LinedSpanPointLink(List<Span> children){
        super(children);

        cachePath = CacheKeyMain.stringKey();
    }

    /** Gets the link path. */
    public String getPath(){
        return getLocalCache(cachePath, () -> {
            Optional<ContentSpan> span = spanFromLast(ContentSpan.class);
            return span.isPresent()? span.get().getTrimmed() : "";
        });
    }

    @Override
    protected String getLookupStart(){
        return LINK_REF;
    }

    @Override
    protected String getLookupEnd(){
        return LINK_END;
    }

    @Override
    public DirectoryType getDirectoryType(){
        return DirectoryType.LINK;
    }

    @Override
    protected SetupParser getParser(String text){
        argumentNotNull(text, "text");
        return text.startsWith(LINED_LINK) &&
            AuxiliaryChecker.checkLineEnd(text, isDocumentLast())?
            LinedParsePointer.LINK: null;
    }
}
