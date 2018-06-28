package com.creativeartie.writerstudio.javafx;

import java.util.*;
import javafx.stage.*;
import javafx.scene.layout.*;
import javafx.beans.property.*;
import javafx.beans.binding.*;
import javafx.scene.control.*;
import org.fxmisc.richtext.model.*;
import javafx.animation.*;
import javafx.geometry.*;

import com.creativeartie.writerstudio.main.*;
import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.resource.*;

import com.google.common.collect.*;

abstract class WriterSceneView extends BorderPane{
    /// %Part 1: Constructor and Class Fields


    private static final double[] VER_DIVIDER = new double[]{.2, .8};
    private static final double[] HOR_DIVIDER = new double[]{.0, 1.0};
    private CheatsheetPaneControl cheatsheetPane;

    WriterSceneView(Stage window){
        getStylesheets().add(FileResources.getMainCss());
        setTop(buildTop());
        setCenter(buildSplitMain());
    }

    /// %Part 2: Layout

    private BorderPane buildTop(){
        return new BorderPane();
    }

    private SplitPane buildSplitMain(){
        SplitPane full = new SplitPane(buildTopTabs(), buildContent());
        full.setOrientation(Orientation.VERTICAL);
        full.setDividerPositions(VER_DIVIDER);
        return full;
    }

    private TabPane buildTopTabs(){
        TabPane top = new TabPane();
        return top;
    }

    private BorderPane buildContent(){
        BorderPane content = new BorderPane();
        content.setCenter(buildSplitCenter());

        cheatsheetPane = new CheatsheetPaneControl();
        content.setBottom(cheatsheetPane);
        return content;
    }

    private SplitPane buildSplitCenter(){
        SplitPane center = new SplitPane(buildLeftTabs(), new BorderPane());
        center.setDividerPositions(HOR_DIVIDER);
        return center;
    }

    private TabPane buildLeftTabs(){
        TabPane left = new TabPane();
        return left;
    }

    /// %Part 3: Abstract Methods

    protected abstract void addBindings();

    /// %Part 4: Properties

    /// %Part 5: Get Child Methods
    CheatsheetPaneControl getCheatsheetPane(){
        return cheatsheetPane;
    }
}
