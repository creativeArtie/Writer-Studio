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

    private final CacheKeyMain<InfoFieldType> cacheFormatTypeField;
    private final CacheKeyOptional<InfoDataSpan> cacheData;
    private final CacheKeyList<StyleInfo> cacheStyles;
    private final CacheKeyMain<Integer> cacheNote;

    /** Creates a {@linkplain LinedSpanCite}.
     *
     * @param children
     *      span children
     * @see LinedParseRest#CITE
     */
    LinedSpanCite(List<Span> children){
        super(children);

        cacheFormatTypeField = new CacheKeyMain<>(InfoFieldType.class);
        cacheData = new CacheKeyOptional<>(InfoDataSpan.class);
        cacheStyles = new CacheKeyList<>(StyleInfo.class);
        cacheNote = CacheKeyMain.integerKey();
    }

    /** Gets the citation field type
     *
     * @return answer
     */
    public InfoFieldType getFormatTypeField(){
        return getLocalCache(cacheFormatTypeField, () -> {
            Optional<InfoFieldSpan> field = spanFromFirst(InfoFieldSpan.class);
            if (field.isPresent()){
                return field.get().getFormatTypeField();
            }
            return InfoFieldType.ERROR;
        });
    }

    /** Gets the citation field data
     *
     * @return answer
     */
    public Optional<InfoDataSpan> getData(){
        return getLocalCache(cacheData, () -> spanFromLast(InfoDataSpan.class));
    }

    @Override
    public List<StyleInfo> getBranchStyles(){
        return getLocalCache(cacheStyles, () -> {
            ImmutableList.Builder<StyleInfo> builder = ImmutableList.builder();
            builder.addAll(super.getBranchStyles()).add(getFormatTypeField());
            if (! getData().isPresent()){
                builder.add(AuxiliaryType.DATA_ERROR);
            }
            return builder.build();
        });
    }

    @Override
    public int getNoteTotal(){
        return getLocalCache(cacheNote, () -> {
            if (getFormatTypeField() != InfoFieldType.ERROR){
                return getData().map(this::getCount).orElse(0);
            }
            return 0;
        });
    }

    /** Gets the note count
     *
     * @return answer
     */
    private int getCount(InfoDataSpan span){
        assert span != null: "Null span";
        if (span instanceof InfoDataSpanFormatted){
            FormattedSpan data = ((InfoDataSpanFormatted)span).getData();
            return data.getPublishTotal() + data.getNoteTotal();
        } else if (span instanceof InfoDataSpanText){
            return ((InfoDataSpanText)span).getData().wordCount();
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
