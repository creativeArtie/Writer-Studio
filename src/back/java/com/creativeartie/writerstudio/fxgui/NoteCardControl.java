package com.creativeartie.writerstudio.fxgui;

import java.util.*;
import javafx.beans.binding.*;
import javafx.collections.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;

class NoteCardControl extends NoteCardView{

    @Override
    protected void addBindings(){
        getNoteTable().getSelectionModel().selectedItemProperty().addListener(
            (d, o, n) -> getNoteCardDetail().setShowNote(n));
    }

    public NoteCardControl setWritingTextProperty(
            ObjectProperty<WritingText> text){
        text.addListener((d, o, n) -> {
            if (doc != null){
                doc.addDocEdited(span -> updateCards());
                updateCards();
            }
        });
        return this;
    }

    public NoteCardControl setLastSelectedProperty(
            ObjectProperty<SpanBranch> selected){
        getNoteCardDetail().goToNoteProperty().addListener(
            (d, o, n) -> selected.setValue(n);
        );
        return this;
    }

    private void updateCards(){
        WritingText text = getWritingText();
        if (text == null){
            getNoteTable().setItems(FXCollections.emptyObservableList());
            return;
        }
        CatalogueMap map = text.getCatalogue();
        TreeSet<SpanBranch> set = map.getIds(AuxiliaryData.TYPE_NOTE);
        set.addAll(map.getIds(AuxiliaryData.TYPE_RESEARCH));
        ArrayList<NoteCardData> data = new ArrayList<>();

        NoteCardSpan last = getShowNote();
        NoteCardData selected = null;
        for(SpanBranch span: set){
            NoteCardData item = new NoteCardData((NoteCardSpan)span);
            data.add(item);
            if (span == last){
                selected = item;
            }
        }
        getNoteTable().setItems(FXCollections.observableList(data));
        if (selected != null){
            getNoteTable().getSelectionModel().select(selected);
        }
    }
}
