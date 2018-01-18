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
import com.creativeartie.jwriter.property.window.*;

abstract class WriterSceneView extends BorderPane{
    private TextPaneControl textArea;
    private HeadingPaneControl tableOfContent;
    private AgendaPaneControl agendaPane;
    private NotesPaneControl userLists;
    private CheatsheetPaneControl langCheatsheet;
    private SimpleObjectProperty<ManuscriptFile> manuscriptFile;
    private ReadOnlyObjectWrapper<WritingText> document;
    private ReadOnlyObjectWrapper<RecordList> records;
    private SimpleBooleanProperty ready;
    private SimpleBooleanProperty isEdited;

    WriterSceneView(Stage window){
        textArea = new TextPaneControl();
        tableOfContent = new HeadingPaneControl();
        agendaPane = new AgendaPaneControl();
        userLists = new NotesPaneControl();
        langCheatsheet = new CheatsheetPaneControl();

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

        agendaPane.agendaFocusedProperty().addListener((data, oldValue,
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

    /// Layout Nodes
    private void layoutLeftPane(){
        setLeft(tableOfContent);
    }

    private void layoutCenterPane(){
        setCenter(textArea);
    }

    private void layoutRightPane(){
        setRight(agendaPane);
    }

    private void layoutTopPane(Stage window){
        VBox pane = new VBox();
        WriterMenuBar bar = new WriterMenuBar(window);
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

    public ReadOnlyObjectProperty<WritingText> doumentProperty(){
        return document.getReadOnlyProperty();
    }

    public WritingText getDocument(){
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

    public void setUpdateded(boolean bool){
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