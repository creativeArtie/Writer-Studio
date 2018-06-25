package com.creativeartie.writerstudio.fxgui;

import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.beans.property.*;
import javafx.beans.binding.*;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.main.*;
import com.creativeartie.writerstudio.resource.*;

/**
 * Controller for the Cheatsheet Pane
 *
 * @see CheatsheetPaneView
 */
final class CheatsheetPaneControl extends CheatsheetPaneView{

    protected void addListeners(){
        writingTextProperty().addListener(
            (d, o, n) -> updateLabels(n, getCaretPosition())
        );
        caretPositionProperty().addListener(
            (d, o, n) -> updateLabels(getWritingText(), n.intValue())
        );
    }

     /** Updates the labels base on the cursor movements. */
    private void updateLabels(WritingText doc, int position){
        for (CheatsheetLabel label: getLabels()){
            label.updateLabelStatus(doc, position);
        }
    }
}
