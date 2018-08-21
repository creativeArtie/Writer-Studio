package com.creativeartie.writerstudio.javafx.utils;

import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;

public class CommonLayoutUtility{

    public static void setWidthPrecent(GridPane pane, double size){
        ColumnConstraints column = new ColumnConstraints();
        column.setPercentWidth(size);
        pane.getColumnConstraints().add(column);
    }

    public static void setHeightPrecent(GridPane pane, double size){
        RowConstraints row = new RowConstraints();
        row.setPercentHeight(size);
        pane.getRowConstraints().add(row);
    }

    public static TabPane buildTabPane(){
        TabPane ans = new TabPane();
        ans.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        return ans;
    }

    public static Tab addTab(TabPane parent, String title, Node child){
        Tab ans = new Tab(title, child);
        parent.getTabs().add(ans);
        return ans;
    }
}
