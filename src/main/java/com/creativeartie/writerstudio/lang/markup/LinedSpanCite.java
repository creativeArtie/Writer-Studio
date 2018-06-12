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

    private final CacheKeyMain<InfoFieldType> cacheField;
    private final CacheKeyMain<InfoDataType> cacheType;
    private final CacheKeyOptional<SpanBranch> cacheData;
    private final CacheKeyMain<Integer> cacheNote;

    /** Creates a {@linkplain LinedSpanCite}.
     *
     * @param children
     *      span children
     * @see LinedParseRest#CITE
     */
    LinedSpanCite(List<Span> children){
        super(children);

        cacheField = new CacheKeyMain<>(InfoFieldType.class);
        cacheType = new CacheKeyMain<>(InfoDataType.class);
        cacheData = new CacheKeyOptional<>(SpanBranch.class);
        cacheNote = CacheKeyMain.integerKey();
    }

    /** Gets the citation field type
     *
     * @return answer
     */
    public InfoFieldType getInfoFieldType(){
        return getLocalCache(cacheField, () ->
            spanFromFirst(InfoFieldSpan.class)
            .map(s -> s.getInfoFieldType())
            .orElse(InfoFieldType.ERROR));
    }

    public InfoDataType getInfoDataType(){
        return getLocalCache(cacheType, () -> getInfoFieldType().getDataType());
    }

    /** Gets the citation field data
     *
     * @return answer
     */
    public Optional<SpanBranch> getData(){
        return getLocalCache(cacheData, () -> spanFromLast(SpanBranch.class));
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
    private int getCount(SpanBranch span){
        assert span != null: "Null span";
        if (span instanceof FormattedSpan){
            FormattedSpan data = (FormattedSpan)span;
            return data.getPublishTotal() + data.getNoteTotal();
        } else if (span instanceof ContentSpan){
            return ((ContentSpan)span).getWordCount();
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
