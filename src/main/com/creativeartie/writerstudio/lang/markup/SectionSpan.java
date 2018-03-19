package com.creativeartie.writerstudio.lang.markup;

import java.util.*;
import java.util.Optional;
import com.google.common.base.*;
import com.google.common.collect.*;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.Checker.*;

/**
 * Section with notes and content
 */
public abstract class SectionSpan extends SpanBranch {

    static boolean allowChild(String text, int parent, boolean heading){
        for (int i = LEVEL_MAX; i > 0; i--){
            if (text.startsWith(getLevelToken(LinedParseLevel.OUTLINE, i))){
                return heading || parent < i;
            }
        }

        for (int i = LEVEL_MAX; i > 0; i--){
            if (text.startsWith(getLevelToken(LinedParseLevel.HEADING, i))){
                return heading? parent < i: false;
            }
        }
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
        if (AuxiliaryChecker.checkSectionEnd(isLast(), text) &&
                checkStart(text)){
            boolean isFirst = true;
            for (String str: Splitter.on(LINED_END).split(text)){
                if (isFirst){
                    isFirst = false;
                    continue;
                }
                if (! allowChild(str, getLevel(),
                        this instanceof SectionSpanHead)){
                    return null;
                }
            }
        } else {
            return null;
        }
        return spanReparser;
    }

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