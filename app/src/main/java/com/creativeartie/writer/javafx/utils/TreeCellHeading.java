package com.creativeartie.writer.javafx.utils;

import javafx.scene.control.*;
import javafx.scene.text.*;
import com.creativeartie.writer.lang.markup.*;

public class TreeCellHeading<T extends SectionSpan> extends TreeCell<T>{
    @Override
    public void updateItem(T item, boolean empty){
        /// Required by JavaFX API:
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            /// Allows WindowSpanParser to create the Label
            TextFlow graphic = TextFlowBuilder.loadHeadingLine(item
                .getHeading());
            setText(null);
            setGraphic(graphic);
        }
    }
}
