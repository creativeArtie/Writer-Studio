package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;

/**
 * Line that store note heading or details. Represented in design/ebnf.txt as
 * {@code LinedNoteHead}, and {@code LinedNoteLine}.
 */
public class LinedSpanNote extends LinedSpan{

    private Optional<Optional<FormatSpanMain>> cacheFormatted;
    private Optional<Optional<CatalogueIdentity>> cacheId;
    private Optional<String> cacheLookup;
    private Optional<Integer> cacheNote;
    private Optional<Boolean> cacheFirst;

    public LinedSpanNote(List<Span> children){
        super(children);
    }

    public Optional<FormatSpanMain> getFormattedSpan(){
        cacheFormatted = getCache(cacheFormatted, () -> spanFromLast(
            FormatSpanMain.class));
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
        return (isFirst()? canParse(text) : canParseWithoutId(text)) &&
            AuxiliaryChecker.checkLineEnd(isLast(), text)?
            LinedParseRest.NOTE: null;
    }

    static boolean canParse(String text){
        return text.startsWith(LINED_NOTE);
    }

    static boolean canParseWithoutId(String text){
        if (canParse(text)){
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
