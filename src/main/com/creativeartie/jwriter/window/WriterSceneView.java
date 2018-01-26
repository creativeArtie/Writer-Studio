package com.creativeartie.jwriter.window;

import java.util.*;
import javafx.stage.*;
import javafx.scene.layout.*;
import javafx.beans.property.*;
import javafx.beans.binding.*;
import org.fxmisc.richtext.model.*;
import javafx.animation.*;

import com.creativeartie.jwriter.main.*;
import com.creativeartie.jwriter.file.*;
import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.property.window.*;

import com.google.common.collect.*;

abstract class WriterSceneView extends BorderPane{
    private TextPaneControl textArea;
    private HeadingPaneControl tableOfContent;
    private AgendaPaneControl agendaPane;
    private NotesPaneControl userLists;
    private CheatsheetPaneControl langCheatsheet;
    private SimpleObjectProperty<ManuscriptFile> manuscriptFile;

    WriterSceneView(Stage window){
        tableOfContent = new HeadingPaneControl();
        userLists = new NotesPaneControl();

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
        setTop(menu);

        agendaPane = initAgendaPane();
        agendaPane.agendaFocusedProperty().addListener((data, oldValue,
            newValue) -> returnFocus());

        agendaPane.agendaSelectedProperty().addListener(
            (data, oldValue, newValue) -> Optional.ofNullable(newValue)
                .ifPresent(value -> selectionChanged(value.getRange()))
        );

        /// bottom
        langCheatsheet = initCheatsheetPane();

        /// left
        tableOfContent = initTableOfContent();

        new AnimationTimer(){
            @Override public void handle(long now) {timerAction(now);}
        }.start();

/*
        tableOfContent.headingFocusedProperty().addListener((data, oldValue,
            newValue) -> listenHeading(newValue));
        tableOfContent.outlineFocusedProperty().addListener((data, oldValue,
            newValue) -> listenOutline(newValue));

        userLists.toLocationProperty().addListener((data, oldValue, newValue)
            -> listenLocClicked(newValue.intValue()));
        userLists.childFocusedProperty().addListener((data, oldValue, newValue)
            -> listenListFocused(newValue));
*/
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
        top.getChildren().addAll(ans);
        return ans;
    }

    private AgendaPaneControl initAgendaPane(){
        AgendaPaneControl ans = new AgendaPaneControl();
        setRight(ans);
        return ans;
    }

    private CheatsheetPaneControl initCheatsheetPane(){
        CheatsheetPaneControl ans = new CheatsheetPaneControl();
        setBottom(ans);
        return ans;
    }

    private HeadingPaneControl initTableOfContent(){
        HeadingPaneControl ans = new HeadingPaneControl();
        setRight(ans);
        return ans;
    }
/*


    private void layoutTopPane(Stage window){
        VBox pane = new VBox();
        WriterMenuBar bar = new WriterMenuBar(window);
        bar.manuscriptFileProperty().bindBidirectional(manuscriptFile);

        pane.getChildren().addAll(bar, userLists);
        setTop(pane);
    }

    private void layoutBottomPane(){
        setBottom(langCheatsheet);
    }*/

    /// Getters
    protected TextPaneControl getTextArea(){
        return textArea;
    }

    protected HeadingPaneControl getTableOfContent(){
        return tableOfContent;
    }

    protected AgendaPaneControl getAgendaPane(){
        return agendaPane;
    }

    protected NotesPaneControl getUserLists(){
        return userLists;
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

    protected abstract void selectionChanged(Range<Integer> range);

    protected abstract void caretChanged(int position);

    protected abstract void returnFocus();

    protected abstract void timerAction(long now);
}