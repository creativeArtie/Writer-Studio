package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.util.ParameterChecker.*;

/** A heading or content note line */
public class LinedSpanNote extends LinedSpan{

    /** Partially check for first line of a {@link NoteCard}.
     *
     * This will not check the ending part of the line.
     *
     * @param text
     *      new text
     * @param answer
     * @see #checkMiddleLine(String)
     * @see #getParse(String)
     * @see NoteCardSpan#getParse(String)
     */
    static boolean checkFirstLine(String text){
        argumentNotNull(text, "text");
        return text.startsWith(LINED_NOTE);
    }

    /** Partially check for middle line of a {@link NoteCard}.
     *
     * This will not check the ending part of the line.
     *
     * @param text
     *      new text
     * @param answer
     */
    static boolean checkMiddleLine(String text){
        argumentNotNull(text, "text");
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

    /** Creates a {@linkplain LinedSpanNote}.
     *
     * @param children
     *      span children
     * @see LinedParseRest#NOTE
     */
    LinedSpanNote(List<Span> children){
        super(children);
        cacheFormatted = new CacheKeyOptional<>(FormattedSpan.class);
        cacheId = new CacheKeyOptional<>(CatalogueIdentity.class);
        cacheLookup = CacheKeyMain.stringKey();
        cacheNote = CacheKeyMain.integerKey();
        cacheFirst = CacheKeyMain.booleanKey();
    }

    /** Gets the formatted content.
     *
     * @return answer
     */
    public Optional<FormattedSpan> getFormattedSpan(){
        return getLocalCache(cacheFormatted, () -> spanFromLast(
            FormattedSpan.class));
    }

    /** Gets the user reference help text.
     *
     * @return answer
     */
    public String getLookupText(){
        return getLocalCache(cacheLookup, () ->
            spanFromFirst(DirectorySpan.class)
                .map(s -> CURLY_CITE + s.getLookupText() + CURLY_END)
                .orElse("")
        );
    }

    /** Shortcut for {@link DirectorySpan#buildId}.
     *
     * @return answer
     */
    Optional<CatalogueIdentity> buildId(){
        return getLocalCache(cacheId, () -> spanFromFirst(DirectorySpan.class)
            .map(span -> span.buildId())
        );
    }

    @Override
    public int getNoteTotal(){
        return getLocalCache(cacheNote, () -> getFormattedSpan()
            .map(span -> span.getGrandTotal())
            .orElse(0)
        );
    }

    @Override
    protected SetupParser getParser(String text){
        argumentNotNull(text, "text");
        return (
            /// First or middle line difference check ...
            (isDocumentFirst()? checkFirstLine(text) : checkMiddleLine(text)) &&
            /// ... and line end check
            AuxiliaryChecker.checkLineEnd(text, isDocumentLast())
        )? LinedParseRest.NOTE: null;
    }
}
