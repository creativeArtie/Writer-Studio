package com.creativeartie.jwriter.window;

import java.util.*;
import javafx.stage.*;
import javafx.scene.layout.*;
import javafx.beans.property.*;
import javafx.beans.binding.*;
import javafx.scene.control.*;
import org.fxmisc.richtext.model.*;
import javafx.animation.*;

import com.creativeartie.jwriter.main.*;
import com.creativeartie.jwriter.file.*;
import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.resource.*;

import com.google.common.collect.*;

abstract class WriterSceneView extends BorderPane{
    private TextPaneControl textArea;
    private HeadingPaneControl tableOfContent;
    private WriterTabControl tabsPane;
    private CheatsheetPaneControl langCheatsheet;
    private SimpleObjectProperty<ManuscriptFile> manuscriptFile;

    WriterSceneView(Stage window){
        getStylesheets().add("data/main.css");

        manuscriptFile = new SimpleObjectProperty<>(this, "manuscriptFile");
        manuscriptFile.addListener((data, oldValue, newValue) ->
            changeDoc(newValue));

        /// Center
        textArea = initTextPane();
        textArea.textChangedProperty().addListener((data, oldValue, newValue) ->
            textChanged(newValue));
        textArea.caretPlacedProperty().addListener((data, oldValue, newValue) ->
            caretChanged(newValue.intValue()));

        /// Top
        VBox top = new VBox();
        WriterMenuBar menu = initMenuBar(window, top);
        tabsPane = initTabPane(top);
        ///agenda pane:
        tabsPane.getTableTabs().forEach(pane -> {
            pane.focusedProperty().addListener((data, oldValue, newValue) ->
                returnFocus());
            pane.itemSelectedProperty().addListener(
                (data, oldValue, newValue) -> selectionChanged(newValue)
            );
        });
        setTop(top);

        /// bottom
        langCheatsheet = initCheatsheetPane();

        /// left
        tableOfContent = initTableOfContent();
        tableOfContent.treeFocusedProperty().addListener((data, oldValue,
            newValue) -> returnFocus());
        tableOfContent.headingSelectedProperty().addListener(
            (data, oldValue, newValue) -> selectionChanged(newValue)
        );
        tableOfContent.outlineSelectedProperty().addListener(
            (data, oldValue, newValue) -> selectionChanged(newValue)
        );

        new AnimationTimer(){
            @Override public void handle(long now) {timerAction(now);}
        }.start();

    }

    /// Layout Nodes
    private TextPaneControl initTextPane(){
        TextPaneControl out = new TextPaneControl();
        setCenter(out);
        return out;
    }

    private WriterMenuBar initMenuBar(Stage window, Pane top){
        WriterMenuBar ans = new WriterMenuBar(window);
        ans.manuscriptFileProperty().bindBidirectional(manuscriptFile);
        top.getChildren().add(ans);
        return ans;
    }

    private WriterTabControl initTabPane(Pane top){
        WriterTabControl ans = new WriterTabControl();
        ans.setMaxHeight(200);
        top.getChildren().add(ans);
        return ans;
    }

    private CheatsheetPaneControl initCheatsheetPane(){
        CheatsheetPaneControl ans = new CheatsheetPaneControl();
        setBottom(ans);
        return ans;
    }

    private HeadingPaneControl initTableOfContent(){
        HeadingPaneControl ans = new HeadingPaneControl();
        setLeft(ans);
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

    protected CheatsheetPaneControl getCheatsheet(){
        return langCheatsheet;
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
}