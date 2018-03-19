package com.creativeartie.writerstudio.window;

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
import com.creativeartie.writerstudio.file.*;
import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.resource.*;

import com.google.common.collect.*;

abstract class WriterSceneView extends BorderPane{
    private TextPaneControl textArea;
    private WriterMenuBar menuBar;
    private HeadingPaneControl tableOfContent;
    private WriterTabControl tabsPane;
    private CheatsheetPaneControl langCheatsheet;
    private SimpleObjectProperty<ManuscriptFile> manuscriptFile;
    private double[] verDividerPos;
    private double[] hozDividerPos;

    WriterSceneView(Stage window){
        getStylesheets().add("data/main.css");
        verDividerPos = new double[]{.2, .8};
        hozDividerPos = new double[]{.0, 1.0};

        manuscriptFile = new SimpleObjectProperty<>(this, "manuscriptFile");
        manuscriptFile.addListener((data, oldValue, newValue) ->
            changeDoc(newValue));

        /// Top
        menuBar = initMenuBar(window);
        setTop(menuBar);

        /// Upper split pane
        tabsPane = initTabPane();
        tabsPane.getTableTabs().forEach(pane -> {
            pane.focusedProperty().addListener((data, oldValue, newValue) ->
                returnFocus());
            pane.itemSelectedProperty().addListener(
                (data, oldValue, newValue) -> selectionChanged(newValue)
            );
        });
        tabsPane.getNoteCardsPane().locationChoosenProperty().addListener(
            (data, oldValue, newValue) -> moveCursor(newValue.intValue())
        );

        /// Inner SplitPane - Right
        textArea = initTextPane();
        textArea.textChangedProperty().addListener((data, oldValue, newValue) ->
            textChanged(newValue));
        textArea.caretPlacedProperty().addListener((data, oldValue, newValue) ->
            caretChanged(newValue.intValue()));
        /// Inner SplitPane - Left
        tableOfContent = initTableOfContent();
        tableOfContent.treeFocusedProperty().addListener((data, oldValue,
            newValue) -> returnFocus());
        tableOfContent.headingSelectedProperty().addListener(
            (data, oldValue, newValue) -> selectionChanged(newValue)
        );
        tableOfContent.outlineSelectedProperty().addListener(
            (data, oldValue, newValue) -> selectionChanged(newValue)
        );

        /// bottom
        langCheatsheet = initCheatsheetPane();

        SplitPane center = new SplitPane(tableOfContent, textArea);
        center.setDividerPositions(hozDividerPos);
        BorderPane bottom = new BorderPane();
        bottom.setCenter(center);
        bottom.setBottom(langCheatsheet);

        SplitPane full = new SplitPane(tabsPane, bottom);
        full.setOrientation(Orientation.VERTICAL);
        full.setDividerPositions(verDividerPos);
        setCenter(full);

        new AnimationTimer(){
            @Override public void handle(long now) {timerAction(now);}
        }.start();

    }

    /// Layout Nodes
    private TextPaneControl initTextPane(){
        TextPaneControl out = new TextPaneControl();
        return out;
    }

    private WriterMenuBar initMenuBar(Stage window){
        WriterMenuBar ans = new WriterMenuBar(window);
        ans.manuscriptFileProperty().bindBidirectional(manuscriptFile);
        return ans;
    }

    private WriterTabControl initTabPane(){
        WriterTabControl ans = new WriterTabControl();
        return ans;
    }

    private CheatsheetPaneControl initCheatsheetPane(){
        CheatsheetPaneControl ans = new CheatsheetPaneControl();
        return ans;
    }

    private HeadingPaneControl initTableOfContent(){
        HeadingPaneControl ans = new HeadingPaneControl();
        return ans;
    }

    /// Getters
    protected TextPaneControl getTextArea(){
        return textArea;
    }

    protected HeadingPaneControl getTableOfContent(){
        return tableOfContent;
    }

    protected List<TableDataControl<?>> getTableTabs(){
        return tabsPane.getTableTabs();
    }

    protected NoteCardControl getNoteCardsPane(){
        return tabsPane.getNoteCardsPane();
    }

    protected CheatsheetPaneControl getCheatsheet(){
        return langCheatsheet;
    }

    protected WriterMenuBar getMenuBar(){
        return menuBar;
    }

    /// Node Properties
    public ObjectProperty<ManuscriptFile> manuscriptFileProperty(){
        return manuscriptFile;
    }

    public ManuscriptFile getManuscriptFile(){
        return manuscriptFile.getValue();
    }

    public void setManuscriptFile(ManuscriptFile doc){
        manuscriptFile.setValue(doc);
    }

    /// Control Methods
    protected abstract void changeDoc(ManuscriptFile file);

    protected abstract void textChanged(PlainTextChange changes);

    protected abstract void selectionChanged(SpanBranch range);

    protected abstract void caretChanged(int position);

    protected abstract void returnFocus();

    protected abstract void timerAction(long now);

    protected abstract void moveCursor(int position);
}