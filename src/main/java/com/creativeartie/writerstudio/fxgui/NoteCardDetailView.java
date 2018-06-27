package com.creativeartie.writerstudio.fxgui;

import java.util.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.beans.property.*;

import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.resource.*;

abstract class NoteCardDetailView extends TitledPane{
    /// %Part 1: Constructor and Class Fields

    private SimpleObjectProperty<NoteCardSpan> showNote;
    private ReadOnlyObjectWrapper<NoteCardSpan> goToNote;

    NoteCardDetailView(){
        setCollapsible(false);

        showNote = new SimpleObjectProperty<>(this, "showNote");
        goToNote = new ReadOnlyObjectWrapper<>(this, "goToNote");

        addBindings();

        clearContent();
    }

    /// %Part 2: Layout

    /// %Part 3: Listener Methods

    protected abstract void addBindings();

    protected abstract void clearContent();

    /// %Part 4: Properties

    /// %Part 4.1: Show Note

    public ObjectProperty<NoteCardSpan> showNoteProperty(){
        return showNote;
    }

    public SimpleObjectProperty<NoteCardSpan> getShowNoteProperty(){
        return showNote;
    }

    public NoteCardSpan getShowNote(){
        return showNote.getValue();
    }

    public void setShowNote(NoteCardSpan value){
        showNote.setValue(value);
        goToNote.setValue(null);
    }

    /// %Part 4.2: Go To Note

    public ReadOnlyObjectProperty<NoteCardSpan> goToNoteProperty(){
        return goToNote.getReadOnlyProperty();
    }

    public ReadOnlyObjectWrapper<NoteCardSpan> getGoToNoteProperty(){
        return goToNote;
    }

    public NoteCardSpan getGoToNote(){
        return goToNote.getValue();
    }

    /// %Part 5: Get Child Methods
}
