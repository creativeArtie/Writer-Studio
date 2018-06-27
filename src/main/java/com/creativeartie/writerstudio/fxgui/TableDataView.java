package com.creativeartie.writerstudio.fxgui;

import javafx.scene.control.*;
import javafx.beans.property.*;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.resource.*;

/**
 * The agenda pane stores a list of to do item from either complete lines or in
 * line.
 */
abstract class TableDataView<T extends TableData> extends TableView<T>{
    /// %Part 1: Constructor and Class Fields

    /**
     * Property binded to agendaList.getSelectionModel().selectItemProperty().
     */
    private final ReadOnlyObjectWrapper<SpanBranch> itemSelected;
    private final SimpleObjectProperty<WritingText> writingText;
    private final SimpleIntegerProperty caretPosition;
    private final SimpleBooleanProperty textReady;
    private final SimpleBooleanProperty refocusText;

    public TableDataView(WindowText empty){
        setFixedCellSize(30);
        setPlaceholder(new Label(empty.getText()));

        buildColumns();

        itemSelected = new ReadOnlyObjectWrapper<>(this, "itemSelected");
        writingText = new SimpleObjectProperty<>(this, "writingText");
        caretPosition = new ReadOnlyIntegerWrapper(this, "caretPosition");
        textReady = new SimpleBooleanProperty(this, "textReady");
        refocusText = new SimpleBooleanProperty(this, "refocusText");

        addBindings();
    }

    /// %Part 2: Layout
    protected abstract void buildColumns();

    /// %Part 3: Listener Methods

    protected abstract void addBindings();

    /// %Part 4: Properties

    /// %Part 4.1 Item Selected

    public ReadOnlyObjectProperty<SpanBranch> itemSelectedProperty(){
        return itemSelected.getReadOnlyProperty();
    }

    protected ReadOnlyObjectWrapper<SpanBranch> getItemSelectedProperty(){
        return itemSelected;
    }

    public SpanBranch getItemSelected(){
        return itemSelected.getValue();
    }

    /// %Part 4.2: WritingText

    public ObjectProperty<WritingText> writingTextProperty(){
        return writingText;
    }

    protected SimpleObjectProperty<WritingText> getWritingTextProperty(){
        return writingText;
    }

    public WritingText getWritingText(){
        return writingText.getValue();
    }

    public void setWritingText(WritingText value){
        writingText.setValue(value);
    }

    /// %Part 4.3: Caret Position

    public IntegerProperty caretPositionProperty(){
        return caretPosition;
    }

    protected SimpleIntegerProperty getCaretPositionProperty(){
        return caretPosition;
    }

    public int getCaretPosition(){
        return caretPosition.getValue();
    }

    public void setCaretPosition(int value){
        caretPosition.setValue(value);
    }

    /// %Part 4.4: Text Ready

    public BooleanProperty textReadyProperty(){
        return textReady;
    }

    protected SimpleBooleanProperty getTextReadyProperty(){
        return textReady;
    }

    public void setTextReady(boolean value){
        textReady.setValue(value);
    }

    public boolean isTextReady(){
        return textReady.getValue();
    }

    /// %Part 4.4: Refocus Text

    public SimpleBooleanProperty refocusTextProperty(){
        return refocusText;
    }

    protected SimpleBooleanProperty getRefocusTextProperty(){
        return refocusText;
    }

    public boolean isRefocusText(){
        return refocusText.getValue();
    }

    public void setRefocusText(boolean value){
        refocusText.setValue(value);
    }

    /// %Part 5: Get Child Methods
}
