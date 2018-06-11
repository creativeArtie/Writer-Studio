package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.google.common.collect.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** A citation for "Cite Worked" page, footnote or in-text. */
public class LinedSpanCite extends LinedSpan {

    /** Check if the line start with {@link LINED_CITE}.
     *
     * @param text
     *      new text
     * @return answer
     * @see #getParser(String)
     * @see NoteCardSpan#getParser(String)
     */
    static boolean checkLine(String text){
        return text.startsWith(LINED_CITE);
    }

    private final CacheKeyMain<InfoFieldType> cacheInfoType;
    private final CacheKeyOptional<InfoDataSpan> cacheData;
    private final CacheKeyMain<Integer> cacheNote;

    /** Creates a {@linkplain LinedSpanCite}.
     *
     * @param children
     *      span children
     * @see LinedParseRest#CITE
     */
    LinedSpanCite(List<Span> children){
        super(children);

        cacheInfoType = new CacheKeyMain<>(InfoFieldType.class);
        cacheData = new CacheKeyOptional<>(InfoDataSpan.class);
        cacheNote = CacheKeyMain.integerKey();
    }

    /** Gets the citation field type
     *
     * @return answer
     */
    public InfoFieldType getInfoFieldType(){
        return getLocalCache(cacheInfoType, () ->
            spanFromFirst(InfoFieldSpan.class)
            .map(s -> s.getInfoFieldType())
            .orElse(InfoFieldType.ERROR));
    }

    /** Gets the citation field data
     *
     * @return answer
     */
    public Optional<InfoDataSpan> getData(){
        return getLocalCache(cacheData, () -> spanFromLast(InfoDataSpan.class));
    }

    @Override
    public int getNoteTotal(){
        return getLocalCache(cacheNote, () -> {
            if (getInfoFieldType() != InfoFieldType.ERROR){
                return getData().map(this::getCount).orElse(0);
            }
            return 0;
        });
    }

    /** Gets the note count
     *
     * @return answer
     * @see #getNoteTotal()
     */
    private int getCount(InfoDataSpan span){
        assert span != null: "Null span";
        if (span instanceof InfoDataSpanFormatted){
            FormattedSpan data = ((InfoDataSpanFormatted)span).getData();
            return data.getPublishTotal() + data.getNoteTotal();
        } else if (span instanceof InfoDataSpanText){
            return ((InfoDataSpanText)span).getData().getWordCount();
        }
        return 0;
    }

    @Override
    protected SetupParser getParser(String text){
        argumentNotNull(text, "text");
        return checkLine(text) && AuxiliaryChecker.checkLineEnd(text, isDocumentLast())?
            LinedParseRest.CITE: null;
    }
}
