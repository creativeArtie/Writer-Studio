package com.creativeartie.writerstudio.window;

import java.util.*;
import java.util.Optional;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.scene.control.cell.*;

import com.creativeartie.writerstudio.main.*;
import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.resource.*;

import com.google.common.base.*;
import com.google.common.collect.*;

/**
 * Stores a list of user notes, hypernotes.
 */
public class TableNoteData implements TableData{
    private ReadOnlyObjectWrapper<Optional<CatalogueIdentity>> noteId;
    private ReadOnlyStringWrapper noteLookupText;
    private ReadOnlyIntegerWrapper noteLocation;
    private ReadOnlyBooleanWrapper noteBookmark;
    private ReadOnlyObjectWrapper<Optional<FormatSpanMain>> noteData;
    private LinedSpanPointNote targetNote;

    TableNoteData(LinedSpanPointNote span){
        
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

    public ReadOnlyObjectProperty<Optional<FormatSpanMain>> noteDataProperty(){
        return noteData.getReadOnlyProperty();
    }

    public Optional<FormatSpanMain> getnoteData(){
        return noteData.getValue();
    }

    @Override
    public LinedSpanPointNote getTargetSpan(){
        return targetNote;
    }
}