package com.creativeartie.jwriter.lang.markup;

import java.util.*;
import java.util.Optional;
import com.google.common.base.*;
import com.google.common.collect.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;

/**
 * Section with notes and content
 */
abstract class SectionSpan extends SpanBranch {
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

    protected final  SectionParser getParser(){
        return spanReparser;
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

    protected <T> List<T> getChildren(Class<T> getting){
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

    protected final boolean canParse(String text, SectionParser[] values){

        boolean check = true;
        for (String line : Splitter.on(LINED_END)
                .split(text.replace(CHAR_ESCAPE + LINED_END, ""))
            ){
            for(SectionParser value: values){
                if (line.startsWith(value.getStarter())){
                    return false;
                }
                if (value == spanReparser){
                    check = false;
                }
                if (spanReparser instanceof SectionParseScene){
                    for (String str: getLevelTokens(LinedParseLevel.HEADING)){
                        if (line.startsWith(str)){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

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
}