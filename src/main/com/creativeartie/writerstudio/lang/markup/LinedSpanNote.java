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

    private Optional<Optional<FormattedSpan>> cacheFormatted;
    private Optional<Optional<CatalogueIdentity>> cacheId;
    private Optional<String> cacheLookup;
    private Optional<Integer> cacheNote;
    private Optional<Boolean> cacheFirst;

    public LinedSpanNote(List<Span> children){
        super(children);
    }

    public Optional<FormattedSpan> getFormattedSpan(){
        cacheFormatted = getCache(cacheFormatted, () -> spanFromLast(
            FormattedSpan.class));
        return cacheFormatted.get();
    }

    public String getLookupText(){
        cacheLookup = getCache(cacheLookup, () ->
            spanFromFirst(DirectorySpan.class)
                .map(span -> CURLY_CITE + span.getLookupText() + CURLY_END)
                .orElse("")
        );
        return cacheLookup.get();
    }

    Optional<CatalogueIdentity> buildId(){
        cacheId = getCache(cacheId, () -> spanFromFirst(DirectorySpan.class)
            .map(span -> span.buildId())
        );
        return cacheId.get();
    }

    public boolean isFirstLine(){
        cacheFirst = getCache(cacheFirst, () -> getParent().indexOf(this) == 0);
        return cacheFirst.get();
    }

    @Override
    public int getNoteTotal(){
        cacheNote = getCache(cacheNote, () -> getFormattedSpan()
            .map(span -> span.getTotalCount())
            .orElse(0)
        );
        return cacheNote.get();
    }

    @Override
    protected SetupParser getParser(String text){
        return (
            (isFirst()? checkFirstLine(text) : checkMiddleLine(text)) &&
            AuxiliaryChecker.checkLineEnd(text, isLast())
        )? LinedParseRest.NOTE: null;
    }

    @Override
    protected void childEdited(){
        cacheFormatted = Optional.empty();
        cacheId = Optional.empty();
        cacheLookup = Optional.empty();
        cacheNote = Optional.empty();
    }

    @Override
    protected void docEdited(){
        cacheFirst = Optional.empty();
    }
}
