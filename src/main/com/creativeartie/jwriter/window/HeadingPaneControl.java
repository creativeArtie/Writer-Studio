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
        fillHeadings();
        getOutlines().clearTree();
    }

    public void fillHeadings(){
        getHeadings().loadSections(documentText);
    }

    public void updateTable(int position){
        if (documentText == null){
            /// Heading Pane is not ready
            return;
        }
        Optional<SpanLeaf> leaf = documentText.locateLeaf(position);
        if (! leaf.isPresent()){
            /// TODO Select first
            return;
        }
        /// TODO Find and select child
        TreeItem<SectionSpan> root = getHeadings().getTree().getRoot();
        SectionSpanHead found = leaf.get().getParent(SectionSpanHead.class)
            .get();
        setSection(root, found, getHeadings().getTree());
        getOutlines().loadScenes(found);
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

//
//    /** Loads the document sections. */
//    public void loadTrees(WritingText document){
//        getHeadings().loadHeadings(document);
//        getOutlines().clearTree();
//    }
//
//     /** Updates the selection base on the cursor movements. */
//    public void setHeading(WritingText doc, int position){
//        doc.locateSpan(position, SpanLeaf.class).ifPresent(span -> {
//            /// should always run b/c every leaf is a SpanLeaf
//
//            /// Get the lowest SectionSpanHead
//            SectionSpanHead head = span.getParent(SectionSpanHead.class).get();
//
//            /// Selection at the top
//            getHeadings().selectHeading(head.getHeading());
//
//            /// Section at the bottom
//            getOutlines().loadOutline(head);
//            Optional<SectionSpanScene> scene = span.getParent(
//                SectionSpanScene.class);
//            scene.ifPresent(
//                found -> getOutlines().selectHeading(found.getHeading())
//            );
//        });
//    }
}
