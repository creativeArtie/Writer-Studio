package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.main.Checker.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;

/**
 * Line that points to a list. Represented in design/ebnf.txt as
 * {@code LinedNumbered}, and {@link LinedBullet}.
 */
public class LinedSpanLevelList extends LinedSpanLevel {
    private LinedParseLevel spanReparser;
    private final CacheKeyMain<Integer> cachePublish;
    private final CacheKeyMain<Integer> cacheNote;

    LinedSpanLevelList(List<Span> children){
        super(children);
        cachePublish = CacheKey.integerKey();
        cacheNote = CacheKey.integerKey();
    }

    @Override
    public int getPublishTotal(){
        return getLocalCache(cachePublish, () -> getFormattedSpan()
            .map(span -> span.getPublishTotal()).orElse(0));
    }

    @Override
    public int getNoteTotal(){
        return getLocalCache(cacheNote, () -> getFormattedSpan()
            .map(span -> span.getNoteTotal()).orElse(0)
        );
    }

    @Override
    protected SetupParser getParser(String text){
        if (AuxiliaryChecker.checkLineEnd(text, isLast())){
            if (getParser(text, LinedParseLevel.BULLET)){
                return LinedParseLevel.BULLET;
            } else if (getParser(text, LinedParseLevel.NUMBERED)){
                return LinedParseLevel.NUMBERED;
            }
        }
        return null;
    }

    /** Check if the line is a bullet or a numbered line.
     *
     * @param text
     *      new text
     * @param parser
     *      the line type
     * @return answer
     */
    private boolean getParser(String text, LinedParseLevel parser){
        for (String token: getLevelTokens(parser)){
            if (text.startsWith(token)){
                return true;
            }
        }
        return false;
    }
}
