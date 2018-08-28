package com.creativeartie.writerstudio.javafx;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.beans.property.*;

import org.fxmisc.richtext.*;
import org.fxmisc.richtext.model.*;

import com.creativeartie.writerstudio.lang.markup.*;

abstract class NoteCardDetailPaneView extends TabPane{
    /// %Part 1: Constructor and Class Fields

    private Tab emptyTab;

    private Tab mainTab;
    private TextFlow noteTitle;
    private TextFlow noteContent;

    private Tab metaTab;
    private TableView<NoteCardData> noteMetaTable;
    private ComboBox<String> showType;
    private Button addField;
    private Button deleteField;
    private Button removeUnused;

    private SimpleObjectProperty<NoteCardSpan> showCard;

    NoteCardDetailPaneView(){
        showCard = new SimpleObjectProperty<>(this, "showCard");
        setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        emptyTab = buildEmptyTab();
        mainTab = buildMainTab();
        metaTab = buildMetaTab();
    }

    /// %Part 2: Layout
    private Tab buildEmptyTab(){
        Label text = new Label("Select a note to view");
        BorderPane pane = new BorderPane();
        pane.setCenter(text);
        return new Tab("None Selected", pane);
    }

    private Tab buildMainTab(){
        BorderPane pane = new BorderPane();
        noteTitle = new TextFlow();
        noteContent = new TextFlow();
        ScrollPane scroll = new ScrollPane(noteContent);
        pane.setTop(noteTitle);
        pane.setCenter(scroll);
        return new Tab("Content", pane);
    }

    private Tab buildMetaTab(){
        BorderPane pane = new BorderPane();
        pane.setTop(buildNoteControls());
        pane.setCenter(buildMetaTable());
        return new Tab("Meta Data", pane);
    }

    private ToolBar buildNoteControls(){
        showType = new ComboBox<>();
        showType.getItems().addAll("Show Used", "Show Unused", "Show All");
        return new ToolBar(showType);
    }

    private TableView<NoteCardData> buildMetaTable(){
        noteMetaTable = new TableView<>();
        TableColumn<NoteCardData, Boolean> use = TableCellFactory
            .getBooleanColumn("Is used", d -> d.inUseProperty());
        TableCellFactory.setPrecentWidth(use, this, 10);

        TableColumn<NoteCardData, InfoFieldType> field = TableCellFactory
            .getFieldTypeColumn("Field", d -> d.fieldTypeProperty());

        TableColumn<NoteCardData, Optional<SpanBranch>> data = TableCellFactory
            .getMetaDataColumn("Value", d -> d.dataTextProperty());

        noteMetaTable.getColumns().addAll(use, field, data);

        return noteMetaTable;
    }


    /// %Part 3: Setup Properties

    public void postLoad(WriterSceneControl control){
        bindChildren(control);
    }

    protected abstract void bindChildren(WriterSceneControl control);

    /// %Part 4: Properties

    /// %Part 4.1: Show Note

    public ObjectProperty<NoteCardSpan> showCardProperty(){
        return showCard;
    }

    public NoteCardSpan getShowCard(){
        return showCard.getValue();
    }

    public void setShowCard(NoteCardSpan value){
        showCard.setValue(value);
    }

    /// %Part 5: Get Child Methods

    Tab getEmptyTab(){
        return emptyTab;
    }

    Tab getMainTab(){
        return mainTab;
    }

    Tab getMetaTab(){
        return metaTab;
    }

    TextFlow getNoteTitle(){
        return noteTitle;
    }

    TextFlow getNoteContent(){
        return noteContent;
    }
}
