package com.creativeartie.jwriter.window;

import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.collections.*;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.main.*;

/**
 * Controller for Agenda Pane.
 *
 * @see AgendaPaneView
 */
class AgendaPaneControl extends AgendaPaneView{

     /** Updates the selections base on the cursor movements. */
     public void updateSelection(WritingText doc, int index){
        Optional<FormatSpanAgenda> inline = doc.locateSpan(index,
            FormatSpanAgenda.class);
        if (inline.isPresent()){
            /// Finds a FormatSpanAgenda to select:
            getAgendaList().getSelectionModel().select(inline.get());
            return;
        }

        Optional<LinedSpanAgenda> line = doc.locateSpan(index,
            LinedSpanAgenda.class);
        if (line.isPresent()){
            /// Finds a LinedSpanAgenda to select:
            getAgendaList().getSelectionModel().select(line.get());
            return;
        }

        /// Nothing is found:
        getAgendaList().getSelectionModel().clearSelection();
     }

    /** Load agenda list. */
    public void loadAgenda(WritingText doc){
        ArrayList<SpanBranch> input = new ArrayList<>();
        doc.getCatalogue().getCategory(AuxiliaryData.TYPE_AGENDA).values()
            .forEach(data -> input.add(data.getTarget()));
        getAgendaList().setItems(FXCollections.observableList(input));
    }
}