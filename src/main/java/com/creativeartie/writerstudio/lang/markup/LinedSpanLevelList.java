package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** A bullet or numbered line item. */
public class LinedSpanLevelList extends LinedSpanLevel {
    private LinedParseLevel spanReparser;
    private final CacheKeyMain<Integer> cachePublish;
    private final CacheKeyMain<Integer> cacheNote;

    /** Creates a {@linkplain LinedSpanLevelList}.
     *
     * @param children
     *      span children
     * @see LinedParseLevel#buildBasic(SetupPointer, List)
     */
    LinedSpanLevelList(List<Span> children){
        super(children);
        cachePublish = CacheKeyMain.integerKey();
        cacheNote = CacheKeyMain.integerKey();
    }

    @Override
    public int getPublishTotal(){
        return getLocalCache(cachePublish, () -> getFormattedSpan()
            .map(s -> s.getPublishTotal()).orElse(0));
    }

    @Override
    public int getNoteTotal(){
        return getLocalCache(cacheNote, () -> getFormattedSpan()
            .map(s -> s.getNoteTotal()).orElse(0)
        );
    }

    @Override
    protected SetupParser getParser(String text){
        argumentNotNull(text, "text");
        if (AuxiliaryChecker.checkLineEnd(text, isDocumentLast())){
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
        assert text != null: "Null text";
        assert parser != null: "Null parser";
        for (String token: LEVEL_STARTERS.get(parser)){
            if (text.startsWith(token)){
                return true;
            }
        }
        return false;
    }
}
