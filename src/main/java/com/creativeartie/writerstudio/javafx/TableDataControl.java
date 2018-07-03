package com.creativeartie.writerstudio.javafx;

import javafx.beans.property.*;
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
    private WritingText writingText;
    private int caretPosition;
    private ObjectProperty<SpanBranch> lastSelected;
    private ReadOnlyBooleanProperty textReady;

    TableDataControl(WindowText text){
        super(text);
    }

    @Override
    protected void setupChildern(WriterSceneControl control){
        control.writingTextProperty().addListener((d, o, n) -> setText(n));
        control.getTextPane().getTextArea().caretPositionProperty().addListener(
            (d, o, n) -> setCaret(n.intValue())
        );
        lastSelected = control.lastSelectedProperty();
        getSelectionModel().selectedItemProperty().addListener((d, o, n) ->
            newSelection(n));
        textReady = control.getTextPane().textReadyProperty();
    }

    /// %Part 1: control#writingTextProperty()

    private void setText(WritingText text){
        writingText = text;
        if (text != null) {
            text.addDocEdited(span -> updateItems());
            updateItems();
            updateSelection();
        }
    }

    /// %Part 2: control#caretPositionProperty()

    /** Updates the selections base on the cursor movements. */
    public void setCaret(int index){
        caretPosition = index;
        updateSelection();
    }

    /// %Part 3: control.lastSelectedProperty()

    private void newSelection(TableData data){
        if (data != null){
            lastSelected.setValue(data.getTargetSpan());
        }
    }

    /// %Part 4: Utilities
    /// %Part 4.1: Update list with abstract methods

    private void updateItems(){
        ArrayList<T> list = new ArrayList<>();
        for (SpanBranch span: writingText.getCatalogue().getIds(getCategory())){
            for (Class<? extends SpanBranch> clazz: getTargetClass()){
                if (clazz.isInstance(span)){
                    list.add(buildSpan(span));
                }
            }
        }
        setItems(FXCollections.observableList(list));
        updateSelection();
    }

     protected abstract List<Class<? extends SpanBranch>> getTargetClass();

     protected abstract String getCategory();

     protected abstract T buildSpan(SpanBranch span);

    /// %Part 3.2: select item

    private void updateSelection(){
        if (! textReady.getValue()) return; ///Already called, stop before error

        if (writingText == null || writingText.isEmpty()){
            return;
        }
        if (caretPosition > writingText.getEnd()){
            /// TODO Select the last item (if there is one)
            return;
        }
        for (Class<? extends SpanBranch> clazz: getTargetClass()) {
            Optional<? extends SpanBranch> span = writingText
                .locateSpan(caretPosition, clazz);
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

}
