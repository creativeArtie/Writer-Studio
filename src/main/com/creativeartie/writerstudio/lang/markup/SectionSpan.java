package com.creativeartie.writerstudio.lang.markup;

import java.util.*; // List
import java.util.Optional;
import com.google.common.base.*; // Splitter
import com.google.common.collect.*; // ImmtableList

import com.creativeartie.writerstudio.lang.*; // (many)

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.Checker.*;

/**
 * Section with notes and content
 */
public abstract class SectionSpan extends SpanBranch {

    /** Check if the edit can become a child of the current {@link Span}.
     *
     * @param text
     *      the new text
     * @param allowed
     *      the high allowed level
     * @param heading
     *      heading or not (aka outline)
     * @return answer
     */
    static boolean allowChild(String text, int allowed, boolean heading){
        List<String> starters = LEVEL_STARTERS.get(LinedParseLevel.OUTLINE);
        for (int i = LEVEL_MAX - 1; i >= 0; i--){
            if (text.startsWith(starters.get(i))){
                /// if (text == outline)
                return heading? true : allowed < i + 1;
            }
        }

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

    private final CacheKeyOptional<LinedSpanLevelSection> cacheHeading;
    private final CacheKeyMain<Integer> cacheLevel;
    private final CacheKeyMain<EditionType> cacheEdition;
    private final CacheKeyList<NoteCardSpan> cacheNotes;
    private final SectionParser spanReparser;

    SectionSpan(List<Span> children, SectionParser reparser){
        super(children);
        spanReparser = reparser;

        cacheHeading = new CacheKeyOptional<>(LinedSpanLevelSection.class);
        cacheLevel = CacheKeyMain.integerKey();
        cacheEdition = new CacheKeyMain<>(EditionType.class);
        cacheNotes = new CacheKeyList<>(NoteCardSpan.class);
    }

    public final Optional<LinedSpanLevelSection> getHeading(){
        return getLocalCache(cacheHeading, () -> spanAtFirst(
            LinedSpanLevelSection.class));
    }

    public final int getLevel(){
        return getLocalCache(cacheLevel, () -> getHeading()
            .map(span -> span.getLevel()).orElse(1));
    }

    public final EditionType getEdition(){
        return getLocalCache(cacheEdition, () -> getHeading()
            .map(span -> span.getEdition()).orElse(EditionType.NONE));
    }

    protected final <T> List<T> getChildren(Class<T> getting){
        ImmutableList.Builder<T> builder = ImmutableList.builder();
        for (Span span: this){
            if (getting.isInstance(span)){
                builder.add(getting.cast(span));
            }
        }
        return builder.build();
    }

    public final List<NoteCardSpan> getNotes(){
        return getLocalCache(cacheNotes, () -> {
            ImmutableList.Builder<NoteCardSpan> lines = ImmutableList.builder();
            for (Span child: this){
                if (child instanceof NoteCardSpan){
                    lines.add((NoteCardSpan) child);
                }
            }
            return lines.build();
        });
    }

    @Override
    protected final SetupParser getParser(String text){
        checkNotNull(text, "text");
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

    /** Check if span is parseable locally with information in the child class.
     *
     * The child classes will call {@link #allowChild(String, int, boolean)}
     * accept for heading 1.
     *
     * @param text
     *      new text
     * @return anwser
     */
    protected abstract boolean checkStart(String text);

    public abstract List<LinedSpan> getLines();

    @Override
    public String toString(){
        StringBuilder text = new StringBuilder("[\n");
        boolean isFirst = true;
        for (Span span: this){
            if (isFirst){
                isFirst = false;
                text.append("\t");
            } else {
                text.append(",");
            }
            String base = span.toString();
            text.append(base.replace("\n", "\n\t"));
        }
        text.append("]\n");
        return text.toString();
    }
}