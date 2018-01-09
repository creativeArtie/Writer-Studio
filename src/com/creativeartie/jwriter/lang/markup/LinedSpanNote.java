package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;

/**
 * Line that store note heading or details. Represented in design/ebnf.txt as
 * {@code LinedNoteHead}, and {@code LinedNoteLine}.
 */
public class LinedSpanNote extends LinedSpan{

    private Optional<Optional<FormatSpanMain>> cacheFormatted;
    private Optional<Optional<CatalogueIdentity>> cacheId;
    private Optional<Integer> cacheNote;

    public LinedSpanNote(List<Span> children){
        super(children);
    }

    public Optional<FormatSpanMain> getFormattedSpan(){
        cacheFormatted = getCache(cacheFormatted, () -> spanFromLast(
            FormatSpanMain.class));
        return cacheFormatted.get();
    }

    Optional<CatalogueIdentity> buildId(){
        cacheId = getCache(cacheId, () -> spanFromFirst(DirectorySpan.class)
            .map(span -> span.buildId())
        );
        return cacheId.get();
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
        return text.startsWith(LINED_NOTE) &&
            AuxiliaryChecker.checkLineEnd(isLast(), text)?
            LinedParseRest.NOTE: null;
    }

    static boolean canParseWithoutId(String text, boolean isLast){
        if (text.startsWith(LINED_NOTE)){
            for (int i = 2; i < text.length(); i++){
                if (! Character.isWhitespace(text.charAt(i))){
                    return ! text.startsWith(DIRECTORY_BEGIN, i);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    protected void childEdited(){
        cacheFormatted = Optional.empty();
        cacheId = Optional.empty();
        cacheNote = Optional.empty();
    }

    @Override
    protected void docEdited(){}
}
