package com.creativeartie.writer.lang.markup;

import java.util.*;

import com.creativeartie.writer.lang.*;

/** Escaped character with the {@link AuxiliaryData#TOKEN_ESCAPE}. */
public final class BasicTextEscape extends SpanBranch{

    private final CacheKeyMain<String> cacheEscape;

    /** Creates a {@linkplain BasicTextEscape}.
     *
     * @param children
     *      span children
     * @see BasicParseText#parseEscape(SetupPointer, List)
     */
    BasicTextEscape(List<Span> children){
        super(children);
        cacheEscape = CacheKeyMain.stringKey();
    }

    /** Gets the escape text.
     *
     * @return answer
     */
    public String getEscape(){
        return getLocalCache(cacheEscape, () -> size() == 2? get(1).getRaw():
            "");
    }

    @Override
    protected SetupParser getParser(String text){
        return null;
    }

    @Override
    public String toString(){
        return getEscape() == "\n"? "\\\n": getRaw();
    }
}
