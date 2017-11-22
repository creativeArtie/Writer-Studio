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
import com.creativeartie.jwriter.window.PaneListsData.IdentityData;

import com.google.common.base.*;
import com.google.common.collect.*;

abstract class PaneListsView extends GridPane{
    private class TypeCell extends ListCell<DirectoryType>{
        @Override
        protected void updateItem(DirectoryType item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null){
                setText(null);
                setGraphic(null);
            } else {
                setText(Utilities.getString(item));
            }
        }
    }

    private class TextCell extends TableCell<PaneListsData, String> {
        private String emptyKey;

        TextCell(String key){
            emptyKey = key;
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
                    out = new Text(Utilities.getString(emptyKey));
                    out.setStyle(Utilities.getCss("Common.NoneFound"));
                } else {
                    out = new Text(item);
                }
                setGraphic(out);
            }
        }
    }

    private class NameCell extends TableCell<PaneListsData, IdentityData> {

        @Override
        protected void updateItem(IdentityData item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                Text name = new Text(item.getName());
                Text target = new Text("\t" + item.getTarget());
                target.setStyle(Utilities.getCss("ListView.SpanTarget"));
                TextFlow graphic = new TextFlow(name, target);
                setGraphic(graphic);
            }
        }
    }

    private class LocCell extends TableCell<PaneListsData,
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
                    Text ans = new Text(Utilities.getString("ListView.NoSpan"));
                    ans.setStyle(Utilities.getCss("Common.NoneFound"));
                    setGraphic(ans);
                }
            }
        }
    }

    private class SpanCell extends TableCell<PaneListsData,
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
                } else if (branch instanceof LinedSpanSection){
                    Optional<LinedSpanSection> ans = item.map(span ->
                        (LinedSpanSection)span);
                    graphic = SpanBranchParser.parseDisplay(ans.get());
                } else if (branch instanceof LinedSpanPointNote){
                    Optional<FormatSpanMain> ans = ((LinedSpanPointNote)branch)
                        .getFormattedSpan();
                    graphic = SpanBranchParser.parseDisplay(ans.orElse(null));
                } else {
                    graphic = null;
                }
                setGraphic(graphic);
            }
        }
    }

    private ListView<DirectoryType> types;
    private TableView<PaneListsData> data;
    private TitledPane dataTitle;
    private ColumnConstraints dataColumn;
    private ColumnConstraints noteColumn;
    private TableColumn<PaneListsData, Optional<SpanBranch>> lineColumn;
    private PaneListsNotePane noteDetail;

    private ReadOnlyIntegerWrapper toLocation;
    private ReadOnlyBooleanWrapper childFocused;

    private static double DATA_FULL_WIDHT = 85.0;
    private static double DATA_HALF_WIDHT = 45.0;
    private static double NOTE_FULL_WIDHT = 40.0;
    private static double NOTE_HALF_WIDHT = 0.0;

    public PaneListsView(){
        types = new ListView<>(FXCollections.observableArrayList(DirectoryType
            .getMenuList()));
        data = new TableView<>();
        noteDetail = new PaneListsNotePane();

        layoutTypes();
        layoutTable();
        layoutNote();

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

    protected TableView<PaneListsData> getDataTable(){
        return data;
    }

    protected TitledPane getTableTitle(){
        return dataTitle;
    }

    protected TableColumn<PaneListsData, Optional<SpanBranch>> getLineColumn(){
        return lineColumn;
    }

    protected PaneListsNotePane getNoteDetail(){
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
        add(setupTitledPane("ListView.Types", types), 0, 0);
    }

    @SuppressWarnings("unchecked")
    private void layoutTable(){
        dataColumn = setupColumnConstraints(DATA_HALF_WIDHT);
        dataTitle = setupTitledPane("ListView.Note", data);
        data.setPlaceholder(new Label(Utilities.getString("ListView.NoItems")));
        data.setFixedCellSize(30);
        data.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<PaneListsData, String> cat = setupColumn("Category",
            "catalogueCategory");
        cat.setCellFactory(table -> new TextCell("ListView.NoCategory"));

        TableColumn<PaneListsData, IdentityData> name = setupColumn("Name",
            "catalogueIdentity");
        name.setCellFactory(table -> new NameCell());

        TableColumn<PaneListsData, String> ref = setupColumn("Ref",
            "refText");
        ref.setCellFactory(table -> new TextCell("ListView.NoRef"));

        TableColumn<PaneListsData, Optional<Range<Integer>>> loc =
            setupColumn("Location", "spanLocation");
        loc.setCellFactory(table -> new LocCell());

        lineColumn = setupColumn("Span", "targetSpan");
        lineColumn.setCellFactory(lineColumn -> new SpanCell());
        lineColumn.minWidthProperty().bind(data.widthProperty().multiply(0.4));

        data.getColumns().addAll(cat, name, ref, loc, lineColumn);
        add(dataTitle, 1, 0);
    }

    private void layoutNote(){
        TitledPane pane = setupTitledPane("ListView.NoteTitle", noteDetail);
        noteColumn = setupColumnConstraints(NOTE_FULL_WIDHT);
        add(pane, 2, 0);
    }

    private ColumnConstraints setupColumnConstraints(double precent){
        ColumnConstraints column = new ColumnConstraints();
        column.setPercentWidth(precent);
        getColumnConstraints().add(column);
        return column;
    }

    private TitledPane setupTitledPane(String key, Node node){
        TitledPane title = new TitledPane(Utilities.getString(key), node);
        title.setPrefHeight(150.0);
        title.setCollapsible(false);
        return title;
    }

    private <T> TableColumn<PaneListsData, T> setupColumn(String key,
        String property)
    {
        TableColumn<PaneListsData, T> ans = new TableColumn<>(Utilities
            .getString("ListView.Col" + key));
        ans.setCellValueFactory(
            new PropertyValueFactory<PaneListsData, T>(property)
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

    public abstract void loadDoc(ManuscriptDocument doc);

    protected abstract void listenType(DirectoryType type);

    protected abstract void listenSelected(PaneListsData data);

    public abstract void refreshPane(ManuscriptDocument doc);
}