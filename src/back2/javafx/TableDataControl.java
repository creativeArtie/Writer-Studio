package com.creativeartie.writerstudio.javafx;

import java.util.*;
import javafx.beans.property.*;
import javafx.collections.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.resource.*;

/**
 * Controller for Agenda Pane.
 *
 * @see AgendaPaneView
 */
abstract class TableDataControl<T extends TableData> extends TableDataView<T>{

    /// %Part 1: Private Fields and Constructor

    private WritingText writingText;
    private int caretPosition;
    private ObjectProperty<SpanBranch> lastSelected;
    private ReadOnlyBooleanProperty textReady;
    private BooleanProperty refocusText;

    TableDataControl(WindowText text){
        super(text);
    }

    /// %Part 2: Property Binding

    /// %Part 3: Bind Children Properties

    @Override
    protected void setupChildern(WriterSceneControl control){
        control.writingTextProperty().addListener((d, o, n) -> loadText(n));
        control.getTextPane().getTextArea().caretPositionProperty().addListener(
            (d, o, n) -> loadCaret(n.intValue())
        );
        getSelectionModel().selectedItemProperty().addListener((d, o, n) ->
            loadSelection(n));
        lastSelected = control.lastSelectedProperty();
        textReady = control.getTextPane().textReadyProperty();
        refocusText = control.refocusTextProperty();
    }

    /// %Part 3.1: control.writingTextProperty()

    private void loadText(WritingText text){
        writingText = text;
        if (text != null) {
            text.addDocEdited(span -> loadText());
            loadText();
        }
    }

    private void loadText(){}

    /// %Part 3.2: control.getTextPane().getTextArea().caretPositionProperty()

    /** Updates the selections base on the cursor movements. */
    public void loadCaret(int index){
        caretPosition = index;
        showSelection();
    }

    /// %Part 3.3: getSelectionModel().selectedItemProperty()

    private void loadSelection(TableData data){
        if (data != null){
            lastSelected.setValue(data.getTargetSpan());
        }
        refocusText.setValue(true);
    }

    /// %Part 4: Utilities
    /// %Part 4.1: Update list with abstract methods

    private void showItems(){
        ArrayList<T> list = new ArrayList<>();
        for (SpanBranch span: writingText.getDocumentCatalogue().getIds(getCategory())){
            for (Class<? extends SpanBranch> clazz: getTargetClass()){
                if (clazz.isInstance(span)){
                    list.add(buildSpan(span));
                }
            }
        }
        setItems(FXCollections.observableList(list));
        showSelection();
    }

     protected abstract List<Class<? extends SpanBranch>> getTargetClass();

     protected abstract String getCategory();

     protected abstract T buildSpan(SpanBranch span);

    /// %Part 3.2: select item

    private void showSelection(){
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
