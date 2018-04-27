package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;

/**
 * Line that store note heading or details. Represented in design/ebnf.txt as
 * {@code LinedNoteHead}, and {@code LinedNoteLine}.
 */
public class LinedSpanNote extends LinedSpan{

    /** Check line as a first line in a {@link NoteCard}.
     *
     * @param text
     *      new text
     * @param answer
     */
    static boolean checkFirstLine(String text){
        return text.startsWith(LINED_NOTE);
    }

    /** Check line as a middle line in a {@link NoteCard}.
     *
     * @param text
     *      new text
     * @param answer
     */
    static boolean checkMiddleLine(String text){
        if (checkFirstLine(text)){
            for (int i = 2; i < text.length(); i++){
                if (text.startsWith(DIRECTORY_BEGIN, i)){
                    return false;
                }
                if (! Character.isWhitespace(text.charAt(i))){
                    return true;
                }
            }
        }
        return false;
    }

    private final CacheKeyOptional<FormattedSpan> cacheFormatted;
    private final CacheKeyOptional<CatalogueIdentity> cacheId;
    private final CacheKeyMain<String> cacheLookup;
    private final CacheKeyMain<Integer> cacheNote;
    private final CacheKeyMain<Boolean> cacheFirst;

    public LinedSpanNote(List<Span> children){
        super(children);
        cacheFormatted = new CacheKeyOptional<>(FormattedSpan.class);
        cacheId = new CacheKeyOptional<>(CatalogueIdentity.class);
        cacheLookup = CacheKeyMain.stringKey();
        cacheNote = CacheKeyMain.integerKey();
        cacheFirst = CacheKeyMain.booleanKey();
    }

    public Optional<FormattedSpan> getFormattedSpan(){
        return getLocalCache(cacheFormatted, () -> spanFromLast(
            FormattedSpan.class));
    }

    public String getLookupText(){
        return getLocalCache(cacheLookup, () ->
            spanFromFirst(DirectorySpan.class)
                .map(span -> CURLY_CITE + span.getLookupText() + CURLY_END)
                .orElse("")
        );
    }

    Optional<CatalogueIdentity> buildId(){
        return getLocalCache(cacheId, () -> spanFromFirst(DirectorySpan.class)
            .map(span -> span.buildId())
        );
    }

    public boolean isFirstLine(){
        return getDocCache(cacheFirst, () -> getParent().indexOf(this) == 0);
    }

    @Override
    public int getNoteTotal(){
        return getLocalCache(cacheNote, () -> getFormattedSpan()
            .map(span -> span.getTotalCount())
            .orElse(0)
        );
    }

    @Override
    protected SetupParser getParser(String text){
        return (
            (isDocumentFirst()? checkFirstLine(text) : checkMiddleLine(text)) &&
            AuxiliaryChecker.checkLineEnd(text, isDocumentLast())
        )? LinedParseRest.NOTE: null;
    }
}
