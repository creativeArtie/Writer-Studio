package com.creativeartie.writer.javafx;

import java.util.*;
import javafx.beans.property.*;

import com.creativeartie.writer.lang.*;
import com.creativeartie.writer.lang.markup.*;

/**
 * Stores a list of user notes, hypernotes.
 */
public class DataInputNote implements DataInput{
    private ReadOnlyObjectWrapper<Optional<CatalogueIdentity>> noteId;
    private ReadOnlyStringWrapper noteLookupText;
    private ReadOnlyIntegerWrapper noteLocation;
    private ReadOnlyObjectWrapper<Optional<FormattedSpan>> noteData;
    private LinedSpanPointNote targetNote;

    DataInputNote(LinedSpanPointNote span){

        noteId = new ReadOnlyObjectWrapper<>(span.getSpanIdentity());
        noteLookupText = new ReadOnlyStringWrapper(span.getLookupText());
        noteLocation = new ReadOnlyIntegerWrapper(span.getStartLine());
        noteData = new ReadOnlyObjectWrapper<>(span.getFormattedSpan());
        targetNote = span;
    }

    public ReadOnlyObjectProperty<Optional<CatalogueIdentity>> noteIdProperty(){
        return noteId.getReadOnlyProperty();
    }

    public Optional<CatalogueIdentity> getNoteId(){
        return noteId.getValue();
    }

    public ReadOnlyStringProperty noteLookupTextProperty(){
        return noteLookupText.getReadOnlyProperty();
    }

    public String getLookupText(){
        return noteLookupText.getValue();
    }

    public ReadOnlyIntegerProperty noteLocationProperty(){
        return noteLocation.getReadOnlyProperty();
    }

    public int getNoteLocation(){
        return noteLocation.getValue();
    }

    public ReadOnlyObjectProperty<Optional<FormattedSpan>> noteDataProperty(){
        return noteData.getReadOnlyProperty();
    }

    public Optional<FormattedSpan> getnoteData(){
        return noteData.getValue();
    }

    @Override
    public LinedSpanPointNote getTargetSpan(){
        return targetNote;
    }
}
