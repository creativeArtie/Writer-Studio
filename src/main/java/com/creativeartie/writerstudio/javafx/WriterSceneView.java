package com.creativeartie.writerstudio.javafx;

import java.util.*;
import javafx.beans.property.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.scene.control.*;

import com.google.common.collect.*;

import com.creativeartie.writerstudio.javafx.utils.*;
import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;


import static com.creativeartie.writerstudio.javafx.utils.LayoutConstants.
    MainWindowConstants.*;

abstract class WriterSceneView extends BorderPane{

    /// %Part 1: Constructor and Class Fields

    private ReadOnlyObjectWrapper<WritingText> writingText;
    private ReadOnlyObjectWrapper<WritingStat> writingStat;
    private ReadOnlyObjectWrapper<WritingData> writingData;
    private SimpleBooleanProperty refocusText;

    private MenuBarMainControl mainMenuBar;
    private TextPaneControl textPane;

    WriterSceneView(Stage window){
        getStylesheets().add(FileResource.MAIN_CSS.getCssPath());

        writingText = new ReadOnlyObjectWrapper<>(this, "writingText");
        writingStat = new ReadOnlyObjectWrapper<>(this, "writingStat");
        writingData = new ReadOnlyObjectWrapper<>(this, "writingData");

        setTop(buildTop(window));
        setCenter(buildSplitMain());
    }

    /// %Part 2: Layout

    /// %Part 2 (pane -> menu bar)
    private MenuBarMainControl buildTop(Stage window){
        mainMenuBar = new MenuBarMainControl(window);
        return mainMenuBar;
    }

    /// %Part 2 (pane -> top-down split)

    private SplitPane buildSplitMain(){
        SplitPane full = new SplitPane(buildTopTabs(), buildContent());
        full.setOrientation(Orientation.VERTICAL);
        full.setDividerPositions(VER_DIVIDER);
        return full;
    }

    /// %Part 2 (pane -> top-down split -> top)

    private TabPane buildTopTabs(){
        return new TabPane();
    }

    /// %Part 2 (pane -> top-down split -> bottom)

    private BorderPane buildContent(){
        BorderPane content = new BorderPane();
        content.setCenter(buildSplitCenter());

        //cheatsheetPane = new CheatsheetPaneControl();
        // content.setBottom(cheatsheetPane);
        return content;
    }

    /// %Part 2 (pane -> top-down split -> bottom -> left-right split)

    private SplitPane buildSplitCenter(){
        textPane = new TextPaneControl();
        SplitPane center = new SplitPane(buildLeftTabs(), textPane);
        center.setDividerPositions(HOR_DIVIDER);
        return center;
    }

    /// %Part 2 (pane -> top-down split -> bottom -> left-right split -> left)

    private TabPane buildLeftTabs(){
        return new TabPane();
    }

    /// %Part 3: Setup Properties

    public void postLoad(Scene scene){
        bindWritingText(writingText);
        bindWritingStat(writingStat);
        bindWritingData(writingData);
        bindChildren(scene);
    }


    protected abstract void bindWritingText(
        ReadOnlyObjectWrapper<WritingText> text);

    protected abstract void bindWritingStat(
        ReadOnlyObjectWrapper<WritingStat> stat);

    protected abstract void bindWritingData(
        ReadOnlyObjectWrapper<WritingData> data);

    protected abstract void bindChildren(Scene scene);

    /// %Part 4: Properties


    /// %Part 4.1: WritingText

    public ReadOnlyObjectProperty<WritingText> writingTextProperty(){
        return writingText.getReadOnlyProperty();
    }

    public WritingText getWritingText(){
        return writingText.getValue();
    }

    /// %Part 4.2: WritingStat

    public ReadOnlyObjectProperty<WritingStat> writingStatProperty(){
        return writingStat.getReadOnlyProperty();
    }

    public WritingStat getWritingStat(){
        return writingStat.getValue();
    }

    /// %Part 4.3: WritingData

    public ReadOnlyObjectProperty<WritingData> writingDataProperty(){
        return writingData.getReadOnlyProperty();
    }

    public WritingData getWritingData(){
        return writingData.getValue();
    }

    /// %Part 5: Get Child Methods

    MenuBarMainControl getMainMenuBar(){
        return mainMenuBar;
    }

    TextPaneControl getTextPane(){
        return textPane;
    }
}
