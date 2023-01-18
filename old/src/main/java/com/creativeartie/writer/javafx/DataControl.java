package com.creativeartie.writer.javafx;

import java.util.*;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.scene.input.*;

import com.creativeartie.writer.lang.*;
import com.creativeartie.writer.lang.markup.*;

/**
 * Controller for Agenda Pane.
 *
 * @see AgendaPaneView
 */
abstract class DataControl<T extends DataInput> extends DataView<T>{

    /// %Part 1: Private Fields and Constructor

    private WritingText writingText;
    private int caretPosition;
    private ObjectProperty<SpanBranch> lastSelected;
    DataControl(String text){
        super(text);
    }

    /// %Part 2: Property Binding

    @Override
    protected void handleSelection(MouseEvent event, DataInput span){
        if (event.getButton().equals(MouseButton.PRIMARY)){
            lastSelected.setValue(span.getTargetSpan());
        }
    }

    /// %Part 3: Bind Children Properties

    @Override
    protected void bindChildren(WriterSceneControl control){
        control.writingTextProperty().addListener((d, o, n) -> listenWritingText(n));
        control.getTextPane().getTextArea().caretPositionProperty().addListener(
            (d, o, n) -> listenCaret(n.intValue())
        );
        lastSelected = control.lastSelectedProperty();
        control.refocusTextProperty();
    }

    /// %Part 3.1: control.writingTextProperty()

    private void listenWritingText(WritingText text){
        writingText = text;
        if (text != null) {
            text.addDocEdited(span -> listenWritingText());
            listenWritingText();
        }
    }

    private void listenWritingText(){
        loadItems();
        showSelection();
    }

    /// %Part 3.2: control.getTextPane().getTextArea().caretPositionProperty()

    /** Updates the selections base on the cursor movements. */
    public void listenCaret(int index){
        caretPosition = index;
        showSelection();
    }

    /// %Part 4: Utilities
    /// %Part 4.1: Update list with abstract methods

    private void loadItems(){
        ObservableList<T> list = FXCollections.observableArrayList();
        for (SpanBranch span: writingText.getDocumentCatalogue().getIds(getCategory())){
            for (Class<? extends SpanBranch> clazz: getTargetClass()){
                if (clazz.isInstance(span)){
                    list.add(buildSpan(span));
                }
            }
        }
        setItems(list);
        showSelection();
    }

     protected abstract List<Class<? extends SpanBranch>> getTargetClass();

     protected abstract String getCategory();

     protected abstract T buildSpan(SpanBranch span);

    /// %Part 3.2: select item

    private void showSelection(){

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
                 for(DataInput data: getItems()){
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
