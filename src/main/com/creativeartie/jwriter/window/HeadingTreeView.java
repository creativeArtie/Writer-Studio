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

/**
 * A {@linkplain TreeView} of either {@link WritingText} or
 * {@link SectionSpanHead}.
 */
abstract class HeadingTreeView extends TitledPane{

    /** A cell in {@link #getTree()}. */
    private class HeadingCell extends TreeCell<Optional<LinedSpanLevelSection>>{
        @Override
        public void updateItem(Optional<LinedSpanLevelSection> item,
                boolean empty){
            /// Required by JavaFX API:
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                /// Allows WindowSpanParser to create the Label
                TextFlow graphic = TextFlowBuilder.loadHeadingLine(item);
                setText(null);
                setGraphic(graphic);
            }
        }
    }

    private final TreeView<Optional<LinedSpanLevelSection>> sectionTree;

    /**
     * Property binded to selectionTree.selectionModel().selectedItemProperty().
     */
    private ReadOnlyObjectWrapper<Optional<LinedSpanLevelSection>>
        sectionSelected;
    /** Property binded to sectionTree.focusedProperty(). */
    private ReadOnlyBooleanWrapper itemFocused;


    public HeadingTreeView(){
        sectionTree = setupTree();

        sectionSelected = new ReadOnlyObjectWrapper<>(this, "sectionSelected");
        ReadOnlyObjectProperty<TreeItem<Optional<LinedSpanLevelSection>>> prop =
            sectionTree.getSelectionModel().selectedItemProperty();
        sectionSelected.bind(Bindings.createObjectBinding(
            /// Optional.of(TreeItem).flatMap(TreeItem -> LinedSpanLevelSection)
            () -> Optional.ofNullable(prop.getValue()).flatMap(item ->
                item.getValue()),
            prop));

        itemFocused = new ReadOnlyBooleanWrapper(this, "itemFocused");
        itemFocused.bind(sectionTree.focusedProperty());

    }

    /// Layout Node
    private TreeView<Optional<LinedSpanLevelSection>> setupTree(){
        TreeView<Optional<LinedSpanLevelSection>> tree = new TreeView<>();
        tree.setShowRoot(false);
        tree.setCellFactory(param -> new HeadingCell());
        setContent(tree);
        return tree;
    }

    /// Getters
    protected TreeView<Optional<LinedSpanLevelSection>> getTree(){
        return sectionTree;
    }

    /// Node Properties
    public ReadOnlyObjectProperty<Optional<LinedSpanLevelSection>>
        selectedSectionProperty()
    {
        return sectionSelected.getReadOnlyProperty();
    }

    public ReadOnlyBooleanProperty itemFocusedProperty() {
        return itemFocused.getReadOnlyProperty();
    }

    public boolean isItemFocused(){
        return itemFocused.getValue();
    }

    /// Control Methods
}