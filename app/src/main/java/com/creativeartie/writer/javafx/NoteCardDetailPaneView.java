package com.creativeartie.writer.javafx;

import java.util.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.beans.property.*;

import com.creativeartie.writer.lang.*;
import com.creativeartie.writer.lang.markup.*;
import com.creativeartie.writer.javafx.utils.*;
import static com.creativeartie.writer.javafx.utils.LayoutConstants.
    NoteCardConstants.*;

abstract class NoteCardDetailPaneView extends TabPane{
    private class ShowMetaCell extends ListCell<ShowMeta>{
        @Override
        public void updateItem(ShowMeta item, boolean empty){
            /// Required by JavaFX API:
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                /// Allows WindowSpanParser to create the Label
                setText(item.getDisplayText());
                setGraphic(null);
            }
        }
    }

    /// %Part 1: Constructor and Class Fields

    private Tab emptyTab;

    private Tab mainTab;
    private TextFlow noteTitle;
    private TextFlow noteContent;

    private Tab metaTab;
    private TableView<NoteCardData> noteMetaTable;
    private ComboBox<ShowMeta> showMetaBox;
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
        Label text = new Label(EMPTY_TEXT_LABEL);
        BorderPane pane = new BorderPane();
        pane.setCenter(text);
        return new Tab(TAB_EMPTY, pane);
    }

    private Tab buildMainTab(){
        BorderPane pane = new BorderPane();
        noteTitle = new TextFlow();
        noteContent = new TextFlow();
        ScrollPane scroll = new ScrollPane(noteContent);
        pane.setTop(noteTitle);
        pane.setCenter(scroll);
        return new Tab(TAB_CONTENT, pane);
    }

    private Tab buildMetaTab(){
        BorderPane pane = new BorderPane();
        pane.setTop(buildNoteControls());
        pane.setCenter(buildMetaTable());
        return new Tab(TAB_META, pane);
    }

    private ToolBar buildNoteControls(){
        showMetaBox = new ComboBox<>();
        showMetaBox.getItems().addAll(ShowMeta.values());
        showMetaBox.getSelectionModel().select(ShowMeta.ALL);
        showMetaBox.setCellFactory(p -> new ShowMetaCell());
        showMetaBox.setButtonCell(new ShowMetaCell());
        return new ToolBar(showMetaBox);
    }

    @SuppressWarnings("unchecked") /// For getColumns().addAdd(TableColumn ...)
    private TableView<NoteCardData> buildMetaTable(){
        noteMetaTable = new TableView<>();
        noteMetaTable.setFixedCellSize(30);
        TableColumn<NoteCardData, Boolean> use = TableCellFactory
            .getBooleanColumn(IN_USE_COLUMN, d -> d.inUseProperty());
        TableCellFactory.setPrecentWidth(use, noteMetaTable, 10);

        TableColumn<NoteCardData, Object> field = TableCellFactory
            .getFieldTypeColumn(FIELD_COLUMN, d -> d.fieldTypeProperty());

        TableColumn<NoteCardData, Optional<SpanBranch>> data = TableCellFactory
            .getMetaDataColumn(VALUE_COLUMN, d -> d.dataTextProperty());

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

    ComboBox<ShowMeta> getShowMetaBox(){
        return showMetaBox;
    }

    TableView<NoteCardData> getNoteMetaTable(){
        return noteMetaTable;
    }
}
