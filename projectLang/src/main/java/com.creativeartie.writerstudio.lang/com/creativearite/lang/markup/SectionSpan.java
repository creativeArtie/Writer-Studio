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

    /// %Part 3: To String Function ############################################

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
