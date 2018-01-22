package com.creativeartie.jwriter.window;

import java.util.*;
import java.util.Optional;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.scene.control.cell.*;

import com.creativeartie.jwriter.main.*;
import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.window.NotesData.IdentityData;
import com.creativeartie.jwriter.property.window.*;

import com.google.common.base.*;
import com.google.common.collect.*;

/**
 * Stores a list of user notes, hyperlinks, footnotes, and endnotes.
 */
abstract class NotesPaneView extends GridPane{

    /** Cell rendering for {@code typeList} */
    private class TypeCell extends ListCell<DirectoryType>{
        @Override
        protected void updateItem(DirectoryType item, boolean empty) {
            /// Required by JavaFX API:
            super.updateItem(item, empty);
            if (empty || item == null){
                setText(null);
                setGraphic(null);
            } else {
                /// set DirectoryType text
                setText(WindowText.getText(item));
            }
        }
    }

    /** Cell rendering for {@code dataTable} that category/reference. */
    private class TextCell extends TableCell<NotesData, String> {
        private WindowText emptyText;

        TextCell(WindowText key){
            emptyText = key;
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            /// Required by JavaFX API:
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                /// Set text:
                Text out;
                if (item.isEmpty()){
                    /// No text is found
                    out = new Text(emptyText.getText());
                    StyleClass.NO_TEXT.addClass(out);
                } else {
                    /// Text is found
                    out = new Text(item);
                }
                setGraphic(out);
            }
        }
    }

    /** Cell rendering for {@code dataTable} to show the name & count. */
    private class NameCell extends TableCell<NotesData, IdentityData> {

        @Override
        protected void updateItem(IdentityData item, boolean empty) {
            /// Required by JavaFX API:
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                /// Set text
                Text name = new Text(item.getName());
                Text target = new Text("\t" + item.getTarget());
                StyleClass.ID_NUMBERED.addClass(target);
                TextFlow graphic = new TextFlow(name, target);
                setGraphic(graphic);
            }
        }
    }

    /** Cell rendering for {@code dataTable} to show the span location */
    private class LocCell extends TableCell<NotesData,
            Optional<Range<Integer>>>{
        @Override
        protected void updateItem(Optional<Range<Integer>> item, boolean empty){
            /// Required by JavaFX API:
            super.updateItem(item, empty);
            if (empty || item == null){
                setText(null);
                setGraphic(null);
            } else {
                /// Set text:
                if (item.isPresent()){
                    /// For when there is id location
                    Range<Integer> range = item.get();
                    Hyperlink ans = new Hyperlink(range.lowerEndpoint() + "");
                    ans.setOnAction(event -> toLocation.set(range.upperEndpoint()));
                    setGraphic(ans);
                } else {
                    /// For when there is no id location
                    Text ans = new Text(WindowText.NO_SPAN_LOC.getText());
                    StyleClass.NO_TEXT.addClass(ans);
                    setGraphic(ans);
                }
            }
        }
    }

    /**
     * Cell rendering for {@code dataTable} to show the span (exclude notes).
     */
    private class SpanCell extends TableCell<NotesData,
            Optional<SpanBranch>>{
        @Override
        protected void updateItem(Optional<SpanBranch> item, boolean empty) {
            /// Required by JavaFX API:
            super.updateItem(item, empty);
            if (empty || item == null || ! item.isPresent()){
                setText(null);
                setGraphic(null);
            } else {
                /// Find the text to show
                TextFlow graphic;
                SpanBranch branch = item.get();
                if (branch instanceof LinedSpanPointLink){
                    /// Link is found
                    String path = ((LinedSpanPointLink)branch).getPath();
                    graphic = new TextFlow(new Hyperlink(path));
                } else if (branch instanceof LinedSpanLevelSection){
                    /// Heading is found
                    Optional<LinedSpanLevelSection> ans = item.map(span ->
                        (LinedSpanLevelSection)span);
                    graphic = TextFlowBuilder.loadHeadingLine(ans);
                } else if (branch instanceof LinedSpanPointNote){
                    /// Footnote, or endnote is found
                    Optional<FormatSpanMain> ans = ((LinedSpanPointNote)branch)
                        .getFormattedSpan();
                    graphic = TextFlowBuilder.loadFormatText(ans);
                } else {
                    /// note is found
                    graphic = null;
                }
                setGraphic(graphic);
            }
        }
    }

    private ListView<DirectoryType> typeList;
    private TableView<NotesData> dataTable;
    private TitledPane dataTitle;
    private ColumnConstraints dataColumn;
    private ColumnConstraints noteColumn;
    private TableColumn<NotesData, Optional<SpanBranch>> lineColumn;
    private NotesDetailPane noteDetail;

    /** Property set by {@link LocCell}.*/
    private ReadOnlyIntegerWrapper toLocation;
    /**
     * Property is binded to dataTable.foucsedProperty and
     * typeList.foucsedProperty
     */
    private ReadOnlyBooleanWrapper childFocused;

    /// percentage that changes table column widths
    private static double DATA_FULL_WIDHT = 85.0;
    private static double DATA_HALF_WIDHT = 45.0;
    private static double NOTE_FULL_WIDHT = 40.0;
    private static double NOTE_HALF_WIDHT = 0.0;

    public NotesPaneView(){
        typeList = setupTypeList();
        dataTable = new TableView<>();
        TitledPane headingPane = new TitledPane();
        noteDetail = new NotesDetailPane(headingPane);
        setupTitledPane(headingPane, noteDetail);
        
        layoutTable();
        layoutNote(headingPane);

        typeList.getSelectionModel().selectedItemProperty().addListener(
            (dataTable, oldValue, newValue) -> listenType(newValue));

        dataTable.getSelectionModel().selectedItemProperty().addListener(
            (dataTable, oldValue, newValue) -> listenSelected(newValue));

        toLocation = new ReadOnlyIntegerWrapper(this, "toLocation");

        childFocused = new ReadOnlyBooleanWrapper(this, "childFocused");
        childFocused.bind(typeList.focusedProperty().or(dataTable.focusedProperty()));
    }



    /// Layout Node
    private ListView<DirectoryType> setupTypeList(){
        ListView<DirectoryType> ans = new ListView<>(FXCollections
            .observableArrayList(DirectoryType.getMenuList()));
        setupColumnConstraints(15.0);
        ans.setCellFactory(list -> new TypeCell());

        TitledPane pane = setupTitledPane(typeList);
        pane.setText(WindowText.ID_LIST_TITLE.getText());
        add(pane, 0, 0);
        return ans;
    }

    @SuppressWarnings("unchecked")
    private void layoutTable(){
        dataColumn = setupColumnConstraints(DATA_HALF_WIDHT);
        dataTitle = setupTitledPane(dataTable);
        dataTable.setPlaceholder(new Label(WindowText.NO_SPAN_FOUND.getText()));
        dataTable.setFixedCellSize(30);
        dataTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<NotesData, String> cat = setupColumn(
            WindowText.COLUMN_CAT, "catalogueCategory");
        cat.setCellFactory(table -> new TextCell(WindowText.NO_ID_CATEGORY));

        TableColumn<NotesData, IdentityData> name = setupColumn(
            WindowText.COLUMN_NAME, "catalogueIdentity");
        name.setCellFactory(table -> new NameCell());

        TableColumn<NotesData, String> ref = setupColumn(
            WindowText.COLUMN_REF, "refText");
        ref.setCellFactory(table -> new TextCell(WindowText.NO_SPAN_ID));

        TableColumn<NotesData, Optional<Range<Integer>>> loc =
            setupColumn(WindowText.COLUMN_LOC, "spanLocation");
        loc.setCellFactory(table -> new LocCell());

        lineColumn = setupColumn(WindowText.COLUMN_SPAN, "targetSpan");
        lineColumn.setCellFactory(lineColumn -> new SpanCell());
        lineColumn.minWidthProperty().bind(dataTable.widthProperty().multiply(0.4));

        dataTable.getColumns().addAll(cat, name, ref, loc, lineColumn);
        add(dataTitle, 1, 0);
    }

    private void layoutNote(TitledPane pane){
        noteColumn = setupColumnConstraints(NOTE_FULL_WIDHT);
        add(pane, 2, 0);
    }

    private ColumnConstraints setupColumnConstraints(double precent){
        ColumnConstraints column = new ColumnConstraints();
        column.setPercentWidth(precent);
        getColumnConstraints().add(column);
        return column;
    }

    private TitledPane setupTitledPane(Node node){
        return setupTitledPane(new TitledPane(), node);
    }

    private TitledPane setupTitledPane(TitledPane title, Node node){
        title.setContent(node);
        title.setPrefHeight(150.0);
        title.setCollapsible(false);
        return title;
    }

    private <T> TableColumn<NotesData, T> setupColumn(WindowText title,
        String property)
    {
        TableColumn<NotesData, T> ans = new TableColumn<>(title.getText());
        ans.setCellValueFactory(
            new PropertyValueFactory<NotesData, T>(property)
        );
        return ans;
    }

    /// Getters
    protected ListView<DirectoryType> getTypes(){
        return typeList;
    }

    protected TableView<NotesData> getDataTable(){
        return dataTable;
    }

    protected TitledPane getTableTitle(){
        return dataTitle;
    }

    protected TableColumn<NotesData, Optional<SpanBranch>> getLineColumn(){
        return lineColumn;
    }

    protected NotesDetailPane getNoteDetail(){
        return noteDetail;
    }

    protected void setNoteDetailVisible(boolean visible){
        if (visible){
            noteColumn.setPercentWidth(NOTE_FULL_WIDHT);
            dataColumn.setPercentWidth(DATA_HALF_WIDHT);
            return;
        }
        noteColumn.setPercentWidth(NOTE_HALF_WIDHT);
        dataColumn.setPercentWidth(DATA_FULL_WIDHT);
    }

    /// Node Properties

    public ReadOnlyIntegerProperty toLocationProperty(){
        return toLocation.getReadOnlyProperty();
    }

    public int getToLocation(){
        return toLocation.getValue();
    }

    public ReadOnlyBooleanProperty childFocusedProperty(){
        return childFocused.getReadOnlyProperty();
    }

    public boolean isChildFocused(){
        return childFocused.getValue();
    }

    /// Control Methods
    protected abstract void listenType(DirectoryType type);

    protected abstract void listenSelected(NotesData data);
}