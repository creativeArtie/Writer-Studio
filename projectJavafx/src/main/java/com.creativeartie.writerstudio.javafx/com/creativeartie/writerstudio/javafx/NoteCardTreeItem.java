package com.creativeartie.writerstudio.javafx;

import java.util.*;
import javafx.collections.*;
import javafx.beans.property.*;
import javafx.scene.control.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;

class NoteCardTreeItem<T> extends TreeItem<T>{

    private SimpleListProperty<NoteCardSpan> noteList;

    NoteCardTreeItem(){
        noteList = new SimpleListProperty<>(this, "noteList",
            FXCollections.observableArrayList());
    }

    NoteCardTreeItem(T value){
        super(value);
        noteList = new SimpleListProperty<>(this, "noteList",
            FXCollections.observableArrayList());
    }

    void addNoteCard(NoteCardSpan value){
        noteList.add(value);
    }

    void addNoteCards(List<NoteCardSpan> values){
        noteList.addAll(values);
    }

    public ListProperty<NoteCardSpan> noteListProperty(){
        return noteList;
    }

    public int getSize(){
        return noteList.size();
    }

    public ObservableList<NoteCardSpan> getNoteList(){
        return noteList.getValue();
    }

    public void setNoteList(Collection<NoteCardSpan> value){
        noteList.clear();
        noteList.addAll(value);
    }
}
