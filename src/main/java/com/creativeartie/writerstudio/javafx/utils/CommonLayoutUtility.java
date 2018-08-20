package com.creativeartie.writerstudio.javafx.utils;

import javafx.scene.layout.*;

public class CommonLayoutUtility{

    public static void addColumnPrecent(GridPane pane, double size){
        ColumnConstraints column = new ColumnConstraints();
        column.setPercentWidth(size);
        pane.getColumnConstraints().add(column);
    }
}
