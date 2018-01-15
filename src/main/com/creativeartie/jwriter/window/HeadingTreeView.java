package com.creativeartie.jwriter.window;

import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.beans.binding.*;
import javafx.beans.property.*;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.main.*;
import com.creativeartie.jwriter.property.window.*;

abstract class HeadingTreeView extends TitledPane{

    private class HeadingCell extends TreeCell<Optional<LinedSpanLevelSection>> {
        @Override
        public void updateItem(Optional<LinedSpanLevelSection> item, boolean empty){
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                TextFlow graphic = WindowSpanParser.parseDisplay(item.orElse(null));
                setText(null);
                setGraphic(graphic);
            }
        }
    }

    private final TreeView<Optional<LinedSpanLevelSection>> tree;

    private ReadOnlyObjectWrapper<Optional<LinedSpanLevelSection>> selectedSection;
    private ReadOnlyBooleanWrapper itemFocused;

    private HashMap<Optional<LinedSpanLevelSection>,
        TreeItem<Optional<LinedSpanLevelSection>>> mapper;

    public HeadingTreeView(){
        tree = new TreeView<>();
        mapper = new HashMap<>();

        setupTree();

        selectedSection = new ReadOnlyObjectWrapper<>(this, "selectedSection");
        ReadOnlyObjectProperty<TreeItem<Optional<LinedSpanLevelSection>>> prop =
            tree.getSelectionModel().selectedItemProperty();
        selectedSection.bind(Bindings.createObjectBinding(
            /// Optional.of(TreeItem).flatMap(TreeItem -> LinedSpanLevelSection)
            () -> Optional.ofNullable(prop.getValue()).flatMap(item ->
                item.getValue()),
            prop));

        itemFocused = new ReadOnlyBooleanWrapper(this, "itemFocused");
        itemFocused.bind(tree.focusedProperty());

    }

    /// Getters

    protected TreeView<Optional<LinedSpanLevelSection>> getTree(){
        return tree;
    }

    protected HashMap<Optional<LinedSpanLevelSection>,
        TreeItem<Optional<LinedSpanLevelSection>>>getMapper()
    {
        return mapper;
    }

    /// Layout Node
    private void setupTree(){
        tree.setShowRoot(false);
        tree.setCellFactory(param -> new HeadingCell());
        setContent(tree);
    }

    /// Node Properties
    public ReadOnlyObjectProperty<Optional<LinedSpanLevelSection>>
        selectedSectionProperty()
    {
        return selectedSection.getReadOnlyProperty();
    }

    public ReadOnlyBooleanProperty itemFocusedProperty() {
        return itemFocused.getReadOnlyProperty();
    }

    public boolean isItemFocused(){
        return itemFocused.getValue();
    }

    /// Control Methods

    public abstract void selectHeading(Optional<LinedSpanLevelSection> section);
}