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

abstract class PaneHeadingTreeView extends TitledPane{

    private class HeadingCell extends TreeCell<Optional<LinedSpanSection>> {
        @Override
        public void updateItem(Optional<LinedSpanSection> item, boolean empty){
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                TextFlow graphic = ParseTextUtilities.setHeading(item);
                setText(null);
                setGraphic(graphic);
            }
        }
    }
    
    private final TreeView<Optional<LinedSpanSection>> tree;

    private ReadOnlyObjectWrapper<Optional<LinedSpanSection>> selectedSection;
    private ReadOnlyBooleanWrapper itemFocused;

    private HashMap<Optional<LinedSpanSection>,
        TreeItem<Optional<LinedSpanSection>>> mapper;

    public PaneHeadingTreeView(){
        tree = new TreeView<>();
        mapper = new HashMap<>();

        setupTree();

        selectedSection = new ReadOnlyObjectWrapper<>(this, "selectedSection");
        ReadOnlyObjectProperty<TreeItem<Optional<LinedSpanSection>>> prop =
            tree.getSelectionModel().selectedItemProperty();
        selectedSection.bind(Bindings.createObjectBinding(
            /// Optional.of(TreeItem).flatMap(TreeItem -> LinedSpanSection)
            () -> Optional.ofNullable(prop.getValue()).flatMap(item ->
                item.getValue()),
            prop));
        
        itemFocused = new ReadOnlyBooleanWrapper(this, "itemFocused");
        itemFocused.bind(tree.focusedProperty());
        
    }

    /// Getters

    protected TreeView<Optional<LinedSpanSection>> getTree(){
        return tree;
    }

    protected HashMap<Optional<LinedSpanSection>,
        TreeItem<Optional<LinedSpanSection>>>getMapper()
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
    public ReadOnlyObjectProperty<Optional<LinedSpanSection>>
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
    public abstract void loadHeadings(List<? extends Section> children);

    public abstract void selectHeading(Optional<LinedSpanSection> section);
}