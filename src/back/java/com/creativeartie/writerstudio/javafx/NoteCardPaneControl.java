package com.creativeartie.writerstudio.javafx;

import java.util.*;
import javafx.collections.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;

class NoteCardPaneControl extends NoteCardPaneView{

    private WritingText writingText;

    @Override
    protected void setupChildern(WriterSceneControl control){
        getNoteCardDetail().setupProperties(control);
        control.writingTextProperty().addListener((d, o, n) -> loadText(n));

        getNoteTable().getSelectionModel().selectedItemProperty().addListener(
            (d, o, n) -> showCard(n));
    }

    /// %Part 2.1: getNoteTable().getSelectionModel().selectedItemProperty()
    private void showCard(NoteCardData data){
        if (data != null){
            getNoteCardDetail().setShowCard(data.getTargetSpan());
        }

    }

    private void loadText(WritingText text){
        writingText = text;
        if (text != null){
            text.addDocEdited(span -> showCards());
            showCards();
        }
    }

    private void showCards(){
        if (writingText == null){
            getNoteTable().setItems(FXCollections.emptyObservableList());
            return;
        }
        CatalogueMap map = writingText.getCatalogue();
        TreeSet<SpanBranch> set = map.getIds(AuxiliaryData.TYPE_NOTE);
        set.addAll(map.getIds(AuxiliaryData.TYPE_RESEARCH));
        ArrayList<NoteCardData> data = new ArrayList<>();

        NoteCardSpan last = getNoteCardDetail().getShowCard();
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
