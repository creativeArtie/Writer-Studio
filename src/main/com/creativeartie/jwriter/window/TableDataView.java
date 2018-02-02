package com.creativeartie.jwriter.window;

import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.beans.binding.*;
import javafx.beans.property.*;
import javafx.scene.control.cell.*;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.main.*;
import com.creativeartie.jwriter.resource.*;

/**
 * The agenda pane stores a list of to do item from either complete lines or in
 * line.
 */
abstract class TableDataView<T extends TableData> extends TableView<T>{

    /**
     * Property binded to agendaList.getSelectionModel().selectItemProperty().
     */
    private final ReadOnlyObjectWrapper<SpanBranch> itemSelected;

    public TableDataView(WindowText empty){
        setFixedCellSize(30);
        setPlaceholder(new Label(empty.getText()));
        initColumns();

        itemSelected = new ReadOnlyObjectWrapper<>(this, "itemSelected");
        ReadOnlyObjectProperty<T> selected = getSelectionModel()
            .selectedItemProperty();
        itemSelected.bind(Bindings.createObjectBinding(() -> Optional
            .ofNullable(selected.get()) /// Maybe null
            .map(data -> data.getTargetSpan()) /// get target if found
            .orElse(null), selected));
    }

    /// Layout Node
    protected abstract void initColumns();

    /// Getters

    /// Node Properties
    ReadOnlyObjectProperty<SpanBranch> itemSelectedProperty(){
         return itemSelected.getReadOnlyProperty();
    }

    SpanBranch getItemSelected(){
        return itemSelected.getValue();
    }

    /// Control Methods
}