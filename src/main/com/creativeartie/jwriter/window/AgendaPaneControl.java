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
    private WritingText docText;
    private int docIndex;

     /** Updates the selections base on the cursor movements. */
     public void updateAgenda(int index){
         docIndex = index;
         updateAgenda();
     }

    private void updateAgenda(){
        if (docText == null){
            return;
        }

        Optional<FormatSpanAgenda> inline = docText.locateSpan(docIndex,
            FormatSpanAgenda.class);
        if (inline.isPresent()){
            /// Finds a FormatSpanAgenda to select:
            getAgendaList().getSelectionModel().select(inline.get());
            return;
        }

        Optional<LinedSpanAgenda> line = docText.locateSpan(docIndex,
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
        docText = doc;
        doc.addUpdater(span -> updateList());
        updateList();
    }

    public void updateList(){
        getAgendaList().setItems(FXCollections.observableList(
            new ArrayList<>(
                docText.getCatalogue().getIds(AuxiliaryData.TYPE_AGENDA)
            )
        ));
        updateAgenda();
    }
}
