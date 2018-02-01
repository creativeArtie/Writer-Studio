package com.creativeartie.jwriter.window;

import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.main.*;

import com.google.common.collect.*;

/**
 * Controller for Heading Pane.
 *
 * @see HeadingPaneView
 */
class HeadingPaneControl extends HeadingPaneView{

    private WritingText documentText;

    public void loadHeadings(WritingText document){
        documentText = document;
        documentText.addUpdater(span -> fillHeadings());
        getOutlines().clearTree();
        fillHeadings();
    }

    public void fillHeadings(){
        if (documentText == null || documentText.isEmpty()){
            getHeadings().clearTree();
            return;
        }
        getHeadings().loadSections(documentText);
    }

    public void updateTable(int position){
        if (documentText == null || documentText.isEmpty()){
            /// Heading Pane is not ready
            return;
        }
        if (position > documentText.getEnd()){
            /// TODO Select the last item
            return;
        }
        Optional<SpanLeaf> leaf = documentText.locateLeaf(position);
        assert leaf.isPresent() : "Leaf is not present.";
        /// Select Heading
        TreeItem<SectionSpan> root = getHeadings().getTree().getRoot();
        SectionSpanHead heading = leaf.get().getParent(SectionSpanHead.class)
            .get();
        setSection(root, heading, getHeadings().getTree());
        getOutlines().loadScenes(heading);

        /// Select outline
        TreeItem<SectionSpan> root1 = getOutlines().getTree().getRoot();
        Optional<SectionSpanScene> scene = leaf.get().getParent(SectionSpanScene
            .class);
        scene.ifPresent(find -> setSection(root1, find, getOutlines()
            .getTree()));
    }

    private void setSection(TreeItem<SectionSpan> parent,
            SectionSpan target, TreeView<SectionSpan> tree){
        for (TreeItem<SectionSpan> child: parent.getChildren()){
            Range<Integer> ptr = child.getValue().getRange();
            Range<Integer> range = target.getRange();
            if (ptr.equals(range)){
                /// Found item
                tree.getSelectionModel().select(child);
                return;
            } else if (ptr.encloses(range)){
                /// traget is a child of child
                setSection(child, target, tree);
                return;
            }
            /// target is a slibing of child
        }
    }
}
