package com.creativeartie.jwriter.window;

import java.util.*;
import javafx.stage.*;
import javafx.scene.layout.*;
import javafx.beans.property.*;
import javafx.beans.binding.*;
import org.fxmisc.richtext.model.*;

import com.creativeartie.jwriter.main.*;
import com.creativeartie.jwriter.file.*;
import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.lang.markup.*;

abstract class SceneWriterView extends BorderPane{
    private PaneTextControl textArea;
    private PaneHeadingControl tableOfContent;
    private PaneAgendaControl agendaList;
    private PaneListsControl userLists;
    private PaneCheatsheetControl langCheatsheet;
    private SimpleObjectProperty<ManuscriptFile> manuscriptFile;
    private ReadOnlyObjectWrapper<ManuscriptDocument> document;
    private ReadOnlyObjectWrapper<RecordList> records;
    private SimpleBooleanProperty ready;
    private SimpleBooleanProperty isEdited;

    SceneWriterView(Stage window){
        textArea = new PaneTextControl();
        tableOfContent = new PaneHeadingControl();
        agendaList = new PaneAgendaControl();
        userLists = new PaneListsControl();
        langCheatsheet = new PaneCheatsheetControl();

        manuscriptFile = new SimpleObjectProperty<>(this, "manuscriptFile");

        document = new ReadOnlyObjectWrapper<>(this, "document");
        document.addListener((data, oldValue, newValue) -> listenDoc());
        document.bind(Bindings.createObjectBinding(
            () -> Optional.ofNullable(manuscriptFile.getValue()).map(
                  value -> value.getDocument()
            ).orElse(null), manuscriptFile));

        records = new ReadOnlyObjectWrapper<>(this, "records");
        records.bind(Bindings.createObjectBinding(
            () -> Optional.ofNullable(manuscriptFile.getValue()).map(
                  value -> value.getRecords()
            ).orElse(null), manuscriptFile));


        textArea.editedProperty().addListener((data, oldValue, newValue) ->
            listenTextChange(newValue));
        textArea.positionProperty().addListener((data, oldValue, newValue) ->
            listenCaret(newValue.intValue()));

        agendaList.agendaFocusedProperty().addListener((data, oldValue,
            newValue) -> listenAgenda(newValue));

        tableOfContent.headingFocusedProperty().addListener((data, oldValue,
            newValue) -> listenHeading(newValue));
        tableOfContent.outlineFocusedProperty().addListener((data, oldValue,
            newValue) -> listenOutline(newValue));

        userLists.toLocationProperty().addListener((data, oldValue, newValue)
            -> listenLocClicked(newValue.intValue()));
        userLists.childFocusedProperty().addListener((data, oldValue, newValue)
            -> listenListFocused(newValue));

        ready = new SimpleBooleanProperty(this, "ready", false);
        isEdited = new SimpleBooleanProperty(this, "isEdited", false);

        layoutLeftPane();
        layoutRightPane();
        layoutTopPane(window);
        layoutBottomPane();
        layoutCenterPane();

        controlSetup();
    }

    /// Getters
    protected PaneTextControl getTextArea(){
        return textArea;
    }

    protected PaneHeadingControl getTableOfContent(){
        return tableOfContent;
    }

    protected PaneAgendaControl getAgendaList(){
        return agendaList;
    }

    protected PaneListsControl getUserLists(){
        return userLists;
    }

    /// Layout Nodes
    private void layoutLeftPane(){
        setLeft(tableOfContent);
    }

    private void layoutCenterPane(){
        textArea.getViewModeLabel().setText(Utilities.getString("WriterScene.Mode"));
        setCenter(textArea);
    }

    private void layoutRightPane(){
        setRight(agendaList);
    }

    private void layoutTopPane(Stage window){
        VBox pane = new VBox();
        MainMenuBar bar = new MainMenuBar(window);
        bar.manuscriptFileProperty().bindBidirectional(manuscriptFile);

        pane.getChildren().addAll(bar, userLists);
        setTop(pane);
    }

    private void layoutBottomPane(){
        setBottom(langCheatsheet);
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

    public ReadOnlyObjectProperty<ManuscriptDocument> doumentProperty(){
        return document.getReadOnlyProperty();
    }

    public ManuscriptDocument getDocument(){
        return document.getValue();
    }
    public ReadOnlyObjectProperty<RecordList> recordsPoperty(){
        return records.getReadOnlyProperty();
    }

    public RecordList getRecords(){
        return records.getValue();
    }

    public BooleanProperty readyProperty(){
        return ready;
    }

    public boolean isTextReady(){
        return ready.get();
    }

    public void setTextReady(boolean bool){
        ready.setValue(bool);
    }

    public BooleanProperty isEditedProperty(){
        return isEdited;
    }

    public boolean isEdited(){
        return isEdited.get();
    }

    public void setEdited(boolean bool){
        isEdited.setValue(bool);
    }

    /// Control Methods
    protected abstract void controlSetup();

    protected abstract void listenDoc();

    protected abstract void listenTextChange(PlainTextChange changes);

    protected abstract void listenCaret(int moveTo);

    protected abstract void listenAgenda(boolean focused);

    protected abstract void listenHeading(boolean focused);

    protected abstract void listenOutline(boolean focused);

    protected abstract void listenLocClicked(int loc);

    protected abstract void listenListFocused(boolean focused);
}