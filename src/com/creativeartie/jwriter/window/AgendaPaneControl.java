package com.creativeartie.jwriter.window;

import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.collections.*;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.main.*;

class AgendaPaneControl extends AgendaPaneView{

    @Override
     public void updateSelection(ManuscriptDocument doc, int index){
        Optional<FormatSpanAgenda> inline = doc.locateSpan(index,
            FormatSpanAgenda.class);
        if (inline.isPresent()){
            getList().getSelectionModel().select(inline.get());
            return;
        }
        Optional<LinedSpanAgenda> line = doc.locateSpan(index,
            LinedSpanAgenda.class);
        if (line.isPresent()){
            getList().getSelectionModel().select(line.get());
            return;
        }
        getList().getSelectionModel().clearSelection();
     }

    @Override
    public void fillAgenda(ManuscriptDocument doc){
        ArrayList<SpanBranch> input = new ArrayList<>();
        for(CatalogueData data : doc.getCatalogue().getCategory(
                AuxiliaryData.TYPE_AGENDA).values())
        {
            input.add(data.getTarget());
        }
        getList().setItems(FXCollections.observableList(input));
    }
}