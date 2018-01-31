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
            selectAgenda(inline.get());
            return;
        }

        Optional<LinedSpanAgenda> line = docText.locateSpan(docIndex,
            LinedSpanAgenda.class);
        if (line.isPresent()){
            /// Finds a LinedSpanAgenda to select:
            selectAgenda(line.get());
            return;
        }

        /// Nothing is found:
        getSelectionModel().clearSelection();
     }

     private void selectAgenda(SpanBranch span){
         int ptr = 0;
         for(AgendaData data: getItems()){
             if (data.getTargetSpan().equals(span)){
                 getSelectionModel().select(ptr);
                 return;
             }
             ptr++;
         }

        /// Nothing is found (somehow):
        getSelectionModel().clearSelection();
     }

    /** Load agenda list. */
    public void loadAgenda(WritingText doc){
        docText = doc;
        doc.addUpdater(span -> updateList());
        updateList();
    }

    public void updateList(){
        setItems(AgendaData.extractList(docText));
        updateAgenda();
    }
}
