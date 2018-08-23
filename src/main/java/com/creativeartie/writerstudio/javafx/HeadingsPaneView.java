package com.creativeartie.writerstudio.javafx;

import java.util.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;

import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.javafx.utils.*;
import static com.creativeartie.writerstudio.javafx.utils.LayoutConstants.
    HeadingConstants.*;

/**
 * A pane with two {@link HeadingTreeControl} objects that
 */
abstract class HeadingsPaneView extends VBox{

    /// %Part 1: Constructor and Class Fields

    private TreeView<SectionSpanHead> headingTree;
    private TitledPane headingPane;
    private TreeView<SectionSpanScene> outlineTree;
    private TitledPane outlinePane;

    public HeadingsPaneView(){
        getChildren().addAll(buildHeadingTree(), buildOutlineTree());
    }

    /// %Part 2: Layout

    /// %Part 2 (content -> top)

    private TitledPane buildHeadingTree(){
        headingTree = buildTree();
        headingPane = new TitledPane(HEADING_TITLE, headingTree);
        return headingPane;
    }

    /// %Part 2 (content -> bottom)

    private TitledPane buildOutlineTree(){
        outlineTree = buildTree();
        outlinePane = new TitledPane(OUTLINE_TITLE, outlineTree);
        return outlinePane;
    }

    /// %Part 2.1 Utilities

    private <T extends SectionSpan> TreeView<T> buildTree(){
        TreeView<T> tree = new TreeView<>();
        tree.setShowRoot(false);
        tree.setCellFactory(param -> {
            TreeCellHeading<T> ans = new TreeCellHeading<>();
            ans.setOnMouseClicked(e -> handleSelection(e, ans.getTreeItem()
                .getValue()));
            return ans;
        });
        return tree;
    }

    /// %Part 3: Setup Properties

    public void postLoad(WriterSceneControl control){
        bindChildren(control);
    }

    protected abstract void handleSelection(MouseEvent event,
        SectionSpan section);

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
