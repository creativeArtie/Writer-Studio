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

abstract class NotesPaneView extends GridPane{
    private class TypeCell extends ListCell<DirectoryType>{
        @Override
        protected void updateItem(DirectoryType item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null){
                setText(null);
                setGraphic(null);
            } else {
                setText(WindowText.getText(item));
            }
        }
    }

    private class TextCell extends TableCell<NotesData, String> {
        private WindowText emptyText;

        TextCell(WindowText key){
            emptyText = key;
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                Text out;
                if (item.isEmpty()){
                    out = new Text(emptyText.getText());
                    out.setStyle(WindowStyle.NOT_FOUND.toCss());
                } else {
                    out = new Text(item);
                }
                setGraphic(out);
            }
        }
    }

    private class NameCell extends TableCell<NotesData, IdentityData> {

        @Override
        protected void updateItem(IdentityData item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                Text name = new Text(item.getName());
                Text target = new Text("\t" + item.getTarget());
                target.setStyle(WindowStyle.NUMBERED_ID.toCss());
                TextFlow graphic = new TextFlow(name, target);
                setGraphic(graphic);
            }
        }
    }

    private class LocCell extends TableCell<NotesData,
            Optional<Range<Integer>>>{
        @Override
        protected void updateItem(Optional<Range<Integer>> item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null){
                setText(null);
                setGraphic(null);
            } else {
                if (item.isPresent()){
                    Range<Integer> range = item.get();
                    Hyperlink ans = new Hyperlink(range.lowerEndpoint() + "");
                    ans.setOnAction(event -> toLocation.set(range.upperEndpoint()));
                    setGraphic(ans);
                } else {
                    Text ans = new Text(WindowText.NO_SPAN_LOC.getText());
                    ans.setStyle(WindowStyle.NOT_FOUND.toCss());
                    setGraphic(ans);
                }
            }
        }
    }

    private class SpanCell extends TableCell<NotesData,
            Optional<SpanBranch>>{
        @Override
        protected void updateItem(Optional<SpanBranch> item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null || ! item.isPresent()){
                setText(null);
                setGraphic(null);
            } else {
                TextFlow graphic;
                SpanBranch branch = item.get();
                if (branch instanceof LinedSpanPointLink){
                    String path = ((LinedSpanPointLink)branch).getPath();
                    graphic = new TextFlow(new Hyperlink(path));
                } else if (branch instanceof LinedSpanLevelSection){
                    Optional<LinedSpanLevelSection> ans = item.map(span ->
                        (LinedSpanLevelSection)span);
                    graphic = WindowSpanParser.parseDisplay(ans.orElse(null));
                } else if (branch instanceof LinedSpanPointNote){
                    Optional<FormatSpanMain> ans = ((LinedSpanPointNote)branch)
                        .getFormattedSpan();
                    graphic = WindowSpanParser.parseDisplay(ans.orElse(null));
                } else {
                    graphic = null;
                }
                setGraphic(graphic);
            }
        }
    }

    private ListView<DirectoryType> types;
    private TableView<NotesData> data;
    private TitledPane dataTitle;
    private ColumnConstraints dataColumn;
    private ColumnConstraints noteColumn;
    private TableColumn<NotesData, Optional<SpanBranch>> lineColumn;
    private NotesDetailPane noteDetail;

    private ReadOnlyIntegerWrapper toLocation;
    private ReadOnlyBooleanWrapper childFocused;

    private static double DATA_FULL_WIDHT = 85.0;
    private static double DATA_HALF_WIDHT = 45.0;
    private static double NOTE_FULL_WIDHT = 40.0;
    private static double NOTE_HALF_WIDHT = 0.0;

    public NotesPaneView(){
        types = new ListView<>(FXCollections.observableArrayList(DirectoryType
            .getMenuList()));
        data = new TableView<>();
        TitledPane headingPane = new TitledPane();
        noteDetail = new NotesDetailPane(headingPane);
        setupTitledPane(headingPane, noteDetail);

        layoutTypes();
        layoutTable();
        layoutNote(headingPane);

        types.getSelectionModel().selectedItemProperty().addListener(
            (data, oldValue, newValue) -> listenType(newValue));

        data.getSelectionModel().selectedItemProperty().addListener(
            (data, oldValue, newValue) -> listenSelected(newValue));

        toLocation = new ReadOnlyIntegerWrapper(this, "toLocation");

        childFocused = new ReadOnlyBooleanWrapper(this, "childFocused");
        childFocused.bind(types.focusedProperty().or(data.focusedProperty()));
    }

    /// Getters
    protected ListView<DirectoryType> getTypes(){
        return types;
    }

    protected TableView<NotesData> getDataTable(){
        return data;
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

    /// Layout Node
    private void layoutTypes(){
        setupColumnConstraints(15.0);
        types.setCellFactory(list -> new TypeCell());
        TitledPane pane = setupTitledPane(types);
        pane.setText(WindowText.ID_LIST_TITLE.getText());
        add(pane, 0, 0);
    }

    @SuppressWarnings("unchecked")
    private void layoutTable(){
        dataColumn = setupColumnConstraints(DATA_HALF_WIDHT);
        dataTitle = setupTitledPane(data);
        data.setPlaceholder(new Label(WindowText.NO_SPAN_FOUND.getText()));
        data.setFixedCellSize(30);
        data.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

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
        lineColumn.minWidthProperty().bind(data.widthProperty().multiply(0.4));

        data.getColumns().addAll(cat, name, ref, loc, lineColumn);
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

    public abstract void loadDoc(WritingText doc);

    protected abstract void listenType(DirectoryType type);

    protected abstract void listenSelected(NotesData data);

    public abstract void refreshPane(WritingText doc);
}