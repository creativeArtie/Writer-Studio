package com.creativeartie.jwriter.window;

import java.util.*;
import java.util.Optional;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.scene.control.cell.*;

import com.creativeartie.jwriter.main.*;
import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.resource.*;

import com.google.common.base.*;
import com.google.common.collect.*;


public class NoteCardControl extends NoteCardView{
    private WritingText currentDoc;
    public void loadCards(WritingText doc){
        currentDoc = doc;
        doc.addUpdater(span -> updateCards());
        updateCards();
    }

    public void updateCards(){
        CatalogueMap map = currentDoc.getCatalogue();
        TreeSet<SpanBranch> set = map.getIds(AuxiliaryData.TYPE_COMMENT);
        set.addAll(map.getIds(AuxiliaryData.TYPE_NOTE));
        ArrayList<NoteCardData> data = new ArrayList<>();

        NoteCardSpan last = Optional
            .ofNullable(getNoteTable().getSelectionModel().getSelectedItem())
            .map(found -> found.getTargetSpan())
            .orElse(null);
        Optional<NoteCardData> selected = Optional.empty();
        for(SpanBranch span: set){
            NoteCardData item = new NoteCardData((NoteCardSpan)span);
            data.add(item);
            if (span == last){
                selected = Optional.of(item);
            }
        }
        getNoteTable().setItems(FXCollections.observableList(data));
        selected.ifPresent(item -> getNoteTable().getSelectionModel()
            .select(item));
    }

    @Override
    protected void noteSelected(NoteCardSpan span){
        getNoteCardDetail().setNoteCard(span);
    }
}