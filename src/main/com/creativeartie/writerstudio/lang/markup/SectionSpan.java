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
        for (int i = LEVEL_MAX; i > 0; i--){
            if (text.startsWith(getLevelToken(LinedParseLevel.OUTLINE, i))){
                /// if (text == outline)
                return heading? true : allowed < i;
            }
        }

        for (int i = LEVEL_MAX; i > 0; i--){
            if (text.startsWith(getLevelToken(LinedParseLevel.HEADING, i))){
                /// if (text == heading)
                return heading? allowed < i: false;
            }
        }
        /// if (text == others)
        return true;
    }

    private Optional<Optional<LinedSpanLevelSection>> cacheHeading;
    private Optional<Integer> cacheLevel;
    private Optional<EditionType> cacheEdition;
    private Optional<Optional<CatalogueIdentity>> cacheId;
    private Optional<List<NoteCardSpan>> cacheNotes;
    private final SectionParser spanReparser;

    SectionSpan(List<Span> children, SectionParser reparser){
        super(children);
        spanReparser = reparser;
    }

    public final Optional<LinedSpanLevelSection> getHeading(){
        cacheHeading = getCache(cacheHeading, () -> spanAtFirst(
            LinedSpanLevelSection.class));
        return cacheHeading.get();
    }

    public final int getLevel(){
        cacheLevel = getCache(cacheLevel, () -> getHeading()
            .map(span -> span.getLevel()).orElse(1));
        return cacheLevel.get();
    }

    public final EditionType getEdition(){
        cacheEdition = getCache(cacheEdition, () -> getHeading()
            .map(span -> span.getEdition()).orElse(EditionType.NONE));
        return cacheEdition.get();
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
        cacheNotes = getCache(cacheNotes, () -> {
            ImmutableList.Builder<NoteCardSpan> lines = ImmutableList.builder();
            for (Span child: this){
                if (child instanceof NoteCardSpan){
                    lines.add((NoteCardSpan) child);
                }
            }
            return lines.build();
        });
        return cacheNotes.get();
    }

    @Override
    protected final SetupParser getParser(String text){
        checkNotNull(text, "text");
        if (AuxiliaryChecker.checkSectionEnd(text, isLast()) &&
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
    protected void childEdited(){
        cacheHeading = Optional.empty();
        cacheLevel = Optional.empty();
        cacheEdition = Optional.empty();
        cacheNotes = Optional.empty();
    }

    @Override
    protected void docEdited(){
        cacheId = Optional.empty();
    }

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