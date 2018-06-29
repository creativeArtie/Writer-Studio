package com.creativeartie.writerstudio.javafx;

import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.beans.binding.*;
import javafx.beans.property.*;
import javafx.scene.control.cell.*;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.main.*;
import com.creativeartie.writerstudio.resource.*;

/**
 * The agenda pane stores a list of to do item from either complete lines or in
 * line.
 */
abstract class TableDataView<T extends TableData> extends TableView<T>{

    /// %Part 1: Constructor and Class Fields

    public TableDataView(WindowText empty){
        setFixedCellSize(30);
        setPlaceholder(new Label(empty.getText()));
        buildColumns();
    }

    /// %Part 2: Layout

    protected abstract void buildColumns();

    /// %Part 3: Setup Properties

    public void setupProperties(WriterSceneControl control){
        setupChildern(control);
    }

    protected abstract void setupChildern(WriterSceneControl control);

    /// %Part 4: Properties

    /// %Part 5: Get Child Methods
}
