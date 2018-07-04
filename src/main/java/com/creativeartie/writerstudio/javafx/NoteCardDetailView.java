package com.creativeartie.writerstudio.javafx;

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

    NoteCardDetailView(){
        setCollapsible(false);

        showNote = new SimpleObjectProperty<>(this, "showNote");
    }

    /// %Part 3: Setup Properties

    public void setupProperties(WriterSceneControl control){
        setupChildern(control);
    }

    protected abstract void setupChildern(WriterSceneControl control);

    /// %Part 4: Properties

    /// %Part 4.1: Show Note

    public ObjectProperty<NoteCardSpan> showNoteProperty(){
        return showNote;
    }

    public NoteCardSpan getShowNote(){
        return showNote.getValue();
    }

    public void setShowNote(NoteCardSpan value){
        showNote.setValue(value);
    }

    /// %Part 5: Get Child Methods
}
