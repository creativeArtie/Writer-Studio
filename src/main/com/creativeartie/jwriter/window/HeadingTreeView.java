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

    private class HeadingCell extends TreeCell<SectionSpan>{
        @Override
        public void updateItem(SectionSpan item, boolean empty){
            /// Required by JavaFX API:
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                /// Allows WindowSpanParser to create the Label
                TextFlow graphic = TextFlowBuilder.loadHeadingLine(item
                    .getHeading());
                setText(null);
                setGraphic(graphic);
            }
        }
    }

    private final TreeView<SectionSpan> sectionTree;

    /**
     * Property binded to selectionTree.selectionModel().selectedItemProperty().
     */
    private ReadOnlyObjectWrapper<SectionSpan> sectionSelected;
    /** Property binded to sectionTree.focusedProperty(). */
    private ReadOnlyBooleanWrapper itemFocused;


    public HeadingTreeView(){
        sectionTree = initTree();

        sectionSelected = new ReadOnlyObjectWrapper<>(this, "sectionSelected");
        ReadOnlyObjectProperty<TreeItem<SectionSpan>> prop =
            sectionTree.getSelectionModel().selectedItemProperty();
        sectionSelected.bind(Bindings.createObjectBinding(
            /// to Optional<TreeItem<SectionSpan>>
            () -> Optional.ofNullable(prop.getValue())
                /// to Optional<SectionSpan>
                .map(item ->item.getValue())
                /// to SectionSpan
                .orElse(null),
            prop));

        itemFocused = new ReadOnlyBooleanWrapper(this, "itemFocused");
        itemFocused.bind(sectionTree.focusedProperty());

    }

    private TreeItem<SectionSpan> initItem(){
        return new TreeItem<>();
    }

    /// Layout Node
    private TreeView<SectionSpan> initTree(){
        TreeView<SectionSpan> tree = new TreeView<>();
        tree.setShowRoot(false);
        tree.setCellFactory(param -> new HeadingCell());
        setContent(tree);
        tree.setRoot(new TreeItem<>());
        return tree;
    }

    /// Getters
    protected TreeView<SectionSpan> getTree(){
        return sectionTree;
    }

    /// Node Properties
    public ReadOnlyObjectProperty<SectionSpan> selectedSectionProperty(){
        return sectionSelected.getReadOnlyProperty();
    }

    public SectionSpan  getSelectedSection(){
        return sectionSelected.getValue();
    }

    public ReadOnlyBooleanProperty itemFocusedProperty() {
        return itemFocused.getReadOnlyProperty();
    }

    public boolean isItemFocused(){
        return itemFocused.getValue();
    }

    /// Control Methods
    protected abstract void clearTree();
}
