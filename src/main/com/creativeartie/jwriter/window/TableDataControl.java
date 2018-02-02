package com.creativeartie.jwriter.window;

import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.collections.*;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.resource.*;
import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.main.*;

/**
 * Controller for Agenda Pane.
 *
 * @see AgendaPaneView
 */
abstract class TableDataControl<T extends TableData> extends TableDataView<T>{
    private WritingText docText;
    private int docIndex;

    TableDataControl(WindowText text){
        super(text);
    }

     /** Updates the selections base on the cursor movements. */
     public void updateTable(int index){
         docIndex = index;
         updateTable();
     }

    private void updateTable(){
        if (docText == null || docText.isEmpty()){
            return;
        }
        if (docIndex > docText.getEnd()){
            /// TODO Select the last item (if there is one)
            return;
        }

        for (Class<? extends SpanBranch> clazz: getTargetClass()) {
            Optional<? extends SpanBranch> span = docText
                .locateSpan(docIndex, clazz);
            if (span.isPresent()){
                 int ptr = 0;
                 for(TableData data: getItems()){
                     if (data.getTargetSpan().equals(span)){
                         getSelectionModel().select(ptr);
                         return;
                     }
                     ptr++;
                 }
            }
        }
        /// Nothing is found:
        getSelectionModel().clearSelection();
     }


    /** Load agenda list. */
    public void loadList(WritingText doc){
        docText = doc;
        doc.addUpdater(span -> updateList());
        updateList();
    }

    public void updateList(){
        ArrayList<T> list = new ArrayList<>();
        for (SpanBranch span: docText.getCatalogue().getIds(getCategory())){
            if (span instanceof FormatSpanAgenda ||
                    span instanceof LinedSpanAgenda){
                list.add(buildSpan(span));
            }
        }
        setItems(FXCollections.observableList(list));
        updateList();
    }

     protected abstract List<Class<? extends SpanBranch>> getTargetClass();

     protected abstract String getCategory();

     protected abstract T buildSpan(SpanBranch span);
}
