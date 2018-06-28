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

    private WritingText writingText;
    private boolean textReady;

    TableDataControl(WindowText text){
        super(text);
    }

    @Override
    protected void addBindings(){
        getItemSelectedProperty().bind(Bindings.createObjectBinding(
            this::bindItemSelection, getSelectionModel().selectedItemProperty()));
    }

    public TableDataControl setWritingTextProperty(
            ObjectProperty<WritingText> text){
        text.addListener((d, o n) -> {
            writingText = n;
            n.addDocEdited(s -> updateItems());
            updateItems();
        });
        return this;
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

    public TableDataControl setTextReady(BooleanProperty ready){
        ready.addListener((d, o, n) -> textReady = ready);
        return this;
    }

    public TableDataControl setCaretPositionProperty(IntegerProperty position){
        position.addListener((d, o, n) -> {
            if (isNotReady()) return;
            if (locateSpan(n.intValue())) return;

            /// Nothing is found:
            getSelectionModel().clearSelection();
            setRefocusText(true);
        });
        return this;
    }

    private boolean isNotReady(){
        return ! textReady || text == null || text.isEmpty();
    }

    private boolean locateSpan(int position){
        if (index > text.getEnd()){
            /// TODO Select the last item (if there is one)
            return false;
        }
        for (Class<? extends SpanBranch> clazz: getTargetClass()) {
            Optional<? extends SpanBranch> span = writingText.locateSpan(index, clazz);
            if (span.isPresent()){
                 int ptr = 0;
                 for(TableData data: getItems()){
                     if (data.getTargetSpan().equals(span.get())){
                         getSelectionModel().select(ptr);
                         return true;
                     }
                     ptr++;
                 }
            }
        }
        return false;
    }

    public TableDataControl setLastSelectedProperty(
            ObjectProperty<SpanBranch> selected){
        getSelectionModel().selectedItemProperty().addListener((d, o, n) -> {
            selected.setValue(n);
        });
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
}
