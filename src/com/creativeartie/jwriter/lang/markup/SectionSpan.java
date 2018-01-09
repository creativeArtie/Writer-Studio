package com.creativeartie.jwriter.lang.markup;

import java.util.*;
import com.google.common.collect.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;

/**
 * Section with notes and content
 */
abstract class SectionSpan extends SpanBranch implements Catalogued{
    private Optional<Optional<SectionSpan>> cacheUpper;
    private Optional<Optional<LinedSpanLevelSection>> cacheHeading;
    private Optional<Integer> cacheLevel;
    private Optional<EditionType> cacheEdition;
    private Optional<Integer> cachePublish;
    private Optional<Integer> cacheNote;
    private Optional<Optional<CatalogueIdentity>> cacheId;
    private Optional<List<LinedSpan>> cacheLines;
    private Optional<List<NoteCardSpan>> cacheNotes;

    SectionSpan(List<Span> children){
        super(children);
    }

    public final Optional<SectionSpan> getUpperLevel(){
        cacheUpper = getCache(cacheUpper, () -> Optional.of(getParent())
            .filter(span -> span instanceof SectionSpan)
            .map(span -> (SectionSpan) span));
        return cacheUpper.get();
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

    public final int getPublishTotal(){
        cachePublish = getCache(cachePublish, () -> {
            int count = 0;
            for (Span span: this){
                count += span instanceof LinedSpan?
                    ((LinedSpan)span).getPublishTotal():
                    (span instanceof NoteCardSpan?
                        0: ((SectionSpan)span).getPublishTotal());
            }
            return count;
        });
        return cachePublish.get();
    }

    @Override
    public final Optional<CatalogueIdentity> getSpanIdentity(){
        cacheId = getCache(cacheId, () ->
            Optional.of(new CatalogueIdentity(TYPE_SECTION, this)));
        return cacheId.get();
    }

    @Override
    public final boolean isId(){
        return true;
    }

    public final int getNoteTotal(){
        cacheNote = getCache(cacheNote, () -> {
            int count = 0;
            for (Span span: this){
                count += span instanceof LinedSpan?
                    ((LinedSpan)span).getNoteTotal():
                    (span instanceof NoteCardSpan?
                        0: ((SectionSpan)span).getNoteTotal());
            }
            return count;
        });
        return cacheNote.get();
    }

    public final List<LinedSpan> getLines(){
        cacheLines = getCache(cacheLines, () -> {
            ImmutableList.Builder<LinedSpan> lines = ImmutableList.builder();
            for (Span child: this){
                if (child instanceof LinedSpan){
                    lines.add((LinedSpan) child);
                }
            }
            return lines.build();
        });
        return cacheLines.get();
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
    protected void childEdited(){
        cacheUpper = Optional.empty();
        cacheHeading = Optional.empty();
        cacheLevel = Optional.empty();
        cacheEdition = Optional.empty();
        cacheNote = Optional.empty();
        cachePublish = Optional.empty();
        cacheLines = Optional.empty();
        cacheNotes = Optional.empty();
    }

    @Override
    protected void docEdited(){
        cacheId = Optional.empty();
    }
}