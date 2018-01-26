package com.creativeartie.jwriter.window;

import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.main.*;

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
        for (Span child: documentText){
            getHeadings().addItem((SectionSpan) child, span ->
                ((SectionSpanHead)span).getSections());
        }
    }

    public void moveCursor(int position){
        Optional<SpanLeaf> leaf = documentText.locateLeaf(position);
        if (! leaf.isPresent()){
            /// TODO Select first
            return;
        }
        /// TODO Find and select child
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
