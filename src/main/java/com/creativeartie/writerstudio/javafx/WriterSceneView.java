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

abstract class WriterSceneView extends BorderPane{

    /// %Part 1: Constructor and Class Fields

    private ReadOnlyObjectWrapper<WritingText> writingText;
    private ReadOnlyObjectWrapper<WritingStat> writingStat;
    private ReadOnlyObjectWrapper<WritingData> writingData;

    private MenuBarMainControl mainMenuBar;

    WriterSceneView(Stage window){
        getStylesheets().add(FileResource.MAIN_CSS.getCssPath());

        writingText = new ReadOnlyObjectWrapper<>(this, "writingText");
        writingStat = new ReadOnlyObjectWrapper<>(this, "writingStat");
        writingData = new ReadOnlyObjectWrapper<>(this, "writingData");

        setTop(buildTop(window));
    }

    /// %Part 2: Layout

    private MenuBarMainControl buildTop(Stage window){
        mainMenuBar = new MenuBarMainControl(window);
        return mainMenuBar;
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

    /// %Part 4.1: writingStat

    public ReadOnlyObjectProperty<WritingStat> writingStatProperty(){
        return writingStat.getReadOnlyProperty();
    }

    public WritingStat getWritingStat(){
        return writingStat.getValue();
    }

    /// %Part 5: Get Child Methods

    MenuBarMainControl getMainMenuBar(){
        return mainMenuBar;
    }
}
