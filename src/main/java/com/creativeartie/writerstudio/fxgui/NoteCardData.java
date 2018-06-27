package com.creativeartie.writerstudio.fxgui;

import java.util.*;
import javafx.beans.property.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;

class NoteCardData{
    private final ReadOnlyObjectWrapper<Optional<CatalogueIdentity>> noteId;
    private final ReadOnlyObjectWrapper<SectionSpan> noteSection;
    private final ReadOnlyObjectWrapper<Optional<FormattedSpan>> noteTitle;
    private final NoteCardSpan targetNote;

    NoteCardData(NoteCardSpan span){
        noteId = new ReadOnlyObjectWrapper<>(span.getSpanIdentity()
            .filter(id -> id.getMain().equals(AuxiliaryData.TYPE_RESEARCH)));
        noteSection = new ReadOnlyObjectWrapper<>(span
            .getParent(SectionSpanHead.class)
            .map(found -> (SectionSpan) found)
            .orElse(null));
        noteTitle = new ReadOnlyObjectWrapper<>(span.getTitle());
        targetNote = span;
    }

    public ReadOnlyObjectProperty<Optional<CatalogueIdentity>> noteIdProperty(){
        return noteId.getReadOnlyProperty();
    }

    public Optional<CatalogueIdentity> getNoteId(){
        return noteId.getValue();
    }

    public ReadOnlyObjectProperty<SectionSpan> noteSectionProperty(){
        return noteSection.getReadOnlyProperty();
    }

    public SectionSpan getNoteSection(){
        return noteSection.getValue();
    }

    public ReadOnlyObjectProperty<Optional<FormattedSpan>> noteTitleProperty(){
        return noteTitle.getReadOnlyProperty();
    }

    public Optional<FormattedSpan> getNoteTitle(){
        return noteTitle.getValue();
    }

    public NoteCardSpan getTargetSpan(){
        return targetNote;
    }
}
