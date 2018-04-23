package com.creativeartie.writerstudio.window;

import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.collections.*;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.resource.*;
import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.main.*;

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
     public void updateLocation(int index){
         docIndex = index;
         updateLocation();
     }

    private void updateLocation(){
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
                     if (data.getTargetSpan().equals(span.get())){
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
        doc.addSpanEdited(span -> updateItems());
        updateItems();
    }

    public void updateItems(){
        ArrayList<T> list = new ArrayList<>();
        for (SpanBranch span: docText.getCatalogue().getIds(getCategory())){
            for (Class<? extends SpanBranch> clazz: getTargetClass()){
                if (clazz.isInstance(span)){
                    list.add(buildSpan(span));
                }
            }
        }
        setItems(FXCollections.observableList(list));
        updateLocation();
    }

     protected abstract List<Class<? extends SpanBranch>> getTargetClass();

     protected abstract String getCategory();

     protected abstract T buildSpan(SpanBranch span);
}
