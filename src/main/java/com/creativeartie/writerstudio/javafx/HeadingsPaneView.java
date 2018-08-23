package com.creativeartie.writerstudio.javafx;

import java.util.*;
import javafx.event.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;

import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.resource.*;
import com.creativeartie.writerstudio.javafx.utils.*;

/**
 * A pane with two {@link HeadingTreeControl} objects that
 */
abstract class HeadingsPaneView extends VBox{

    /// %Part 1: Constructor and Class Fields

    private TreeView<SectionSpanHead> headingTree;
    private TitledPane headingPane;
    private TreeView<SectionSpanScene> outlineTree;
    private TitledPane outlinePane;
    private EventHandler<? super MouseEvent> selectionEvent;

    public HeadingsPaneView(){
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
        tree.setCellFactory(param -> {
            TreeCellHeading ans = new TreeCellHeading<>();
            ans.setOnMouseClicked(selectionEvent);
            return ans;
        });
        return tree;
    }

    /// %Part 3: Setup Properties

    public void postLoad(WriterSceneControl control){
        selectionEvent = getSelectionHandler();
        bindChildren(control);
    }

    protected abstract void bindChildren(WriterSceneControl control);

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
