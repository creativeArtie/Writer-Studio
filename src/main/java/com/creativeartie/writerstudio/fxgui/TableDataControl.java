package com.creativeartie.writerstudio.fxgui;

import javafx.beans.binding.*;
import javafx.collections.*;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.resource.*;
import com.creativeartie.writerstudio.lang.markup.*;

/**
 * Controller for Agenda Pane.
 *
 * @see AgendaPaneView
 */
abstract class TableDataControl<T extends TableData> extends TableDataView<T>{

    TableDataControl(WindowText text){
        super(text);
    }

    @Override
    protected void addBindings(){
        getItemSelectedProperty().bind(Bindings.createObjectBinding(
            this::bindItemSelection, getSelectionModel().selectedItemProperty()));

        caretPositionProperty().addListener((d, o, n) -> updateLocation());

        writingTextProperty().addListener((d, o, n) -> loadList(n));
    }

    private SpanBranch bindItemSelection(){
        ObservableList<T> data = getSelectionModel().getSelectedItems();
        return data.isEmpty()? null : data.get(0).getTargetSpan();
    }

    private void updateLocation(){
        if (! isTextReady()) return;
        WritingText text = getWritingText();
        if (text == null || text.isEmpty()){
            return;
        }
        int index = getCaretPosition();
        if (index > text.getEnd()){
            /// TODO Select the last item (if there is one)
            return;
        }

        for (Class<? extends SpanBranch> clazz: getTargetClass()) {
            Optional<? extends SpanBranch> span = text.locateSpan(index, clazz);
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
        setRefocusText(true);
     }


    /** Load agenda list. */
    private void loadList(WritingText text){
        text.addDocEdited(s -> updateItems());
        updateItems();
    }

    private void updateItems(){
        WritingText text = getWritingText();
        if (text == null) return;
        ArrayList<T> list = new ArrayList<>();

        for (SpanBranch span: text.getCatalogue().getIds(getCategory())){
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
