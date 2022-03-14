package com.creativeartie.writer.javafx;

import javafx.scene.control.*;
import javafx.scene.input.*;

/**
 * The agenda pane stores a list of to do item from either complete lines or in
 * line.
 */
abstract class DataView<T extends DataInput> extends TableView<T>{

    /// %Part 1: Constructor and Class Fields

    public DataView(String empty){
        setFixedCellSize(30);
        setPlaceholder(new Label(empty));
        setRowFactory( t -> {
            TableRow<T> row = new TableRow<>();
            row.setOnMouseClicked(e -> handleSelection(e, row.getItem()));
            return row;
        });
        buildColumns();
    }

    /// %Part 2: Layout

    protected abstract void buildColumns();

    /// %Part 3: Setup Properties

    public void postLoad(WriterSceneControl control){
        bindChildren(control);
    }

    protected abstract void handleSelection(MouseEvent event, T item);

    protected abstract void bindChildren(WriterSceneControl control);

    /// %Part 4: Properties

    /// %Part 5: Get Child Methods
}
