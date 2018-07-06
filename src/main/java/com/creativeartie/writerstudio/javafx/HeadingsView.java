package com.creativeartie.writerstudio.javafx;

import java.util.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;

import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.resource.*;

/**
 * A pane with two {@link HeadingTreeControl} objects that
 */
abstract class HeadingsView extends VBox{

    private class HeadingCell<T extends SectionSpan> extends TreeCell<T>{
        @Override
        public void updateItem(T item, boolean empty){
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

    /// %Part 1: Constructor and Class Fields

    private TreeView<SectionSpanHead> headingTree;
    private TitledPane headingPane;
    private TreeView<SectionSpanScene> outlineTree;
    private TitledPane outlinePane;

    public HeadingsView(){
        getChildren().addAll(buildHeadingTree(), buildOutlineTree());
    }

    /// %Part 2: Layout

    private TitledPane buildHeadingTree(){
        headingTree = buildTree();
        headingPane = new TitledPane(WindowText.HEADING_TITLE.getText(),
            headingTree);
        return headingPane;
    }

    private TitledPane buildOutlineTree(){
        outlineTree = buildTree();
        outlinePane = new TitledPane(WindowText.OUTLINE_TITLE.getText(),
            outlineTree);
        return outlinePane;
    }

    private <T extends SectionSpan> TreeView<T> buildTree(){
        TreeView<T> tree = new TreeView<>();
        tree.setShowRoot(false);
        tree.setCellFactory(param -> new HeadingCell<>());
        return tree;
    }

    /// %Part 3: Setup Properties

    public void setupProperties(WriterSceneControl control){
        setupChildern(control);
    }

    protected abstract void setupChildern(WriterSceneControl control);

    /// %Part 4: Properties

    /// %Part 5: Get Child Methods

    TreeView<SectionSpanHead> getHeadingTree(){
        return headingTree;
    }

    TitledPane getHeadingPane(){
        return headingPane;
    }

    TitledPane getOutlinePane(){
        return outlinePane;
    }

    TreeView<SectionSpanScene> getOutlineTree(){
        return outlineTree;
    }
}
