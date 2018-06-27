package com.creativeartie.writerstudio.fxgui;

import java.util.*;
import javafx.beans.binding.*;
import javafx.collections.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;

class NoteCardControl extends NoteCardView{

    @Override
    protected void addBindings(){
        writingTextProperty().addListener((d, o, n) -> loadCards(n));
        showNoteProperty().bind(Bindings.createObjectBinding(this::bindShowNote,
            getNoteTable().getSelectionModel().selectedItemProperty()
        ));
        getNoteCardDetail().showNoteProperty().bind(showNoteProperty());
    }

    private void loadCards(WritingText doc){
        if (doc != null){
            doc.addDocEdited(span -> updateCards());
            updateCards();
        }
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

    private NoteCardSpan bindShowNote(){
        List<NoteCardData> selecteds = getNoteTable().getSelectionModel()
            .getSelectedItems();
        return selecteds.isEmpty()? null: selecteds.get(0).getTargetSpan();
    }
}
