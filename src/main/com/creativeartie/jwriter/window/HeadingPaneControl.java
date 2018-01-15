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


    @Override
    public void loadHeadings(ManuscriptDocument document){
        getHeadings().loadHeadings(document);
        getOutlines().clear();
    }

    @Override
    public void setHeading(ManuscriptDocument doc, int position){
        doc.locateSpan(position, SpanLeaf.class).ifPresent(span -> {
            SectionSpanHead head = span.getParent(SectionSpanHead.class).get();
            getHeadings().selectHeading(head.getHeading());
            getOutlines().loadOutline(head);
            Optional<SectionSpanScene> scene = span.getParent(
                SectionSpanScene.class);
            scene.ifPresent(found -> getOutlines().selectHeading(found
                .getHeading()));
        });
    }
}