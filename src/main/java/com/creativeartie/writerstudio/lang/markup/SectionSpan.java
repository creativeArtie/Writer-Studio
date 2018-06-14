package com.creativeartie.writerstudio.lang.markup;

import java.util.*;
import java.util.Optional;
import com.google.common.base.*;
import com.google.common.collect.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** Section with notes and content */
public abstract class SectionSpan extends SpanBranch {

    /// %Part 1: Constructor & Fields ##########################################
    private final CacheKeyOptional<LinedSpanLevelSection> cacheHeading;
    private final CacheKeyMain<EditionType> cacheEdition;
    private final CacheKeyMain<String> cacheDetail;
    private final CacheKeyList<NoteCardSpan> cacheNotes;
    private final CacheKeyList<LinedSpan> cacheLines;
    private final SectionParser spanReparser;
    private final CacheKeyMain<Integer> cachePublish;
    private final CacheKeyMain<Integer> cacheNote;

    /** Creates a {@linkplain SectionSpan}.
     *
     * @param children
     *      span children
     * @param reparser
     *      span reparser
     */
    SectionSpan(List<Span> children, SectionParser reparser){
        super(children);
        spanReparser = argumentNotNull(reparser, "reparser");

        cacheHeading = new CacheKeyOptional<>(LinedSpanLevelSection.class);
        cacheEdition = new CacheKeyMain<>(EditionType.class);
        cacheDetail = CacheKeyMain.stringKey();
        cacheNotes = new CacheKeyList<>(NoteCardSpan.class);
        cacheLines = new CacheKeyList<>(LinedSpan.class);

        cachePublish = CacheKeyMain.integerKey();
        cacheNote = CacheKeyMain.integerKey();
    }

    /// %Part 2: Constant gets #################################################

    /** Gets the section headings.
     *
     * @return answer
     */
    public final Optional<LinedSpanLevelSection> getHeading(){
        return getLocalCache(cacheHeading, () -> spanAtFirst(
            LinedSpanLevelSection.class));
    }

    /** Gets the section level.
     *
     * @return answer
     */
    public abstract int getLevel();

    /** Gets the section edtion type.
     *
     * @return answer
     */
    public final EditionType getEditionType(){
        return getLocalCache(cacheEdition, () -> getHeading()
            .map(s -> s.getEditionType()).orElse(EditionType.NONE));
    }

    /** Gets the section edtion detail.
     *
     * @return answer
     */
    public final String getEditionDetail(){
        return getLocalCache(cacheDetail, () -> getHeading()
            .map(s -> s.getEditionDetail()).orElse(""));
    }

    /** Gets the lines in a section
     *
     * @return answer
     */
    public final List<LinedSpan> getLines(){
        return getLocalCache(cacheLines, () -> getChildren(LinedSpan.class));
    }

    /** Gets the section notes.
     *
     * @return answer
     */
    public final List<NoteCardSpan> getNotes(){
        return getLocalCache(cacheNotes, () -> getChildren(NoteCardSpan.class));
    }

    /** Gets the section publish word count.
     *
     * @return answer
     */
    public final int getPublishCount(){
        return getLocalCache(cachePublish,
            () -> getLines().stream().mapToInt(s -> s.getPublishTotal()).sum()
        );
    }

    /** Gets the section + children publish word count.
     *
     * @return answer
     */
    public abstract int getPublishTotal();


    /** Gets the section note word count.
     *
     * @return answer
     */
    public final int getNoteCount(){
        return getLocalCache(cacheNote, () ->
            getLines().stream().mapToInt(s -> s.getNoteTotal()).sum() +
            getNotes().stream().mapToInt(n -> n.getNoteTotal()).sum()
        );
    }

    public abstract int getNoteTotal();

    /// %Part 3: Get Parser ####################################################

    @Override
    protected final SetupParser getParser(String text){
        argumentNotNull(text, "text");
        if (AuxiliaryChecker.checkSectionEnd(text, isDocumentLast()) &&
                checkStart(text)){

            /// Line per line checking
            boolean checking = true;
            for (String str: Splitter.on(LINED_END).split(text)){
                if (checking){
                    /// already checked or last line ends with escape
                    checking = false;
                    continue;
                }
                if (! allowChild(str, getLevel(), this instanceof
                        SectionSpanHead)){
                    /// not descendant
                    return null;
                }
                if (str.endsWith(TOKEN_ESCAPE)){
                    checking = true;
                }
            }

        /// returns
        } else {
            return null;
        }
        return spanReparser;
    }

    /** Check if the first line can be this span's child.
     *
     * The child classes will call {@link #allowChild(String, int, boolean)}
     * accept for heading 1.
     *
     * @param text
     *      new text
     * @return anwser
     * @see #getParser(String)
     */
    protected abstract boolean checkStart(String text);

    /** Check if the line can be this span's child.
     *
     * @param text
     *      new text
     * @param allowed
     *      high allowed level
     * @param heading
     *      is heading
     * @return answer
     * @see #checkStart(String)
     * @see #getPaser(String)
     */
    static final boolean allowChild(String text, int allowed, boolean heading){
        argumentNotNull(text, "text");
        argumentClose(allowed, "allowed", 0, LEVEL_MAX - 1);

        /// check outline
        List<String> starters = LEVEL_STARTERS.get(LinedParseLevel.OUTLINE);
        for (int i = LEVEL_MAX - 1; i >= 0; i--){
            if (text.startsWith(starters.get(i))){
                /// if (text == outline)
                return heading? true : allowed < i + 1;
            }
        }

        /// if (outline) { don't check heading; }
        if (! heading) return false;

        /// check heading
        starters = LEVEL_STARTERS.get(LinedParseLevel.HEADING);
        for (int i = LEVEL_MAX - 1; i >= 0; i--){
            if (text.startsWith(starters.get(i))){
                /// if (text == heading)
                return heading? allowed < i + 1: false;
            }
        }
        /// if (text == others)
        return true;
    }

    /// %Part 4: To String Function ############################################

    @Override
    public String toString(){
        StringBuilder text = new StringBuilder("[\n");
        boolean isFirst = true;
        for (Span span: this){
            if (isFirst){
                /// add tab for line
                isFirst = false;
                text.append("\t");
            } else {
                text.append(",");
            }
            String base = span.toString();
            /// add a tab to each line end (added by LinedSpan/SectionSpan)
            text.append(base.replace("\n", "\n\t"));
        }
        text.append("]\n");
        return text.toString();
    }
}
